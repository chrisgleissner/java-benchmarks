package com.github.chrisgleissner.benchmarks;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableLong;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static com.github.chrisgleissner.benchmarks.Constants.OPERATIONS_PER_PER_INVOCATION;

@OperationsPerInvocation(OPERATIONS_PER_PER_INVOCATION)
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
