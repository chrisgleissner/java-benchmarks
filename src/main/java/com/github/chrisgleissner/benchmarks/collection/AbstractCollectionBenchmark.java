package com.github.chrisgleissner.benchmarks.collection;

import com.github.chrisgleissner.benchmarks.AbstractBenchmark;
import org.openjdk.jmh.annotations.OperationsPerInvocation;

import static com.github.chrisgleissner.benchmarks.Constants.OPERATIONS_PER_PER_INVOCATION;

@OperationsPerInvocation(OPERATIONS_PER_PER_INVOCATION)
public abstract class AbstractCollectionBenchmark extends AbstractBenchmark {
}
