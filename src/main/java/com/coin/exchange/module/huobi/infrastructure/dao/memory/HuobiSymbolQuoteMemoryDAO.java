/**
 * @(#) HuobiSymbolQuoteMemoryDAO.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.infrastructure.dao.memory;

import static com.coin.exchange.module.huobi.infrastructure.common.AppConstants.CACHE_KEY_PREFIX;
import com.coin.exchange.common.LimitQueue;
import com.coin.exchange.common.base.MemoryDAO;
import com.coin.exchange.common.utils.StringsHelper;
import com.coin.exchange.module.huobi.third.HuobiApiRetrofitFactory;
import com.coin.exchange.module.huobi.third.clientobject.MarketDepthCO;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/25 by Storys.Zhang in coin_exchange
 */
@Component
public class HuobiSymbolQuoteMemoryDAO implements MemoryDAO {

    @Autowired
    private HuobiApiRetrofitFactory huobiApiRetrofitFactory;

    private Cache<String, LimitQueue<MarketDepthCO>> cacheSymbolQuoteList;

    @PostConstruct
    public void init() {
        cacheSymbolQuoteList = CacheBuilder.newBuilder().maximumSize(128)
            .expireAfterWrite(12, TimeUnit.HOURS).build();
    }

    public LimitQueue<MarketDepthCO> get(String symbol) {
        return cacheSymbolQuoteList.getIfPresent(cacheKey() + symbol);
    }

    public ConcurrentMap<String, LimitQueue<MarketDepthCO>> findAll() {
        return cacheSymbolQuoteList.asMap();
    }

    @SneakyThrows
    public MarketDepthCO getMarketDepthCO(String symbol){
        return huobiApiRetrofitFactory.huobiApi()
            .depth(symbol.toLowerCase(), "step0").execute().body().getTick();
    }

    public void update(String symbol, LimitQueue<MarketDepthCO> limitQueue) {
        cacheSymbolQuoteList.put(cacheKey() + symbol, limitQueue);
    }

    public void  delete(String symbol){
        cacheSymbolQuoteList.invalidate(cacheKey() + symbol);
    }

    @Override
    public String cacheKey() {
        return StringsHelper.join(CACHE_KEY_PREFIX, "quote_");
    }
}
