/**
 * @(#) HuobiOrderE.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.domian.entity;

import com.coin.exchange.common.utils.Arith;
import com.coin.exchange.common.utils.BigDecimalHelper;
import com.coin.exchange.common.utils.StringsHelper;
import com.coin.exchange.module.huobi.dto.OrderSubmitResultBO;
import com.coin.exchange.module.huobi.third.HuobiApiRetrofitFactory;
import com.coin.exchange.module.huobi.third.clientobject.OrderCO;
import com.coin.exchange.module.huobi.third.clientobject.ResponseCO;
import com.coin.exchange.module.huobi.third.command.OrderPlaceCmd;
import com.google.common.util.concurrent.RateLimiter;
import java.math.BigDecimal;

import jdk.nashorn.internal.ir.IfNode;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/25 by Storys.Zhang in coin_exchange
 */
@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HuobiOrderE {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String accountId;

    @Getter
    @Setter
    private String symbol;

    @Setter
    private int amountPrecision;

    @Setter
    @Getter
    private BigDecimal balance;

    /**
     * 1s请求1次
     */
    private RateLimiter limiter = RateLimiter.create(10);

    @Autowired
    private HuobiApiRetrofitFactory huobiApiRetrofitFactory;

    /**
     * 卖出下单
     *
     * 买盘,[price(成交价), amount(成交量)], 按price降序
     *
     * @return 订单ID
     */
    @SneakyThrows
    private OrderSubmitResultBO taker() {
        OrderPlaceCmd cmd = new OrderPlaceCmd();
        cmd.setAccountId(accountId);
        cmd.setAmount(Arith.subBigDecimal(balance, amountPrecision));
        cmd.setSource("api");
        cmd.setSymbol(this.symbol);
        cmd.setType("sell-market");
        final ResponseCO<String> response = huobiApiRetrofitFactory.huobiApi().orderPlace(cmd, true)
            .execute().body();
        if (!response.isSuccess()) {
            log.error("火币下单卖出{}失败,balance={}, errorCode = {},message={}", this.symbol,
                this.balance.toPlainString(),
                response.getErrCode(), response.getErrMsg());
            return OrderSubmitResultBO.builder().errCode(response.getErrCode())
                .errMsg(response.getErrMsg()).symbol(this.symbol).build();
        }
        if (StringsHelper.isNotBlank(response.getData())) {
            this.id = response.getData();
            return OrderSubmitResultBO.builder().errCode(response.getErrCode())
                .errMsg(response.getErrMsg()).orderId(response.getData()).symbol(this.symbol)
                .build();
        }
        return null;
    }

    /**
     * 买入下单
     *
     * 卖盘,[price(成交价), amount(成交量)], 按price升序
     *
     * @return 订单ID
     */
    @SneakyThrows
    private OrderSubmitResultBO maker(BigDecimal amount) {
        try {
            OrderPlaceCmd cmd = new OrderPlaceCmd();
            cmd.setAccountId(accountId);
            cmd.setAmount(Arith.subBigDecimal(amount, amountPrecision));
            cmd.setSource("api");
            cmd.setSymbol(this.symbol);
            cmd.setType("buy-market");
            ResponseCO<String> response = huobiApiRetrofitFactory.huobiApi().orderPlace(cmd, true)
                .execute().body();
            if (!response.isSuccess()) {
                log.error("火币下单购买{}失败balance={}, errorCode = {},message={}", this.symbol,
                    this.balance.toPlainString(),
                    response.getErrCode(), response.getErrMsg());
                return OrderSubmitResultBO.builder().errCode(response.getErrCode())
                    .errMsg(response.getErrMsg()).symbol(this.symbol).build();
            }
            if (StringsHelper.isNotBlank(response.getData())) {
                this.id = response.getData();
                return OrderSubmitResultBO.builder().errCode(response.getErrCode())
                    .errMsg(response.getErrMsg()).orderId(response.getData())
                    .symbol(this.symbol)
                    .build();
            }
            return null;
        } catch (Exception e) {
            log.error("火币下单购买{}失败balance={}", this.symbol, this.balance.toPlainString(), e);
            return null;
        }
    }

    @SneakyThrows
    private OrderSubmitResultBO cancele() {
        try {
            final ResponseCO<String> response = huobiApiRetrofitFactory.huobiApi()
                .submitCancel(this.id, true).execute().body();
            if (!response.isSuccess()) {
                log.error("火币{}撤单失败,message={},code={}", this.symbol, response.getErrMsg(),
                    response.getErrCode());
                return OrderSubmitResultBO.builder().errCode(response.getErrCode())
                    .errMsg(response.getErrMsg()).symbol(this.symbol).build();
            }
            if (StringsHelper.isNotBlank(response.getData())) {
                this.id = response.getData();
                return OrderSubmitResultBO.builder().errCode(response.getErrCode())
                    .errMsg(response.getErrMsg()).orderId(response.getData())
                    .symbol(this.symbol)
                    .build();
            }
            return null;
        } catch (Exception e) {
            log.error("火币撤单{}失败balance={}", this.symbol, this.balance.toPlainString(), e);
            return null;
        }
    }

    @SneakyThrows
    public OrderCO queryOrder() {
        final ResponseCO<OrderCO> response = huobiApiRetrofitFactory.huobiApi()
            .orderDetail(this.id, true).execute().body();
        if (!response.isSuccess()) {
            log.error("火币查询订单失败,message={}", response.getErrMsg());
        }
        return response.getData();
    }

    public boolean queryOrderStatus(String status) {
        int index = 10;
        while (index > 0) {
            try {
                limiter.acquire();
                final OrderCO orderCO = queryOrder();
                log.info("symbol={}, orderId={}, status={}, price={}, fieldCashAmount={}, fieldFees={}", this.symbol, orderCO.getId(),
                    orderCO.getState(), orderCO.getPrice(), orderCO.getFieldCashAmount(), orderCO.getFieldFees());
                if (orderCO.getState().equalsIgnoreCase(status)) {
                    return Boolean.TRUE;
                }
            } catch (Exception e) {
                log.error("查询订单信息失败", e);
            }
            index--;
        }
        return Boolean.FALSE;
    }

    public Boolean doMaker(){
        BigDecimal amount = balance.compareTo(new BigDecimal(20)) > 0 ? new BigDecimal(10) : balance;
        final OrderSubmitResultBO maker = maker(amount);
        String orderId = maker.getOrderId();
        log.info("执行买入OrderId={}, 买入前余额:{}", orderId, BigDecimalHelper.printBigDecimal(balance));
        if (StringsHelper.isNotBlank(orderId)){
            final boolean price = queryOrderStatus("filled");
            if (!price){
                log.info("执行买入OrderId={},成交失败", orderId);
                final OrderSubmitResultBO cancele = cancele();
                if (StringsHelper.isNotBlank(cancele.getOrderId())){
                    queryOrderStatus("canceled");
                }
            }else {
                log.info("执行买入OrderId={},全部成交成功");
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public boolean doTaker(){
        final OrderSubmitResultBO taker = taker();
        String orderId = taker.getOrderId();
        log.info("执行卖出OrderId={}", orderId);
        if (StringsHelper.isNotBlank(orderId)){
            final Boolean price = queryOrderStatus("filled");
            if (!price){
                log.info("执行买入OrderId={},成交失败", orderId);
                final OrderSubmitResultBO cancele = cancele();
                if (StringsHelper.isNotBlank(cancele.getOrderId())){
                    queryOrderStatus("canceled");
                }
            }else {
                log.info("执行卖出OrderId={},全部成交成功", orderId);
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
