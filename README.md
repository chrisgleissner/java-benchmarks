# Java 11 JMH Benchmarks

[![Build Status](https://travis-ci.org/chrisgleissner/jutil.svg?branch=master)](https://travis-ci.org/chrisgleissner/benchmarks)

Java 11 [JMH](https://openjdk.java.net/projects/code-tools/jmh/) benchmarks for field access, counters, collections, etc. 

View the [Latest Results](https://jmh.morethan.io/?source=https://raw.githubusercontent.com/chrisgleissner/benchmarks/master/src/main/resources/jmh-result.json)
or download them as [JSON](https://raw.githubusercontent.com/chrisgleissner/benchmarks/master/src/main/resources/jmh-result.json).


### Setup

The benchmarks were obtained using JMH on OpenJDK 11.0.1 64Bit 
and Ubuntu 18.04 running inside VirtualBox 5.2 on an Intel I7-6700K clocked at 4.6GHz. All 4 physical cores were allocated to the VirtualBox VM.

To run the benchmarks on your own system, clone this repository, install Open JDK 11 (or above) and Maven 3.5 (or above), then run the following command:

```
mvn clean install; java -Xms4g -Xmx4g -jar target/benchmarks.jar -gc true -rf json
```
 
To view all available JMH command line options, run `java -jar target/benchmarks.jar -h`

## Counter

AtomicInteger vs [MutableInt](https://commons.apache.org/proper/commons-lang/javadocs/api-release/index.html) vs int,
same for the corresponding long types.

1,000 calls per op.

```
Benchmark                      Mode  Cnt        Score          Error  Units
CounterBenchmark.atomicInteger avgt   10        5.747 ±        0.031  us/op
CounterBenchmark.atomicLong    avgt   10        5.743 ±        0.022  us/op
CounterBenchmark.mutableInt    avgt   10        4.212 ±        0.027  us/op
CounterBenchmark.mutableLong   avgt   10        4.418 ±        0.010  us/op
CounterBenchmark.primitiveInt  avgt   10        3.750 ±        0.004  us/op
CounterBenchmark.primitiveLong avgt   10        4.004 ±        0.027  us/op
```

## Getter and Setter

Comparing various ways of getting and setting object fields. 

Ordered by performance from top to bottom, the ranking is:
1. Direct call
1. [LambdaMetaFactory](https://docs.oracle.com/javase/8/docs/api/java/lang/invoke/LambdaMetafactory.html) - as fast as direct, but requires at least a private accessor method 
1. Reflection - 60% of the direct performance
1. MethodHandle and VarHandle - 20% of the direct performance

1 call per op.

### Getter

```
Benchmark                                  Mode  Cnt        Score          Error  Units
GetterBenchmark.direct                     avgt   10        4.216 ±        0.170  ns/op
GetterBenchmark.lambdaMetaFactoryForGetter avgt   10        4.152 ±        0.013  ns/op
GetterBenchmark.methodHandleForField       avgt   10       20.960 ±        0.017  ns/op
GetterBenchmark.methodHandleForGetter      avgt   10       20.563 ±        0.092  ns/op
GetterBenchmark.reflection                 avgt   10        6.837 ±        0.418  ns/op
GetterBenchmark.varHandle                  avgt   10       21.546 ±        0.115  ns/op
```

### Setter

```
Benchmark                                  Mode  Cnt        Score          Error  Units
SetterBenchmark.direct                     avgt   10        4.866 ±        0.006  ns/op
SetterBenchmark.lambdaMetaFactoryForSetter avgt   10        5.079 ±        0.020  ns/op
SetterBenchmark.methodHandleForField       avgt   10       20.921 ±        0.061  ns/op
SetterBenchmark.methodHandleForSetter      avgt   10       20.541 ±        0.024  ns/op
SetterBenchmark.reflection                 avgt   10        8.296 ±        0.279  ns/op
SetterBenchmark.varHandle                  avgt   10       22.806 ±        0.099  ns/op
```

## Object Cache

Compares the use of ints, custom int wrappers instantiation, and a custom int wrapper cache.

1,000 calls per op.

```
Benchmark                                  Mode  Cnt        Score          Error  Units
ObjectCacheBenchmark.cache                 avgt   10        1.854 ±        0.166  us/op
ObjectCacheBenchmark.constructor           avgt   10        2.352 ±        0.025  us/op
ObjectCacheBenchmark.primitive             avgt   10        0.487 ±        0.008  us/op
```

## Arrays, Collections, and Maps

### Array Add

Compares adding elements to int/Integer/long/Long arrays.

1,000 adds (or a single copy operation) per op.

```
Benchmark                                Mode  Cnt        Score          Error  Units
ArrayAddBenchmark.intArrayAdd            avgt   10        0.283 ±        0.002  us/op
ArrayAddBenchmark.intArrayClone          avgt   10        0.335 ±        0.007  us/op
ArrayAddBenchmark.intArrayCopyOf         avgt   10        0.334 ±        0.002  us/op
ArrayAddBenchmark.intWrapperArrayAdd     avgt   10        1.915 ±        0.075  us/op
ArrayAddBenchmark.intWrapperArrayClone   avgt   10        0.334 ±        0.003  us/op
ArrayAddBenchmark.intWrapperArrayCopyOf  avgt   10        0.334 ±        0.001  us/op
ArrayAddBenchmark.longArrayAdd           avgt   10        0.468 ±        0.005  us/op
ArrayAddBenchmark.longArrayClone         avgt   10        0.666 ±        0.011  us/op
ArrayAddBenchmark.longArrayCopyOf        avgt   10        0.662 ±        0.005  us/op
ArrayAddBenchmark.longWrapperArrayAdd    avgt   10        1.918 ±        0.049  us/op
ArrayAddBenchmark.longWrapperArrayClone  avgt   10        0.334 ±        0.004  us/op
ArrayAddBenchmark.longWrapperArrayCopyOf avgt   10        0.334 ±        0.001  us/op
```

### Collection Add

Adding Integer elements to empty collections and maps. 

1,000 adds (or map puts) per op.

```
Benchmark                                    Mode  Cnt        Score          Error  Units
CollectionAddBenchmark.ArrayDeque            avgt   10       16.191 ±        1.446  us/op
CollectionAddBenchmark.ArrayList             avgt   10       21.306 ±       11.225  us/op
CollectionAddBenchmark.ArrayListNoResize     avgt   10        7.871 ±        0.013  us/op
CollectionAddBenchmark.ConcurrentHashMap     avgt   10       25.412 ±        0.037  us/op
CollectionAddBenchmark.ConcurrentLinkedDeque avgt   10       74.740 ±       14.737  us/op
CollectionAddBenchmark.ConcurrentSkipListSet avgt   10       69.552 ±        3.930  us/op
CollectionAddBenchmark.CopyOnWriteArrayList  avgt   10    15660.896 ±      328.200  us/op
CollectionAddBenchmark.CopyOnWriteArraySet   avgt   10      260.462 ±        0.753  us/op
CollectionAddBenchmark.HashMap               avgt   10        8.062 ±        0.162  us/op
CollectionAddBenchmark.HashSet               avgt   10        8.560 ±        0.072  us/op
CollectionAddBenchmark.LinkedBlockingDeque   avgt   10       78.386 ±       16.954  us/op
CollectionAddBenchmark.LinkedBlockingQueue   avgt   10       86.543 ±        4.462  us/op
CollectionAddBenchmark.LinkedHashMap         avgt   10        8.889 ±        0.034  us/op
CollectionAddBenchmark.LinkedHashSet         avgt   10        7.131 ±        0.064  us/op
CollectionAddBenchmark.LinkedList            avgt   10       69.726 ±       15.858  us/op
CollectionAddBenchmark.LinkedTransferQueue   avgt   10       69.145 ±       10.849  us/op
CollectionAddBenchmark.PriorityBlockingQueue avgt   10       36.235 ±        0.652  us/op
CollectionAddBenchmark.PriorityQueue         avgt   10       22.280 ±        1.632  us/op
CollectionAddBenchmark.Stack                 avgt   10       30.806 ±       19.612  us/op
CollectionAddBenchmark.TreeSet               avgt   10       39.268 ±        0.606  us/op
CollectionAddBenchmark.Vector                avgt   10       32.926 ±       21.069  us/op
CollectionAddBenchmark.VectorNoResize        avgt   10       10.158 ±        1.046  us/op

```

## Collection Iterate

Iterating over all 1,000 elements of pre-populated collections and maps. 

One iteration over all elements per op.

```
Benchmark                                        Mode  Cnt        Score          Error  Units
CollectionIterateBenchmark.ArrayBlockingQueue    avgt   10       14.545 ±        0.063  us/op
CollectionIterateBenchmark.ArrayDeque            avgt   10        5.505 ±        0.046  us/op
CollectionIterateBenchmark.ArrayList             avgt   10        5.940 ±        0.034  us/op
CollectionIterateBenchmark.ArrayListNoResize     avgt   10        6.027 ±        0.044  us/op
CollectionIterateBenchmark.ConcurrentHashMap     avgt   10        9.104 ±        0.046  us/op
CollectionIterateBenchmark.ConcurrentLinkedDeque avgt   10        5.785 ±        0.205  us/op
CollectionIterateBenchmark.ConcurrentSkipListSet avgt   10        7.270 ±        0.822  us/op
CollectionIterateBenchmark.CopyOnWriteArrayList  avgt   10        5.050 ±        0.010  us/op
CollectionIterateBenchmark.CopyOnWriteArraySet   avgt   10        5.034 ±        0.010  us/op
CollectionIterateBenchmark.HashMap               avgt   10        6.187 ±        0.006  us/op
CollectionIterateBenchmark.HashSet               avgt   10        6.991 ±        0.150  us/op
CollectionIterateBenchmark.LinkedBlockingDeque   avgt   10       13.787 ±        0.066  us/op
CollectionIterateBenchmark.LinkedBlockingQueue   avgt   10       26.712 ±        0.402  us/op
CollectionIterateBenchmark.LinkedHashMap         avgt   10        4.800 ±        0.022  us/op
CollectionIterateBenchmark.LinkedHashSet         avgt   10        6.217 ±        0.077  us/op
CollectionIterateBenchmark.LinkedList            avgt   10        5.929 ±        0.020  us/op
CollectionIterateBenchmark.LinkedTransferQueue   avgt   10        6.159 ±        0.021  us/op
CollectionIterateBenchmark.PriorityBlockingQueue avgt   10        5.074 ±        0.063  us/op
CollectionIterateBenchmark.PriorityQueue         avgt   10        5.482 ±        0.008  us/op
CollectionIterateBenchmark.Stack                 avgt   10        6.528 ±        0.030  us/op
CollectionIterateBenchmark.TreeSet               avgt   10        6.443 ±        0.027  us/op
CollectionIterateBenchmark.Vector                avgt   10        7.153 ±        0.040  us/op
CollectionIterateBenchmark.VectorNoResize        avgt   10        7.078 ±        0.027  us/op

```

### Concurrent Collection

Concurrent get (10 threads), add (2 threads) and remove (1 thread) of Integer elements for a number of thread-safe collection classes.

The non thread-safe ArrayList class is included in this benchmark and gets protected by wrapping it via `Collections.synchronizedList()`.

Each data structure is pre-populated with 100,000 elements prior to benchmarking. Access occurs for the head of the data structure (where the concept of head is supported), otherwise (such as in the instance of maps) by key.

1 get, add, or remove per op.

```
Benchmark                                                                  Mode  Cnt        Score          Error  Units
ConcCollectionBenchmark.ArrayBlockingQueue                                 avgt   10      363.098 ±      104.212  ns/op
ConcCollectionBenchmark.ArrayBlockingQueue:ArrayBlockingQueueAdd           avgt   10     1032.711 ±      822.364  ns/op
ConcCollectionBenchmark.ArrayBlockingQueue:ArrayBlockingQueueGet           avgt   10      239.022 ±       55.867  ns/op
ConcCollectionBenchmark.ArrayBlockingQueue:ArrayBlockingQueueRemove        avgt   10      264.631 ±      143.687  ns/op
ConcCollectionBenchmark.ArrayList                                          avgt   10  5994548.575 ± 27517339.013  ns/op
ConcCollectionBenchmark.ArrayList:ArrayListAdd                             avgt   10   231800.054 ±   751475.262  ns/op
ConcCollectionBenchmark.ArrayList:ArrayListGet                             avgt   10  7734432.825 ± 35787039.998  ns/op
ConcCollectionBenchmark.ArrayList:ArrayListRemove                          avgt   10   121203.117 ±   295489.345  ns/op
ConcCollectionBenchmark.ConcurrentHashMap                                  avgt   10      102.020 ±       18.206  ns/op
ConcCollectionBenchmark.ConcurrentHashMap:ConcurrentHashMapAdd             avgt   10      406.319 ±      120.475  ns/op
ConcCollectionBenchmark.ConcurrentHashMap:ConcurrentHashMapGet             avgt   10       40.876 ±        4.801  ns/op
ConcCollectionBenchmark.ConcurrentHashMap:ConcurrentHashMapRemove          avgt   10      104.855 ±       49.691  ns/op
ConcCollectionBenchmark.ConcurrentLinkedDeque                              avgt   10      161.079 ±       23.294  ns/op
ConcCollectionBenchmark.ConcurrentLinkedDeque:ConcurrentLinkedDequeAdd     avgt   10      313.311 ±       34.702  ns/op
ConcCollectionBenchmark.ConcurrentLinkedDeque:ConcurrentLinkedDequeGet     avgt   10       89.386 ±       20.103  ns/op
ConcCollectionBenchmark.ConcurrentLinkedDeque:ConcurrentLinkedDequeRemove  avgt   10      573.546 ±      229.567  ns/op
ConcCollectionBenchmark.ConcurrentSkipListSet                              avgt   10       76.340 ±       26.898  ns/op
ConcCollectionBenchmark.ConcurrentSkipListSet:ConcurrentSkipListSetAdd     avgt   10      260.780 ±      106.721  ns/op
ConcCollectionBenchmark.ConcurrentSkipListSet:ConcurrentSkipListSetGet     avgt   10       20.310 ±        2.596  ns/op
ConcCollectionBenchmark.ConcurrentSkipListSet:ConcurrentSkipListSetRemove  avgt   10      267.754 ±      152.685  ns/op
ConcCollectionBenchmark.CopyOnWriteArrayList                               avgt   10   143359.849 ±    10783.184  ns/op
ConcCollectionBenchmark.CopyOnWriteArrayList:CopyOnWriteArrayAdd           avgt   10   505111.126 ±   162177.578  ns/op
ConcCollectionBenchmark.CopyOnWriteArrayList:CopyOnWriteArrayListGet       avgt   10       13.828 ±        0.335  ns/op
ConcCollectionBenchmark.CopyOnWriteArrayList:CopyOnWriteArrayRemove        avgt   10   498338.676 ±   136372.601  ns/op
ConcCollectionBenchmark.CopyOnWriteArraySet                                avgt   10    80090.257 ±     4588.779  ns/op
ConcCollectionBenchmark.CopyOnWriteArraySet:CopyOnWriteArraySetAdd         avgt   10   280638.345 ±    24488.064  ns/op
ConcCollectionBenchmark.CopyOnWriteArraySet:CopyOnWriteArraySetGet         avgt   10       19.331 ±        0.977  ns/op
ConcCollectionBenchmark.CopyOnWriteArraySet:CopyOnWriteArraySetRemove      avgt   10   279896.804 ±    26231.637  ns/op
``` 
