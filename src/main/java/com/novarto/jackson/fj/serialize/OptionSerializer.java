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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.novarto.jackson.fj.core.BaseSerializer;
import fj.data.Option;

import java.io.IOException;

public class OptionSerializer extends BaseSerializer<Option<?>>
{

    private static final long serialVersionUID = 1L;

    private final boolean plainMode;

    public OptionSerializer(JavaType type, boolean plainMode)
    {
        super(type);
        this.plainMode = plainMode;
    }

    //    @Override
    //    Object toJavaObj(Option<?> value) throws IOException
    //    {
    //        if (value.isDefined())
    //        {
    //            return plainMode ? value.get() : Arrays.asList("defined", value.get());
    //        }
    //        else
    //        {
    //            return plainMode ? null : Collections.singleton("undefined");
    //        }
    //    }

    @Override
    public boolean isEmpty(SerializerProvider provider, Option<?> value)
    {
        return value.isNone();
    }

    @Override
    public void serialize(Option<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException
    {
        if (value.isSome())
        {
            if (plainMode)
            {
                gen.writeObject(value.some());
            }
            else
            {
                gen.writeStartArray();
                gen.writeObject("defined");
                gen.writeObject(value.some());
                gen.writeEndArray();
            }
        }
        else
        {
            if (plainMode)
            {
                gen.writeNull();
            }
            else
            {
                gen.writeStartArray();
                gen.writeObject("undefined");
                gen.writeEndArray();
            }

        }
    }
}
