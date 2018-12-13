/**
 * @(#) StringsHelper.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.common.utils;

import com.google.common.base.Strings;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/21 by Storys.Zhang in coin_exchange
 */
public final class StringsHelper {

    /**
     * 字符串不为空
     *
     * @param cs
     * @return
     */
    public static boolean isBlank(String cs) {
        return Strings.isNullOrEmpty(cs);
    }

    /**
     * 字符串不为空
     *
     * @param cs
     * @return
     */
    public static boolean isNotBlank(String cs) {
        return !Strings.isNullOrEmpty(cs);
    }

    /**
     * 多个字符串拼接
     *
     * @param s
     * @return
     */
    public static String join(Object... s) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < s.length; i++) {
            stringBuffer.append(s[i]);
        }
        return stringBuffer.toString();
    }
}
