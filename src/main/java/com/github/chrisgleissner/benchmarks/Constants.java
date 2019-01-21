package com.github.chrisgleissner.benchmarks;

public class Constants {

    /**
     * To reduce the overhead of JMH and improve measurement accuracy, we are performing of the measured code (e.g. adding
     * and element to a data structure) for each benchmark invocation.
     */
    public static final int OPERATIONS_PER_PER_INVOCATION = 1_000;

    /**
     * Some benchmarks require pre-initialization of data structures, e.g. ArrayList instances that must not be resized.
     *
     * This constant specifies an upper limit of the number of invocations expected for each iteration and needs to be
     * set higher than the value derived via the maximum of the warm-up and measurement times defined on {@link AbstractBenchmark}
     * divided by the minimum of the invocation times expected across the benchmarks that rely on this constant.
     */
    private static final int MAX_INVOCATIONS_PER_ITERATION = 50_000;

    public static final int MAX_OPERATIONS_PER_ITERATION = OPERATIONS_PER_PER_INVOCATION * MAX_INVOCATIONS_PER_ITERATION;
}
