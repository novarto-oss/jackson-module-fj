package com.novarto.jackson.fj;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import fj.*;
import fj.data.*;
import fj.data.hamt.HashArrayMappedTrie;
import fj.test.Gen;
import fj.test.Property;
import fj.test.runner.PropertyTestRunner;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static com.novarto.jackson.fj.GenUtils.*;
import static com.novarto.lang.testutil.TestUtil.tryTo;
import static fj.P.p;
import static fj.data.Either.left;
import static fj.data.Either.right;
import static fj.test.Arbitrary.*;
import static fj.test.Property.prop;
import static fj.test.Property.property;

@RunWith(PropertyTestRunner.class)
public class FjModuleTest
{
    public final Gen<Floof> arbFloof = arbList(arbInteger).map(xs -> new Floof(xs));

    public final Gen<ComplexBean> arbComplexBean =
            arbList(arbFloof).bind(floofs -> arbString.map(str -> new ComplexBean(floofs, str)));

    public final Gen<SimpleBean> arbSimpleBean =
            arbString.bind(str -> arbInteger.map(integ -> new SimpleBean(str, integ)));

    public final Gen<GenOptBean<Integer>> genOptBeanArbitrary =
            arbString.bind(str -> arbInteger.map(integ -> new GenOptBean<>(str, Option.fromNull(integ))));

    public final Gen<EithersHolder> eithersHolderGen =
            arbList(arbEither(arbString, arbString)).map(xs -> new EithersHolder(xs));

    public Property paramModuleWorks()
    {
        return property(arbSimpleBean, bean -> serializeDeserialize(bean, left(SimpleBean.class)));
    }

