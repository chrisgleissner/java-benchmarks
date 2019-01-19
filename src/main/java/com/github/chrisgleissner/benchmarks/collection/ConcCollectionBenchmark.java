package com.github.chrisgleissner.benchmarks.collection;

import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static java.util.Collections.synchronizedCollection;
import static java.util.Collections.synchronizedList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(1)
public class ConcCollectionBenchmark {

    private static int MAX_BOUNDED_SIZE = 10_000_000;

    @State(Scope.Benchmark)
    public static class ArrayBlockingQueueState extends AbstractState {
        ArrayBlockingQueue<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new ArrayBlockingQueue(MAX_BOUNDED_SIZE);
            target.addAll(initialData);
        }
    }

    @Benchmark
    @GroupThreads(100)
    @Group("ArrayBlockingQueue")
    public Integer ArrayBlockingQueueGet(ArrayBlockingQueueState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    @GroupThreads(1)
    @Group("ArrayBlockingQueue")
    public Collection<Integer> ArrayBlockingQueueAdd(ArrayBlockingQueueState s) {
        return benchmarkAdd(s.source, s.target);
    }

    @Benchmark
    @GroupThreads(1)
    @Group("ArrayBlockingQueue")
    public Integer ArrayBlockingQueueRemove(ArrayBlockingQueueState s) {
        return benchmarkRemove(s.target);
    }

    // ============================================

    public static class ArrayListState extends AbstractState {
        List<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = synchronizedList(new ArrayList(MAX_BOUNDED_SIZE));
            target.addAll(initialData);
        }
    }

    @Benchmark
    @GroupThreads(100)
    @Group("ArrayList")
    public Integer ArrayListGet(ArrayListState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    @GroupThreads(1)
    @Group("ArrayList")
    public Collection<Integer> ArrayListAdd(ArrayListState s) {
        return benchmarkAdd(s.source, s.target);
    }

    @Benchmark
    @GroupThreads(1)
    @Group("ArrayList")
    public Integer ArrayListRemove(ArrayListState s) {
        return benchmarkRemove(s.target);
    }

    // ============================================

    public static class ConcurrentHashMapState extends AbstractState {
        ConcurrentHashMap<Integer, Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new ConcurrentHashMap();
            target.putAll(initialData.stream().collect(toMap(identity(), identity())));
        }
    }

    @Benchmark
    @GroupThreads(100)
    @Group("ConcurrentHashMap")
    public Integer ConcurrentHashMapGet(ConcurrentHashMapState s) {
        return benchmarkGet(s.source, s.target);
    }

    @Benchmark
    @GroupThreads(1)
    @Group("ConcurrentHashMap")
    public Map<Integer, Integer> ConcurrentHashMapAdd(ConcurrentHashMapState s) {
        return benchmarkAdd(s.source, s.target);
    }

    @Benchmark
    @GroupThreads(1)
    @Group("ConcurrentHashMap")
    public Map<Integer, Integer> ConcurrentHashMapRemove(ConcurrentHashMapState s) {
        return benchmarkRemove(s.source, s.target);
    }

    // ============================================

    public static class ConcurrentLinkedDequeState extends AbstractState {
        ConcurrentLinkedDeque<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new ConcurrentLinkedDeque();
            target.addAll(initialData);
        }
    }

    @Benchmark
    @GroupThreads(100)
    @Group("ConcurrentLinkedDeque")
    public Integer ConcurrentLinkedDequeGet(ConcurrentLinkedDequeState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    @GroupThreads(1)
    @Group("ConcurrentLinkedDeque")
    public Collection<Integer> ConcurrentLinkedDequeAdd(ConcurrentLinkedDequeState s) {
        return benchmarkAdd(s.source, s.target);
    }

    @Benchmark
    @GroupThreads(1)
    @Group("ConcurrentLinkedDeque")
    public Integer ConcurrentLinkedDequeRemove(ConcurrentLinkedDequeState s) {
        return benchmarkRemove(s.target);
    }

    // ============================================

    public static class ConcurrentSkipListSetState extends AbstractState {
        ConcurrentSkipListSet<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new ConcurrentSkipListSet();
            target.addAll(initialData);
        }
    }

    @Benchmark
    @GroupThreads(100)
    @Group("ConcurrentSkipListSet")
    public Integer ConcurrentSkipListSetGet(ConcurrentSkipListSetState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    @GroupThreads(1)
    @Group("ConcurrentSkipListSet")
    public Collection<Integer> ConcurrentSkipListSetAdd(ConcurrentSkipListSetState s) {
        return benchmarkAdd(s.source, s.target);
    }

    @Benchmark
    @GroupThreads(1)
    @Group("ConcurrentSkipListSet")
    public boolean ConcurrentSkipListSetRemove(ConcurrentSkipListSetState s) {
        return benchmarkRemove(s.source, s.target);
    }

    // ============================================

    public static class CopyOnWriteArrayListState extends AbstractState {
        CopyOnWriteArrayList<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new CopyOnWriteArrayList();
            target.addAll(initialData);
        }
    }

    @Benchmark
    @GroupThreads(100)
    @Group("CopyOnWriteArrayList")
    public Integer CopyOnWriteArrayListGet(CopyOnWriteArrayListState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    @GroupThreads(1)
    @Group("CopyOnWriteArrayList")
    public Collection<Integer> CopyOnWriteArrayAdd(CopyOnWriteArrayListState s) {
        return benchmarkAdd(s.source, s.target);
    }

    @Benchmark
    @GroupThreads(1)
    @Group("CopyOnWriteArrayList")
    public Integer CopyOnWriteArrayRemove(CopyOnWriteArrayListState s) {
        return benchmarkRemove(s.target);
    }

    // ============================================

    public static class CopyOnWriteArraySetState extends AbstractState {
        CopyOnWriteArraySet<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new CopyOnWriteArraySet();
            target.addAll(initialData);
        }
    }

    @Benchmark
    @GroupThreads(100)
    @Group("CopyOnWriteArraySet")
    public Integer CopyOnWriteArraySetGet(CopyOnWriteArraySetState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    @GroupThreads(1)
    @Group("CopyOnWriteArraySet")
    public Collection<Integer> CopyOnWriteArraySetAdd(CopyOnWriteArraySetState s) {
        return benchmarkAdd(s.source, s.target);
    }

    @Benchmark
    @GroupThreads(1)
    @Group("CopyOnWriteArraySet")
    public boolean CopyOnWriteArraySetRemove(CopyOnWriteArraySetState s) {
        return benchmarkRemove(s.source, s.target);
    }

    // ============================================

    @State(Scope.Group)
    public static abstract class AbstractState {
        static Random random = new Random();
        static Collection<Integer> initialData = IntStream.range(MAX_BOUNDED_SIZE / 2, MAX_BOUNDED_SIZE / 2 + 100_000).boxed().collect(toList());

        int source;

        @Setup
        public void doSetup() {
            source = random.nextInt();
        }
    }

    private Integer benchmarkGet(Queue<Integer> target) {
        return target.peek();
    }

    private Integer benchmarkGet(NavigableSet<Integer> target) {
        return target.first();
    }

    private Integer benchmarkGet(List<Integer> target) {
        return target.get(0);
    }

    private Integer benchmarkGet(Set<Integer> target) {
        return target.iterator().next();
    }

    private Integer benchmarkGet(Integer key, Map<Integer, Integer> target) {
        return target.get(key);
    }

    private Map<Integer, Integer> benchmarkAdd(Integer i, Map<Integer, Integer> target) {
        target.put(i, i);
        return target;
    }

    private Collection<Integer> benchmarkAdd(Integer i, Collection<Integer> target) {
        target.add(i);
        return target;
    }

    private Integer benchmarkRemove(List<Integer> target) {
        return target.remove(0);
    }

    private Integer benchmarkRemove(Queue<Integer> target) {
        return target.remove();
    }

    private Map<Integer, Integer> benchmarkRemove(Integer i, Map<Integer, Integer> target) {
        target.remove(i);
        return target;
    }

    private boolean benchmarkRemove(Integer i, Set<Integer> target) {
        return target.remove(i);
    }
}
