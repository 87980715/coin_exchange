/**
 * @(#) OpenOrdersCO.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.third.clientobject;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/25 by Storys.Zhang in coin_exchange
 */
@Data
@ApiModel(value = "获取所有当前帐号下未成交订单")
public class OpenOrdersCO {

    @ApiModelProperty(value = "订单号")
    private long id;

    @ApiModelProperty(value = "交易对")
    private String symbol;

    @ApiModelProperty(value = "下单价格")
    private BigDecimal price;

    @ApiModelProperty(value = "下单时间（毫秒)Unix时间戳")
    @SerializedName(value = "created-at")
    private int createdAt;

    @ApiModelProperty(value = "订单类型(buy-market, sell-market, buy-limit, sell-limit, buy-ioc, sell-ioc)")
    private String type;

    @ApiModelProperty(value = "下单时间（毫秒）对于非“部分成交”订单，此字段为 0")
    @SerializedName(value = "filled-amount")
    private String filledAmount;

    @ApiModelProperty(value = "已成交部分的订单价格(=已成交单量x下单价格)对于非“部分成交”订单，此字段为 0")
    @SerializedName(value = "filled-cash-amount")
    private String filledCashAmount;

    @ApiModelProperty(value = "已成交部分所收取手续费(对于非“部分成交”订单，此字段为 0)")
    @SerializedName(value = "filled-fees")
    private String filledFees;

    @ApiModelProperty(value = "订单来源(sys, web, api, app)")
    private String source;

    @ApiModelProperty(value = "此订单状态(submitted（已提交）, partial-filled（部分成交）, cancelling（正在取消）)")
    private String state;
}
