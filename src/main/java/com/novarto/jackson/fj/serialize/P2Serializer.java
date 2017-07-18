package com.novarto.jackson.fj.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.novarto.jackson.fj.core.BaseSerializer;
import fj.P2;

import java.io.IOException;

public class P2Serializer extends BaseSerializer<P2<?, ?>>
{

        private static final long serialVersionUID = 1L;

        public P2Serializer(JavaType type)
        {
            super(type);
        }

        @Override public void serialize(P2<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException
        {
            gen.writeStartObject();
            gen.writeFieldName("_1");
            gen.writeObject(value._1());
            gen.writeFieldName("_2");
            gen.writeObject(value._2());
            gen.writeEndObject();
        }
}
