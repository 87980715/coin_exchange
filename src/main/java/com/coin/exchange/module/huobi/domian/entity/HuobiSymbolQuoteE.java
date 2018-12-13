/**
 * @(#) HuobiSymbolQuoteE.java
 * <p>
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.domian.entity;

import com.coin.exchange.common.LimitQueue;
import com.coin.exchange.common.utils.Arith;
import com.coin.exchange.common.utils.BigDecimalHelper;
import com.coin.exchange.common.utils.LocalDateHelper;
import com.coin.exchange.common.utils.StringsHelper;
import com.coin.exchange.module.huobi.domian.HuobiFactory;
import com.coin.exchange.module.huobi.domian.repository.TradeTraceRepository;
import com.coin.exchange.module.huobi.dto.TradeTraceHandler;
import com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel.HuobiAccountTunnelI;
import com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel.HuobiSymbolQuoteTunnelI;
import com.coin.exchange.module.huobi.third.clientobject.MarketDepthCO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.coin.exchange.module.huobi.domian.HuobiQuoteConfig.*;


/**
 * @author storys.zhang@gmail.com
 * <p>
 * Created at 2018/9/25 by Storys.Zhang in coin_exchange
 */
@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HuobiSymbolQuoteE {

    @Setter
    @Getter
    private String symbol;

    @Setter
    @Getter
    private String currency;

    @Setter
    @Getter
    private String quoteCurrency;

    @Setter
    @Getter
    private String accountId;

    /**
     * 报价拉取频率限制
     */
    private RateLimiter limiter = RateLimiter.create(0.2);

    @Autowired
    private HuobiSymbolQuoteTunnelI huobiSymbolQuoteTunnel;
    @Autowired
    private HuobiFactory huobiFactory;
    @Autowired
    private TradeTraceRepository tradeTraceRepository;
    @Autowired
    private HuobiAccountTunnelI huobiAccountTunnelI;

    private volatile boolean waitingMaker = true;

    private AtomicInteger overUpCount = new AtomicInteger(0);

    private ConcurrentMap<Integer, BigDecimal> overUpContainer = Maps.newConcurrentMap();

    public void load() {
        while (true) {
            acquire();
            try {
                quote();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void quote() {
        final MarketDepthCO marketDepth = huobiSymbolQuoteTunnel.getMarketDepthCO(this.symbol);
        marketDepth.setAsks(Lists.newArrayList(marketDepth.getAsks().subList(0, 5)));
        marketDepth.setBids(Lists.newArrayList(marketDepth.getBids().subList(0, 5)));
        marketDepth.setCreateAt(LocalDateHelper.getCurrentTime(null));
        final LimitQueue<MarketDepthCO> limitQueue = huobiSymbolQuoteTunnel.get(this.symbol);
        if (Objects.isNull(limitQueue)) {
            final LimitQueue<MarketDepthCO> marketDepthCOS = new LimitQueue<>(60);
            marketDepthCOS.offer(marketDepth);
            huobiSymbolQuoteTunnel.update(this.symbol, marketDepthCOS);
        } else {
            final MarketDepthCO last = limitQueue.peekLast();
            final BigDecimal askLast = last.getAsks().get(0)[0];
            final BigDecimal bidLast = last.getBids().get(0)[0];
            final BigDecimal askCurrent = marketDepth.getAsks().get(0)[0];
            final BigDecimal bidCurrent = marketDepth.getBids().get(0)[0];
            final MarketDepthCO firt = limitQueue.peek();
            final BigDecimal askFirst = firt.getAsks().get(0)[0];
            final BigDecimal bidFirst = firt.getBids().get(0)[0];
            if (waitingMaker) {

                BigDecimal div = Arith.div(Arith.sub(askCurrent, askFirst), askFirst);
                log.info("waiting  marker {}, {}, {}, 振幅{}, waitingMaker={}, quoteCurrency={}, currency={}", BigDecimalHelper.printBigDecimal(askFirst), BigDecimalHelper.printBigDecimal(askLast), BigDecimalHelper.printBigDecimal(askCurrent), BigDecimalHelper.printPercent(div), waitingMaker, quoteCurrency, currency);
                if (askLast.compareTo(askCurrent) >= 0) {
                    limitQueue.clear();
                    limitQueue.offer(marketDepth);
                    huobiSymbolQuoteTunnel.update(this.symbol, limitQueue);
                } else {
                    if (div.compareTo(MIN_GO_UP) >= 0) {
                        log.info("买入->->上涨趋势涨幅{},超过阀值{}, 立即买入，上升趋势{}, {}, {}", BigDecimalHelper.printPercent(div), BigDecimalHelper.printPercent(MIN_GO_UP), BigDecimalHelper.printBigDecimal(askFirst), BigDecimalHelper.printBigDecimal(askLast), BigDecimalHelper.printBigDecimal(askCurrent));
                        maker(limitQueue, marketDepth, StringsHelper.join("(买入)涨幅", BigDecimalHelper.printPercent(div), "超过最低阀值", BigDecimalHelper.printPercent(MIN_GO_UP), "->"));
                    } else {
                        limitQueue.offer(marketDepth);
                        huobiSymbolQuoteTunnel.update(this.symbol, limitQueue);
                    }
                }
            } else {
                BigDecimal div = Arith.div(Arith.sub(bidCurrent, askFirst), askFirst);
                log.info("waiting  taker {}, {},  {}, 振幅{}, 队列长度:{}, waitingMaker={}, quoteCurrency={}, currency={}", BigDecimalHelper.printBigDecimal(askFirst), BigDecimalHelper.printBigDecimal(bidLast), BigDecimalHelper.printBigDecimal(bidCurrent), BigDecimalHelper.printPercent(div), limitQueue.size(), waitingMaker, quoteCurrency, currency);

                if (overUpCount.get() > 0) {
                    BigDecimal lastDiv = overUpContainer.get(overUpCount.get());
                    if (Arith.div(Arith.sub(lastDiv, div), lastDiv).compareTo(MIN_GO_DOWN) >= 0) {
                        log.info("卖出->->止盈，涨幅{}, 最后一次涨幅{},超过最低跌幅{}, 马上卖出, 下跌趋势{}, {}, {}", BigDecimalHelper.printPercent(div), BigDecimalHelper.printPercent(lastDiv), BigDecimalHelper.printPercent(MIN_GO_DOWN), BigDecimalHelper.printBigDecimal(askFirst), BigDecimalHelper.printBigDecimal(bidLast), BigDecimalHelper.printBigDecimal(askCurrent));
                        taker(StringsHelper.join("(卖出)止盈", BigDecimalHelper.printPercent(div)));
                        return;
                    }
                }
                scan:
                {
                    if (div.compareTo(BigDecimal.ZERO) < 0 && div.abs().compareTo(MAX_GO_DOWN) >= 0) {
                        log.info("卖出->->止损{}, 跌幅超过最大阀值{}，马上卖出, 下跌趋势{}, {}, {}", BigDecimalHelper.printPercent(div.abs()), BigDecimalHelper.printPercent(MAX_GO_DOWN), BigDecimalHelper.printBigDecimal(askFirst), BigDecimalHelper.printBigDecimal(bidLast), BigDecimalHelper.printBigDecimal(askCurrent));
                        taker(StringsHelper.join("(卖出)下跌超过最大跌幅", BigDecimalHelper.printPercent(div.abs()), "超过最大跌幅阀值", BigDecimalHelper.printPercent(MAX_GO_DOWN)));
                        break scan;
                    }
                    if (div.compareTo(MAX_GO_UP) >= 0) {
                        int i = overUpCount.incrementAndGet();
                        overUpContainer.put(i, div);
                        log.info("等待卖出-->-->（上浮） 涨幅连续达标 {}次, 上涨幅度{}, 超过阀值{}, 上升趋势{}, {}, {}", i, BigDecimalHelper.printPercent(div), BigDecimalHelper.printPercent(MAX_GO_UP), BigDecimalHelper.printBigDecimal(askFirst), BigDecimalHelper.printBigDecimal(askLast), BigDecimalHelper.printBigDecimal(askCurrent));
                        break scan;
                    }
                    limitQueue.offer(marketDepth);
                    huobiSymbolQuoteTunnel.update(this.symbol, limitQueue);
                }
            }
            huobiSymbolQuoteTunnel.update(this.symbol, limitQueue);
        }
    }

    private void acquire() {
        limiter.acquire();
    }

    private void maker(LimitQueue<MarketDepthCO> limitQueue, MarketDepthCO marketDepth, String remarks) {
        TradeTraceHandler tradeTraceHandler = new TradeTraceHandler();
        tradeTraceHandler.init(accountId, symbol, quoteCurrency, remarks);
        Boolean price = huobiFactory.getHuobiOrder(accountId, symbol, quoteCurrency).doMaker();
        if (price) {
            tradeTraceRepository.init();
            limitQueue.clear();
            limitQueue.offer(marketDepth);
            log.info("买入价格：{}", price);
            huobiSymbolQuoteTunnel.update(this.symbol, limitQueue);
            waitingMaker = false;
            huobiAccountTunnelI.updateBalance(accountId);
            limiter.setRate(0.6d);
        } else {
            huobiSymbolQuoteTunnel.delete(symbol);
        }
    }

    private void taker(String remarks) {
        boolean exe = huobiFactory.getHuobiOrder(accountId, symbol, currency).doTaker();
        if (exe) {
            huobiSymbolQuoteTunnel.delete(symbol);
            waitingMaker = true;
            TradeTraceHandler.threadLocal.get().setRemarks(TradeTraceHandler.threadLocal.get().getRemarks() + remarks);
            tradeTraceRepository.complite();
            //limiter.setRate(0.2d);
        } else {
            huobiSymbolQuoteTunnel.delete(symbol);
        }
        overUpCount.set(0);
        overUpContainer.clear();
    }
}