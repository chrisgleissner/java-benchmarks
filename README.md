# JMH Benchmarks

Benchmarks for LambdaMetaFactory, counters, List/array access, etc.

All benchmarks were performed using JMH via `mvn clean install; java -Xms4g -Xmx4g -jar target/benchmarks.jar`. 

You can run a single benchmark via `java -jar target/benchmarks.jar SetterBenchmark`.

The following measurements were obtained on OpenJDK 11.0.1 64Bit and Ubuntu 18.04 run inside VirtualBox 5.2 on an Intel I7-6700K clocked at 4.6GHz.
All 4 cores were allocated to the VM.

## Counters

AtomicInteger vs [MutableInt](https://commons.apache.org/proper/commons-lang/javadocs/api-release/index.html) vs int,
same for the corresponding long types.

10,000 calls per op.

```
Benchmark                         Mode  Cnt        Score         Error  Units
CounterBenchmark.atomicInteger    avgt   10       57.202 ±       0.276  us/op
CounterBenchmark.atomicLong       avgt   10       57.097 ±       0.218  us/op
CounterBenchmark.mutableInt       avgt   10       43.779 ±       0.458  us/op
CounterBenchmark.mutableLong      avgt   10       43.994 ±       0.207  us/op
CounterBenchmark.primitiveInt     avgt   10       37.485 ±       0.226  us/op
CounterBenchmark.primitiveLong    avgt   10       40.520 ±       0.464  us/op
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
Benchmark                                     Mode  Cnt           Score          Error  Units
GetterBenchmark.direct                        avgt   10           3.951 ±        0.005  ns/op
GetterBenchmark.lambdaMetaFactoryForGetter    avgt   10           4.182 ±        0.035  ns/op
GetterBenchmark.methodHandleForField          avgt   10          19.478 ±        0.243  ns/op
GetterBenchmark.methodHandleForGetter         avgt   10          19.242 ±        0.401  ns/op
GetterBenchmark.reflection                    avgt   10           6.086 ±        0.050  ns/op
GetterBenchmark.varHandle                     avgt   10          20.060 ±        0.145  ns/op
```

### SetterBenchmark

```
Benchmark                                      Mode  Cnt           Score          Error  Units
SetterBenchmark.direct                         avgt   10           4.980 ±        0.096  ns/op
SetterBenchmark.lambdaMetaFactoryForSetter     avgt   10           4.717 ±        0.111  ns/op
SetterBenchmark.methodHandleForField           avgt   10          20.414 ±        1.327  ns/op
SetterBenchmark.methodHandleForSetter          avgt   10          19.141 ±        0.404  ns/op
SetterBenchmark.reflection                     avgt   10           6.123 ±        0.105  ns/op
SetterBenchmark.varHandle                      avgt   10          20.782 ±        0.204  ns/op
```

## ObjectPool

Compares the use of ints, custom int wrappers instantiation, and a custom int wrapper cache.

10,000 calls per op.

```
Benchmark                                      Mode  Cnt        Score         Error  Units
ObjectCacheBenchmark.cache                     avgt   10       17.312 ±       0.057  us/op
ObjectCacheBenchmark.constructor               avgt   10       20.775 ±       0.296  us/op
ObjectCacheBenchmark.primitive                 avgt   10        4.284 ±       0.053  us/op
```

## Collection and Arrays

### Array Add

Compares adding elements to int/Integer/long/Long arrays.

10,000 adds (or a single copy operation) per op.

```
Benchmark                                           Mode  Cnt        Score         Error  Units
ArrayAddBenchmark.intArrayAdd                       avgt   10        2.931 ±       0.017  us/op
ArrayAddBenchmark.intArrayClone                     avgt   10        3.568 ±       0.048  us/op
ArrayAddBenchmark.intArrayCopyOf                    avgt   10        3.565 ±       0.056  us/op
ArrayAddBenchmark.intArraySystemArrayCopy           avgt   10        3.547 ±       0.018  us/op
ArrayAddBenchmark.intWrapperArrayAdd                avgt   10       18.837 ±       0.128  us/op
ArrayAddBenchmark.intWrapperArrayClone              avgt   10        3.618 ±       0.038  us/op
ArrayAddBenchmark.intWrapperArrayCopyOf             avgt   10        3.618 ±       0.038  us/op
ArrayAddBenchmark.intWrapperArraySystemArrayCopy    avgt   10        3.622 ±       0.052  us/op
ArrayAddBenchmark.longArrayAdd                      avgt   10        5.973 ±       0.046  us/op
ArrayAddBenchmark.longArrayClone                    avgt   10        7.187 ±       0.121  us/op
ArrayAddBenchmark.longArrayCopyOf                   avgt   10        7.142 ±       0.096  us/op
ArrayAddBenchmark.longArraySystemArrayCopy          avgt   10        7.194 ±       0.070  us/op
ArrayAddBenchmark.longWrapperArrayAdd               avgt   10       18.778 ±       0.123  us/op
ArrayAddBenchmark.longWrapperArrayClone             avgt   10        3.626 ±       0.037  us/op
ArrayAddBenchmark.longWrapperArrayCopyOf            avgt   10        3.611 ±       0.027  us/op
ArrayAddBenchmark.longWrapperArraySystemArrayCopy   avgt   10        3.633 ±       0.042  us/op
```

### Collection Add

Adding Integer elements to empty collections and maps. 

10,000 adds (or map puts) per op.


```
Benchmark                                     Mode  Cnt       Score      Error  Units
CollectionAddBenchmark.ArrayDeque             avgt   10     154.876 ±    6.278  us/op
CollectionAddBenchmark.ArrayList              avgt   10     148.269 ±    7.644  us/op
CollectionAddBenchmark.ArrayListNoResize      avgt   10      72.363 ±    3.528  us/op
CollectionAddBenchmark.ConcurrentHashMap      avgt   10     272.294 ±    2.511  us/op
CollectionAddBenchmark.ConcurrentLinkedDeque  avgt   10     659.722 ±    4.871  us/op
CollectionAddBenchmark.ConcurrentSkipListSet  avgt   10     854.643 ±   31.776  us/op
CollectionAddBenchmark.CopyOnWriteArrayList   avgt   10  173436.145 ± 2555.475  us/op
CollectionAddBenchmark.CopyOnWriteArraySet    avgt   10   27903.081 ±  286.386  us/op
CollectionAddBenchmark.HashMap                avgt   10      78.644 ±    7.914  us/op
CollectionAddBenchmark.HashSet                avgt   10      81.986 ±    3.718  us/op
CollectionAddBenchmark.LinkedBlockingDeque    avgt   10     738.731 ±   10.379  us/op
CollectionAddBenchmark.LinkedBlockingQueue    avgt   10     564.505 ±  415.666  us/op
CollectionAddBenchmark.LinkedHashMap          avgt   10      87.631 ±   10.606  us/op
CollectionAddBenchmark.LinkedHashSet          avgt   10      85.709 ±   14.606  us/op
CollectionAddBenchmark.LinkedList             avgt   10     493.971 ±   21.827  us/op
CollectionAddBenchmark.LinkedTransferQueue    avgt   10     671.724 ±  165.420  us/op
CollectionAddBenchmark.PriorityBlockingQueue  avgt   10     355.267 ±   12.489  us/op
CollectionAddBenchmark.PriorityQueue          avgt   10     235.520 ±    6.315  us/op
CollectionAddBenchmark.Stack                  avgt   10     132.575 ±    7.389  us/op
CollectionAddBenchmark.TreeSet                avgt   10     479.322 ±    5.275  us/op
CollectionAddBenchmark.Vector                 avgt   10     134.026 ±    6.441  us/op
CollectionAddBenchmark.VectorNoResize         avgt   10      73.735 ±   10.983  us/op
```

## Collection Iterate

Iterating over all 10,000 elements of pre-populated collections and maps. 

One iteration over all elements per op.

```
Benchmark                                           Mode  Cnt        Score         Error  Units
CollectionIterateBenchmark.ArrayBlockingQueue       avgt   10      156.459 ±       7.685  us/op
CollectionIterateBenchmark.ArrayDeque               avgt   10       54.291 ±       0.447  us/op
CollectionIterateBenchmark.ArrayList                avgt   10       60.138 ±       0.541  us/op
CollectionIterateBenchmark.ArrayListNoResize        avgt   10       58.104 ±       0.666  us/op
CollectionIterateBenchmark.ConcurrentHashMap        avgt   10      141.337 ±       6.609  us/op
CollectionIterateBenchmark.ConcurrentLinkedDeque    avgt   10       69.541 ±       2.508  us/op
CollectionIterateBenchmark.ConcurrentSkipListSet    avgt   10       74.831 ±       3.983  us/op
CollectionIterateBenchmark.CopyOnWriteArrayList     avgt   10       51.989 ±       6.389  us/op
CollectionIterateBenchmark.CopyOnWriteArraySet      avgt   10       50.873 ±       0.554  us/op
CollectionIterateBenchmark.HashMap                  avgt   10       57.741 ±       0.723  us/op
CollectionIterateBenchmark.HashSet                  avgt   10       77.470 ±       2.497  us/op
CollectionIterateBenchmark.LinkedBlockingDeque      avgt   10      148.750 ±       9.582  us/op
CollectionIterateBenchmark.LinkedBlockingQueue      avgt   10      270.227 ±       4.182  us/op
CollectionIterateBenchmark.LinkedHashMap            avgt   10       48.381 ±       0.248  us/op
CollectionIterateBenchmark.LinkedHashSet            avgt   10       61.155 ±       0.434  us/op
CollectionIterateBenchmark.LinkedList               avgt   10       61.727 ±       2.141  us/op
CollectionIterateBenchmark.LinkedTransferQueue      avgt   10       62.214 ±       2.468  us/op
CollectionIterateBenchmark.PriorityBlockingQueue    avgt   10       47.266 ±       0.465  us/op
CollectionIterateBenchmark.PriorityQueue            avgt   10       54.441 ±       0.918  us/op
CollectionIterateBenchmark.Stack                    avgt   10       65.207 ±       0.974  us/op
CollectionIterateBenchmark.TreeSet                  avgt   10       71.716 ±       1.913  us/op
CollectionIterateBenchmark.Vector                   avgt   10       64.666 ±       0.162  us/op
CollectionIterateBenchmark.VectorNoResize           avgt   10       64.693 ±       0.247  us/op

```

### Concurrent Collection

Concurrent get (100 threads), add (1 thread) and remove (1 thread) of Integer elements for a number of thread-safe collection classes.

The non thread-safe ArrayList class is included in this benchmark and gets protected by wrapping it via `Collections.synchronizedList()`.

Each data structure is pre-populated with 100,000 elements prior to benchmarking. Access occurs for the head of the data structure (where the concept of head is supported), otherwise (such as in the instance of maps) by key.

1 get, add, or remove per op.

```
Benchmark                                                                  Mode  Cnt        Score         Error  Units
ConcCollectionBenchmark.ArrayList                                          avgt   10   655541.535 ± 1246038.062  us/op
ConcCollectionBenchmark.ArrayList:ArrayListAdd                             avgt   10  1521202.521 ± 7272593.007  us/op
ConcCollectionBenchmark.ArrayList:ArrayListGet                             avgt   10   653390.892 ± 1210708.747  us/op
ConcCollectionBenchmark.ArrayList:ArrayListRemove                          avgt   10     4944.835 ±    1969.179  us/op
ConcCollectionBenchmark.ConcurrentHashMap                                  avgt   10        0.264 ±       0.016  us/op
ConcCollectionBenchmark.ConcurrentHashMap:ConcurrentHashMapAdd             avgt   10        2.228 ±       1.619  us/op
ConcCollectionBenchmark.ConcurrentHashMap:ConcurrentHashMapGet             avgt   10        0.242 ±       0.019  us/op
ConcCollectionBenchmark.ConcurrentHashMap:ConcurrentHashMapRemove          avgt   10        0.456 ±       0.138  us/op
ConcCollectionBenchmark.ConcurrentLinkedDeque                              avgt   10        0.250 ±       0.063  us/op
ConcCollectionBenchmark.ConcurrentLinkedDeque:ConcurrentLinkedDequeAdd     avgt   10        1.146 ±       0.277  us/op
ConcCollectionBenchmark.ConcurrentLinkedDeque:ConcurrentLinkedDequeGet     avgt   10        0.219 ±       0.061  us/op
ConcCollectionBenchmark.ConcurrentLinkedDeque:ConcurrentLinkedDequeRemove  avgt   10        2.450 ±       0.642  us/op
ConcCollectionBenchmark.ConcurrentSkipListSet                              avgt   10        0.192 ±       0.005  us/op
ConcCollectionBenchmark.ConcurrentSkipListSet:ConcurrentSkipListSetAdd     avgt   10        3.236 ±       0.352  us/op
ConcCollectionBenchmark.ConcurrentSkipListSet:ConcurrentSkipListSetGet     avgt   10        0.145 ±       0.004  us/op
ConcCollectionBenchmark.ConcurrentSkipListSet:ConcurrentSkipListSetRemove  avgt   10        1.889 ±       0.485  us/op
ConcCollectionBenchmark.CopyOnWriteArrayList                               avgt   10       36.112 ±       9.502  us/op
ConcCollectionBenchmark.CopyOnWriteArrayList:CopyOnWriteArrayAdd           avgt   10     1649.173 ±     741.056  us/op
ConcCollectionBenchmark.CopyOnWriteArrayList:CopyOnWriteArrayListGet       avgt   10        0.118 ±       0.003  us/op
ConcCollectionBenchmark.CopyOnWriteArrayList:CopyOnWriteArrayRemove        avgt   10     2022.463 ±     973.523  us/op
ConcCollectionBenchmark.CopyOnWriteArraySet                                avgt   10       34.887 ±       5.212  us/op
ConcCollectionBenchmark.CopyOnWriteArraySet:CopyOnWriteArraySetAdd         avgt   10     1788.162 ±     267.450  us/op
ConcCollectionBenchmark.CopyOnWriteArraySet:CopyOnWriteArraySetGet         avgt   10        0.124 ±       0.002  us/op
ConcCollectionBenchmark.CopyOnWriteArraySet:CopyOnWriteArraySetRemove      avgt   10     1757.897 ±     329.423  us/op
``` 