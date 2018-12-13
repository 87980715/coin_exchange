package com.coin.exchange.module.huobi.infrastructure.tunnel.dataobject;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author storys.zhang@gmail.com
 * <p>
 * Created at 2018/11/22 by Storys.Zhang in coin_exchange
 */
@Data
public class TradeTraceDO {

    /** 唯一ID */
    public long id;

    /** 交易对 */
    public String symbol;

    /** 起始货币 */
    public String beginCurrency;

    /** 起始余额 */
    public BigDecimal beginBalance;

    /** 结束货币 */
    public String endCurrency;

    /** 结束余额 */
    public BigDecimal endBalance;

    /** 收益 */
    public BigDecimal income;

    /** 收益率 */
    public BigDecimal yieldRate;

    /** 开始交易时间 */
    public Date beginTradeTime;

    /** 结束交易时间 */
    public Date endTradeTime;

    /** 创建时间 */
    public Date createTime;

    private String remarks;
}
