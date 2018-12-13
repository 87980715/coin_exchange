/**
 * @(#) HuobiAccountConvertor.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.domian.convertor;

import com.coin.exchange.common.utils.SpringContextHolder;
import com.coin.exchange.module.huobi.domian.entity.HuobiAccountE;
import com.coin.exchange.module.huobi.third.clientobject.AccountCO;
import org.springframework.stereotype.Component;

/**
 *
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/25 by Storys.Zhang in coin_exchange
 */
@Component
public class HuobiAccountConvertor {

    public HuobiAccountE clientToEntity(AccountCO client){
        final HuobiAccountE account = SpringContextHolder.getBean(HuobiAccountE.class);
        account.setId(client.getId().toString());
        return account;
    }
}
