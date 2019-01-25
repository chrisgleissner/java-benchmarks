package com.github.chrisgleissner.benchmarks;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(MILLISECONDS)
@Warmup(iterations = 5, time = 300, timeUnit = MILLISECONDS)
@Measurement(iterations = 5, time = 300, timeUnit = MILLISECONDS)
@Fork(1)
public abstract class AbstractBenchmark {

}
