/**
 * @(#) HuobiAccountTunnel.java
 * <p>
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel.impl;

import com.coin.exchange.module.huobi.infrastructure.dao.memory.HuobiAccountBalanceMemoryDAO;
import com.coin.exchange.module.huobi.infrastructure.dao.memory.HuobiAccountMemoryDAO;
import com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel.HuobiAccountTunnelI;
import com.coin.exchange.module.huobi.third.clientobject.AccountBalanceCO;
import com.coin.exchange.module.huobi.third.clientobject.AccountCO;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/21 by Storys.Zhang in coin_exchange
 */
@Component
public class HuobiAccountTunnel implements HuobiAccountTunnelI {

    @Autowired
    private HuobiAccountMemoryDAO memoryDAO;

    @Autowired
    private HuobiAccountBalanceMemoryDAO huobiAccountBalanceMemoryDAO;

    @Override
    public List<AccountCO> findAll() {
        return memoryDAO.findAll();
    }

    @Override
    public AccountBalanceCO getBalance(String accountId) {
        return huobiAccountBalanceMemoryDAO.getBalance(accountId);
    }

    @Override
    public AccountCO get(String id) {
        return memoryDAO.get(id);
    }

    @Override
    public BigDecimal getBalanceByCurrency(String accountId, String currency) {
        return huobiAccountBalanceMemoryDAO.getBalanceByCurrency(accountId, currency);
    }

    @Override
    public void updateBalance(String accountId) {
        huobiAccountBalanceMemoryDAO.updateBalance(accountId);
    }
}
