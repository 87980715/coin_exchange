/**
 * @(#) CoinExchangeApplication.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange;

import com.coin.exchange.common.utils.SpringContextHolder;
import com.coin.exchange.config.web.jackson.CustomBigDecimalSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/21 by Storys.Zhang in coin_exchange
 */
@Import(value = {SpringContextHolder.class})
@SpringBootApplication
public class CoinExchangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.coin.exchange.CoinExchangeApplication.class, args);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer initJackson(){
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.serializerByType(BigDecimal.class, new CustomBigDecimalSerializer   ());
    }
}
