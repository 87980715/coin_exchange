/**
 * @(#) AccountCO.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.third.clientobject;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 *
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/21 by Storys.Zhang in coin_exchange
 */
@Data
public class AccountCO {
    private Long id;

    private String type;

    private String state;

    @SerializedName(value = "user-id")
    private String userId;
}
