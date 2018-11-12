package com.github.chrisgleissner.benchmarks;

import org.apache.commons.lang3.mutable.MutableInt;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;

@BenchmarkMode(Mode.All)
@Warmup(iterations = 5, time = 1, timeUnit = SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = SECONDS)
@Fork(3)
public class CounterBenchmark {

    @State(Scope.Thread)
    public static class MyState {

        @Setup(Level.Trial)
        public void doSetup() {
            i = 0;
            atomicInteger = new AtomicInteger();
            mutableInt = new MutableInt();
        }

        @TearDown(Level.Trial)
        public void doTearDown() {
        }

        public int i;
        public AtomicInteger atomicInteger;
        public MutableInt mutableInt;
    }

    @Benchmark
    public void incrementInt(Blackhole blackhole, MyState state) {
        blackhole.consume(state.i++);
    }

    @Benchmark
    public void incrementAtomicInteger(Blackhole blackhole, MyState state) {
        blackhole.consume(state.atomicInteger.getAndIncrement());
    }

    @Benchmark
    public void incrementMutableInt(Blackhole blackhole, MyState state) {
        blackhole.consume(state.mutableInt.getAndIncrement());
    }
}
