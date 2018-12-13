/**
 * @(#) AppConstants.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.infrastructure.common;

import java.math.BigDecimal;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/21 by Storys.Zhang in coin_exchange
 */
public final class AppConstants {

    /**
     * 缓存的key前缀
     */
    public static final String CACHE_KEY_PREFIX = "huobi_";

    /**
     * 默认费率
     */
    public static final BigDecimal DEFAULT_FEE = new BigDecimal("0.002");
}
