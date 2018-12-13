package com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel.impl;

import com.coin.exchange.common.utils.LocalDateHelper;
import com.coin.exchange.module.huobi.infrastructure.dao.mybatis.TradeTraceMybatisDAO;
import com.coin.exchange.module.huobi.infrastructure.tunnel.dataobject.TradeTraceDO;
import com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel.TradeTracTunnelI;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author storys.zhang@gmail.com
 * <p>
 * Created at 2018/11/22 by Storys.Zhang in coin_exchange
 */
@Component
public class TradeTracTunnel implements TradeTracTunnelI {

    @Resource
    private TradeTraceMybatisDAO mybatisDAO;

    @Override
    public void insert(TradeTraceDO record) {
        record.setCreateTime(LocalDateHelper.getCurrentTime());
        mybatisDAO.insert(record);
    }

    @Override
    public void update(TradeTraceDO record) {
        mybatisDAO.update(record);
    }

    @Override
    public TradeTraceDO get(long id) {
        return mybatisDAO.get(id);
    }
}
