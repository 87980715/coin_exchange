/**
 * @(#) AccountType.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.dto.valueobject;

/**
 *
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/25 by Storys.Zhang in coin_exchange
 */
public enum  AccountType {
    /**
     * 现货账户
     */
    SPOT,
    /**
     * 杠杆账户
     */
    MARGIN,
    /**
     * OTC账户
     */
    OTC,
    /**
     * 点卡账户
     */
    POINT
    ;
}
