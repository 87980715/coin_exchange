/**
 * @(#) HuobiSymbolQuoteTunnel.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel.impl;

import com.coin.exchange.common.LimitQueue;
import com.coin.exchange.module.huobi.infrastructure.dao.memory.HuobiSymbolQuoteMemoryDAO;
import com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel.HuobiSymbolQuoteTunnelI;
import com.coin.exchange.module.huobi.third.clientobject.MarketDepthCO;
import java.util.concurrent.ConcurrentMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/25 by Storys.Zhang in coin_exchange
 */
@Component
public class HuobiSymbolQuoteTunnel implements HuobiSymbolQuoteTunnelI {

    @Autowired
    private HuobiSymbolQuoteMemoryDAO memoryDAO;

    @Override
    public ConcurrentMap<String, LimitQueue<MarketDepthCO>> findAll() {
        return memoryDAO.findAll();
    }

    @Override
    public LimitQueue<MarketDepthCO> get(String symbol) {
        return memoryDAO.get(symbol);
    }

    @Override
    public void update(String symbol, LimitQueue<MarketDepthCO> limitQueue) {
        memoryDAO.update(symbol, limitQueue);
    }

    @Override
    public MarketDepthCO getMarketDepthCO(String symbol) {
        return memoryDAO.getMarketDepthCO(symbol);
    }

    @Override
    public void delete(String symbol) {
        memoryDAO.delete(symbol);
    }
}
