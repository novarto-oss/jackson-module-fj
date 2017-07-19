package com.novarto.jackson.fj.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.novarto.jackson.fj.core.ValueDeserializer;
import fj.data.Option;
import fj.data.Tree;
import fj.data.TreeZipper;

import java.io.IOException;

public class TreeDeserializer extends ValueDeserializer<Tree<?>>
{
    private static final long serialVersionUID = 1L;

    private static final Option<Object> NONE = Option.none();

    private JsonDeserializer<?> stringDeserializer;

    public TreeDeserializer(JavaType valueType)
    {
        super(valueType, 1);
    }

    @Override public Tree<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
    {

        JsonToken currentToken;

        final JavaType containedType = javaType.getBindings().getTypeParameters().get(0);

        TreeZipper<Object> zipper = null;

        while ((currentToken = p.nextToken()) != null)
        {

            if (currentToken.equals(JsonToken.FIELD_NAME))
            {

                String fieldName = (String) stringDeserializer.deserialize(p, ctxt);
                if ("data".equals(fieldName))
                {
                    p.nextToken();

                    Tree<Object> currNode = Tree.leaf(p.readValueAs(containedType.getRawClass()));

                    //FIRST ITERATION, create zipper
                    if (zipper == null)
                    {
                        zipper = TreeZipper.fromTree(currNode);
                    }
                    else
                    {
                        zipper = zipper.insertRight(currNode);
                    }

                }

            }

            else if (currentToken.equals(JsonToken.START_ARRAY))
            {
                Option<Object> nextData = nextDataAfterStartArray(p, containedType, ctxt);

                if (nextData.isSome())
                {
                    Object data = nextData.some();
                    zipper = zipper.insertDownFirst(Tree.leaf(data));

                }
            }

            else if (currentToken.equals(JsonToken.END_ARRAY))
            {

                zipper = zipper.parent().some();
            }
        }

        return zipper.root().toTree();

    }

    private Option<Object> nextDataAfterStartArray(JsonParser p, JavaType type, DeserializationContext ctx) throws IOException
    {
        JsonToken token = p.nextToken();
        if (token == JsonToken.END_ARRAY)
        {
            return NONE;
        }
        else if (token != JsonToken.START_OBJECT)
        {
            throw new IllegalStateException();
        }

        token = p.nextToken();
        if (token != JsonToken.FIELD_NAME)
        {
            throw new IllegalStateException();
        }

        String fieldName = (String) stringDeserializer.deserialize(p, ctx);
        if (!"data".equals(fieldName))
        {
            throw new IllegalStateException();
        }

        p.nextToken();

        return Option.some(p.readValueAs(type.getRawClass()));

    }

    @Override public void resolve(DeserializationContext ctxt) throws JsonMappingException
    {
        super.resolve(ctxt);
        stringDeserializer = ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), null);
    }

}
