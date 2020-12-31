package com.github.chrisgleissner.benchmarks.collection;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static java.util.concurrent.TimeUnit.*;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 50, time = 1000, timeUnit = MILLISECONDS)
@Measurement(iterations = 50, time = 1000, timeUnit = MILLISECONDS)
@Fork(value = 1, jvmArgsPrepend = { "-Xmx32g"})
@OperationsPerInvocation(1)
public class StreamBenchmark extends AbstractCollectionBenchmark {

    @Benchmark
    public int integerStreamMax(MyState state) {
        return state.integers.stream().max(comparingInt(i -> i)).orElse(0);
    }

    @Benchmark
    public int integerStreamMaxParallel(MyState state) {
        return state.integers.stream().parallel().max(comparingInt(i -> i)).orElse(0);
    }

    @Benchmark
    public Object integerStreamGroupBy(MyState state) {
        return state.integers.stream().collect(groupingBy(i -> i % 2 == 0));
    }

    @Benchmark
    public Object integerStreamGroupByParallel(MyState state) {
        return state.integers.stream().parallel().collect(groupingBy(i -> i % 2 == 0));
    }

    @Benchmark
    public int integerStreamReduceToSum(MyState state) {
        return state.integers.stream().reduce(0, (i, j) -> i + j);
    }
    @Benchmark
    public int integerStreamReduceToSumParallel(MyState state) {
        return state.integers.stream().parallel().reduce(0, (i, j) -> i + j);
    }

    @Benchmark
    public int integerStreamSum(MyState state) {
        return state.integers.stream().mapToInt(i -> i).sum();
    }

    @Benchmark
    public int integerStreamSumParallel(MyState state) {
        return state.integers.stream().parallel().mapToInt(i -> i).sum();
    }

    @Benchmark
    public void integerStreamForEach(MyState state, Blackhole blackhole) {
        final Collection<Integer> target = new ArrayList<>();
        for (Integer threshold : state.range) {
            state.integers.stream().filter(i -> i < threshold).forEach(target::add);
        }
        blackhole.consume(target);
    }

    @Benchmark
    public void integerStreamForEachParallel(MyState state, Blackhole blackhole) {
        final Set<Integer> target = new ConcurrentSkipListSet<>();
        for (Integer threshold : state.range) {
            state.integers.stream().parallel().filter(i -> i < threshold).forEach(target::add);
        }
        blackhole.consume(target);
    }

    @Benchmark
    public void integerStreamCollect(MyState state, Blackhole blackhole) {
        Collection<Integer> target = new ArrayList<>();
        for (Integer threshold : state.range) {
            target = state.integers.stream().filter(i -> i < threshold).collect(toList());
        }
        blackhole.consume(target);
    }

    @Benchmark
    public void integerStreamCollectParallel(MyState state, Blackhole blackhole) {
        Collection<Integer> target = new ArrayList<>();
        for (Integer threshold : state.range) {
           target = state.integers.stream().parallel().filter(i -> i < threshold).collect(toList());
        }
        blackhole.consume(target);
    }

    @Benchmark
    public void integerFor(MyState state, Blackhole blackhole) {
        Collection<Integer> source = state.integers;
        Collection<Integer> target = new ArrayList<>();
        for (Integer threshold : state.range) {
            Integer filter = threshold;
            for (Integer i : source)
                if (i < filter)
                    target.add(i);
        }
        blackhole.consume(target);
    }

    @Benchmark
    public void intStream(MyState state, Blackhole blackhole) {
        int[] target = new int[state.collectionSize];
        for (Integer threshold : state.range) {
            target = Arrays.stream(state.ints).filter(i -> i < threshold).toArray();
        }
        blackhole.consume(target);
    }

    @Benchmark
    public void intStreamParallel(MyState state, Blackhole blackhole) {
        int[] target = new int[state.collectionSize];
        for (Integer threshold : state.range) {
            Arrays.stream(state.ints).parallel().filter(i -> i < threshold).toArray();
        }
        blackhole.consume(target);
    }

    @Benchmark
    public void intFor(MyState state, Blackhole blackhole) {
        int[] source = state.ints;
        int[] target = new int[source.length];
        int targetIndex = 0;
        for (Integer threshold : state.range) {
            targetIndex = 0;
            int filter = threshold;
            for (int i = 0; i < source.length; i++) {
                int candidate = source[i];
                if (candidate < filter)
                    target[targetIndex++] = candidate;
            }
        }
        int[] trimmedTarget = new int[targetIndex];
        System.arraycopy(source, 0, trimmedTarget, 0, targetIndex);
        blackhole.consume(trimmedTarget);
    }

    @State(Scope.Benchmark)
    public static class MyState {
        static Random random = new Random();
        public List<Integer> integers;
        public List<Integer> range;
        public int[] ints;

        @Param({"1", "10", "100", "1000", "10000", "100000","1000000", "10000000"})
        int collectionSize;

        @Setup(Level.Trial)
        public void doSetup() {
            if (collectionSize <= 100) {
                range = range(0, collectionSize).boxed().collect(toList());
            }
            else if (collectionSize <= 100000) {
                int step = collectionSize / 100;
                range = range(0, 100).map(x -> x * step).boxed().collect(toList());
            }
            else {
                int step = collectionSize / 2;
                range = range(0, 2).map(x -> x * step).boxed().collect(toList());
            }
            integers = range(0, collectionSize).boxed().collect(toList());
            Collections.shuffle(integers, random);
            ints = integers.stream().mapToInt(Integer::valueOf).toArray();

            // Fix data layout so that all elements are plced continuously in memory (not in random order ad after shuffle)
            List<Integer> old_int = integers;
            integers = new ArrayList<Integer>(old_int.size());
            for (Integer i: old_int) {
                integers.add(Integer.valueOf(i.intValue()));
            }
        }
    }

}
