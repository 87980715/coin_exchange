package com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel;

import com.coin.exchange.module.huobi.infrastructure.tunnel.dataobject.TradeTraceDO;

/**
 * @author storys.zhang@gmail.com
 * <p>
 * Created at 2018/11/22 by Storys.Zhang in coin_exchange
 */
public interface TradeTracTunnelI {

    /**
     * insert
     *
     * @param record
     */
    void insert(TradeTraceDO record);

    /**
     * update
     *
     * @param record
     */
    void update(TradeTraceDO record);

    /**
     * get
     * @param id
     * @return
     */
    TradeTraceDO get(long id);
}
