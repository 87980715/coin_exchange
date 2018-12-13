/**
 * @(#) HuobiFactory.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.domian;

import com.coin.exchange.common.utils.SpringContextHolder;
import com.coin.exchange.module.huobi.domian.convertor.HuobiAccountConvertor;
import com.coin.exchange.module.huobi.domian.entity.HuobiAccountE;
import com.coin.exchange.module.huobi.domian.entity.HuobiOrderE;
import com.coin.exchange.module.huobi.dto.valueobject.AccountType;
import com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel.HuobiAccountTunnelI;
import com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel.HuobiSymbolTunnelI;
import com.coin.exchange.module.huobi.third.clientobject.AccountCO;
import com.coin.exchange.module.huobi.third.clientobject.SymbolCO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/25 by Storys.Zhang in coin_exchange
 */
@Component
public class HuobiFactory {

    @Autowired
    private HuobiAccountTunnelI huobiAccountTunnel;
    @Autowired
    private HuobiAccountConvertor huobiAccountConvertor;
    @Autowired
    private HuobiSymbolTunnelI huobiSymbolTunnel;

    public HuobiAccountE huobiAccount() {
        final List<AccountCO> all = huobiAccountTunnel.findAll();
        final AccountCO accountCO = all.stream()
            .filter(p -> p.getType().equalsIgnoreCase(AccountType.SPOT.toString())).findFirst().get();
        return huobiAccountConvertor.clientToEntity(accountCO);
    }

    public HuobiOrderE getHuobiOrder(String accountId, String symbol, String currency) {
        HuobiOrderE order = SpringContextHolder.getBean(HuobiOrderE.class);
        order.setAccountId(accountId);
        order.setBalance(huobiAccountTunnel.getBalanceByCurrency(accountId, currency));
        order.setSymbol(symbol);
        final SymbolCO symbolCO = huobiSymbolTunnel.getSymbol(symbol);
        order.setAmountPrecision(symbolCO.getAmountPrecision());
        return order;
    }

    public  HuobiOrderE getFinalityHuobiOrder(String accountId, String symbol){
        HuobiOrderE order = SpringContextHolder.getBean(HuobiOrderE.class);
        order.setAccountId(accountId);
        order.setSymbol(symbol);
        return order;
    }

}
