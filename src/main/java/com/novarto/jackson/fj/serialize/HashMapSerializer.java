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
import fj.data.HashMap;
import fj.function.Effect0;
import fj.function.TryEffect0;

import java.io.IOException;

public class HashMapSerializer extends BaseSerializer<HashMap<?, ?>>
{

    private static final long serialVersionUID = 1L;

    public HashMapSerializer(JavaType type)
    {
        super(type);
    }

    @Override
    public void serialize(HashMap<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException
    {
        gen.writeStartObject();
        value.foreachDoEffect(x -> rethrow(() ->
        {
            gen.writeFieldName(x._1().toString());
            gen.writeObject(x._2());
        }));
    }

    //    @Override
    //    Object toJavaObj(Map<?, ?> value) throws IOException {
    //        final LinkedHashMap<Object, Object> result = new LinkedHashMap<>();
    //        value.forEach(e -> result.put(e._1, e._2));
    //        return result;
    //    }

    private static Effect0 rethrow(TryEffect0<Exception> body)
    {
        return () ->
        {
            try
            {
                body.f();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        };
    }
}
