/**
 * @(#) ResponseTickCO.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.third.clientobject;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/25 by Storys.Zhang in coin_exchange
 */
@Data
public class ResponseTickCO<T> {

    /**
     * ok, error
     */
    private String status;

    /**
     * 数据所属的 channel，格式： market.$symbol.trade.detail
     */
    private String ch;

    /**
     * 发送时间
     */
    private Long ts;

    /**
     * 数据
     */
    private T tick;

    @SerializedName(value = "err-code")
    private String errCode;

    @SerializedName(value = "err-msg")
    private String errMsg;

    /**
     * 调用成功
     *
     * @return
     */
    public boolean isSuccess() {
        return "ok".equalsIgnoreCase(this.status);
    }
}
