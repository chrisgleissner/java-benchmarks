package com.github.chrisgleissner.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OperationsPerInvocation;

import java.util.stream.IntStream;

import static com.github.chrisgleissner.benchmarks.Constants.OPERATIONS_PER_PER_INVOCATION;

@OperationsPerInvocation(OPERATIONS_PER_PER_INVOCATION)
public class ObjectCacheBenchmark {

    private static final Id[] idPool = IntStream.range(0, OPERATIONS_PER_PER_INVOCATION).mapToObj(Id::new).toArray(Id[]::new);

    @Benchmark
    public Id[] cache() {
        Id[] ids = new Id[OPERATIONS_PER_PER_INVOCATION];
        for (int i = 0; i < OPERATIONS_PER_PER_INVOCATION; i++)
            ids[i] = id(i);
        return ids;
    }

    private static Id id(int id) {
        return idPool[id];
    }

    @Benchmark
    public Id[] constructor() {
        Id[] ids = new Id[OPERATIONS_PER_PER_INVOCATION];
        for (int i = 0; i < OPERATIONS_PER_PER_INVOCATION; i++)
            ids[i] = new Id(i);
        return ids;
    }

    @Benchmark
    public int[] primitive() {
        int[] ids = new int[OPERATIONS_PER_PER_INVOCATION];
        for (int i = 0; i < OPERATIONS_PER_PER_INVOCATION; i++)
            ids[i] = i;
        return ids;
    }

    record Id(int id) {
    }
}
