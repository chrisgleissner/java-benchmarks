package com.github.chrisgleissner.benchmarks.collection;

import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 2)
@Fork(3)
public class CollectionAddBenchmark {

    private static int MAX_SIZE = 10_000;

    @Benchmark
    public Collection<Integer> ArrayBlockingQueue(MyState s) {
        return benchmarkAdd(s, new ArrayBlockingQueue<>(MAX_SIZE));
    }

    @Benchmark
    public Collection<Integer> ArrayDeque(MyState s) {
        return benchmarkAdd(s, new ArrayDeque<>());
    }

    @Benchmark
    public Collection<Integer> ArrayList(MyState s) {
        return benchmarkAdd(s, new ArrayList<>());
    }

    @Benchmark
    public Collection<Integer> ArrayListMaxInitialCapacity(MyState s) {
        return benchmarkAdd(s, new ArrayList<>(MAX_SIZE));
    }

    @Benchmark
    public Map<Integer, Integer> ConcurrentHashMap(MyState s) {
        return benchmarkAdd(s, new ConcurrentHashMap<>());
    }

    @Benchmark
    public Collection<Integer> ConcurrentLinkedDeque(MyState s) {
        return benchmarkAdd(s, new ConcurrentLinkedDeque<>());
    }

    @Benchmark
    public Collection<Integer> ConcurrentSkipListSet(MyState s) {
        return benchmarkAdd(s, new ConcurrentSkipListSet<>());
    }

    @Benchmark
    public Collection<Integer> CopyOnWriteArrayList(MyState s) {
        return benchmarkAdd(s, new CopyOnWriteArrayList<>());
    }

    @Benchmark
    public Collection<Integer> CopyOnWriteArraySet(MyState s) {
        return benchmarkAdd(s, new CopyOnWriteArraySet<>());
    }

    @Benchmark
    public Map<Integer, Integer> HashMap(MyState s) {
        return benchmarkAdd(s, new HashMap<>());
    }

    @Benchmark
    public Collection<Integer> HashSet(MyState s) {
        return benchmarkAdd(s, new HashSet<>());
    }

    @Benchmark
    public Collection<Integer> LinkedBlockingDeque(MyState s) {
        return benchmarkAdd(s, new LinkedBlockingDeque<>());
    }

    @Benchmark
    public Collection<Integer> LinkedBlockingQueue(MyState s) {
        return benchmarkAdd(s, new LinkedBlockingQueue<>());
    }

    @Benchmark
    public Collection<Integer> LinkedHashSet(MyState s) {
        return benchmarkAdd(s, new LinkedHashSet<>());
    }

    @Benchmark
    public Map<Integer, Integer> LinkedHashMap(MyState s) {
        return benchmarkAdd(s, new LinkedHashMap<>());
    }

    @Benchmark
    public Collection<Integer> LinkedList(MyState s) {
        return benchmarkAdd(s, new LinkedList<>());
    }

    @Benchmark
    public Collection<Integer> LinkedTransferQueue(MyState s) {
        return benchmarkAdd(s, new LinkedTransferQueue<>());
    }

    @Benchmark
    public Collection<Integer> PriorityBlockingQueue(MyState s) {
        return benchmarkAdd(s, new PriorityBlockingQueue<>());
    }

    @Benchmark
    public Collection<Integer> PriorityQueue(MyState s) {
        return benchmarkAdd(s, new PriorityBlockingQueue<>());
    }

    @Benchmark
    public Collection<Integer> Stack(MyState s) {
        return benchmarkAdd(s, new Stack<>());
    }

    @Benchmark
    public Collection<Integer> TreeSet(MyState s) {
        return benchmarkAdd(s, new TreeSet<>());
    }

    @Benchmark
    public Collection<Integer> Vector(MyState s) {
        return benchmarkAdd(s, new Vector<>());
    }

    private Collection<Integer> benchmarkAdd(MyState s, Collection<Integer> target) {
        for (Integer i : s.ints)
            target.add(i);
        return target;
    }

    private Map<Integer, Integer> benchmarkAdd(MyState s, Map<Integer, Integer> target) {
        for (Integer i : s.ints)
            target.put(i, i);
        return target;
    }

    @State(Scope.Benchmark)
    public static class MyState {
        Integer[] ints;

        @Setup
        public void doSetup() {
            ints = IntStream.range(0, MAX_SIZE).boxed().toArray(Integer[]::new);
        }
    }
}
