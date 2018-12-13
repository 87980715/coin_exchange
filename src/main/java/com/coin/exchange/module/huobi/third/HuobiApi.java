/**
 * @(#) HuobiApi.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.third;

import com.coin.exchange.module.huobi.third.clientobject.AccountBalanceCO;
import com.coin.exchange.module.huobi.third.clientobject.AccountCO;
import com.coin.exchange.module.huobi.third.clientobject.MarketDepthCO;
import com.coin.exchange.module.huobi.third.clientobject.OpenOrdersCO;
import com.coin.exchange.module.huobi.third.clientobject.OrderCO;
import com.coin.exchange.module.huobi.third.clientobject.ResponseCO;
import com.coin.exchange.module.huobi.third.clientobject.ResponseTickCO;
import com.coin.exchange.module.huobi.third.clientobject.SymbolCO;
import com.coin.exchange.module.huobi.third.command.OrderPlaceCmd;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 *
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/21 by Storys.Zhang in coin_exchange
 */
public interface HuobiApi {

    /**
     * 查询支持的所有交易对及精度
     *
     * @return
     */
    @GET("/v1/common/symbols")
    Call<ResponseCO<List<SymbolCO>>> symbols();


    /**
     * 查询当前用户的所有账户(即account-id)，Pro站和HADAX account-id通用
     *
     * @param signature 必须签名
     * @return
     */
    @GET("/v1/account/accounts")
    Call<ResponseCO<List<AccountCO>>> accounts(
        @Header(HuobiProperties.REQUIRE_SIGNATURE) boolean signature);


    /**
     * 查询Pro站指定账户的余额
     *
     * @param accountId
     * @param signature 必须签名
     * @return
     */
    @GET("/v1/account/accounts/{account-id}/balance")
    Call<ResponseCO<AccountBalanceCO>> balance(@Path("account-id") String accountId,
        @Header(HuobiProperties.REQUIRE_SIGNATURE) boolean signature);

    /**
     * 市场深度行情（单个symbol）
     *
     * @param symbol 交易对
     * @param type Depth 类型		step0, step1, step2, step3, step4, step5（合并深度0-5）；step0时，不合并深度
     * @return
     */
    @GET("/market/depth")
    Call<ResponseTickCO<MarketDepthCO>> depth(@Query("symbol") String symbol, @Query("type") String type);

    /**
     * 下单
     *
     * @param cmd
     * @param signature  签名
     * @return 返回订单ID
     */
    @POST("/v1/order/orders/place")
    Call<ResponseCO<String>> orderPlace(@Body OrderPlaceCmd cmd, @Header(HuobiProperties.REQUIRE_SIGNATURE) boolean signature);


    /**
     * 申请撤销一个订单请求
     *
     * @param orderId
     * @param signature  签名
     * @return 订单 ID
     */
    @POST("/v1/order/orders/{order-id}/submitcancel")
    Call<ResponseCO<String>> submitCancel(@Path("order-id") String orderId, @Header(HuobiProperties.REQUIRE_SIGNATURE) boolean signature);

    /**
     * 查询某个订单详情
     *
     * @param orderId
     * @param signature  签名
     * @return
     */
    @GET("/v1/order/orders/{order-id}")
    Call<ResponseCO<OrderCO>> orderDetail(@Path("order-id") String orderId, @Header(HuobiProperties.REQUIRE_SIGNATURE) boolean signature);
}

