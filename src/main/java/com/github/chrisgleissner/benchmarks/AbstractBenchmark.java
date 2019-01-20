package com.github.chrisgleissner.benchmarks;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 15, time = 300, timeUnit = MILLISECONDS)
@Measurement(iterations = 15, time = 300, timeUnit = MILLISECONDS)
@Fork(1)
public abstract class AbstractBenchmark {

    /**
     * To reduce the overhead of JMH and improve measurement accuracy, we are performing of the measured code (e.g. adding
     * and element to a data structure) for each benchmark invocation.
     */
    static final int LOOPS_PER_INVOCATION = 1_000;

    /**
     * Some benchmarks require pre-initialization of data structures, e.g. ArrayList instances that must not be resized.
     * This constant specifies an upper limit of the number of invocations expected for each iteration.
     */
    private static final int MAX_INVOCATIONS_PER_ITERATION = 50_000;

    static final int MAX_LOOPS_PER_ITERATION = LOOPS_PER_INVOCATION * MAX_INVOCATIONS_PER_ITERATION;
}
