package com.github.chrisgleissner.benchmarks.collection;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.IntStream;

import static java.util.Collections.synchronizedList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@OperationsPerInvocation(1)
public class ConcCollectionBenchmark extends AbstractCollectionBenchmark {

    private static final int GET_THREADS = 10;
    private static final int ADD_THREADS = 2;
    private static final int INITIAL_SIZE = 100_000;
    private static final int MAX_BOUNDED_SIZE = 10_000_000;

    @Benchmark
    @GroupThreads(GET_THREADS)
    @Group("ArrayBlockingQueue")
    public Object ArrayBlockingQueueGet(ArrayBlockingQueueState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    @GroupThreads(ADD_THREADS)
    @Group("ArrayBlockingQueue")
    public Object ArrayBlockingQueueAdd(ArrayBlockingQueueState s) {
        return benchmarkAdd(s.source, s.target);
    }

    @Benchmark
    @GroupThreads
    @Group("ArrayBlockingQueue")
    public Object ArrayBlockingQueueRemove(ArrayBlockingQueueState s) {
        return benchmarkRemove(s.target);
    }

    @Benchmark
    @GroupThreads(GET_THREADS)
    @Group("ArrayList")
    public Object ArrayListGet(ArrayListState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    @GroupThreads(ADD_THREADS)
    @Group("ArrayList")
    public Object ArrayListAdd(ArrayListState s) {
        return benchmarkAdd(s.source, s.target);
    }

    @Benchmark
    @GroupThreads
    @Group("ArrayList")
    public Integer ArrayListRemove(ArrayListState s) {
        return benchmarkRemove(s.target);
    }

    @Benchmark
    @GroupThreads(GET_THREADS)
    @Group("ConcurrentHashMap")
    public Integer ConcurrentHashMapGet(ConcurrentHashMapState s) {
        return benchmarkGet(s.source, s.target);
    }


    @Benchmark
    @GroupThreads(ADD_THREADS)
    @Group("ConcurrentHashMap")
    public Object ConcurrentHashMapAdd(ConcurrentHashMapState s) {
        return benchmarkAdd(s.source, s.target);
    }

    @Benchmark
    @GroupThreads
    @Group("ConcurrentHashMap")
    public Object ConcurrentHashMapRemove(ConcurrentHashMapState s) {
        return benchmarkRemove(s.source, s.target);
    }

    @Benchmark
    @GroupThreads(GET_THREADS)
    @Group("ConcurrentLinkedDeque")
    public Object ConcurrentLinkedDequeGet(ConcurrentLinkedDequeState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    @GroupThreads(ADD_THREADS)
    @Group("ConcurrentLinkedDeque")
    public Object ConcurrentLinkedDequeAdd(ConcurrentLinkedDequeState s) {
        return benchmarkAdd(s.source, s.target);
    }

    @Benchmark
    @GroupThreads
    @Group("ConcurrentLinkedDeque")
    public Object ConcurrentLinkedDequeRemove(ConcurrentLinkedDequeState s) {
        return benchmarkRemove(s.target);
    }

    @Benchmark
    @GroupThreads(GET_THREADS)
    @Group("ConcurrentLinkedQueue")
    public Integer ConcurrentLinkedQueueGet(ConcurrentLinkedQueueState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    @GroupThreads(ADD_THREADS)
    @Group("ConcurrentLinkedQueue")
    public Object ConcurrentLinkedQueueAdd(ConcurrentLinkedQueueState s) {
        return benchmarkAdd(s.source, s.target);
    }

    @Benchmark
    @GroupThreads
    @Group("ConcurrentLinkedQueue")
    public Object ConcurrentLinkedQueueRemove(ConcurrentLinkedQueueState s) {
        return benchmarkRemove(s.target);
    }

    @Benchmark
    @GroupThreads(GET_THREADS)
    @Group("ConcurrentSkipListMap")
    public Object ConcurrentSkipMapSetGet(ConcurrentSkipListMapState s) {
        return benchmarkGet(s.source, s.target);
    }

    @Benchmark
    @GroupThreads(ADD_THREADS)
    @Group("ConcurrentSkipListMap")
    public Object ConcurrentSkipListMapAdd(ConcurrentSkipListMapState s) {
        return benchmarkAdd(s.source, s.target);
    }

    @Benchmark
    @GroupThreads
    @Group("ConcurrentSkipListMap")
    public Object ConcurrentSkipListMapRemove(ConcurrentSkipListMapState s) {
        return benchmarkRemove(s.source, s.target);
    }

    @Benchmark
    @GroupThreads(GET_THREADS)
    @Group("ConcurrentSkipListSet")
    public Object ConcurrentSkipListSetGet(ConcurrentSkipListSetState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    @GroupThreads(ADD_THREADS)
    @Group("ConcurrentSkipListSet")
    public Object ConcurrentSkipListSetAdd(ConcurrentSkipListSetState s) {
        return benchmarkAdd(s.source, s.target);
    }

    @Benchmark
    @GroupThreads
    @Group("ConcurrentSkipListSet")
    public Object ConcurrentSkipListSetRemove(ConcurrentSkipListSetState s) {
        return benchmarkRemove(s.source, s.target);
    }

    @Benchmark
    @GroupThreads(GET_THREADS)
    @Group("CopyOnWriteArrayList")
    public Object CopyOnWriteArrayListGet(CopyOnWriteArrayListState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    @GroupThreads(ADD_THREADS)
    @Group("CopyOnWriteArrayList")
    public Object CopyOnWriteArrayAdd(CopyOnWriteArrayListState s) {
        return benchmarkAdd(s.source, s.target);
    }

    @Benchmark
    @GroupThreads(ADD_THREADS)
    @Group("CopyOnWriteArrayList")
    public Object CopyOnWriteArrayRemove(CopyOnWriteArrayListState s) {
        return benchmarkRemove(s.target);
    }

    @Benchmark
    @GroupThreads(GET_THREADS)
    @Group("CopyOnWriteArraySet")
    public Object CopyOnWriteArraySetGet(CopyOnWriteArraySetState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    @GroupThreads(ADD_THREADS)
    @Group("CopyOnWriteArraySet")
    public Object CopyOnWriteArraySetAdd(CopyOnWriteArraySetState s) {
        return benchmarkAdd(s.source, s.target);
    }

    @Benchmark
    @GroupThreads(ADD_THREADS)
    @Group("Vector")
    public Object VectorRemove(VectorState s) {
        return benchmarkRemove(s.target);
    }

    @Benchmark
    @GroupThreads(GET_THREADS)
    @Group("Vector")
    public Object VectorGet(VectorState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    @GroupThreads(ADD_THREADS)
    @Group("Vector")
    public Object VectorAdd(VectorState s) {
        return benchmarkAdd(s.source, s.target);
    }

    @Benchmark
    @GroupThreads(ADD_THREADS)
    @Group("CopyOnWriteArraySet")
    public Object CopyOnWriteArraySetRemove(CopyOnWriteArraySetState s) {
        return benchmarkRemove(s.source, s.target);
    }

    private Integer benchmarkGet(List<Integer> target) {
        return target.get(0);
    }

    private Integer benchmarkRemove(List<Integer> target) {
        return target.remove(0);
    }

    private Integer benchmarkGet(Integer key, Map<Integer, Integer> target) {
        return target.get(key);
    }

    private Integer benchmarkGet(Set<Integer> target) {
        return target.iterator().next();
    }

    private Integer benchmarkGet(Queue<Integer> target) {
        return target.peek();
    }

    private Integer benchmarkRemove(Queue<Integer> target) {
        return target.poll();
    }

    private Map<Integer, Integer> benchmarkRemove(Integer i, Map<Integer, Integer> target) {
        target.remove(i);
        return target;
    }

    private Integer benchmarkGet(NavigableSet<Integer> target) {
        return target.first();
    }

    private Collection<Integer> benchmarkAdd(Integer i, Collection<Integer> target) {
        target.add(i);
        return target;
    }

    private boolean benchmarkRemove(Integer i, Set<Integer> target) {
        return target.remove(i);
    }

    private Map<Integer, Integer> benchmarkAdd(Integer i, Map<Integer, Integer> target) {
        target.put(i, i);
        return target;
    }

    @State(Scope.Benchmark)
    public static class ArrayBlockingQueueState extends AbstractState {
        ArrayBlockingQueue<Integer> target;

        public void doSetup() {
            super.doSetup();
            target = new ArrayBlockingQueue(MAX_BOUNDED_SIZE);
            target.addAll(initialData);
        }
    }

    public static class ArrayListState extends AbstractState {
        List<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = synchronizedList(new ArrayList(MAX_BOUNDED_SIZE));
            target.addAll(initialData);
        }
    }

    public static class ConcurrentHashMapState extends AbstractState {
        ConcurrentHashMap<Integer, Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new ConcurrentHashMap();
            target.putAll(initialData.stream().collect(toMap(identity(), identity())));
        }
    }

    public static class ConcurrentLinkedDequeState extends AbstractState {
        ConcurrentLinkedDeque<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new ConcurrentLinkedDeque();
            target.addAll(initialData);
        }
    }

    public static class ConcurrentLinkedQueueState extends AbstractState {
        ConcurrentLinkedQueue<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new ConcurrentLinkedQueue();
            target.addAll(initialData);
        }
    }

    public static class ConcurrentSkipListMapState extends AbstractState {
        ConcurrentSkipListMap<Integer, Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new ConcurrentSkipListMap();
            target.putAll(initialData.stream().collect(toMap(identity(), identity())));
        }
    }

    public static class ConcurrentSkipListSetState extends AbstractState {
        ConcurrentSkipListSet<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new ConcurrentSkipListSet();
            target.addAll(initialData);
        }
    }

    public static class CopyOnWriteArrayListState extends AbstractState {
        CopyOnWriteArrayList<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new CopyOnWriteArrayList();
            target.addAll(initialData);
        }
    }

    public static class CopyOnWriteArraySetState extends AbstractState {
        CopyOnWriteArraySet<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new CopyOnWriteArraySet();
            target.addAll(initialData);
        }
    }

    public static class VectorState extends AbstractState {
        Vector<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new Vector();
            target.addAll(initialData);
        }
    }

    @State(Scope.Group)
    public static abstract class AbstractState {
        static Random random = new Random();

        int source;
        Collection<Integer> initialData;

        @Setup(Level.Iteration)
        public void doSetup() {
            source = random.nextInt();
            initialData = IntStream.range(MAX_BOUNDED_SIZE / 2, MAX_BOUNDED_SIZE / 2 + INITIAL_SIZE).boxed().collect(toList());
        }
    }
}
