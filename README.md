# JMH Benchmarks

Benchmarks for LambdaMetaFactory, counters, List/array access, etc.

All benchmarks were performed using JMH via `mvn clean install; java -Xms1g -Xmx1g -jar target/benchmarks.jar`. 

You can run a single benchmark via `java -jar target/benchmarks.jar SetterBenchmark`.

The following measurements were obtained on OpenJDK 11.0.1 64Bit and Ubuntu 18.04 run inside VirtualBox 5.2 on an Intel I7-6700K clocked at 4.6GHz.
All 4 cores were allocated to the VM.

## Counters

AtomicInteger vs [MutableInt](https://commons.apache.org/proper/commons-lang/javadocs/api-release/index.html) vs int,
same for the corresponding long types.

10,000 calls per op, 4 threads.

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

10,000 calls per op, 4 threads.

```
Benchmark                         Mode  Cnt      Score      Error  Units
ObjectCacheBenchmark.cache                                      avgt   10       24717.721 ±     1395.363  ns/op
ObjectCacheBenchmark.constructor                                avgt   10       63253.711 ±     1730.443  ns/op
ObjectCacheBenchmark.primitive                                  avgt   10        9272.462 ±      293.206  ns/op
```

## Collection and Arrays

### Array Add

Compares adding elements to int/Integer/long/Long arrays.

10,000 adds (or a single copy operation) per op.

```
Benchmark                                          Mode  Cnt      Score     Error  Units
ArrayAddBenchmark.intArrayAdd                      avgt   10   3271.170 ± 634.894  ns/op
ArrayAddBenchmark.intArrayClone                    avgt   10   3844.599 ± 247.968  ns/op
ArrayAddBenchmark.intArrayCopyOf                   avgt   10   3582.752 ± 551.979  ns/op
ArrayAddBenchmark.intArraySystemArrayCopy          avgt   10   3698.474 ± 326.682  ns/op
ArrayAddBenchmark.intWrapperArrayAdd               avgt   10  18112.826 ± 339.506  ns/op
ArrayAddBenchmark.intWrapperArrayClone             avgt   10   3538.936 ±  87.128  ns/op
ArrayAddBenchmark.intWrapperArrayCopyOf            avgt   10   3566.759 ±  31.702  ns/op
ArrayAddBenchmark.intWrapperArraySystemArrayCopy   avgt   10   3703.386 ± 145.069  ns/op
ArrayAddBenchmark.longArrayAdd                     avgt   10   8578.231 ± 208.816  ns/op
ArrayAddBenchmark.longArrayClone                   avgt   10   6964.639 ± 112.279  ns/op
ArrayAddBenchmark.longArrayCopyOf                  avgt   10   7094.989 ± 219.463  ns/op
ArrayAddBenchmark.longArraySystemArrayCopy         avgt   10   7105.964 ± 329.539  ns/op
ArrayAddBenchmark.longWrapperArrayAdd              avgt   10  17837.441 ± 234.407  ns/op
ArrayAddBenchmark.longWrapperArrayClone            avgt   10   4182.566 ± 778.375  ns/op
ArrayAddBenchmark.longWrapperArrayCopyOf           avgt   10   3539.978 ±  55.301  ns/op
ArrayAddBenchmark.longWrapperArraySystemArrayCopy  avgt   10   3556.568 ±  78.921  ns/op
```

### Collection Add

Adding elements to collections. 

10,000 adds per op.


```
Benchmark                                           Mode  Cnt         Score         Error  Units
CollectionAddBenchmark.ArrayBlockingQueue           avgt   10    128801.987 ±    1570.413  ns/op
CollectionAddBenchmark.ArrayDeque                   avgt   10     38475.253 ±     805.859  ns/op
CollectionAddBenchmark.ArrayList                    avgt   10     68778.432 ±    7807.870  ns/op
CollectionAddBenchmark.ArrayListMaxInitialCapacity  avgt   10     56270.505 ±     712.803  ns/op
CollectionAddBenchmark.ConcurrentHashMap            avgt   10    474268.946 ±    7590.076  ns/op
CollectionAddBenchmark.ConcurrentLinkedDeque        avgt   10    130836.257 ±    4858.130  ns/op
CollectionAddBenchmark.ConcurrentSkipListSet        avgt   10    821430.718 ±   22529.265  ns/op
CollectionAddBenchmark.CopyOnWriteArrayList         avgt   10  25851831.566 ± 3076717.120  ns/op
CollectionAddBenchmark.CopyOnWriteArraySet          avgt   10  56486277.628 ± 3817445.556  ns/op
CollectionAddBenchmark.HashMap                      avgt   10    278880.529 ±    6600.343  ns/op
CollectionAddBenchmark.HashSet                      avgt   10    275134.318 ±    3873.290  ns/op
CollectionAddBenchmark.LinkedBlockingDeque          avgt   10    160542.393 ±    2256.626  ns/op
CollectionAddBenchmark.LinkedBlockingQueue          avgt   10    205966.161 ±    3068.404  ns/op
CollectionAddBenchmark.LinkedHashSet                avgt   10    529266.343 ±   12136.384  ns/op
CollectionAddBenchmark.LinkedList                   avgt   10     38428.862 ±    1382.738  ns/op
CollectionAddBenchmark.LinkedTransferQueue          avgt   10    155267.267 ±   11045.283  ns/op
CollectionAddBenchmark.PriorityBlockingQueue        avgt   10    164797.141 ±    1515.961  ns/op
CollectionAddBenchmark.PriorityQueue                avgt   10    165097.919 ±    2178.816  ns/op
CollectionAddBenchmark.Stack                        avgt   10     32481.071 ±     297.768  ns/op
CollectionAddBenchmark.TreeSet                      avgt   10    470955.698 ±   10526.683  ns/op
CollectionAddBenchmark.Vector                       avgt   10     33809.608 ±    1422.664  ns/op
```

### Concurrent Collection

Concurrent get (100 threads), add (1 thread) and remove (1 thread) for a number of thread-safe collection classes.

Each data structure is pre-populated with 100,000 elements prior to benchmarking. 

Access occurs for the head of the data structure (where the concept of head is supported), otherwise (such as in the instance of maps) by key.

ArrayList is protected by wrapping it via `Collections.synchronizedList()`.

1 get, add, or remove per op.

```
Benchmark                                                                           Mode  Cnt         Score           Error  Units
ConcurrentCollectionBenchmark.ArrayBlockingQueue                                 avgt   10     44680.539 ±     21001.042  ns/op
ConcurrentCollectionBenchmark.ArrayBlockingQueue:ArrayBlockingQueueAdd           avgt   10      1693.742 ±      1131.158  ns/op
ConcurrentCollectionBenchmark.ArrayBlockingQueue:ArrayBlockingQueueGet           avgt   10     43805.251 ±     24527.676  ns/op
ConcurrentCollectionBenchmark.ArrayBlockingQueue:ArrayBlockingQueueRemove        avgt   10    175196.050 ±    827307.543  ns/op
ConcurrentCollectionBenchmark.ArrayList                                          avgt   10  50158093.173 ± 108050542.101  ns/op
ConcurrentCollectionBenchmark.ArrayList:ArrayListAdd                             avgt   10   1632171.921 ±   7786279.509  ns/op
ConcurrentCollectionBenchmark.ArrayList:ArrayListGet                             avgt   10  50979603.200 ± 110205900.037  ns/op
ConcurrentCollectionBenchmark.ArrayList:ArrayListRemove                          avgt   10  16533011.636 ±   8168842.929  ns/op
ConcurrentCollectionBenchmark.ConcurrentHashMap                                  avgt   10       415.776 ±       129.528  ns/op
ConcurrentCollectionBenchmark.ConcurrentHashMap:ConcurrentHashMapAdd             avgt   10      3282.034 ±      2168.599  ns/op
ConcurrentCollectionBenchmark.ConcurrentHashMap:ConcurrentHashMapGet             avgt   10       381.065 ±       114.265  ns/op
ConcurrentCollectionBenchmark.ConcurrentHashMap:ConcurrentHashMapRemove          avgt   10      1020.668 ±      1327.161  ns/op
ConcurrentCollectionBenchmark.ConcurrentLinkedDeque                              avgt   10       295.548 ±        90.582  ns/op
ConcurrentCollectionBenchmark.ConcurrentLinkedDeque:ConcurrentLinkedDequeAdd     avgt   10      1045.734 ±       330.540  ns/op
ConcurrentCollectionBenchmark.ConcurrentLinkedDeque:ConcurrentLinkedDequeGet     avgt   10       264.106 ±        87.843  ns/op
ConcurrentCollectionBenchmark.ConcurrentLinkedDeque:ConcurrentLinkedDequeRemove  avgt   10      2689.496 ±       833.780  ns/op
ConcurrentCollectionBenchmark.ConcurrentSkipListSet                              avgt   10       170.185 ±        17.355  ns/op
ConcurrentCollectionBenchmark.ConcurrentSkipListSet:ConcurrentSkipListSetAdd     avgt   10      1842.670 ±      1189.880  ns/op
ConcurrentCollectionBenchmark.ConcurrentSkipListSet:ConcurrentSkipListSetGet     avgt   10       145.781 ±         7.842  ns/op
ConcurrentCollectionBenchmark.ConcurrentSkipListSet:ConcurrentSkipListSetRemove  avgt   10       938.053 ±       128.877  ns/op
ConcurrentCollectionBenchmark.CopyOnWriteArrayList                               avgt   10     42468.867 ±     54444.695  ns/op
ConcurrentCollectionBenchmark.CopyOnWriteArrayList:CopyOnWriteArrayAdd           avgt   10   1659181.408 ±   1537127.634  ns/op
ConcurrentCollectionBenchmark.CopyOnWriteArrayList:CopyOnWriteArrayListGet       avgt   10       152.261 ±        20.291  ns/op
ConcurrentCollectionBenchmark.CopyOnWriteArrayList:CopyOnWriteArrayRemove        avgt   10   2657416.942 ±   5488423.800  ns/op
ConcurrentCollectionBenchmark.CopyOnWriteArraySet                                avgt   10     43249.485 ±      9751.317  ns/op
ConcurrentCollectionBenchmark.CopyOnWriteArraySet:CopyOnWriteArraySetAdd         avgt   10   2332074.455 ±    614272.448  ns/op
ConcurrentCollectionBenchmark.CopyOnWriteArraySet:CopyOnWriteArraySetGet         avgt   10       130.319 ±        16.175  ns/op
ConcurrentCollectionBenchmark.CopyOnWriteArraySet:CopyOnWriteArraySetRemove      avgt   10   2066341.137 ±    647276.208  ns/op
``` 