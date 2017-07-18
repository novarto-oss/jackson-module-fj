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
package com.novarto.jackson.fj.serialize;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.novarto.jackson.fj.FjModule;
import fj.P1;
import fj.P2;
import fj.data.*;

public class FjSerializers extends Serializers.Base
{

    private final FjModule.Options options;

    public FjSerializers(FjModule.Options options)
    {
        this.options = options;
    }

    @Override
    public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc)
    {

        Class<?> raw = type.getRawClass();
        if (List.class.isAssignableFrom(raw))
        {
            return new IterableSerializer<List<?>>(type);
        }

        if (Option.class.isAssignableFrom(raw))
        {
            return new OptionSerializer(type, options.plainOption());
        }

        if (Either.class.isAssignableFrom(raw))
        {
            return new EitherSerializer(type);
        }

        if (P1.class.isAssignableFrom(raw))
        {
            return new LazySerializer(type);
        }

        if (HashSet.class.isAssignableFrom(raw))
        {
            return new IterableSerializer<HashSet<?>>(type);
        }

        if (HashMap.class.isAssignableFrom(raw))
        {
            return new HashMapSerializer(type);
        }

        if (Tree.class.isAssignableFrom(raw))
        {
            return new TreeSerializer(type);
        }

        if (P2.class.isAssignableFrom(raw))
        {
            return new P2Serializer(type);
        }

        return super.findSerializer(config, type, beanDesc);
    }
}
