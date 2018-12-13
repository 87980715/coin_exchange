package com.coin.exchange.module.huobi.domian.repository;

import com.coin.exchange.common.utils.Arith;
import com.coin.exchange.common.utils.LocalDateHelper;
import com.coin.exchange.module.huobi.domian.HuobiFactory;
import com.coin.exchange.module.huobi.dto.TradeTraceDTO;
import com.coin.exchange.module.huobi.dto.TradeTraceHandler;
import com.coin.exchange.module.huobi.infrastructure.tunnel.dataobject.TradeTraceDO;
import com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel.HuobiAccountTunnelI;
import com.coin.exchange.module.huobi.infrastructure.tunnel.datatunnel.TradeTracTunnelI;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author storys.zhang@gmail.com
 * <p>
 * Created at 2018/11/22 by Storys.Zhang in coin_exchange
 */
@Repository
public class TradeTraceRepository {

    @Autowired
    private TradeTracTunnelI tradeTracTunnel;

    @Autowired
    private HuobiAccountTunnelI huobiAccountTunnelI;

    public void init() {
        TradeTraceDTO tradeTraceDTO = TradeTraceHandler.threadLocal.get();
        Date currentTime = LocalDateHelper.getCurrentTime();
        BigDecimal balance = huobiAccountTunnelI.getBalanceByCurrency(tradeTraceDTO.getAccountId(), tradeTraceDTO.getQuoteCurrency());
        TradeTraceDO dataObject = new TradeTraceDO();
        dataObject.setBeginCurrency(tradeTraceDTO.getQuoteCurrency());
        dataObject.setBeginBalance(balance);
        dataObject.setBeginTradeTime(currentTime);
        dataObject.setSymbol(tradeTraceDTO.getSymbol());
        dataObject.setRemarks(tradeTraceDTO.getRemarks());
        tradeTracTunnel.insert(dataObject);
        tradeTraceDTO.setTradeTraceId(dataObject.getId());
    }

    public void complite() {
        TradeTraceDTO tradeTraceDTO = TradeTraceHandler.threadLocal.get();
        Date currentTime = LocalDateHelper.getCurrentTime();
        huobiAccountTunnelI.updateBalance(tradeTraceDTO.getAccountId());
        TradeTraceDO record = tradeTracTunnel.get(tradeTraceDTO.getTradeTraceId());
        record.setEndCurrency(record.getBeginCurrency());
        BigDecimal balance = huobiAccountTunnelI.getBalanceByCurrency(tradeTraceDTO.getAccountId(), record.getBeginCurrency());
        record.setEndBalance(balance);
        record.setEndTradeTime(currentTime);
        record.setRemarks(tradeTraceDTO.getRemarks());
        record.setIncome(Arith.sub(record.getEndBalance(), record.getBeginBalance()));
        record.setYieldRate(Arith.div(Arith.sub(record.getEndBalance(), record.getBeginBalance()), record.getBeginBalance()));
        tradeTracTunnel.update(record);
        TradeTraceHandler.threadLocal.remove();
    }

}
