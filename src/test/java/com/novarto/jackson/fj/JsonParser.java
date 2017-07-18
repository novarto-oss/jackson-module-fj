package com.novarto.jackson.fj;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created by fmap on 27.11.15.
 */
public class JsonParser
{
    public static final ObjectMapper MAPPER;

    static
    {
        MAPPER = new ObjectMapper();
        MAPPER.registerModule(new FjModule());
        MAPPER.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, true);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}
