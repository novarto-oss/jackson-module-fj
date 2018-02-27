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

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.novarto.jackson.fj.FjModule;
import fj.P1;
import fj.P2;
import fj.data.*;
import fj.data.hamt.HashArrayMappedTrie;

public class FjDeserializers extends Deserializers.Base
{

    private final FjModule.Options options;

    public FjDeserializers(FjModule.Options options)
    {
        this.options = options;
    }

    @Override
    public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc)
            throws JsonMappingException
    {
        Class<?> raw = type.getRawClass();

        if (List.class.isAssignableFrom(raw))
        {
            return new ListDeserializer(type);
        }

        if (Option.class.isAssignableFrom(raw))
        {
            return new OptionDeserializer(type, options.plainOption());
        }

        if (Either.class.isAssignableFrom(raw))
        {
            return new EitherDeserializer(type);
        }

        if (P1.class.isAssignableFrom(raw))
        {
            return new LazyDeserializer(type);
        }

        if (P2.class.isAssignableFrom(raw))
        {
           return new P2Deserializer(type);
        }

        if (HashSet.class.isAssignableFrom(raw))
        {
            return new HashSetDeserializer(type);
        }

        if (HashMap.class.isAssignableFrom(raw))
        {
            return new HashMapDeserializer(type);
        }

        if (Tree.class.isAssignableFrom(raw))
        {
            return new TreeDeserializer(type);
        }

        if (HashArrayMappedTrie.class.isAssignableFrom(raw))
        {
            return new HamtDeserializer(type);
        }



        return super.findBeanDeserializer(type, config, beanDesc);
    }

    @Override
    public JsonDeserializer<?> findCollectionLikeDeserializer(CollectionLikeType type, DeserializationConfig config,
            BeanDescription beanDesc, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
            throws JsonMappingException
    {
        Class<?> raw = type.getRawClass();

        if (List.class.isAssignableFrom(raw))
        {
            return new ListDeserializer(type);
        }

        if (HashSet.class.isAssignableFrom(raw))
        {
            return new HashSetDeserializer(type);
        }

        return super.findCollectionLikeDeserializer(type, config, beanDesc, elementTypeDeserializer, elementDeserializer);
    }


}
