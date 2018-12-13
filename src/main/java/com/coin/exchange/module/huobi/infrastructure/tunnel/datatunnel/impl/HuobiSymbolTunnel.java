/**
 * @(#) HuobiSymbolTunnel.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel.impl;

import com.coin.exchange.module.huobi.third.clientobject.SymbolCO;
import com.coin.exchange.module.huobi.infrastructure.dao.memory.HuobiSymbolMemoryDAO;
import com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel.HuobiSymbolTunnelI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/21 by Storys.Zhang in coin_exchange
 */
@Component
public class HuobiSymbolTunnel implements HuobiSymbolTunnelI {

    @Autowired
    private HuobiSymbolMemoryDAO memoryDao;

    @Override
    public SymbolCO getSymbol(String symbol) {
        return memoryDao.findAllSymbol().stream()
            .filter(p -> p.getSymbol().equalsIgnoreCase(symbol)).findAny().orElse(null);
    }
}
