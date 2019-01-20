package com.github.chrisgleissner.benchmarks;

import com.github.chrisgleissner.benchmarks.AbstractBenchmark;
import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class ArrayAddBenchmark extends AbstractBenchmark {

    private static int MAX_SIZE = 10_000;

    @State(Scope.Thread)
    public static class MyState {
        Integer[] intWrappers;
        int[] ints;
        Long[] longWrappers;
        long[] longs;

        @Setup
        public void doSetup()  {
            intWrappers = IntStream.range(0, MAX_SIZE).boxed().toArray(Integer[]::new);
            ints = IntStream.range(0, MAX_SIZE).toArray();

            longWrappers = LongStream.range(0, MAX_SIZE).boxed().toArray(Long[]::new);
            longs = LongStream.range(0, MAX_SIZE).toArray();
        }
    }


    // int / Integer Array

    @Benchmark
    public int[] intArrayClone(MyState s) {
        return s.ints.clone();
    }

    @Benchmark
    public int[] intArrayCopyOf(MyState s) {
        return Arrays.copyOf(s.ints, MAX_SIZE);
    }

    @Benchmark
    public int[] intArraySystemArrayCopy(MyState s) {
        int[] ints = new int[MAX_SIZE];
        System.arraycopy(s.ints, 0, ints, 0, MAX_SIZE);
        return ints;
    }

    @Benchmark
    public int[] intArrayAdd(MyState s) {
        int[] ints = new int[MAX_SIZE];
        for (int i = 0; i < MAX_SIZE; i++)
            ints[i] = s.ints[i];
        return ints;
    }

    @Benchmark
    public Integer[] intWrapperArrayClone(MyState s) {
        return s.intWrappers.clone();
    }

    @Benchmark
    public Integer[] intWrapperArrayCopyOf(MyState s) {
        return Arrays.copyOf(s.intWrappers, MAX_SIZE);
    }

    @Benchmark
    public Integer[] intWrapperArraySystemArrayCopy(MyState s) {
        Integer[] ints = new Integer[MAX_SIZE];
        System.arraycopy(s.intWrappers, 0, ints, 0, MAX_SIZE);
        return ints;
    }

    @Benchmark
    public Integer[] intWrapperArrayAdd(MyState s) {
        Integer[] ints = new Integer[MAX_SIZE];
        for (int i = 0; i < MAX_SIZE; i++)
            ints[i] = s.intWrappers[i];
        return ints;
    }


    // long / Long Array

    @Benchmark
    public long[] longArrayClone(MyState s) {
        return s.longs.clone();
    }

    @Benchmark
    public long[] longArrayCopyOf(MyState s) {
        return Arrays.copyOf(s.longs, MAX_SIZE);
    }

    @Benchmark
    public long[] longArraySystemArrayCopy(MyState s) {
        long[] longs = new long[MAX_SIZE];
        System.arraycopy(s.longs, 0, longs, 0, MAX_SIZE);
        return longs;
    }

    @Benchmark
    public long[] longArrayAdd(MyState s) {
        long[] longs = new long[MAX_SIZE];
        for (int i = 0; i < MAX_SIZE; i++)
            longs[i] = s.longs[i];
        return longs;
    }

    @Benchmark
    public Long[] longWrapperArrayClone(MyState s) {
        return s.longWrappers.clone();
    }

    @Benchmark
    public Long[] longWrapperArrayCopyOf(MyState s) {
        return Arrays.copyOf(s.longWrappers, MAX_SIZE);
    }

    @Benchmark
    public Long[] longWrapperArraySystemArrayCopy(MyState s) {
        Long[] longs = new Long[MAX_SIZE];
        System.arraycopy(s.longWrappers, 0, longs, 0, MAX_SIZE);
        return longs;
    }

    @Benchmark
    public Long[] longWrapperArrayAdd(MyState s) {
        Long[] longs = new Long[MAX_SIZE];
        for (int i = 0; i < MAX_SIZE; i++)
            longs[i] = s.longWrappers[i];
        return longs;
    }
}
