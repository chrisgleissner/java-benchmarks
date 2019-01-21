package com.github.chrisgleissner.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;

import java.util.Collection;
import java.util.Map;

import static com.github.chrisgleissner.benchmarks.Constants.MAX_OPERATIONS_PER_ITERATION;
import static com.github.chrisgleissner.benchmarks.Constants.OPERATIONS_PER_PER_INVOCATION;
import static java.util.stream.IntStream.range;

public class CollectionAddBenchmark extends AbstractCollectionBenchmark {

    static Integer[] integers = range(0, MAX_OPERATIONS_PER_ITERATION).boxed().toArray(Integer[]::new);

    public static class MyState extends AbstractCollectionBenchmarkState {
        int i = 0;

        public Integer nextValue() {
            return integers[i = (i + 1) % MAX_OPERATIONS_PER_ITERATION];
        }
    }

    @Benchmark
    public Collection<Integer> ArrayDeque(MyState s) {
        return benchmarkAdd(s, s.ad);
    }

    @Benchmark
    public Collection<Integer> ArrayList(MyState s) {
        return benchmarkAdd(s, s.al);
    }

    @Benchmark
    public Collection<Integer> ArrayListNoResize(MyState s) {
        return benchmarkAdd(s, s.alnr);
    }

    @Benchmark
    public Map<Integer, Integer> ConcurrentHashMap(MyState s) {
        return benchmarkAdd(s, s.chm);
    }

    @Benchmark
    public Collection<Integer> ConcurrentLinkedDeque(MyState s) {
        return benchmarkAdd(s, s.cld);
    }

    @Benchmark
    public Collection<Integer> ConcurrentSkipListSet(MyState s) {
        return benchmarkAdd(s, s.csls);
    }

    @Benchmark
    public Collection<Integer> CopyOnWriteArrayList(MyState s) {
        return benchmarkAdd(s, s.cowal);
    }

    @Benchmark
    public Collection<Integer> CopyOnWriteArraySet(MyState s) {
        return benchmarkAdd(s, s.cowas);
    }

    @Benchmark
    public Map<Integer, Integer> HashMap(MyState s) {
        return benchmarkAdd(s, s.hm);
    }

    @Benchmark
    public Collection<Integer> HashSet(MyState s) {
        return benchmarkAdd(s, s.hs);
    }

    @Benchmark
    public Collection<Integer> LinkedBlockingDeque(MyState s) {
        return benchmarkAdd(s, s.lbd);
    }

    @Benchmark
    public Collection<Integer> LinkedBlockingQueue(MyState s) {
        return benchmarkAdd(s, s.lbq);
    }

    @Benchmark
    public Collection<Integer> LinkedHashSet(MyState s) {
        return benchmarkAdd(s, s.lhs);
    }

    @Benchmark
    public Map<Integer, Integer> LinkedHashMap(MyState s) {
        return benchmarkAdd(s, s.lhm);
    }

    @Benchmark
    public Collection<Integer> LinkedList(MyState s) {
        return benchmarkAdd(s, s.ll);
    }

    @Benchmark
    public Collection<Integer> LinkedTransferQueue(MyState s) {
        return benchmarkAdd(s, s.ltq);
    }

    @Benchmark
    public Collection<Integer> PriorityBlockingQueue(MyState s) {
        return benchmarkAdd(s, s.pbq);
    }

    @Benchmark
    public Collection<Integer> PriorityQueue(MyState s) {
        return benchmarkAdd(s, s.pq);
    }

    @Benchmark
    public Collection<Integer> Stack(MyState s) {
        return benchmarkAdd(s, s.s);
    }

    @Benchmark
    public Collection<Integer> TreeSet(MyState s) {
        return benchmarkAdd(s, s.ts);
    }

    @Benchmark
    public Collection<Integer> Vector(MyState s) {
        return benchmarkAdd(s, s.v);
    }

    @Benchmark
    public Collection<Integer> VectorNoResize(MyState s) {
        return benchmarkAdd(s, s.vnr);
    }

    private Collection<Integer> benchmarkAdd(MyState s, Collection<Integer> target) {
        for (int i = 0; i < OPERATIONS_PER_PER_INVOCATION; i++) {
            Integer value = s.nextValue();
            target.add(value);
        }
        return target;
    }

    private Map<Integer, Integer> benchmarkAdd(MyState s, Map<Integer, Integer> target) {
        for (int i = 0; i < OPERATIONS_PER_PER_INVOCATION; i++) {
            Integer value = s.nextValue();
            target.put(value, value);
        }
        return target;
    }

}
