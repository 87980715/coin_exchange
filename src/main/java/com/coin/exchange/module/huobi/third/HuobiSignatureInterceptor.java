/**
 * @(#) HuobiSignatureInterceptor.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.module.huobi.third;

import com.coin.exchange.common.utils.StringsHelper;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;
import java.util.SortedMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/21 by Storys.Zhang in coin_exchange
 */
@Component
public class HuobiSignatureInterceptor implements Interceptor {

    @Autowired
    private HuobiProperties huobiProperties;

    enum HttpMethodType {
        /**
         * post
         */
        POST,
        /**
         * get
         */
        GET;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        //通过header确定是否需要签名
        final String requireSignature = request.header(HuobiProperties.REQUIRE_SIGNATURE);
        if (StringsHelper.isBlank(requireSignature) || Boolean.FALSE.toString()
            .equalsIgnoreCase(requireSignature)) {
            return chain.proceed(request);
        }
        String method = request.method();
        if (HttpMethodType.GET.toString().equalsIgnoreCase(method)) {
            return chain.proceed(buildGetParamAndSignature(request));
        } else {
            return chain.proceed(buildPostParamAndSignature(request));
        }
    }

    private Request buildPostParamAndSignature(Request reqest) {
        String method = reqest.method();
        String uri = reqest.url().uri().getPath();
        final HuobiApiSignature apiSignature = new HuobiApiSignature();
        Map<String, String> params = Maps.newHashMap();
        apiSignature
            .createSignature(huobiProperties.getAccessKey(), huobiProperties.getSecretKey(), method,
                huobiProperties.getDomainName(), uri, params);
        HttpUrl.Builder urlBuilder = reqest.url().newBuilder();
        params.entrySet().stream()
            .forEach(e -> urlBuilder.addQueryParameter(e.getKey(), e.getValue()));
        return reqest.newBuilder().url(urlBuilder.build()).build();
    }

    private Request buildGetParamAndSignature(Request req) {
        final HttpUrl httpUrl = req.url();
        final int querySize = httpUrl.querySize();
        final Map<String, String> params = Maps.newHashMap();
        for (int i = 0; i < querySize; i++) {
            String queryName = httpUrl.queryParameterName(i);
            String queryValue = httpUrl.queryParameterValue(i);
            params.put(queryName, queryValue);
        }
        String method = req.method();
        String uri = req.url().uri().getPath();
        final HuobiApiSignature apiSignature = new HuobiApiSignature();
        apiSignature
            .createSignature(huobiProperties.getAccessKey(), huobiProperties.getSecretKey(), method,
                huobiProperties.getDomainName(), uri, params);

        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        params.entrySet().stream()
            .forEach(e -> urlBuilder.addQueryParameter(e.getKey(), e.getValue()));
        return req.newBuilder().url(urlBuilder.build()).build();
    }

    /**
     * @author storys.zhang@gmail.com
     *
     * Created at 2018/9/21 by Storys.Zhang in coin_exchange
     */
    class HuobiApiSignature {

        final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");
        final ZoneId ZONE_GMT = ZoneId.of("Z");

        /**
         * 创建一个有效的签名。该方法为客户端调用，将在传入的params中添加AccessKeyId、Timestamp、SignatureVersion、SignatureMethod、Signature参数。
         *
         * @param accessKey
         * @param secretKey
         * @param method 请求方法，"GET"或"POST"
         * @param host 请求域名，例如"be.huobi.com"
         * @param uri 请求路径，注意不含?以及后的参数，例如"/v1/api/info"
         * @param params 原始请求参数，以Key-Value存储，注意Value不要编码
         */

        public Map<String, String> createSignature(String accessKey, String secretKey,
            String method, String host,
            String uri, Map<String, String> params) {
            StringBuilder sb = new StringBuilder(1024);
            sb.append(method.toUpperCase()).append('\n')
                .append(host.toLowerCase()).append('\n')
                .append(uri).append('\n');
            params.remove("Signature");
            params.put("AccessKeyId", accessKey);
            params.put("SignatureVersion", "2");
            params.put("SignatureMethod", "HmacSHA256");
            params.put("Timestamp", gmtNow());
            SortedMap<String, Object> param = Maps.newTreeMap((o1, o2) -> o1.compareTo(o2));
            param.putAll(params);
            param.entrySet().stream()
                .forEach(e -> e.setValue(HuobiHelper.urlEncode(e.getValue().toString())));

            sb.append(Joiner.on("&").withKeyValueSeparator("=").join(param));
            params.put("Signature", sign(sb.toString(), secretKey));
            return params;
        }

        private String sign(String payload, String secretKey) {
            Mac hmacSha256;
            try {
                hmacSha256 = Mac.getInstance("HmacSHA256");
                SecretKeySpec secKey =
                    new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),
                        "HmacSHA256");
                hmacSha256.init(secKey);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("No such algorithm: " + e.getMessage());
            } catch (InvalidKeyException e) {
                throw new RuntimeException("Invalid key: " + e.getMessage());
            }
            byte[] hash = hmacSha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String actualSign = Base64.getEncoder().encodeToString(hash);
            return actualSign;
        }

        String gmtNow() {
            return Instant.ofEpochSecond(epochNow()).atZone(ZONE_GMT).format(DT_FORMAT);
        }

        long epochNow() {
            return Instant.now().getEpochSecond();
        }
    }
}
