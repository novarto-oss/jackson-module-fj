# jackson-module-fj

This is a [jackson](https://github.com/FasterXML/jackson) module that brings support for
[FunctionalJava](https://github.com/functionaljava/functionaljava) data structures.



# Usage
`jackson-module-fj` is hosted on [Artifactory OSS](https://oss.jfrog.org/artifactory/libs-snapshot/) repository.

## Download
With `gradle`, you can refer to `jackson-module-fj` snapshot version in the following way:

```groovy
repositories {
    maven {
        url 'https://oss.jfrog.org/artifactory/libs-snapshot/'
    }
}

dependencies {

    compile('com.novarto:jackson-module-fj:0.9-SNAPSHOT') {
        changing = true
    }
}
```
You can refer to `jackson-module-fj` in Maven and other tools equivalently.

## Refer from your code

You just need to register the module in your object mapper:
```java
        mapper.registerModule(new FjModule());
```

Full example is available [here](src/test/java/com/novarto/jackson/fj/JsonParser.java).

If you're using immutable data structures, chances are you are also using immutable beans / data classes.
Therefore `jackson-module-fj` automatically registers the [parameter names module](https://github.com/FasterXML/jackson-modules-java8/tree/master/parameter-names).
This way serialization / deserialization of immutable java classes works out of the box, without having to specify annotations, etc.

## Compiler settings

For [parameter names module](https://github.com/FasterXML/jackson-modules-java8/tree/master/parameter-names) to work,
you need to retain parameter information in the produced bytecode. This happens via the `-parameters` compiler flag.

In gradle this is achieved like this:

```groovy
[compileJava, compileTestJava].each() {
    it.options.compilerArgs += ["-parameters"]
}
```

# Features

## Supported data structures

Currently `jackson-module-fj` supports the following `fj` data types:

-  `List`
- `Option`
- `Either`
- `P1`
- `P2`
- `HashSet`
- `HashMap`
- `Tree`

The list is likely to expand. If you require support for a new data structure, open an issue, or better yet - send us a PR.

## Miscellaneous

- Extensive automated testing of serializer/deserializer implementations via [property based testing](https://en.wikipedia.org/wiki/QuickCheck)
