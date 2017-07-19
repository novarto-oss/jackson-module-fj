package com.novarto.jackson.fj;

import fj.data.List;
import fj.data.Stream;
import fj.data.Tree;
import fj.test.Gen;

import static fj.test.Arbitrary.arbBoolean;
import static fj.test.Gen.choose;
import static fj.test.Gen.sequenceN;

public final class GenUtils
{
    private GenUtils()
    {
        throw new UnsupportedOperationException();
    }


    public static <A> Gen<Tree<A>> treeGen(Gen<A> elementGen, int depthLimit, int breadthLimit)
    {

        return arbBoolean
                .bind(hasChildren -> elementGen.bind(data -> sizedListGen(elementGen, breadthLimit).bind(children -> {
                    if (hasChildren && depthLimit > 0)
                    {
                        List<Gen<Tree<A>>> tmp = children.map(child ->
                                treeGen(elementGen, depthLimit - 1, breadthLimit));

                        Gen<Stream<Tree<A>>> childStreamGen = Gen.sequence(tmp).map(xs -> xs.toStream());

                        return childStreamGen.map(xs -> Tree.node(data, xs));
                    }
                    else
                    {
                        return Gen.value(Tree.leaf(data));
                    }
                })));

    }



    public static <A> Gen<List<A>> sizedListGen(Gen<A> dataGen, int limit)
    {
        return choose(0, limit).bind(size -> sequenceN(size, dataGen));
    }

}
