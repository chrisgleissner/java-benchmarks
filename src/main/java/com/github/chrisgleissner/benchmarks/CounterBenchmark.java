package com.github.chrisgleissner.benchmarks;

import org.apache.commons.lang3.mutable.MutableInt;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.atomic.AtomicInteger;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5, time = 2)
@Measurement(iterations = 5, time = 3)
@Fork(2)
public class CounterBenchmark {

    @State(Scope.Thread)
    public static class MyState {

        @Setup(Level.Trial)
        public void setUp() {
            i = 0;
            atomicInteger = new AtomicInteger();
            mutableInt = new MutableInt();
        }

        public int i;
        public AtomicInteger atomicInteger;
        public MutableInt mutableInt;
    }

    @Benchmark
    public void countInt(Blackhole blackhole, MyState state) {
        blackhole.consume(state.i++);
    }

    @Benchmark
    public void countAtomicInteger(Blackhole blackhole, MyState state) {
        blackhole.consume(state.atomicInteger.getAndIncrement());
    }

    @Benchmark
    public void countMutableInt(Blackhole blackhole, MyState state) {
        blackhole.consume(state.mutableInt.getAndIncrement());
    }
}
