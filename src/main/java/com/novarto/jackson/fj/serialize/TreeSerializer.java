package com.novarto.jackson.fj.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.novarto.jackson.fj.core.BaseSerializer;
import fj.data.Tree;

import java.io.IOException;

public class TreeSerializer extends BaseSerializer<Tree<?>>
{
    private static final long serialVersionUID = 1L;

    public TreeSerializer(JavaType type)
    {
        super(type);
    }

    @Override public void serialize(Tree<?> tree, JsonGenerator gen, SerializerProvider provider) throws IOException
    {
        gen.writeStartObject();

        gen.writeFieldName("data");
        gen.writeObject(tree.root());

        gen.writeFieldName("children");
        gen.writeStartArray();
        for (Tree<?> child : tree.subForest()._1())
        {
            serialize(child, gen, provider);
        }
        gen.writeEndArray();
        gen.writeEndObject();

    }
}
