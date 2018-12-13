package com.coin.exchange.module.huobi.infrastructure.dao.mybatis;

import com.coin.exchange.module.huobi.infrastructure.tunnel.dataobject.TradeTraceDO;

/**
 * @author storys.zhang@gmail.com
 * <p>
 * Created at 2018/11/22 by Storys.Zhang in coin_exchange
 */
public interface TradeTraceMybatisDAO {

    int insert(TradeTraceDO record);

    int update(TradeTraceDO record);

    TradeTraceDO get(long id);
}
