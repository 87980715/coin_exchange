/**
 * @(#) HuobiSymbolTunnelI.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel;

import com.coin.exchange.module.huobi.third.clientobject.SymbolCO;

/**
 *
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/21 by Storys.Zhang in coin_exchange
 */
public interface HuobiSymbolTunnelI {

    /**
     * 查询交易对信息
     *
     * @param symbol
     * @return
     */
    SymbolCO getSymbol(String symbol);
}
