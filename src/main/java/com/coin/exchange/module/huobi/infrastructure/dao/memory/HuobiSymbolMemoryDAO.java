/**
 * @(#) HuobiSymbolMemoryDAO.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.infrastructure.dao.memory;

import static com.coin.exchange.module.huobi.infrastructure.common.AppConstants.CACHE_KEY_PREFIX;
import com.coin.exchange.common.base.MemoryDAO;
import com.coin.exchange.common.utils.StringsHelper;
import com.coin.exchange.module.huobi.third.HuobiApiRetrofitFactory;
import com.coin.exchange.module.huobi.third.clientobject.ResponseCO;
import com.coin.exchange.module.huobi.third.clientobject.SymbolCO;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/21 by Storys.Zhang in coin_exchange
 */
@Component
public class HuobiSymbolMemoryDAO implements MemoryDAO {

    @Autowired
    private HuobiApiRetrofitFactory huobiApiRetrofitFactory;

    private Cache<String, List<SymbolCO>> cacheSymbolList;

    @PostConstruct
    public void init() {
        cacheSymbolList = CacheBuilder.newBuilder().maximumSize(64)
            .expireAfterWrite(12, TimeUnit.HOURS).build();
    }
    @SneakyThrows
    public List<SymbolCO> findAllSymbol() {
        return cacheSymbolList.get(cacheKey(), () -> {
            final ResponseCO<List<SymbolCO>> symbols = huobiApiRetrofitFactory
                .huobiApi().symbols().execute().body();
            return symbols.getData();
        });
    }

    @Override
    public String cacheKey() {
        return StringsHelper.join(CACHE_KEY_PREFIX, "symbol");
    }
}
