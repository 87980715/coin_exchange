package com.coin.exchange.module.huobi.infrastructure.dao.memory;

import com.coin.exchange.common.base.MemoryDAO;
import com.coin.exchange.common.utils.StringsHelper;
import com.coin.exchange.module.huobi.third.HuobiApiRetrofitFactory;
import com.coin.exchange.module.huobi.third.clientobject.AccountBalanceCO;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.coin.exchange.module.huobi.infrastructure.common.AppConstants.CACHE_KEY_PREFIX;

/**
 * @author storys.zhang@gmail.com
 * <p>
 * Created at 2018/11/22 by Storys.Zhang in coin_exchange
 */
@Component
public class HuobiAccountBalanceMemoryDAO implements MemoryDAO {

    @Autowired
    private HuobiApiRetrofitFactory huobiApiRetrofitFactory;

    private Cache<String, AccountBalanceCO> cacheAccountBalanceList;

    @PostConstruct
    public void init() {
        cacheAccountBalanceList = CacheBuilder.newBuilder().maximumSize(64)
                .expireAfterAccess(1, TimeUnit.HOURS).build();
    }

    @SneakyThrows
    public AccountBalanceCO getBalance(String accountId) {
        return cacheAccountBalanceList.get(cacheKey() + accountId, () -> huobiApiRetrofitFactory.huobiApi().balance(accountId, true).execute()
                .body().getData());
    }

    public BigDecimal getBalanceByCurrency(String accountId, String currency) {
        final Optional<AccountBalanceCO.Balance> trade = getBalance(accountId).getList().stream().filter(
                p -> p.getCurrency().equalsIgnoreCase(currency) && p.getType()
                        .equalsIgnoreCase("TRADE")).findAny();
        return trade.isPresent() ? trade.get().getBalance() : BigDecimal.ZERO;

    }

    public void updateBalance(String accountId) {
        cacheAccountBalanceList.invalidate(cacheKey() + accountId);
        getBalance(accountId);
    }

    @Override
    public String cacheKey() {
        return StringsHelper.join(CACHE_KEY_PREFIX, "account_balance_");
    }
}
