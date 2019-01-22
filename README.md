# Java 11 JMH Benchmarks

[![Build Status](https://travis-ci.org/chrisgleissner/jutil.svg?branch=master)](https://travis-ci.org/chrisgleissner/benchmarks)

Java 11 [JMH](https://openjdk.java.net/projects/code-tools/jmh/) benchmarks for field access, counters, collections, etc. 

View the [Latest Results](https://jmh.morethan.io/?source=https://raw.githubusercontent.com/chrisgleissner/benchmarks/master/jmh-result.json)
or download them as [JSON](https://raw.githubusercontent.com/chrisgleissner/benchmarks/master/jmh-result.json).


## Setup

The benchmarks were obtained using JMH on OpenJDK 11.0.1 64Bit 
and Ubuntu 18.04 running inside VirtualBox 5.2 on an Intel I7-6700K clocked at 4.6GHz. All 4 physical cores were allocated to the VirtualBox VM.

To run the benchmarks on your own system, clone this repository, install Open JDK 11 (or above) and Maven 3.5 (or above), then run

```
mvn clean install
java -jar target/benchmarks.jar -gc true -rf json -jvmArgs "-Xms4g -Xms4g -Xcomp"
```

To view all available JMH command line options, run `java -jar target/benchmarks.jar -h`

Alternatively, use the `benchmark` Bash script. To only run a single benchmark, specify its name such as `benchmark TimeBenchmark`. 


## Benchmarks

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


### Object Cache

Compares the use of ints, custom int wrappers instantiation, and a custom int wrapper cache.

### Collection

#### Array Add / Collection Add

Compares adding elements to int/Integer/long/Long arrays as well as empty collections and maps.

#### Collection Iterate

Iterating over all elements of pre-populated collections and maps, one iteration over all elements per op.

#### Concurrent Collection

Concurrent get (10 threads), add (2 threads) and remove (1 thread) of Integer elements for a number of thread-safe collection classes. The non thread-safe ArrayList class is included in this benchmark and gets protected by wrapping it via `Collections.synchronizedList()`.

Each data structure is pre-populated with 100,000 elements prior to benchmarking. Access occurs for the head of the data structure (where the concept of head is supported), otherwise (such as in the instance of maps) by key.

#### Stream

Compares streaming over primitive and wrapper classes compared with using a for loop. The stream performs a filter.

## Time

Compares `System.currentTimeMillis`, `System.nanoTime`, and various `java.time` classes.
