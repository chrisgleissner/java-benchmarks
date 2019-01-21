package com.github.chrisgleissner.benchmarks;

public class Constants {

    /**
     * To reduce the overhead of JMH and improve measurement accuracy, we are performing of the measured code (e.g. adding
     * and element to a data structure) for each benchmark invocation.
     */
    public static final int LOOPS_PER_INVOCATION = 1_000;

    /**
     * Some benchmarks require pre-initialization of data structures, e.g. ArrayList instances that must not be resized.
     * This constant specifies an upper limit of the number of invocations expected for each iteration.
     */
    private static final int MAX_INVOCATIONS_PER_ITERATION = 50_000;

    static final int MAX_LOOPS_PER_ITERATION = LOOPS_PER_INVOCATION * MAX_INVOCATIONS_PER_ITERATION;
}
