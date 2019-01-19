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

Adding Integer elements to collections and maps. 

10,000 adds (or map puts) per op.


```
CollectionAddBenchmark.ArrayBlockingQueue           avgt   30    128980.709 ±     824.172  ns/op
CollectionAddBenchmark.ArrayDeque                   avgt   30     38090.086 ±     189.611  ns/op
CollectionAddBenchmark.ArrayList                    avgt   30     68548.313 ±    2658.032  ns/op
CollectionAddBenchmark.ArrayListMaxInitialCapacity  avgt   30     55869.729 ±     353.292  ns/op
CollectionAddBenchmark.ConcurrentHashMap            avgt   30    471491.121 ±    2491.825  ns/op
CollectionAddBenchmark.ConcurrentLinkedDeque        avgt   30    131918.110 ±    2627.505  ns/op
CollectionAddBenchmark.ConcurrentSkipListSet        avgt   30    867192.930 ±   25997.034  ns/op
CollectionAddBenchmark.CopyOnWriteArrayList         avgt   30  24500152.607 ± 1805639.971  ns/op
CollectionAddBenchmark.CopyOnWriteArraySet          avgt   30  57891796.457 ± 3720937.515  ns/op
CollectionAddBenchmark.HashMap                      avgt   30    279095.276 ±    5443.099  ns/op
CollectionAddBenchmark.HashSet                      avgt   30    204969.146 ±   45711.665  ns/op
CollectionAddBenchmark.LinkedBlockingDeque          avgt   30    162958.593 ±    1999.146  ns/op
CollectionAddBenchmark.LinkedBlockingQueue          avgt   30    208178.419 ±    2281.415  ns/op
CollectionAddBenchmark.LinkedHashMap                avgt   30    117933.177 ±    7289.984  ns/op
CollectionAddBenchmark.LinkedHashSet                avgt   30    540632.672 ±   10148.733  ns/op
CollectionAddBenchmark.LinkedList                   avgt   30     40847.156 ±    2503.620  ns/op
CollectionAddBenchmark.LinkedTransferQueue          avgt   30    153723.661 ±    5541.205  ns/op
CollectionAddBenchmark.PriorityBlockingQueue        avgt   30    168025.837 ±     721.178  ns/op
CollectionAddBenchmark.PriorityQueue                avgt   30    168680.932 ±    1063.233  ns/op
CollectionAddBenchmark.Stack                        avgt   30     34150.358 ±     348.824  ns/op
CollectionAddBenchmark.TreeSet                      avgt   30    558911.094 ±   43806.722  ns/op
CollectionAddBenchmark.Vector                       avgt   30     33914.859 ±     423.102  ns/op
```

### Concurrent Collection

Concurrent get (100 threads), add (1 thread) and remove (1 thread) of Integer elements for a number of thread-safe collection classes.

The non thread-safe ArrayList class is included in this benchmark and gets protected by wrapping it via `Collections.synchronizedList()`.

Each data structure is pre-populated with 100,000 elements prior to benchmarking. Access occurs for the head of the data structure (where the concept of head is supported), otherwise (such as in the instance of maps) by key.



1 get, add, or remove per op.

```
Benchmark                                                                           Mode  Cnt         Score           Error  Units
ConcCollectionBenchmark.ArrayBlockingQueue                                 avgt   10     44680.539 ±     21001.042  ns/op
ConcCollectionBenchmark.ArrayBlockingQueue:ArrayBlockingQueueAdd           avgt   10      1693.742 ±      1131.158  ns/op
ConcCollectionBenchmark.ArrayBlockingQueue:ArrayBlockingQueueGet           avgt   10     43805.251 ±     24527.676  ns/op
ConcCollectionBenchmark.ArrayBlockingQueue:ArrayBlockingQueueRemove        avgt   10    175196.050 ±    827307.543  ns/op
ConcCollectionBenchmark.ArrayList                                          avgt   10  50158093.173 ± 108050542.101  ns/op
ConcCollectionBenchmark.ArrayList:ArrayListAdd                             avgt   10   1632171.921 ±   7786279.509  ns/op
ConcCollectionBenchmark.ArrayList:ArrayListGet                             avgt   10  50979603.200 ± 110205900.037  ns/op
ConcCollectionBenchmark.ArrayList:ArrayListRemove                          avgt   10  16533011.636 ±   8168842.929  ns/op
ConcCollectionBenchmark.ConcurrentHashMap                                  avgt   10       415.776 ±       129.528  ns/op
ConcCollectionBenchmark.ConcurrentHashMap:ConcurrentHashMapAdd             avgt   10      3282.034 ±      2168.599  ns/op
ConcCollectionBenchmark.ConcurrentHashMap:ConcurrentHashMapGet             avgt   10       381.065 ±       114.265  ns/op
ConcCollectionBenchmark.ConcurrentHashMap:ConcurrentHashMapRemove          avgt   10      1020.668 ±      1327.161  ns/op
ConcCollectionBenchmark.ConcurrentLinkedDeque                              avgt   10       295.548 ±        90.582  ns/op
ConcCollectionBenchmark.ConcurrentLinkedDeque:ConcurrentLinkedDequeAdd     avgt   10      1045.734 ±       330.540  ns/op
ConcCollectionBenchmark.ConcurrentLinkedDeque:ConcurrentLinkedDequeGet     avgt   10       264.106 ±        87.843  ns/op
ConcCollectionBenchmark.ConcurrentLinkedDeque:ConcurrentLinkedDequeRemove  avgt   10      2689.496 ±       833.780  ns/op
ConcCollectionBenchmark.ConcurrentSkipListSet                              avgt   10       170.185 ±        17.355  ns/op
ConcCollectionBenchmark.ConcurrentSkipListSet:ConcurrentSkipListSetAdd     avgt   10      1842.670 ±      1189.880  ns/op
ConcCollectionBenchmark.ConcurrentSkipListSet:ConcurrentSkipListSetGet     avgt   10       145.781 ±         7.842  ns/op
ConcCollectionBenchmark.ConcurrentSkipListSet:ConcurrentSkipListSetRemove  avgt   10       938.053 ±       128.877  ns/op
ConcCollectionBenchmark.CopyOnWriteArrayList                               avgt   10     42468.867 ±     54444.695  ns/op
ConcCollectionBenchmark.CopyOnWriteArrayList:CopyOnWriteArrayAdd           avgt   10   1659181.408 ±   1537127.634  ns/op
ConcCollectionBenchmark.CopyOnWriteArrayList:CopyOnWriteArrayListGet       avgt   10       152.261 ±        20.291  ns/op
ConcCollectionBenchmark.CopyOnWriteArrayList:CopyOnWriteArrayRemove        avgt   10   2657416.942 ±   5488423.800  ns/op
ConcCollectionBenchmark.CopyOnWriteArraySet                                avgt   10     43249.485 ±      9751.317  ns/op
ConcCollectionBenchmark.CopyOnWriteArraySet:CopyOnWriteArraySetAdd         avgt   10   2332074.455 ±    614272.448  ns/op
ConcCollectionBenchmark.CopyOnWriteArraySet:CopyOnWriteArraySetGet         avgt   10       130.319 ±        16.175  ns/op
ConcCollectionBenchmark.CopyOnWriteArraySet:CopyOnWriteArraySetRemove      avgt   10   2066341.137 ±    647276.208  ns/op
``` 