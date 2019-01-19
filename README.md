# JMH Benchmarks

Benchmarks for LambdaMetaFactory, counters, etc.

All benchmarks were performed using JMH via `mvn clean install; java -jar target/benchmarks.jar`. 

You can run a single benchmark via `java -jar target/benchmarks.jar SetterBenchmark`.

The following measurements were obtained on OpenJDK 11.0.1 64Bit and Ubuntu 18.04 run inside VirtualBox 5.2 on an Intel I7-6700K clocked at 4.6GHz.
All 4 cores were allocated to the VM.

## Counters

AtomicInteger vs [MutableInt](https://commons.apache.org/proper/commons-lang/javadocs/api-release/index.html) vs int,
same for the corresponding long types.

10,000 ops per benchmark iteration, 4 threads.

```
Benchmark                       Mode  Cnt      Score      Error  Units
CounterBenchmark.atomicInteger  avgt    5  58331.685 ±  495.083  ns/op
CounterBenchmark.atomicLong     avgt    5  59190.863 ± 2449.507  ns/op
CounterBenchmark.mutableInt     avgt    5  44834.095 ±  413.847  ns/op
CounterBenchmark.mutableLong    avgt    5  45150.437 ±  987.252  ns/op
CounterBenchmark.primitiveInt   avgt    5  38397.222 ±  739.448  ns/op
CounterBenchmark.primitiveLong  avgt    5  42252.327 ±  334.210  ns/op
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
GetterBenchmark.lambdaMetaFactoryForGetter  avgt   10   4.184 ± 0.058  ns/op
GetterBenchmark.direct                      avgt   10   4.215 ± 0.046  ns/op
GetterBenchmark.reflection                  avgt   10   6.747 ± 0.057  ns/op
GetterBenchmark.methodHandleForGetter       avgt   10  20.631 ± 0.314  ns/op
GetterBenchmark.varHandle                   avgt   10  21.791 ± 0.382  ns/op
GetterBenchmark.methodHandleForField        avgt   10  20.979 ± 0.136  ns/op
```

### SetterBenchmark

```
Benchmark                                   Mode  Cnt   Score   Error  Units
SetterBenchmark.direct                      avgt   10   4.855 ± 0.086  ns/op
SetterBenchmark.lambdaMetaFactoryForSetter  avgt   10   5.087 ± 0.071  ns/op
SetterBenchmark.reflection                  avgt   10   8.399 ± 0.069  ns/op
SetterBenchmark.methodHandleForSetter       avgt   10  20.711 ± 0.361  ns/op
SetterBenchmark.methodHandleForField        avgt   10  20.961 ± 0.150  ns/op
SetterBenchmark.varHandle                   avgt   10  22.938 ± 0.395  ns/op
```

## ObjectPool

Compares the use of ints, custom int wrappers instantiation, and a custom int wrapper cache.

10,000 ops per benchmark iteration, 4 threads.

```
Benchmark                         Mode  Cnt      Score      Error  Units
ObjectCacheBenchmark.primitive    avgt   10   8055.249 ±  338.087  ns/op
ObjectCacheBenchmark.cache        avgt   10  19614.683 ±  389.400  ns/op
ObjectCacheBenchmark.constructor  avgt   10  63886.403 ± 8032.899  ns/op
```
