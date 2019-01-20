package com.github.chrisgleissner.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;

import java.util.Collection;
import java.util.Map;

public class CollectionAddBenchmark extends AbstractBenchmark {

    public static class MyState extends AbstractCollectionBenchmarkState {
    }

    // Testing this requires consuming each element after it's added, thus affecting the comparability with other collections
    //    @Benchmark
    //    public Collection<Integer> ArrayBlockingQueue(MyState s) {
    //        return benchmarkAdd(s.ints, s.abq);
    //    }

    @Benchmark
    public Collection<Integer> ArrayDeque(MyState s) {
        return benchmarkAdd(s.ints, s.ad);
    }

    @Benchmark
    public Collection<Integer> ArrayList(MyState s) {
        return benchmarkAdd(s.ints, s.al);
    }

    @Benchmark
    public Collection<Integer> ArrayListNoResize(MyState s) {
        return benchmarkAdd(s.ints, s.alnr);
    }

    @Benchmark
    public Map<Integer, Integer> ConcurrentHashMap(MyState s) {
        return benchmarkAdd(s.ints, s.chm);
    }

    @Benchmark
    public Collection<Integer> ConcurrentLinkedDeque(MyState s) {
        return benchmarkAdd(s.ints, s.cld);
    }

    @Benchmark
    public Collection<Integer> ConcurrentSkipListSet(MyState s) {
        return benchmarkAdd(s.ints, s.csls);
    }

    @Benchmark
    public Collection<Integer> CopyOnWriteArrayList(MyState s) {
        return benchmarkAdd(s.ints, s.cowal);
    }

    @Benchmark
    public Collection<Integer> CopyOnWriteArraySet(MyState s) {
        return benchmarkAdd(s.ints, s.cowas);
    }

    @Benchmark
    public Map<Integer, Integer> HashMap(MyState s) {
        return benchmarkAdd(s.ints, s.hm);
    }

    @Benchmark
    public Collection<Integer> HashSet(MyState s) {
        return benchmarkAdd(s.ints, s.hs);
    }

    @Benchmark
    public Collection<Integer> LinkedBlockingDeque(MyState s) {
        return benchmarkAdd(s.ints, s.lbd);
    }

    @Benchmark
    public Collection<Integer> LinkedBlockingQueue(MyState s) {
        return benchmarkAdd(s.ints, s.lbq);
    }

    @Benchmark
    public Collection<Integer> LinkedHashSet(MyState s) {
        return benchmarkAdd(s.ints, s.lhs);
    }

    @Benchmark
    public Map<Integer, Integer> LinkedHashMap(MyState s) {
        return benchmarkAdd(s.ints, s.lhm);
    }

    @Benchmark
    public Collection<Integer> LinkedList(MyState s) {
        return benchmarkAdd(s.ints, s.ll);
    }

    @Benchmark
    public Collection<Integer> LinkedTransferQueue(MyState s) {
        return benchmarkAdd(s.ints, s.ltq);
    }

    @Benchmark
    public Collection<Integer> PriorityBlockingQueue(MyState s) {
        return benchmarkAdd(s.ints, s.pbq);
    }

    @Benchmark
    public Collection<Integer> PriorityQueue(MyState s) {
        return benchmarkAdd(s.ints, s.pq);
    }

    @Benchmark
    public Collection<Integer> Stack(MyState s) {
        return benchmarkAdd(s.ints, s.s);
    }

    @Benchmark
    public Collection<Integer> TreeSet(MyState s) {
        return benchmarkAdd(s.ints, s.ts);
    }

    @Benchmark
    public Collection<Integer> Vector(MyState s) {
        return benchmarkAdd(s.ints, s.v);
    }

    @Benchmark
    public Collection<Integer> VectorNoResize(MyState s) {
        return benchmarkAdd(s.ints, s.vnr);
    }

    private Collection<Integer> benchmarkAdd(Integer[] ints, Collection<Integer> target) {
        for (Integer i : ints)
            target.add(i);
        return target;
    }

    private Map<Integer, Integer> benchmarkAdd(Integer[] ints, Map<Integer, Integer> target) {
        for (Integer i : ints)
            target.put(i, i);
        return target;
    }

}
