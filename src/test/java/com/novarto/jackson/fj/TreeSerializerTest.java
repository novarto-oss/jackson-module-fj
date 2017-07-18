package com.novarto.jackson.fj;

import com.fasterxml.jackson.core.JsonProcessingException;
import fj.data.List;
import fj.data.Tree;
import org.junit.Test;

import static com.novarto.jackson.fj.JsonParser.MAPPER;

public class TreeSerializerTest
{
    @Test
    public void testIt() throws JsonProcessingException
    {
        Tree<SimpleBean> tree = Tree.node(new SimpleBean("root", 1),

                List.arrayList(Tree.leaf(new SimpleBean("leaf1", 2)), Tree.leaf(new SimpleBean("leaf2", 3))));

        System.out.println(MAPPER.writeValueAsString(tree));

    }
}
