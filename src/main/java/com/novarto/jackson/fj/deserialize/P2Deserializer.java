package com.novarto.jackson.fj.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.novarto.jackson.fj.core.ValueDeserializer;
import fj.P2;

import java.io.IOException;

import static fj.P.p;

public class P2Deserializer extends ValueDeserializer<P2<?, ?>>
{
    private static final long serialVersionUID = 1L;

    private final JavaType javaType;

    public P2Deserializer(JavaType valueType)
    {
        super(valueType, 2);
        this.javaType = valueType;
    }

    @Override public P2<?, ?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException
    {
        Pos pos = null;
        Object one = null;
        Object two = null;
        boolean oneDeserialized = false;
        boolean twoDeserialized = false;

        while (p.nextToken() != JsonToken.END_OBJECT)
        {
            JsonToken token = p.getCurrentToken();
            if (token == JsonToken.FIELD_NAME)
            {
                switch (p.getText())
                {
                case "_1":
                {
                    pos = Pos.ONE;
                    break;
                }
                case "_2":
                {
                    pos = Pos.TWO;
                    break;
                }

                default:
                    throw ctxt.mappingException("fj.P2 - unrecognized field: " + p.getText());
                }
            }
            else if (pos == Pos.ONE)
            {
                one = deserializer(0).deserialize(p, ctxt);
                oneDeserialized = true;
            }
            else if (pos == Pos.TWO)
            {
                two = deserializer(1).deserialize(p, ctxt);
                twoDeserialized = true;
            }
        }

        if (!(oneDeserialized && twoDeserialized))
        {
            throw ctxt.mappingException("_1 or _2 missing");
        }
        return p(one, two);
    }

    private enum Pos
    {
        ONE, TWO
    }
}
