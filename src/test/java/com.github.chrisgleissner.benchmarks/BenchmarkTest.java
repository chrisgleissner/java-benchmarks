package com.github.chrisgleissner.benchmarks;

import org.junit.Test;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openjdk.jmh.runner.options.TimeValue.milliseconds;

public class BenchmarkTest {

        @Test
        public void objectCacheBenchmark() throws Exception {
            Options opt = new OptionsBuilder()
                    .include("ObjectCacheBenchmark")
                    .warmupTime(milliseconds(100))
                    .warmupIterations(1)
                    .measurementTime(milliseconds(100))
                    .measurementIterations(1)
                    .forks(0)
                    .shouldFailOnError(true)
                    .build();

            Collection<RunResult> results = new Runner(opt).run();
            assertThat(results).isNotEmpty();
            results.forEach(r -> {
                assertThat(r.getPrimaryResult().getScore()).isGreaterThan(0.0);
            });
        }
}
