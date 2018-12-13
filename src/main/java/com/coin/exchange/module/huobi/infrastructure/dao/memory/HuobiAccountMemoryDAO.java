/**
 * @(#) HuobiAccountMemoryDAO.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.infrastructure.dao.memory;

import static com.coin.exchange.module.huobi.infrastructure.common.AppConstants.CACHE_KEY_PREFIX;
import com.coin.exchange.common.base.MemoryDAO;
import com.coin.exchange.common.utils.StringsHelper;
import com.coin.exchange.module.huobi.third.HuobiApiRetrofitFactory;
import com.coin.exchange.module.huobi.third.clientobject.AccountBalanceCO;
import com.coin.exchange.module.huobi.third.clientobject.AccountBalanceCO.Balance;
import com.coin.exchange.module.huobi.third.clientobject.AccountCO;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/21 by Storys.Zhang in coin_exchange
 */
@Component
public class HuobiAccountMemoryDAO implements MemoryDAO {

    @Autowired
    private HuobiApiRetrofitFactory huobiApiRetrofitFactory;

    private Cache<String, List<AccountCO>> cacheAccountList;

    @PostConstruct
    public void init() {
        cacheAccountList = CacheBuilder.newBuilder().maximumSize(64)
            .expireAfterAccess(12, TimeUnit.HOURS).build();
    }

    @SneakyThrows
    public List<AccountCO> findAll() {
        return cacheAccountList.get(cacheKey(),
            () -> huobiApiRetrofitFactory.huobiApi().accounts(true).execute().body()
                .getData()
        );
    }

    @SneakyThrows
    public AccountCO get(String id) {
        return findAll().stream().filter(p -> p.getId().toString().equalsIgnoreCase(id))
            .findAny().orElse(null);
    }

    @Override
    public String cacheKey() {
        return StringsHelper.join(CACHE_KEY_PREFIX, "account");
    }

}
