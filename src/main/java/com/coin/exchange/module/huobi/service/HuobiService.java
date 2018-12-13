/**
 * @(#) HuobiService.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.service;

import com.coin.exchange.common.LimitQueue;
import com.coin.exchange.common.base.ApiResponseVO;
import com.coin.exchange.common.utils.SpringContextHolder;
import com.coin.exchange.module.huobi.domian.HuobiFactory;
import com.coin.exchange.module.huobi.domian.entity.HuobiAccountE;
import com.coin.exchange.module.huobi.domian.entity.HuobiOrderE;
import com.coin.exchange.module.huobi.domian.entity.HuobiSymbolQuoteE;
import com.coin.exchange.module.huobi.dto.valueobject.AccountType;
import com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel.HuobiAccountTunnelI;
import com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel.HuobiSymbolQuoteTunnelI;
import com.coin.exchange.module.huobi.third.clientobject.AccountBalanceCO;
import com.coin.exchange.module.huobi.third.clientobject.AccountCO;
import com.coin.exchange.module.huobi.third.clientobject.MarketDepthCO;
import com.coin.exchange.module.huobi.third.clientobject.OrderCO;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListeningExecutorService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/25 by Storys.Zhang in coin_exchange
 */
@Service
public class HuobiService {

    @Autowired
    private HuobiAccountTunnelI huobiAccountTunnel;
    @Autowired
    private HuobiSymbolQuoteTunnelI huobiSymbolQuoteTunnelI;
    @Autowired
    private HuobiFactory huobiFactory;
    @Autowired
    @Qualifier("huobiMarketDepthListeningExecutorService")
    private ListeningExecutorService listeningExecutorService;

    public ApiResponseVO<AccountBalanceCO> accounts() {
        final List<AccountCO> all = huobiAccountTunnel.findAll();
        final Optional<AccountCO> first = all.stream()
            .filter(p -> p.getType().equalsIgnoreCase(AccountType.SPOT.toString())).findFirst();
        if (!first.isPresent()) {
            return new ApiResponseVO<>();
        }
        final AccountBalanceCO balance = huobiAccountTunnel.getBalance(first.get().getId() + "");
        balance.setList(
            balance.getList().stream().filter(p -> p.getBalance().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors
                    .toList()));
        return new ApiResponseVO<>(balance);
    }


    public ApiResponseVO<List<Map<String, Object>>> finadAllSymbolQuote() {
        final ConcurrentMap<String, LimitQueue<MarketDepthCO>> all = huobiSymbolQuoteTunnelI
            .findAll();
        final List<Map<String, Object>> collect = all.entrySet().stream().map(entity -> {
            Map<String, Object> map = Maps.newHashMap();
            map.put(entity.getKey(), entity.getValue().getQueue().iterator());
            return map;
        }).collect(Collectors.toList());
        final ApiResponseVO<List<Map<String, Object>>> responseVO = new ApiResponseVO<>(
            collect);
        return responseVO;
    }

    public void quote(String currency, String quote) {
        listeningExecutorService.submit((Callable<Void>) () -> {
            String symbol = currency + quote;
            final HuobiAccountE huobiAccount = huobiFactory.huobiAccount();
            final HuobiSymbolQuoteE symbolQuoteE = SpringContextHolder
                .getBean(HuobiSymbolQuoteE.class);
            symbolQuoteE.setSymbol(symbol);
            symbolQuoteE.setAccountId(huobiAccount.getId());
            symbolQuoteE.setCurrency(currency);
            symbolQuoteE.setQuoteCurrency(quote);
            symbolQuoteE.load();
            return null;
        });
    }


    public ApiResponseVO<Void> taker(String accountId, String currency){
        final HuobiOrderE finalityHuobiOrder = huobiFactory
                .getFinalityHuobiOrder(accountId, currency+"eth");
        finalityHuobiOrder.setBalance(huobiAccountTunnel.getBalanceByCurrency(accountId, currency));
        finalityHuobiOrder.doTaker();
        return new ApiResponseVO();
    }

    public ApiResponseVO<Void> maker(String accountId, String currency){
        final HuobiOrderE finalityHuobiOrder = huobiFactory
                .getFinalityHuobiOrder(accountId, currency+"eth");
        finalityHuobiOrder.setBalance(huobiAccountTunnel.getBalanceByCurrency(accountId, currency));
        finalityHuobiOrder.doMaker();
        return new ApiResponseVO();
    }
}
