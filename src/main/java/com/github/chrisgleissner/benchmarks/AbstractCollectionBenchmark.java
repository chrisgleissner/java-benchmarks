package com.github.chrisgleissner.benchmarks;

import org.openjdk.jmh.annotations.OperationsPerInvocation;

import static com.github.chrisgleissner.benchmarks.Constants.LOOPS_PER_INVOCATION;

@OperationsPerInvocation(LOOPS_PER_INVOCATION)
public abstract class AbstractCollectionBenchmark extends AbstractBenchmark {
}
