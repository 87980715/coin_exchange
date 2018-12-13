package com.coin.exchange;

import com.coin.exchange.module.huobi.infrastructure.tunnel.dataobject.TradeTraceDO;
import com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel.HuobiSymbolTunnelI;
import com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel.TradeTracTunnelI;
import com.coin.exchange.module.huobi.third.clientobject.SymbolCO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CoinExchangeApplicationTests {

    @Autowired
    private TradeTracTunnelI tradeTracTunnel;
    @Test
    public void contextLoads() {
        TradeTraceDO tradeTraceDO = tradeTracTunnel.get(8);
        System.out.println(tradeTraceDO);
    }

}
