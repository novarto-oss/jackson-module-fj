package com.novarto.jackson.fj.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.novarto.jackson.fj.core.BaseSerializer;
import fj.data.hamt.HashArrayMappedTrie;

import java.io.IOException;

import static com.novarto.jackson.fj.util.Util.rethrow;

public class HamtSerializer extends BaseSerializer<HashArrayMappedTrie<?, ?>>
{

    private static final long serialVersionUID = 1L;

    public HamtSerializer(JavaType type)
    {
        super(type);
    }

    @Override
    public void serialize(HashArrayMappedTrie<?, ?> hamt, JsonGenerator gen, SerializerProvider provider) throws IOException
    {
        gen.writeStartObject();
        hamt.toStream().foreachDoEffect(x -> rethrow(() ->
        {
            gen.writeFieldName(x._1().toString());
            gen.writeObject(x._2());
        }).f());
        gen.writeEndObject();
    }

}
