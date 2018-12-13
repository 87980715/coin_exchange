/**
 * @(#) OrderSubmitResultBO.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/25 by Storys.Zhang in coin_exchange
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSubmitResultBO {

    private String orderId;

    private String symbol;

    private String errCode;

    private String errMsg;
}
