/**
 * @(#) CustomBigDecimalSerializer.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.config.web.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.math.BigDecimal;

/**
 *
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/8/27 by Storys.Zhang in coinmax
 */
public class CustomBigDecimalSerializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException, JsonProcessingException {
        BigDecimal d = new BigDecimal(value.toString());
        gen.writeString(d.stripTrailingZeros().toPlainString());

    }

    @Override
    public Class<BigDecimal> handledType() {
        return BigDecimal.class;
    }
}
