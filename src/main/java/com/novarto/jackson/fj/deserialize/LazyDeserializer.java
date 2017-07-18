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
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.novarto.jackson.fj.core.ValueDeserializer;
import fj.P;
import fj.P1;

import java.io.IOException;

public class LazyDeserializer extends ValueDeserializer<P1<?>>
{

    private static final long serialVersionUID = 1L;

    public LazyDeserializer(JavaType valueType)
    {
        super(valueType, 1);
    }

    @Override
    public P1<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
    {
        Object obj = deserializer(0).deserialize(p, ctxt);
        return P.lazy(() -> obj);
    }

    @Override
    public P1<?> getNullValue(DeserializationContext ctxt)
    {
        return P.lazy(() -> null);
    }
}
