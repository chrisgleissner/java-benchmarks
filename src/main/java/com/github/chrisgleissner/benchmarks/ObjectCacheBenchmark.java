package com.github.chrisgleissner.benchmarks;

import lombok.Value;
import org.openjdk.jmh.annotations.*;
import static java.util.concurrent.TimeUnit.*;
import java.util.stream.IntStream;

import static com.github.chrisgleissner.benchmarks.Constants.OPERATIONS_PER_PER_INVOCATION;

@OperationsPerInvocation(OPERATIONS_PER_PER_INVOCATION)
@Warmup(iterations = 20, time = 1, timeUnit = SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = SECONDS)
public class ObjectCacheBenchmark extends AbstractBenchmark {

    private static Id[] idPool = IntStream.range(0, OPERATIONS_PER_PER_INVOCATION).mapToObj(Id::new).toArray(Id[]::new);

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

    @Value
    private static class Id {
        int id;
    }
}
