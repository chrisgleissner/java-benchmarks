# JMH Benchmarks

Benchmarks for LambdaMetaFactory, counters, etc.

All benchmarks were performed using JMH via `mvn clean install; java -Xms1g -Xmx1g -jar target/benchmarks.jar`. 

You can run a single benchmark via `java -jar target/benchmarks.jar SetterBenchmark`.

The following measurements were obtained on OpenJDK 11.0.1 64Bit and Ubuntu 18.04 run inside VirtualBox 5.2 on an Intel I7-6700K clocked at 4.6GHz.
All 4 cores were allocated to the VM.

## Counters

AtomicInteger vs [MutableInt](https://commons.apache.org/proper/commons-lang/javadocs/api-release/index.html) vs int,
same for the corresponding long types.

10,000 ops per benchmark iteration, 4 threads.

```
Benchmark                       Mode  Cnt      Score      Error  Units
CounterBenchmark.atomicInteger                                  avgt    5       58055.708 ±      815.505  ns/op
CounterBenchmark.atomicLong                                     avgt    5       59661.901 ±     7173.469  ns/op
CounterBenchmark.mutableInt                                     avgt    5       46396.683 ±      862.583  ns/op
CounterBenchmark.mutableLong                                    avgt    5       44606.222 ±     1170.934  ns/op
CounterBenchmark.primitiveInt                                   avgt    5       38184.281 ±      560.482  ns/op
CounterBenchmark.primitiveLong                                  avgt    5       42260.893 ±     4464.644  ns/op
```

## Getters and Setters

Comparing various ways of getting and setting object fields. 

Ordered by performance from top to bottom, the ranking is:
1. Direct call
1. [LambdaMetaFactory](https://docs.oracle.com/javase/8/docs/api/java/lang/invoke/LambdaMetafactory.html) - as fast as direct, but requires at least a private accessor method 
1. Reflection - 60% of the direct performance
1. MethodHandle and VarHandle - 20% of the direct performance

### GetterBenchmark

```
Benchmark                                   Mode  Cnt   Score   Error  Units
GetterBenchmark.direct                                          avgt   10           3.951 ±        0.005  ns/op
GetterBenchmark.lambdaMetaFactoryForGetter                      avgt   10           4.182 ±        0.035  ns/op
GetterBenchmark.methodHandleForField                            avgt   10          19.478 ±        0.243  ns/op
GetterBenchmark.methodHandleForGetter                           avgt   10          19.242 ±        0.401  ns/op
GetterBenchmark.reflection                                      avgt   10           6.086 ±        0.050  ns/op
GetterBenchmark.varHandle                                       avgt   10          20.060 ±        0.145  ns/op
```

### SetterBenchmark

```
Benchmark                                   Mode  Cnt   Score   Error  Units
SetterBenchmark.direct                                          avgt   10           4.980 ±        0.096  ns/op
SetterBenchmark.lambdaMetaFactoryForSetter                      avgt   10           4.717 ±        0.111  ns/op
SetterBenchmark.methodHandleForField                            avgt   10          20.414 ±        1.327  ns/op
SetterBenchmark.methodHandleForSetter                           avgt   10          19.141 ±        0.404  ns/op
SetterBenchmark.reflection                                      avgt   10           6.123 ±        0.105  ns/op
SetterBenchmark.varHandle                                       avgt   10          20.782 ±        0.204  ns/op
```

## ObjectPool

Compares the use of ints, custom int wrappers instantiation, and a custom int wrapper cache.

10,000 ops per benchmark iteration, 4 threads.

```
Benchmark                         Mode  Cnt      Score      Error  Units
ObjectCacheBenchmark.cache                                      avgt   10       24717.721 ±     1395.363  ns/op
ObjectCacheBenchmark.constructor                                avgt   10       63253.711 ±     1730.443  ns/op
ObjectCacheBenchmark.primitive                                  avgt   10        9272.462 ±      293.206  ns/op
```

## List And Array Addition

Compares adding elements to int/Integer/long/Long arrays, copying arrays, as well as adding elements to various Integer List implementations.

100,000 ops per benchmark iteration.


```
ListAndArrayBenchmark.intArrayAdd                               avgt   10       46582.177 ±      305.824  ns/op
ListAndArrayBenchmark.intArrayClone                             avgt   10       33837.833 ±      354.567  ns/op
ListAndArrayBenchmark.intArrayCopyOf                            avgt   10       34221.058 ±      213.797  ns/op
ListAndArrayBenchmark.intArraySystemArrayCopy                   avgt   10       33864.166 ±      203.409  ns/op

ListAndArrayBenchmark.intWrapperArrayAdd                        avgt   10      199193.707 ±     3934.894  ns/op
ListAndArrayBenchmark.intWrapperArrayClone                      avgt   10       50362.692 ±      266.787  ns/op
ListAndArrayBenchmark.intWrapperArrayCopyOf                     avgt   10       50655.731 ±      878.335  ns/op

ListAndArrayBenchmark.intWrapperArrayListAdd                    avgt   10      587171.486 ±    18024.785  ns/op
ListAndArrayBenchmark.intWrapperArrayListMaxInitialCapacityAdd  avgt   10      275162.756 ±   137232.241  ns/op
ListAndArrayBenchmark.intWrapperArraySystemArrayCopy            avgt   10       51665.321 ±      924.988  ns/op
ListAndArrayBenchmark.intWrapperCopyOnWriteArrayListAdd         avgt   10  2565683865.000 ± 80386661.015  ns/op
ListAndArrayBenchmark.intWrapperLinkedListAdd                   avgt   10      517665.444 ±     6696.515  ns/op

ListAndArrayBenchmark.longArrayAdd                              avgt   10      123265.985 ±     7725.410  ns/op
ListAndArrayBenchmark.longArrayClone                            avgt   10       87251.127 ±    38007.220  ns/op
ListAndArrayBenchmark.longArrayCopyOf                           avgt   10       80124.664 ±     7157.881  ns/op
ListAndArrayBenchmark.longArraySystemArrayCopy                  avgt   10       80467.364 ±    11408.959  ns/op

ListAndArrayBenchmark.longWrapperArrayAdd                       avgt   10      200024.661 ±     4073.460  ns/op
ListAndArrayBenchmark.longWrapperArrayClone                     avgt   10       53393.300 ±      929.811  ns/op
ListAndArrayBenchmark.longWrapperArrayCopyOf                    avgt   10       71964.753 ±    17535.314  ns/op
ListAndArrayBenchmark.longWrapperArraySystemArrayCopy           avgt   10       81811.972 ±     2025.381  ns/op
```

# Run complete. Total time: 00:14:57

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

Benchmark                                                       Mode  Cnt           Score          Error  Units
