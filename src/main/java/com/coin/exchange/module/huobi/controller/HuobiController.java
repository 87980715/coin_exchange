/**
 * @(#) HuobiController.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.controller;

import com.coin.exchange.common.base.ApiResponseVO;
import com.coin.exchange.module.huobi.domian.HuobiQuoteConfig;
import com.coin.exchange.module.huobi.service.HuobiService;
import com.coin.exchange.module.huobi.third.clientobject.AccountBalanceCO;
import com.coin.exchange.module.huobi.third.clientobject.OrderCO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/25 by Storys.Zhang in coin_exchange
 */
@Api(tags = {"火币API"})
@RestController
@RequestMapping(value = "/huobi")
public class HuobiController {

    @Autowired
    private HuobiService huobiService;

    @ApiOperation(value = "账户信息", tags = "报价")
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public ApiResponseVO<AccountBalanceCO> accounts() {
        return huobiService.accounts();
    }

    @ApiOperation(value = "所有交易对计价数据")
    @RequestMapping(value = "/symbolquote", method = RequestMethod.GET)
    public ApiResponseVO<List<Map<String, Object>>> finadAllSymbolQuote() {
        return huobiService.finadAllSymbolQuote();
    }


    @ApiOperation(value = "报价", tags = "报价")
    @RequestMapping(value = "/quote", method = RequestMethod.POST)
    public ApiResponseVO<AccountBalanceCO> quote(@ApiParam(required = true, value = "代币", defaultValue = "ETH")  @RequestParam("currency") String currency,
                                                 @ApiParam(required = true, value = "计价代币", defaultValue = "USDT")  @RequestParam("quote") String quote) {
        huobiService.quote(currency.toLowerCase(), quote.toLowerCase());
        return new ApiResponseVO<>();
    }

    @ApiOperation(value = "报价阀值百分比设置", tags = "报价")
    @RequestMapping(value = "/quote/config", method = RequestMethod.POST)
    public ApiResponseVO<Void> config(@ApiParam(required = true, value = "最小涨幅买入，连续涨幅阀值执行买入操作", defaultValue = "0.002") @RequestParam("minGoUp") BigDecimal minGoUp,
                                      @ApiParam(required = true, value = "最大涨幅卖出，单次最大涨幅卖出控制", defaultValue = "0.012")  @RequestParam("maxGoUp") BigDecimal maxGoUp,
                                      @ApiParam(required = true, value = "最小跌幅卖出，已买入等待卖出时出现下跌，还没到止损阀值", defaultValue = "0.02") @RequestParam("minGoDown") BigDecimal minGoDown,
                                      @ApiParam(required = true, value = " 最大跌幅卖出，止损阀值", defaultValue = "0.05") @RequestParam("maxGoDown")  BigDecimal maxGoDown){
        HuobiQuoteConfig.MAX_GO_DOWN = maxGoDown;
        HuobiQuoteConfig.MAX_GO_UP = maxGoUp;
        HuobiQuoteConfig.MIN_GO_DOWN = minGoDown;
        HuobiQuoteConfig.MIN_GO_UP = minGoUp;
        return new ApiResponseVO<>();
    }

    @ApiOperation(value = "卖出")
    @RequestMapping(value = "/taker", method = RequestMethod.POST)
    public ApiResponseVO<Void> taker(@ApiParam(required = true, value = "账户id", defaultValue = "5002628") @RequestParam("accountId") String accountId,
                                        @ApiParam(required = true, value = "要卖出的代币")  @RequestParam("currency") String currency) {
        return huobiService.taker(accountId, currency);
    }

    @ApiOperation(value = "买入")
    @RequestMapping(value = "/maker", method = RequestMethod.POST)
    public ApiResponseVO<Void> maker(@ApiParam(required = true, value = "账户id", defaultValue = "5002628") @RequestParam("accountId") String accountId,
                                        @ApiParam(required = true, value = "要买入的代币")  @RequestParam("currency") String currency) {
        return huobiService.maker(accountId, currency);
    }
}
