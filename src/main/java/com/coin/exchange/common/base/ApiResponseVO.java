/**
 * @(#) ApiResponseVO.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.common.base;

import com.coin.exchange.common.utils.StringsHelper;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import lombok.Getter;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/25 by Storys.Zhang in coin_exchange
 */
@Getter
@ApiModel(value = "api返回信息")
public class ApiResponseVO<T> {

    @ApiModelProperty(value = "状态码，0：成功", required = true)
    @JsonProperty(value = "st")
    private Integer status;

    @ApiModelProperty(value = "提示消息")
    @JsonProperty(value = "msg")
    private String message;

    @Getter
    @ApiModelProperty(value = "错误码")
    @JsonProperty(value = "errCode")
    private String errorCode;

    @ApiModelProperty(value = "数据")
    private T data;

    public ApiResponseVO() {
        this.status = 0;
        this.message = "操作成功";
    }

    public ApiResponseVO(T data) {
        this(0, "操作成功", null, data);
    }

    public ApiResponseVO(Integer status, String message) {
        this(status, message, null, null);
    }

    public ApiResponseVO(Integer status, String message, String errorCode, T data) {
        this.status = Objects.isNull(status) ? -1 : status;
        this.message = StringsHelper.isBlank(message) ? "操作失败" : message;
        this.errorCode = errorCode;
        this.data = data;
    }

}
