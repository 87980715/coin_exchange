/**
 * @(#) OrderCO.java
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
@ApiModel(value = "订单详情")
public class OrderCO {

    @ApiModelProperty(value = "订单ID")
    private long id;

    @ApiModelProperty(value = "交易对")
    private String symbol;

    @ApiModelProperty(value = "账户 ID")
    @SerializedName("account-id")
    private long accountId;

    @ApiModelProperty(value = "订单数量")
    private BigDecimal amount;

    @ApiModelProperty(value = "订单价格")
    private BigDecimal price;

    @ApiModelProperty(value = "订单创建时间")
    @SerializedName("created-at")
    private long createdAt;

    @ApiModelProperty(value = "订单类型(buy-market：市价买, sell-market：市价卖, buy-limit：限价买, sell-limit：限价卖, buy-ioc：IOC买单, sell-ioc：IOC卖单)")
    private String type;

    @ApiModelProperty(value = "已成交数量")
    @SerializedName("field-amount")
    private BigDecimal fieldAmount;

    @ApiModelProperty(value = "已成交总金额")
    @SerializedName("field-cash-amount")
    private BigDecimal fieldCashAmount;

    @ApiModelProperty(value = "已成交手续费（买入为币，卖出为钱）")
    @SerializedName("field-fees")
    private BigDecimal fieldFees;

    @ApiModelProperty(value = "订单变为终结态的时间，不是成交时间，包含“已撤单”状态")
    @SerializedName("finished-at")
    private long finishedAt;

    @ApiModelProperty(value = "userId")
    @SerializedName("user-id")
    private int userId;

    @ApiModelProperty(value = "订单来源")
    private String source;

    @ApiModelProperty(value = "订单状态(submitting , submitted 已提交, partial-filled 部分成交, partial-canceled 部分成交撤销, filled 完全成交, canceled 已撤销)")
    private String state;

    @ApiModelProperty(value = "订单撤销时间")
    @SerializedName("canceled-at")
    private long canceledAt;

    @ApiModelProperty(value = "exchange")
    private String exchange;

    @ApiModelProperty(value = "batchzi")
    private String batch;
}
