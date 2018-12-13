/**
 * @(#) ResponseCO.java
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
public class ResponseCO<T> {
    /**
     * ok, error
     */
    private String status;

    /**
     * 发送时间
     */
    private Long ts;

    /**
     * 数据
     */
    private T data;

    @SerializedName(value = "err-code")
    private String errCode;

    @SerializedName(value = "err-msg")
    private String errMsg;

    /**
     * 调用成功
     *
     * @return
     */
    public boolean isSuccess(){
        return "ok".equalsIgnoreCase(this.status);
    }

}
