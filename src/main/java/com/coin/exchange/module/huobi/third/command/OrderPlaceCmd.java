/**
 * @(#) OrderPlaceCmd.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.third.command;

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
@ApiModel(value = "下单操作")
public class OrderPlaceCmd {

    @ApiModelProperty(value = "账户 ID，使用accounts方法获得。币币交易使用‘spot’账户的accountid；借贷资产交易，请使用‘margin’账户的accountid'", required = true)
    @SerializedName(value = "account-id")
    private String accountId;

    @ApiModelProperty(value = "限价单表示下单数量，市价买单时表示买多少钱，市价卖单时表示卖多少币", required = true)
    private BigDecimal amount;

    @ApiModelProperty(value = "下单价格，市价单不传该参数")
    private BigDecimal price;

    @ApiModelProperty(value = "订单来源(api，如果使用借贷资产交易，请填写‘margin-api’)")
    private String source;

    @ApiModelProperty(value = "交易对")
    private String symbol;

    @ApiModelProperty(value =
        "订单类型(buy-market：市价买, sell-market：市价卖, buy-limit：限价买, sell-limit：限价卖, buy-ioc：IOC买单, sell-ioc：IOC卖单, buy-limit-maker, sell-limit-maker (详细说明见下)\n"
            + "buy-limit-maker\n"
            + "当“下单价格”>=“市场最低卖出价”，订单提交后，系统将拒绝接受此订单；\n"
            + "\n"
            + "当“下单价格”<“市场最低卖出价”，提交成功后，此订单将被系统接受。"
            + "sell-limit-maker\n"
            + "\n"
            + "当“下单价格”<=“市场最高买入价”，订单提交后，系统将拒绝接受此订单；\n"
            + "\n"
            + "当“下单价格”>“市场最高买入价”，提交成功后，此订单将被系统接受。)", required = true)
    private String type;
}
