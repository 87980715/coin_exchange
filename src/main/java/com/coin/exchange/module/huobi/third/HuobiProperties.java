/**
 * @(#) HuobiProperties.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.third;

import com.coin.exchange.common.utils.StringsHelper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/21 by Storys.Zhang in coin_exchange
 */
@Configuration
class HuobiProperties {
    /**
     * 参数定义方法是否需要进行签名
     */
    public static final String REQUIRE_SIGNATURE = "signature";

    @Getter
    @Value("${bourse.huobi.access-key}")
    private String accessKey;

    @Getter
    @Value("${bourse.huobi.secret-key}")
    private String secretKey;

    @Getter
    @Value("${bourse.huobi.domain-name}")
    private String domainName;

    @Getter
    @Value("${bourse.huobi.http-protocol}")
    private String httpProtocol;

    @Getter
    @Value("${bourse.huobi.okhttp.connect-timeout}")
    private long okhttpConnectTimeout;

    @Getter
    @Value("${bourse.huobi.okhttp.read-timeout}")
    private long okhttpReadTimeout;



    public String getBaseUrl(){
        return StringsHelper.join(getHttpProtocol(), "://", getDomainName());
    }
}
