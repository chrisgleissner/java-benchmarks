
package com.github.chrisgleissner.benchmarks;

import lombok.Value;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.lang.System.nanoTime;
import static java.util.stream.Collectors.toList;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(1)
public class ObjectPoolBenchmark {

    @Value
    private static class Id {
        int id;
    }

    private static final int POOL_SIZE = 10000;

    private static Id[] idPool = IntStream.range(0, POOL_SIZE).mapToObj(Id::new).toArray(Id[]::new);

    private static Id id(int id) {
        return idPool[id];
    }


    @State(Scope.Thread)
    public static class MyState {
        private int i;
        private int[] numbers;

        @Setup
        public void doSetup() {
            List<Integer> ints = IntStream.range(0, POOL_SIZE).boxed().collect(toList());
            Collections.shuffle(ints, new Random(nanoTime()));
            this.numbers = ints.stream().mapToInt(Integer::intValue).toArray();
            System.out.println("Created " + numbers.length + " numbers");
        }

        public int nextInt() {
            i = i++ % POOL_SIZE;
            return numbers[i];
        }
    }

    @Benchmark
    public void pool(Blackhole blackhole, MyState state) {
        blackhole.consume(id(state.nextInt()));
    }

    @Benchmark
    public void constructor(Blackhole blackhole, MyState state) {
        blackhole.consume(new Id(state.nextInt()));
    }

    @Benchmark
    public void primitive(Blackhole blackhole, MyState state) {
        blackhole.consume(state.nextInt());
    }
}
