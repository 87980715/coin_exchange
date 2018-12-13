/**
 * @(#) HuobiAccountTunnelI.java
 * <p>
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel;

import com.coin.exchange.module.huobi.third.clientobject.AccountBalanceCO;
import com.coin.exchange.module.huobi.third.clientobject.AccountCO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author storys.zhang@gmail.com
 * <p>
 * Created at 2018/9/21 by Storys.Zhang in coin_exchange
 */
public interface HuobiAccountTunnelI {
    /**
     * 查询所有账户信息
     *
     * @return
     */
    List<AccountCO> findAll();

    /**
     * 查询账户余额信息
     *
     * @param accountId
     * @return
     */
    AccountBalanceCO getBalance(String accountId);

    /**
     * 查询账户信息
     *
     * @param id
     * @return
     */
    AccountCO get(String id);

    /**
     * 账户余额
     *
     * @param accountId
     * @param currency
     * @return
     */
    BigDecimal getBalanceByCurrency(String accountId, String currency);

    /**
     * 更新余额
     *
     * @param accountId
     */
    void updateBalance(String accountId);

}
