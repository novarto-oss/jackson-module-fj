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
import fj.data.Either;

import java.io.IOException;

public class EitherSerializer extends BaseSerializer<Either<?, ?>>
{

    private static final long serialVersionUID = 1L;

    public EitherSerializer(JavaType type)
    {
        super(type);
    }

    @Override
    public void serialize(Either<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException
    {
        gen.writeStartArray();
        if (value.isLeft())
        {
            gen.writeObject("left");
            gen.writeObject(value.left().value());
        }
        else
        {
            gen.writeObject("right");
            gen.writeObject(value.right().value());
        }
        gen.writeEndArray();
    }

}
