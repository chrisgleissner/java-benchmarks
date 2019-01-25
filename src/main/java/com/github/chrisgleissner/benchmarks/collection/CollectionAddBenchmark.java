package com.github.chrisgleissner.benchmarks.collection;

import org.openjdk.jmh.annotations.Benchmark;

import java.util.Collection;
import java.util.Map;

import static com.github.chrisgleissner.benchmarks.Constants.MAX_OPERATIONS_PER_ITERATION;
import static com.github.chrisgleissner.benchmarks.Constants.OPERATIONS_PER_PER_INVOCATION;
import static java.util.stream.IntStream.range;

public class CollectionAddBenchmark extends AbstractCollectionBenchmark {

    static Integer[] integers = range(0, MAX_OPERATIONS_PER_ITERATION).boxed().toArray(Integer[]::new);

    @Benchmark
    public Object ArrayBlockingQueue(MyState s) {
        return benchmarkAdd(s, s.abq);
    }

    @Benchmark
    public Object ArrayDeque(MyState s) {
        return benchmarkAdd(s, s.ad);
    }

    @Benchmark
    public Object ArrayList(MyState s) {
        return benchmarkAdd(s, s.al);
    }

    @Benchmark
    public Object ArrayListNoResize(MyState s) {
        return benchmarkAdd(s, s.alnr);
    }

    @Benchmark
    public Object ConcurrentHashMap(MyState s) {
        return benchmarkPut(s, s.chm);
    }

    @Benchmark
    public Object ConcurrentLinkedDeque(MyState s) {
        return benchmarkAdd(s, s.cld);
    }

    @Benchmark
    public Object ConcurrentLinkedQueue(MyState s) {
        return benchmarkAdd(s, s.clq);
    }

    @Benchmark
    public Object ConcurrentSkipListMap(MyState s) {
        return benchmarkPut(s, s.cslm);
    }

    @Benchmark
    public Object ConcurrentSkipListSet(MyState s) {
        return benchmarkAdd(s, s.csls);
    }

    @Benchmark
    public Object CopyOnWriteArrayList(MyState s) {
        return benchmarkAdd(s, s.cowal);
    }

    @Benchmark
    public Object CopyOnWriteArraySet(MyState s) {
        return benchmarkAdd(s, s.cowas);
    }

    @Benchmark
    public Object HashMap(MyState s) {
        return benchmarkPut(s, s.hm);
    }

    @Benchmark
    public Object HashSet(MyState s) {
        return benchmarkAdd(s, s.hs);
    }

    @Benchmark
    public Object LinkedBlockingDeque(MyState s) {
        return benchmarkAdd(s, s.lbd);
    }

    @Benchmark
    public Object LinkedBlockingQueue(MyState s) {
        return benchmarkAdd(s, s.lbq);
    }

    @Benchmark
    public Object LinkedHashSet(MyState s) {
        return benchmarkAdd(s, s.lhs);
    }

    @Benchmark
    public Object LinkedHashMap(MyState s) {
        return benchmarkPut(s, s.lhm);
    }

    @Benchmark
    public Object LinkedList(MyState s) {
        return benchmarkAdd(s, s.ll);
    }

    @Benchmark
    public Object LinkedTransferQueue(MyState s) {
        return benchmarkAdd(s, s.ltq);
    }

    @Benchmark
    public Object PriorityBlockingQueue(MyState s) {
        return benchmarkAdd(s, s.pbq);
    }

    @Benchmark
    public Object PriorityQueue(MyState s) {
        return benchmarkAdd(s, s.pq);
    }

    @Benchmark
    public Object Stack(MyState s) {
        return benchmarkAdd(s, s.s);
    }

    @Benchmark
    public Object TreeSet(MyState s) {
        return benchmarkAdd(s, s.ts);
    }

    @Benchmark
    public Object Vector(MyState s) {
        return benchmarkAdd(s, s.v);
    }

    @Benchmark
    public Object VectorNoResize(MyState s) {
        return benchmarkAdd(s, s.vnr);
    }

    private Collection<Integer> benchmarkAdd(MyState s, Collection<Integer> target) {
        for (int i = 0; i < OPERATIONS_PER_PER_INVOCATION; i++) {
            Integer value = s.nextValue();
            target.add(value);
        }
        return target;
    }

    private Map<Integer, Integer> benchmarkPut(MyState s, Map<Integer, Integer> target) {
        for (int i = 0; i < OPERATIONS_PER_PER_INVOCATION; i++) {
            Integer value = s.nextValue();
            target.put(value, value);
        }
        return target;
    }

    public static class MyState extends AbstractCollectionBenchmarkState {
        int i = 0;

        public Integer nextValue() {
            return integers[i = (i + 1) % MAX_OPERATIONS_PER_ITERATION];
        }
    }


}
