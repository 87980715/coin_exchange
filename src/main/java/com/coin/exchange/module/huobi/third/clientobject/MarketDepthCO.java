/**
 * @(#) MarketDepthCO.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.third.clientobject;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/25 by Storys.Zhang in coin_exchange
 */
@Data
public class MarketDepthCO {

    /**
     * 消息id
     */
    private String id;

    private String symbol;

    /**
     * 拉取耗時
     */
    private BigDecimal pullElapsedMilliSecond;

    /**
     * 消息生成时间，单位：毫秒
     */
    private String ts;

    /**
     * 买盘,[price(成交价), amount(成交量)], 按price降序
     */
    private List<BigDecimal[]> bids;

    /**
     * 卖盘,[price(成交价), amount(成交量)], 按price升序
     */
    private List<BigDecimal[]> asks;

    private String createAt;

}
