# JMH Benchmarks

Benchmarks for LambdaMetaFactory, counters, etc.

All benchmarks were performed using JMH via `mvn clean install; java -jar target/benchmarks.jar`. 

You can run a single benchmark via `java -jar target/benchmarks.jar SetterBenchmark`.

The following measurements were obtained on OpenJDK 11.0.1 64Bit and Ubuntu 18.04 run inside VirtualBox 5.2 on an Intel I7-6700K clocked at 4.6GHz.
All 4 cores were allocated to the VM.

## Counters

AtomicInteger vs [MutableInt](https://commons.apache.org/proper/commons-lang/javadocs/api-release/index.html) vs int

```
Benchmark                                   Mode  Cnt   Score   Error  Units
CounterBenchmark.atomicInteger              avgt   10   5.522 ± 0.025  ns/op
CounterBenchmark.atomicLong                 avgt   10   5.567 ± 0.110  ns/op

CounterBenchmark.mutableInt                 avgt   10   4.442 ± 0.035  ns/op
CounterBenchmark.mutableLong                avgt   10   4.185 ± 0.011  ns/op

CounterBenchmark.primitiveInt               avgt   10   4.186 ± 0.010  ns/op
CounterBenchmark.primitiveLong              avgt   10   4.215 ± 0.054  ns/op
```

## Getters and Setters

Comparing various ways of getting and setting object fields. 

Ordered by performance from top to bottom, the ranking is:
1. Direct call
1. [LambdaMetaFactory](https://docs.oracle.com/javase/8/docs/api/java/lang/invoke/LambdaMetafactory.html) - as fast as direct, but requires at least a private accessor method 
1. Reflection - 60% of the direct performance
1. MethodHandle and VarHandle - 20% of the direct performance

### GetterBenchmark

Ordered by score:

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

Ordered by score:

```
Benchmark                                   Mode  Cnt   Score   Error  Units
SetterBenchmark.direct                      avgt   10   4.855 ± 0.086  ns/op
SetterBenchmark.lambdaMetaFactoryForSetter  avgt   10   5.087 ± 0.071  ns/op
SetterBenchmark.reflection                  avgt   10   8.399 ± 0.069  ns/op
SetterBenchmark.methodHandleForSetter       avgt   10  20.711 ± 0.361  ns/op
SetterBenchmark.methodHandleForField        avgt   10  20.961 ± 0.150  ns/op
SetterBenchmark.varHandle                   avgt   10  22.938 ± 0.395  ns/op
```
