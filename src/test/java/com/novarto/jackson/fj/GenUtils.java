package com.novarto.jackson.fj;

import fj.data.List;
import fj.data.Tree;
import fj.test.Gen;
import fj.test.Rand;

import java.util.concurrent.atomic.AtomicInteger;

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
        return treeGen(elementGen, depthLimit, breadthLimit, new AtomicInteger(), Rand.standard);
    }

    private static <A> Gen<Tree<A>> treeGen(Gen<A> elementGen, int depthLimit, int breadthLimit, AtomicInteger i, Rand r)
    {

        return arbBoolean
                .bind(hasChildren -> elementGen.bind(data -> sizedListGen(elementGen, breadthLimit).map(children -> {
                    if (hasChildren && depthLimit > 0)
                    {
                        return Tree.node(data, children.toStream().map(child -> {
                            i.incrementAndGet();
                            return treeGen(elementGen, depthLimit - 1, breadthLimit, i, r).gen(i.get(), r);
                        }));
                    }
                    else
                    {
                        return Tree.leaf(data);
                    }
                })));


    }

    public static <A> Gen<List<A>> sizedListGen(Gen<A> dataGen, int limit)
    {
        return choose(0, limit).bind(size -> sequenceN(size, dataGen));
    }

}
