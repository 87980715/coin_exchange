/**
 * @(#) SymbolCO.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.third.clientobject;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;
import lombok.Data;

/**
 *
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/21 by Storys.Zhang in coin_exchange
 */
@Data
public class SymbolCO {

    /**
     * 基础币种
     */
    @SerializedName("base-currency")
    private String baseCurrency;

    /**
     * 计价币种
     */
    @SerializedName("quote-currency")
    private String quoteCurrency;

    /**
     * 价格精度位数（0为个位)
     */
    @SerializedName("price-precision")
    private int pricePrecision;

    /**
     * 数量精度位数（0为个位）
     */
    @SerializedName("amount-precision")
    private int amountPrecision;

    /**
     * 交易区	main主区，innovation创新区，bifurcation分叉区
     */
    @SerializedName("symbol-partition")
    private String symbolPartition;

    private String symbol;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SymbolCO)) {
            return false;
        }
        SymbolCO symbolCO = (SymbolCO) o;
        return Objects.equals(getSymbol(), symbolCO.getSymbol());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSymbol());
    }
}
