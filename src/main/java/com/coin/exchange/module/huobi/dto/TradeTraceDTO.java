package com.coin.exchange.module.huobi.dto;

import lombok.Data;

/**
 * @author storys.zhang@gmail.com
 * <p>
 * Created at 2018/11/22 by Storys.Zhang in coin_exchange
 */
@Data
public class TradeTraceDTO {

    private String accountId;

    private String symbol;

    private String quoteCurrency;

    private Long tradeTraceId;

    private String remarks;

}
