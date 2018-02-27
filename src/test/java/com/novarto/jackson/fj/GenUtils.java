package com.novarto.jackson.fj;

import fj.Equal;
import fj.Hash;
import fj.Ord;
import fj.P2;
import fj.data.List;
import fj.data.Set;
import fj.data.Stream;
import fj.data.Tree;
import fj.data.hamt.HashArrayMappedTrie;
import fj.test.Gen;

import static fj.test.Arbitrary.*;
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


    public static <A, B> Gen<HashArrayMappedTrie<A, B>> hamtGen(Gen<A> keyGen, Gen<B> valGen, Equal<A> eq, Hash<A> hash)
    {

        return arbList(arbP2(keyGen, valGen)).map(xs ->
                xs.foldLeft((acc, kv) -> acc.set(kv._1(), kv._2()), HashArrayMappedTrie.empty(eq, hash))
        );
    }

    public static <A, B> Equal<HashArrayMappedTrie<A, B>> hamtEqual(Equal<A> kEq, Equal<B> vEq, Ord<P2<A, B>> kvOrd)
    {

        Equal<Set<P2<A, B>>> setEq = Equal.setEqual(Equal.p2Equal(kEq, vEq));
        return Equal.equal((m1, m2) -> {
            Set<P2<A, B>> xs = Set.iterableSet(kvOrd, m1.toList());
            Set<P2<A, B>> ys = Set.iterableSet(kvOrd, m2.toList());

            return setEq.eq(xs, ys);
        });
    }

}
