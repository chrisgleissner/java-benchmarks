package com.github.chrisgleissner.benchmarks.collection;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
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

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@OperationsPerInvocation(1_000_000)
public class StreamBenchmark extends AbstractCollectionBenchmark {
    public static final int COLLECTION_SIZE = 1_000_000;

    @State(Scope.Benchmark)
    public static class MyState {
        static Random random = new Random();

        public List<Integer> integers;
        public int[] ints;
        public Integer integerFilter = random.nextInt(COLLECTION_SIZE);
        public int intFilter = integerFilter;

        @Setup(Level.Iteration)
        public void doSetup() {
            integers = range(0, COLLECTION_SIZE).boxed().collect(toList());
            Collections.shuffle(integers, random);
            ints = integers.stream().mapToInt(Integer::valueOf).toArray();
        }
    }

    @Benchmark
    public void integerStreamForEach(MyState state, Blackhole blackhole) {
        Collection<Integer> target = new ArrayList<>();
        state.integers.stream().filter(i -> i < state.integerFilter).forEach(target::add);
        blackhole.consume(target);
    }

    @Benchmark
    public void integerStreamForEachParallel(MyState state, Blackhole blackhole) {
        Collection<Integer> target = new ArrayList<>();
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
}
