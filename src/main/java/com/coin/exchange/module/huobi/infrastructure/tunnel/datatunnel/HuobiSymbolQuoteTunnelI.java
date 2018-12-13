/**
 * @(#) HuobiSymbolQuoteTunnelI.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel;

import com.coin.exchange.common.LimitQueue;
import com.coin.exchange.module.huobi.third.clientobject.MarketDepthCO;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/25 by Storys.Zhang in coin_exchange
 */
public interface HuobiSymbolQuoteTunnelI {

    /**
     * 所有交易对信息
     *
     * @return
     */
    ConcurrentMap<String, LimitQueue<MarketDepthCO>> findAll();

    /**
     * 查询一个交易对信息
     *
     * @param symbol
     * @return
     */
    LimitQueue<MarketDepthCO> get(String symbol);

    /**
     * 修改
     *
     * @param symbol
     * @param limitQueue
     */
    void update(String symbol, LimitQueue<MarketDepthCO> limitQueue);

    /**
     * 获取一个交易对信息
     *
     * @param symbol
     * @return
     */
    MarketDepthCO getMarketDepthCO(String symbol);

    /**
     * 删除
     *
     * @param symbol
     */
    void  delete(String symbol);
}
