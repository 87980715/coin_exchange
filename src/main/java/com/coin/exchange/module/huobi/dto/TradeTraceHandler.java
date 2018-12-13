package com.coin.exchange.module.huobi.dto;

/**
 * @author storys.zhang@gmail.com
 * <p>
 * Created at 2018/11/22 by Storys.Zhang in coin_exchange
 */
public class TradeTraceHandler {

    public static ThreadLocal<TradeTraceDTO> threadLocal = new ThreadLocal<>();

    public TradeTraceHandler(){
        threadLocal.set(new TradeTraceDTO());
    }

    public void init(String accountId, String symbol, String quoteCurrency, String remarks){
        threadLocal.get().setSymbol(symbol);
        threadLocal.get().setQuoteCurrency(quoteCurrency);
        threadLocal.get().setAccountId(accountId);
        threadLocal.get().setRemarks(remarks);
    }

}
