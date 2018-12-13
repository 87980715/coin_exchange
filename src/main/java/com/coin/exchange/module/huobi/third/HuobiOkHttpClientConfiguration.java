/**
 * @(#) HuobiOkHttpClientConfiguration.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.third;

import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/21 by Storys.Zhang in coin_exchange
 */
@Configuration
class HuobiOkHttpClientConfiguration {

    @Autowired
    private HuobiSignatureInterceptor huobiSignatureInterceptor;
    @Autowired
    private HuobiProperties huobiProperties;

    @Bean("huobiOkHttpClient")
    public OkHttpClient okHttpClient() {
        final ConnectionPool connectionPool = new ConnectionPool(5, 5, TimeUnit.MINUTES);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(huobiProperties.getOkhttpConnectTimeout(), TimeUnit.MILLISECONDS)
            .readTimeout(huobiProperties.getOkhttpReadTimeout(), TimeUnit.MILLISECONDS)
            .writeTimeout(1000, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .connectionPool(connectionPool)
            .addInterceptor(huobiSignatureInterceptor)
        ;
        return builder.build();
    }
}
