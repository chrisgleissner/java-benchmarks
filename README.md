# Java 21 JMH Benchmarks

[![Build Status](https://travis-ci.org/chrisgleissner/jutil.svg?branch=master)](https://travis-ci.org/chrisgleissner/benchmarks)

Java 21 [JMH](https://github.com/openjdk/jmh) benchmarks for field access, counters, collections, sequential and parallel streams, etc.

## Results

View [All Benchmark Results](https://jmh.morethan.io/?source=https://raw.githubusercontent.com/chrisgleissner/benchmarks/master/jmh-result-all.json)
or download them as [JSON](https://raw.githubusercontent.com/chrisgleissner/java-benchmarks/main/jmh-result-all.json).

## Environment

Details of test environment used to obtain results.

### Software

- Java: Amazon Corretto 21.0.3.9.1
- OS: Ubuntu 22.04.4 LTS
- Kernel: 5.15.86-051586-generic

### Hardware

- CPU: Intel Core i7-6700K at 4.00GHz with 4 cores (8 threads)
- Motherboard: Asus Z170-A
- RAM: 32 GiB DDR4 (2 x Corsair 16 GiB, 2133 MT/s)
- Virtualization: None; bare metal desktop

## Run

Run all benchmarks:

```shell
./benchmark.sh
```

Run specific benchmark(s):

```shell
./benchmark.sh --includes <REGEX>
```

Examples:

```shell
./benchmark.sh --includes Time.*
./benchmark.sh -i Time
./benchmark.sh -i nano
```

After completion, please find the results at `./jmh-result-all.json`.

## Details

> [!NOTE]
> Unless stated otherwise, benchmark throughput measurements are for a single operation, e.g. a single addition to a collection
> or a single iterator advancement.

### Counter

AtomicInteger vs [MutableInt](https://commons.apache.org/proper/commons-lang/javadocs/api-release/index.html) vs int,
same for the corresponding long types.

### Getter / Setter

Comparing various ways of getting and setting object fields.

Ordered by performance from top to bottom, the ranking is:

1. Direct call
1. [LambdaMetaFactory](https://docs.oracle.com/javase/8/docs/api/java/lang/invoke/LambdaMetafactory.html) - almost as fast as direct, but requires at least a private accessor method
1. Reflection - ca. 30% of the direct performance
1. MethodHandle and VarHandle - ca. 20% of the direct performance

### Collection

#### ArrayAdd / CollectionAdd

Compares adding elements to int/Integer/long/Long arrays as well as empty collections and maps.

#### CollectionIterate

Iterating over all elements of pre-populated collections and maps.

#### ConCollection

Concurrent get (10 threads), add (2 threads) and remove (1 thread) of Integer elements for a number of thread-safe collection classes. The non thread-safe ArrayList class is included in this benchmark and gets protected by wrapping it via `Collections.synchronizedList()`.

Each data structure gets populated before he benchmark. Access occurs for the head of the data structure (where the concept of head is supported), otherwise (such as in the instance of maps) by key.

#### Stream

Compares streaming over primitive and wrapper classes compared with using a for loop. The stream collects filtered elements into a target data structure. This benchmarks also compares single threaded with parallel streaming over data structures of varying length.

> In contrast to the other benchmarks, the measurements here are for processing the entire stream. The benchmark is run repeatedly
> for increasing stream lengths, from 1 to 10 million in "one order of magnitude" increments. Thus, as the stream length increases, the measured
> throughput decreases.

### ObjectCache

Compares the use of ints, custom int wrappers instantiation, and a custom int wrapper cache.

### Time

Compares `System.currentTimeMillis`, `System.nanoTime`, and various `java.time` classes.
