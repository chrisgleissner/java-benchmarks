package com.github.chrisgleissner.benchmarks;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableLong;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CounterBenchmark extends AbstractBenchmark {

    @State(Scope.Thread)
    public static class MyState {

        @Setup
        public void setUp() {
            i = 0;
            l = 0;

            atomicInt = new AtomicInteger();
            atomicLong = new AtomicLong();

            mutableInt = new MutableInt();
            mutableLong = new MutableLong();
        }

        public int i;
        public long l;

        public AtomicInteger atomicInt;
        public AtomicLong atomicLong;

        public MutableInt mutableInt;
        public MutableLong mutableLong;
    }


    @Benchmark
    public void primitiveInt(Blackhole blackhole, MyState state) {
        for (int i = 0; i < LOOPS_PER_INVOCATION; i++)
            blackhole.consume(state.i++);
    }

    @Benchmark
    public void primitiveLong(Blackhole blackhole, MyState state) {
        for (int i = 0; i < LOOPS_PER_INVOCATION; i++)
            blackhole.consume(state.l++);
    }

    @Benchmark
    public void atomicInteger(Blackhole blackhole, MyState state) {
        for (int i = 0; i < LOOPS_PER_INVOCATION; i++)
            blackhole.consume(state.atomicInt.getAndIncrement());
    }

    @Benchmark
    public void atomicLong(Blackhole blackhole, MyState state) {
        for (int i = 0; i < LOOPS_PER_INVOCATION; i++)
            blackhole.consume(state.atomicLong.getAndIncrement());
    }

    @Benchmark
    public void mutableInt(Blackhole blackhole, MyState state) {
        for (int i = 0; i < LOOPS_PER_INVOCATION; i++)
            blackhole.consume(state.mutableInt.getAndIncrement());
    }

    @Benchmark
    public void mutableLong(Blackhole blackhole, MyState state) {
        for (int i = 0; i < LOOPS_PER_INVOCATION; i++)
            blackhole.consume(state.mutableLong.getAndIncrement());
    }
}