    public Property paramModuleWorksForVanillaCollections()
    {
        return property(arbArrayList(arbSimpleBean), arrayList -> serializeDeserialize(arrayList,
                right(JsonParser.MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, SimpleBean.class))));
    }

    public Property canDeserializeFjListOfString()
    {

        return property(arbList(arbString), list -> serializeDeserialize(list,
                right(JsonParser.MAPPER.getTypeFactory().constructCollectionLikeType(List.class, String.class))));
    }

    public Property canDeserializeFjListOfInt()
    {

        return property(arbList(arbInteger), list -> serializeDeserialize(list,
                right(JsonParser.MAPPER.getTypeFactory().constructCollectionLikeType(List.class, Integer.class))));
    }

    public Property canDeserializeBeanWithFjListOfPrimitive()
    {
        return property(arbFloof, floof -> serializeDeserialize(floof, left(Floof.class)));
    }

    public Property canDeserializeFjListOfObjects()
    {
        return property(arbList(arbSimpleBean), list -> serializeDeserialize(list,
                right(JsonParser.MAPPER.getTypeFactory().constructCollectionLikeType(List.class, SimpleBean.class))));
    }

    public Property canDeserComplexBean()
    {
        return property(arbComplexBean, cb -> serializeDeserialize(cb, left(ComplexBean.class)));
    }

    public Property canDeserializeOptionOfPrimitive()
    {
        return property(arbOption(arbInteger), opt -> serializeDeserialize(opt, left(Option.class)));
    }

    public Property canDeserializeBeanWithOpt()
    {
        return property(genOptBeanArbitrary, optBean -> serializeDeserialize(optBean, left(GenOptBean.class)));
    }

    public Property canDeserializeHashMap()
    {
        TypeReference<HashMap<String, ComplexBean>> type = new TypeReference<HashMap<String, ComplexBean>>()
        {
        };

        return property(arbHashMap(arbString, arbComplexBean).map(x -> HashMap.fromMap(x)),
                map -> serializeDeserialize(map, type, hashMapEqual()));
    }

    public Property canDeserializeHashMapNonStringKey()
    {
        TypeReference<HashMap<Integer, ComplexBean>> type = new TypeReference<HashMap<Integer, ComplexBean>>()
        {
        };

        return property(arbHashMap(arbInteger, arbComplexBean).map(x -> HashMap.fromMap(x)),
                map -> serializeDeserialize(map, type, hashMapEqual()));
    }

    public Property canDeserializeHamt()
    {
        TypeReference<HashArrayMappedTrie<String, ComplexBean>> type =
                new TypeReference<HashArrayMappedTrie<String, ComplexBean>>()
        {
        };

        return property(hamtGen(arbString, arbComplexBean, Equal.stringEqual, Hash.stringHash),
                hamt -> serializeDeserialize(hamt, type, hamtEqual(Equal.stringEqual, Equal.anyEqual(),
                        Ord.p2Ord(Ord.stringOrd, complexBeanOrd()))));
    }

    public Property canDeserializeHamtNonStringKey()
    {
        TypeReference<HashArrayMappedTrie<Integer, ComplexBean>> type =
                new TypeReference<HashArrayMappedTrie<Integer, ComplexBean>>()
        {
        };

        return property(hamtGen(arbInteger, arbComplexBean, Equal.intEqual, Hash.intHash),
                hamt -> serializeDeserialize(hamt, type,
                        hamtEqual(Equal.intEqual, Equal.anyEqual(), Ord.p2Ord(Ord.intOrd, complexBeanOrd()))));
    }

    public Property canDeserializeHashSet()
    {
        TypeReference<HashSet<ComplexBean>> type = new TypeReference<HashSet<ComplexBean>>()
        {
        };

        return property(arbHashSet(arbComplexBean).map(x -> HashSet.fromSet(x)),
                map -> serializeDeserialize(map, type, hashSetEqual()));
    }

    public Property canDeserializeEither()
    {
        TypeReference<Either<String, ComplexBean>> type = new TypeReference<Either<String, ComplexBean>>()
        {
        };

        return property(arbEither(arbString, arbComplexBean), either -> serializeDeserialize(either, type));
    }

    public Property canDeserializeWrappedEithers()
    {
        TypeReference<EithersHolder> type = new TypeReference<EithersHolder>()
        {
        };

        return property(eithersHolderGen, either -> serializeDeserialize(either, type));
    }


    public Property canDeserializeIterableOfEither()
    {
        TypeReference<Either<String, ComplexBean>> type = new TypeReference<Either<String, ComplexBean>>()
        {
        };

        return property(arbEither(arbString, arbComplexBean), either -> serializeDeserialize(either, type));
    }

    public Property canDeserializeProduct1()
    {
        TypeReference<P1<SimpleBean>> type = new TypeReference<P1<SimpleBean>>()
        {
        };

        return property(arbP1(arbSimpleBean), p -> serializeDeserialize(p, type));
    }


    public Property canDeserializeProduct2Simple()
    {
        TypeReference<P2<String, Integer>> type = new TypeReference<P2<String, Integer>>()
        {
        };

        return property(arbString, arbInteger, (x, y) -> serializeDeserialize(p(x, y), type));

    }

    public Property canDeserializeProduct2Mixed()
    {
        TypeReference<P2<String, ComplexBean>> type = new TypeReference<P2<String, ComplexBean>>()
        {
        };

        return property(arbString, arbComplexBean, (x, y) -> serializeDeserialize(p(x, y), type));

    }

    public Property canDeserializeProduct2Beans()
    {
        TypeReference<P2<SimpleBean, ComplexBean>> type = new TypeReference<P2<SimpleBean, ComplexBean>>()
        {
        };

        return property(arbSimpleBean, arbComplexBean, (x, y) -> serializeDeserialize(p(x, y), type));

    }

    public Property canDeserializeTreeFloof()
    {

        return property(treeGen(arbFloof, 5, 10), tree -> serializeDeserialize(tree,
                new TypeReference<Tree<Floof>>()
                {
                }));

    }

    public Property canDeserializeTreeSimple()
    {

        return property(treeGen(arbInteger, 5, 10), tree -> serializeDeserialize(tree,
                new TypeReference<Tree<Integer>>()
                {
                }));

    }

    public Property canDeserializeTreeComplex()
    {

        return property(treeGen(arbComplexBean, 3, 5), tree -> serializeDeserialize(tree,
                new TypeReference<Tree<ComplexBean>>()
                {
                }));

    }

    public Property canDeserializeTreeSimpleBean()
    {

        return property(treeGen(arbSimpleBean, 5, 10), tree -> serializeDeserialize(tree,
                new TypeReference<Tree<SimpleBean>>()
                {
                }));

    }





    private static <A> Property serializeDeserialize(A in, Either<java.lang.Class<A>, JavaType> type, Equal<A> equal)
    {
        boolean success = tryTo(() -> {

            String json = JsonParser.MAPPER.writeValueAsString(in);
            A out = type.isLeft() ?
                    JsonParser.MAPPER.readValue(json, type.left().value())
                    : JsonParser.MAPPER.readValue(json, type.right().value());

            boolean isEqual = equal.eq(in, out);
            return isEqual;
        });

        return prop(success);
    }

    public static <A> Property serializeDeserialize(A in, Either<java.lang.Class<A>, JavaType> type)
    {
        return serializeDeserialize(in, type, Equal.anyEqual());
    }

    public static <A> Property serializeDeserialize(A in, TypeReference<A> type)
    {

        return serializeDeserialize(in, right(JsonParser.MAPPER.getTypeFactory().constructType(type)));
    }

    public static <A> Property serializeDeserialize(A in, TypeReference<A> type, Equal<A> customEqual)
    {

        return serializeDeserialize(in, right(JsonParser.MAPPER.getTypeFactory().constructType(type)), customEqual);
    }

    private static <A, B> Equal<HashMap<A, B>> hashMapEqual()
    {
        return Equal.equal(x -> y -> new java.util.HashSet<>(x.toList().toJavaList())
                .equals(new java.util.HashSet<>(y.toList().toJavaList())));
    }

    private static <A> Equal<HashSet<A>> hashSetEqual()
    {
        return Equal.equal(xs -> ys -> xs.toJavaSet().equals(ys.toJavaSet()));
    }

    public static class Floof
    {
        public final List<Integer> xs;

        public Floof(List<Integer> xs)
        {
            this.xs = xs;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }

            Floof floof = (Floof) o;

            return !(xs != null ? !xs.equals(floof.xs) : floof.xs != null);

        }

        @Override
        public int hashCode()
        {
            return xs != null ? xs.hashCode() : 0;
        }

        @Override
        public String toString()
        {
            return "Floof{" +
                    "xs=" + xs +
                    '}';
        }
    }


    public static class GenOptBean<A>
    {
        public final String descr;
        public final Option<A> opt;

        public GenOptBean(String descr, Option<A> opt)
        {
            this.descr = descr;
            this.opt = opt;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }

            GenOptBean<?> that = (GenOptBean<?>) o;

            if (descr != null ? !descr.equals(that.descr) : that.descr != null)
            {
                return false;
            }
            return !(opt != null ? !opt.equals(that.opt) : that.opt != null);

        }

        @Override
        public int hashCode()
        {
            int result = descr != null ? descr.hashCode() : 0;
            result = 31 * result + (opt != null ? opt.hashCode() : 0);
            return result;
        }
    }

    public static class ComplexBean
    {
        public final List<Floof> floofList;
        public final String description;

        public ComplexBean(List<Floof> floofList, String description)
        {
            this.floofList = floofList;
            this.description = description;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }

            ComplexBean that = (ComplexBean) o;

            if (floofList != null ? !floofList.equals(that.floofList) : that.floofList != null)
            {
                return false;
            }
            return !(description != null ? !description.equals(that.description) : that.description != null);

        }

        @Override
        public int hashCode()
        {
            int result = floofList != null ? floofList.hashCode() : 0;
            result = 31 * result + (description != null ? description.hashCode() : 0);
            return result;
        }

        @Override public String toString()
        {
            return "ComplexBean{" + "floofList=" + floofList + ", description='" + description + '\'' + '}';
        }
    }

    public static class EithersHolder
    {
        public final List<Either<String, String>> eithers;

        public EithersHolder(List<Either<String, String>> eithers)
        {
            this.eithers = eithers;
        }

        @Override public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;

            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }

            EithersHolder that = (EithersHolder) o;

            return eithers.equals(that.eithers);
        }

        @Override public int hashCode()
        {
            return eithers.hashCode();
        }

        @Override public String toString()
        {
            final StringBuilder sb = new StringBuilder("EithersHolder{");
            sb.append("eithers=").append(eithers);
            sb.append('}');
            return sb.toString();
        }
    }

    private static Ord<ComplexBean> complexBeanOrd()
    {
        return Ord.ord((x, y) -> {
            P2<List<Floof>, String> p1 = p(x.floofList, x.description);
            P2<List<Floof>, String> p2 = p(y.floofList, y.description);

            return Ord.p2Ord(Ord.listOrd(floofOrd()), Ord.stringOrd).compare(p1, p2);
        });
    }

    private static Ord<Floof> floofOrd()
    {
        return Ord.ord((x, y) -> {
            return Ord.listOrd(Ord.intOrd).compare(x.xs, y.xs);
        });
    }

}
