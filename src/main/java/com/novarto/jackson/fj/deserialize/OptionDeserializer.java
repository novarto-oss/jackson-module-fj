/**
 * Copyright 2015 The Javaslang Authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.novarto.jackson.fj.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.novarto.jackson.fj.core.ValueDeserializer;
import fj.data.Option;

import java.io.IOException;

public class OptionDeserializer extends ValueDeserializer<Option<?>>
{

    private static final long serialVersionUID = 1L;

    private final JavaType javaType;
    private final boolean plainMode;
    private JsonDeserializer<?> stringDeserializer;

    public OptionDeserializer(JavaType valueType, boolean plainMode)
    {
        super(valueType, 1);
        this.javaType = valueType;
        this.plainMode = plainMode;
    }

    @Override
    public Option<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
    {
        if (plainMode)
        {
            Object obj = deserializer(0).deserialize(p, ctxt);
            return Option.fromNull(obj);
        }
        boolean defined = false;
        Object value = null;
        int cnt = 0;
        while (p.nextToken() != JsonToken.END_ARRAY)
        {
            cnt++;
            switch (cnt)
            {
            case 1:
                String def = (String) stringDeserializer.deserialize(p, ctxt);
                if ("defined".equals(def))
                {
                    defined = true;
                }
                else if ("undefined".equals(def))
                {
                    defined = false;
                }
                else
                {
                    ctxt.handleUnexpectedToken(javaType.getRawClass(), p);
                }
                break;
            case 2:
                value = deserializer(0).deserialize(p, ctxt);
                break;
            default:
                throw new IllegalStateException();
            }

        }
        if (defined)
        {
            if (cnt != 2)
            {
                ctxt.handleUnexpectedToken(javaType.getRawClass(), p);
            }
            return Option.some(value);
        }
        else
        {
            if (cnt != 1)
            {
                ctxt.handleUnexpectedToken(javaType.getRawClass(), p);
            }
            return Option.none();
        }
    }

    @Override
    public void resolve(DeserializationContext ctxt) throws JsonMappingException
    {
        super.resolve(ctxt);
        stringDeserializer = ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), null);
    }

    @Override
    public Option<?> getNullValue(DeserializationContext ctxt) throws JsonMappingException
    {
        return Option.none();
    }
}
