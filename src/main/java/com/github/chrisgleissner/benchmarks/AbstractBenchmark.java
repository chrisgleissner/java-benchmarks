package com.github.chrisgleissner.benchmarks;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 7, time = 2)
@Measurement(iterations = 10, time = 2)
@Fork(1)
public abstract class AbstractBenchmark {
}
