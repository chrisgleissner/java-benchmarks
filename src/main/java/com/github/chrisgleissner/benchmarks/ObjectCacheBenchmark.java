
package com.github.chrisgleissner.benchmarks;

import lombok.Value;
import org.openjdk.jmh.annotations.Benchmark;

import java.util.stream.IntStream;

public class ObjectCacheBenchmark extends AbstractBenchmark {

    @Value
    private static class Id {
        int id;
    }

    private static Id[] idPool = IntStream.range(0, LOOPS_PER_INVOCATION).mapToObj(Id::new).toArray(Id[]::new);

    private static Id id(int id) {
        return idPool[id];
    }


    @Benchmark
    public Id[] cache() {
        Id[] ids = new Id[LOOPS_PER_INVOCATION];
        for (int i = 0; i < LOOPS_PER_INVOCATION; i++)
            ids[i] = id(i);
        return ids;
    }

    @Benchmark
    public Id[] constructor() {
        Id[] ids = new Id[LOOPS_PER_INVOCATION];
        for (int i = 0; i < LOOPS_PER_INVOCATION; i++)
            ids[i] = new Id(i);
        return ids;
    }

    @Benchmark
    public int[] primitive() {
        int[] ids = new int[LOOPS_PER_INVOCATION];
        for (int i = 0; i < LOOPS_PER_INVOCATION; i++)
            ids[i] = i;
        return ids;
    }
}
