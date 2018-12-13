package com.coin.exchange.module.huobi.domian;

import java.math.BigDecimal;

/**
 * @author storys.zhang@gmail.com
 * <p>
 * Created at 2018/11/23 by Storys.Zhang in coin_exchange
 */
public class HuobiQuoteConfig {

    /**
     * 最小涨幅买入，连续涨幅阀值执行买入操作
     */
    public static volatile BigDecimal MIN_GO_UP = new BigDecimal("0.002");
    /**
     * 最大涨幅卖出，单次最大涨幅卖出控制
     */
    public static volatile BigDecimal MAX_GO_UP = new BigDecimal("0.012");

    /**
     * 最小跌幅卖出，止盈
     */
    public static volatile BigDecimal MIN_GO_DOWN = new BigDecimal("0.05");
    /**
     * 最大跌幅卖出，止损阀值
     */
    public static volatile  BigDecimal MAX_GO_DOWN = new BigDecimal("0.02");

}
