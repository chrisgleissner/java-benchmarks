# Java 11 JMH Benchmarks

[![Build Status](https://travis-ci.org/chrisgleissner/jutil.svg?branch=master)](https://travis-ci.org/chrisgleissner/benchmarks)

Java 11 [JMH](https://openjdk.java.net/projects/code-tools/jmh/) benchmarks for field access, counters, collections, sequential and parallel streams, etc. 

View [All Benchmark Results](https://jmh.morethan.io/?source=https://raw.githubusercontent.com/chrisgleissner/benchmarks/master/jmh-result-all.json)
or download them as [JSON](https://raw.githubusercontent.com/chrisgleissner/benchmarks/master/jmh-result-all.json).

The benchmarks were obtained using JMH on OpenJDK 11.0.1 64Bit 
and Ubuntu 18.04 running inside VirtualBox 5.2 on an Intel I7-6700K clocked at 4.6GHz. All 4 physical cores were allocated to the VirtualBox VM.


## Run Benchmarks

To run the benchmarks on your own system, first:
* Clone this repository
* Install Open JDK 11 (or above)
* Install Maven 3.5 (or above)

### All Benchmarks

Execute: 
```
mvn clean install
java -jar target/benchmarks.jar -gc true -rf json -jvmArgs "-Xms4g -Xms4g -Xcomp"
```

If you have Bash on your system, you can also just run the `benchmark` script which does the same.

### Specific Benchmarks

To run a single or a set of benchmarks, execute `benchmark <regexp>...` where `<regexp>...` is one or more comma-separated regular
expressions referring to benchmark names. 

For example, `benchmark Getter.*,Counter.*` will run the Getter and the Counter benchmarks. 

### Options

To view all available JMH command line options, run `java -jar target/benchmarks.jar -h`


## Benchmark Overview

> Unless stated otherwise, benchmark throughput measurements are for a single operation, e.g. a single addition to a collection
> or a single iterator advancement.

### Counter

AtomicInteger vs [MutableInt](https://commons.apache.org/proper/commons-lang/javadocs/api-release/index.html) vs int,
same for the corresponding long types.

### Getter / Setter

Comparing various ways of getting and setting object fields.

Ordered by performance from top to bottom, the ranking is:
1. Direct call
1. [LambdaMetaFactory](https://docs.oracle.com/javase/8/docs/api/java/lang/invoke/LambdaMetafactory.html) - as fast as direct, but requires at least a private accessor method 
1. Reflection - 60% of the direct performance
1. MethodHandle and VarHandle - 20% of the direct performance


### Collection

#### ArrayAdd / CollectionAdd

Compares adding elements to int/Integer/long/Long arrays as well as empty collections and maps.

#### CollectionIterate

Iterating over all elements of pre-populated collections and maps, one iteration over all elements per op.

#### ConCollection

Concurrent get (10 threads), add (2 threads) and remove (1 thread) of Integer elements for a number of thread-safe collection classes. The non thread-safe ArrayList class is included in this benchmark and gets protected by wrapping it via `Collections.synchronizedList()`.

Each data structure is pre-populated with 100,000 elements prior to benchmarking. Access occurs for the head of the data structure (where the concept of head is supported), otherwise (such as in the instance of maps) by key.

#### Stream

Compares streaming over primitive and wrapper classes compared with using a for loop. The stream performs a filter.

> In contrast to the other benchmarks, the measurements here are for processing the entire stream. The benchmark is run repeatedly
for increasing stream lengths, from 1 to 10 million in "one order of magnitude" increments. Thus, as the stream length increases, the measured
throughput decreases.

### ObjectCache

Compares the use of ints, custom int wrappers instantiation, and a custom int wrapper cache.

### Time

Compares `System.currentTimeMillis`, `System.nanoTime`, and various `java.time` classes.
