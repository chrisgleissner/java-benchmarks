package com.github.chrisgleissner.benchmarks.collection;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
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

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

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
        Collection<Integer> target = new ArrayList<>();
        state.integers.stream().filter(i -> i < state.integerFilter).forEach(target::add);
        blackhole.consume(target);
    }

    @Benchmark
    public void integerStreamForEachParallel(MyState state, Blackhole blackhole) {
        Set<Integer> target = new ConcurrentSkipListSet<>();
        state.integers.stream().parallel().filter(i -> i < state.integerFilter).forEach(target::add);
        blackhole.consume(target);
    }

    @Benchmark
    public void integerStreamCollect(MyState state, Blackhole blackhole) {
        blackhole.consume(state.integers.stream().filter(i -> i < state.integerFilter).collect(toList()));
    }

    @Benchmark
    public void integerStreamCollectParallel(MyState state, Blackhole blackhole) {
        blackhole.consume(state.integers.stream().parallel().filter(i -> i < state.integerFilter).collect(toList()));
    }

    @Benchmark
    public void integerFor(MyState state, Blackhole blackhole) {
        Collection<Integer> source = state.integers;
        Collection<Integer> target = new ArrayList<>();
        Integer filter = state.integerFilter;
        for (Integer i : source)
            if (i < filter)
                target.add(i);
        blackhole.consume(target);
    }

    @Benchmark
    public void intStream(MyState state, Blackhole blackhole) {
        blackhole.consume(Arrays.stream(state.ints).filter(i -> i < state.intFilter).toArray());
    }

    @Benchmark
    public void intStreamParallel(MyState state, Blackhole blackhole) {
        blackhole.consume(Arrays.stream(state.ints).parallel().filter(i -> i < state.intFilter).toArray());
    }

    @Benchmark
    public void intFor(MyState state, Blackhole blackhole) {
        int[] source = state.ints;
        int[] target = new int[source.length];
        int targetIndex = 0;
        int filter = state.intFilter;
        for (int i = 0; i < source.length; i++) {
            int candidate = source[i];
            if (candidate < filter)
                target[targetIndex++] = candidate;
        }
        int[] trimmedTarget = new int[targetIndex];
        System.arraycopy(source, 0, trimmedTarget, 0, targetIndex);
        blackhole.consume(trimmedTarget);
    }

    @State(Scope.Benchmark)
    public static class MyState {
        static Random random = new Random();
        public List<Integer> integers;
        public int[] ints;
        public Integer integerFilter;
        public int intFilter;
        @Param({"1", "10", "100", "1000", "10000", "100000", "1000000", "10000000"})
        int collectionSize;

        @Setup(Level.Iteration)
        public void doSetup() {
            integerFilter = random.nextInt(collectionSize);
            intFilter = integerFilter;

            integers = range(0, collectionSize).boxed().collect(toList());
            Collections.shuffle(integers, random);
            ints = integers.stream().mapToInt(Integer::valueOf).toArray();
        }
    }
}
