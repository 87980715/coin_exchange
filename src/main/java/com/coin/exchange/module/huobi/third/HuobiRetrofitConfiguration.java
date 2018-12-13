/**
 * @(#) HuobiRetrofitConfiguration.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.third;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/21 by Storys.Zhang in coin_exchange
 */
@Configuration
class HuobiRetrofitConfiguration {

    @Autowired
    @Qualifier("huobiOkHttpClient")
    private OkHttpClient okHttpClient;

    @Autowired
    private HuobiProperties huobiProperties;

    @Bean("huobiRetrofit")
    public Retrofit retrofit() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(huobiProperties.getBaseUrl())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        return retrofit;
    }

}
