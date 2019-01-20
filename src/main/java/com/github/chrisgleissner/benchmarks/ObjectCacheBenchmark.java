
package com.github.chrisgleissner.benchmarks;

import lombok.Value;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ObjectCacheBenchmark extends AbstractBenchmark {

    @Value
    private static class Id {
        int id;
    }

    private static final int CACHE_SIZE = 10000;

    private static Id[] idPool = IntStream.range(0, CACHE_SIZE).mapToObj(Id::new).toArray(Id[]::new);

    private static Id id(int id) {
        return idPool[id];
    }


    @Benchmark
    public Id[] cache() {
        Id[] ids = new Id[CACHE_SIZE];
        for (int i = 0; i < CACHE_SIZE; i++)
            ids[i] = id(i);
        return ids;
    }

    @Benchmark
    public Id[] constructor() {
        Id[] ids = new Id[CACHE_SIZE];
        for (int i = 0; i < CACHE_SIZE; i++)
            ids[i] = new Id(i);
        return ids;
    }

    @Benchmark
    public int[] primitive() {
        int[] ids = new int[CACHE_SIZE];
        for (int i = 0; i < CACHE_SIZE; i++)
            ids[i] = i;
        return ids;
    }
}
