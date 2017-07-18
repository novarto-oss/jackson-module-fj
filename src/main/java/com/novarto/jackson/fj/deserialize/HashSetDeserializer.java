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

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.novarto.jackson.fj.core.FromMutableIterableDeserializer;
import fj.Equal;
import fj.Hash;
import fj.data.HashSet;

public class HashSetDeserializer extends FromMutableIterableDeserializer<HashSet<?>, HashSet<Object>>
{

    private static final long serialVersionUID = 1L;

    private final JavaType javaType;

    public HashSetDeserializer(JavaType valueType)
    {
        super(valueType, 1);
        javaType = valueType;
    }

    @Override
    protected HashSet<Object> create(HashSet<Object> xs, DeserializationContext ctxt) throws JsonMappingException
    {
        return xs;
    }

    @Override
    protected HashSet<Object> createMutableIterable()
    {
        return new HashSet<>(Equal.anyEqual(), Hash.anyHash());
    }

    @Override
    protected void add(HashSet<Object> xs, Object x)
    {
        xs.set(x);
    }
}
