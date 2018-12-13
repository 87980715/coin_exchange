/**
 * @(#) AccountBalanceCO.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.third.clientobject;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/21 by Storys.Zhang in coin_exchange
 */
@Data
public class AccountBalanceCO {
    /**
     * 账户 ID
     */
    private Long id;

    /**
     * 账户类型	spot：现货账户
     */
    private String type;

    /**
     * 账户状态	working：正常 lock：账户被锁定
     */
    private String state;

    @SerializedName("user-id")
    private Long userId;

    /**
     * 子账户数组
     */
    @SerializedName("list")
    private java.util.List<Balance> list;

    @Data
    public static class Balance {

        /**
         * 币种
         */
        private String currency;

        /**
         * 类型	trade: 交易余额，frozen: 冻结余额
         */
        private String type;

        /**
         * 余额
         */
        private BigDecimal balance;
    }
}
