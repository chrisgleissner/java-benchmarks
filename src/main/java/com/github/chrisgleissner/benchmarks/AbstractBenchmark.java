package com.github.chrisgleissner.benchmarks;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import static java.util.concurrent.TimeUnit.SECONDS;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(SECONDS)
@Warmup(iterations = 50, time = 1, timeUnit = SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = SECONDS)
@Fork(1)
public abstract class AbstractBenchmark {

}
