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
import com.novarto.jackson.fj.core.MaplikeDeserializer;
import fj.P;
import fj.P2;
import fj.data.HashMap;
import fj.data.List;

import java.io.IOException;

public class HashMapDeserializer extends MaplikeDeserializer<HashMap<?, ?>>
{

    private static final long serialVersionUID = 1L;

    public HashMapDeserializer(JavaType valueType)
    {
        super(valueType);
    }

    @Override
    public HashMap<Object, Object> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
    {
        final List.Buffer<P2<Object, Object>> result = List.Buffer.empty();
        while (p.nextToken() != JsonToken.END_OBJECT)
        {
            String name = p.getCurrentName();
            Object key = keyDeserializer.deserializeKey(name, ctxt);
            p.nextToken();
            result.snoc(P.p(key, valueDeserializer.deserialize(p, ctxt)));
        }
        return HashMap.iterableHashMap(result);
    }
}
