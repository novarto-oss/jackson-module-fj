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
package com.novarto.jackson.fj.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

public abstract class FromMutableIterableDeserializer<T, I extends Iterable<Object>> extends ValueDeserializer<T>
{

    private static final long serialVersionUID = 1L;

    public FromMutableIterableDeserializer(JavaType valueType, int typeCount)
    {
        super(valueType, typeCount);
    }

    protected abstract T create(I list, DeserializationContext ctxt) throws JsonMappingException;

    protected abstract I createMutableIterable();

    protected abstract void add(I xs, Object x);

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
    {
        JsonDeserializer<?> deserializer = deserializer(0);
        I buffer = createMutableIterable();
        while (p.nextToken() != JsonToken.END_ARRAY)
        {
            add(buffer, deserializer.deserialize(p, ctxt));
        }
        return create(buffer, ctxt);
    }
}
