/**
 * @(#) HuobiApiRetrofitFactory.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.third;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/21 by Storys.Zhang in coin_exchange
 */
@Component
public class HuobiApiRetrofitFactory {

    @Autowired
    @Qualifier("huobiRetrofit")
    private Retrofit retrofit;

    public HuobiApi huobiApi() {
        return retrofit.create(HuobiApi.class);
    }
}
