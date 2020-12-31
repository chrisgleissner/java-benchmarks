package com.github.chrisgleissner.benchmarks;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableLong;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static com.github.chrisgleissner.benchmarks.Constants.OPERATIONS_PER_PER_INVOCATION;
import static java.util.concurrent.TimeUnit.*;

@OperationsPerInvocation(OPERATIONS_PER_PER_INVOCATION)
@Warmup(iterations = 20, time = 1, timeUnit = SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = SECONDS)
public class CounterBenchmark extends AbstractBenchmark {

    @Benchmark
    public void primitiveInt(Blackhole blackhole, MyState state) {
        for (int i = 0; i < OPERATIONS_PER_PER_INVOCATION; i++)
            blackhole.consume(state.i++);
    }

    @Benchmark
    public void primitiveLong(Blackhole blackhole, MyState state) {
        for (int i = 0; i < OPERATIONS_PER_PER_INVOCATION; i++)
            blackhole.consume(state.l++);
    }

    @Benchmark
    public void atomicInteger(Blackhole blackhole, MyState state) {
        for (int i = 0; i < OPERATIONS_PER_PER_INVOCATION; i++)
            blackhole.consume(state.atomicInt.getAndIncrement());
    }

    @Benchmark
    public void atomicLong(Blackhole blackhole, MyState state) {
        for (int i = 0; i < OPERATIONS_PER_PER_INVOCATION; i++)
            blackhole.consume(state.atomicLong.getAndIncrement());
    }

    @Benchmark
    public void mutableInt(Blackhole blackhole, MyState state) {
        for (int i = 0; i < OPERATIONS_PER_PER_INVOCATION; i++)
            blackhole.consume(state.mutableInt.getAndIncrement());
    }

    @Benchmark
    public void mutableLong(Blackhole blackhole, MyState state) {
        for (int i = 0; i < OPERATIONS_PER_PER_INVOCATION; i++)
            blackhole.consume(state.mutableLong.getAndIncrement());
    }

    @State(Scope.Thread)
    public static class MyState {

        public int i;
        public long l;
        public AtomicInteger atomicInt;
        public AtomicLong atomicLong;
        public MutableInt mutableInt;
        public MutableLong mutableLong;

        @Setup
        public void setUp() {
            i = 0;
            l = 0;

            atomicInt = new AtomicInteger();
            atomicLong = new AtomicLong();

            mutableInt = new MutableInt();
            mutableLong = new MutableLong();
        }
    }
}
