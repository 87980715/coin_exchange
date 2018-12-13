package com.coin.exchange.common.utils;

import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author storys.zhang@gmail.com
 * <p>
 * Created at 2018/11/21 by Storys.Zhang in coin_exchange
 */
public class BigDecimalHelper {

    public static String printPercent(BigDecimal b) {
        b.stripTrailingZeros();
        return printPercent(b, 4);
    }
    public static String printPercent(BigDecimal b, int scale) {
        DecimalFormat df = new DecimalFormat("0." + Strings.repeat("0", scale) + "%");
        return df.format(b);
    }

    public static String printBigDecimal(BigDecimal b){
        return  b.stripTrailingZeros().toPlainString();
    }
}
