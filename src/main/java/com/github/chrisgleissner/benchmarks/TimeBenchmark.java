package com.github.chrisgleissner.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Warmup;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Warmup(iterations = 20, time = 1000, timeUnit = MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = MILLISECONDS)
public class TimeBenchmark extends AbstractBenchmark {

    @Benchmark
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Benchmark
    public long nanoTime() {
        return System.nanoTime();
    }

    @Benchmark
    public LocalDateTime localDateTime() {
        return LocalDateTime.now();
    }

    @Benchmark
    public ZonedDateTime zonedDateTime() {
        return ZonedDateTime.now();
    }

    @Benchmark
    public LocalDate localDate() {
        return LocalDate.now();
    }
}
