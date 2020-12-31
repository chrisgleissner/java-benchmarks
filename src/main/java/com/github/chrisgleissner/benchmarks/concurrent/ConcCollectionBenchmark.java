package com.github.chrisgleissner.benchmarks.concurrent;

import com.github.chrisgleissner.benchmarks.collection.AbstractCollectionBenchmark;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import static java.util.stream.IntStream.range;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.IntStream;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.synchronizedList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 50, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@OperationsPerInvocation(1)
public class ConcCollectionBenchmark extends AbstractCollectionBenchmark {
    private static final int GET_THREADS = 10;
    private static final int ADD_THREADS = 2;
    private static final int INITIAL_SIZE = 500_000;
    private static final int MAX_BOUNDED_SIZE = 10_000_000;

    @Benchmark
    public Object ArrayBlockingQueueGet(ArrayBlockingQueueState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    public Object ArrayBlockingQueueAdd(ArrayBlockingQueueState s) {
        Collection<Integer> result = null;
        for (int i = 0; i < s.source_list.size(); i++) {
            result = benchmarkAdd(s.source_list.get(i), s.target);
        }
        return result;
    }

    @Benchmark
    public Object ArrayBlockingQueueRemove(ArrayBlockingQueueState s) {
        return benchmarkRemove(s.target);
    }

    @Benchmark
    public Object ArrayListGet(ArrayListState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    public Object ArrayListAdd(ArrayListState s) {
        Collection<Integer> result = null;
        for (int i = 0; i < s.source_list.size(); i++) {
            result = benchmarkAdd(s.source_list.get(i), s.target);
        }
        return result;
    }

    @Benchmark
    public Integer ArrayListRemove(ArrayListState s) {
        return benchmarkRemove(s.target);
    }

    @Benchmark
    public Integer ConcurrentHashMapGet(ConcurrentHashMapState s) {
        Integer result = 0;
        for (int i = 0; i < s.source_list.size(); i++) {
            result = benchmarkGet(s.source_list.get(i), s.target);
        }
        return result;
    }


    @Benchmark
    public Object ConcurrentHashMapAdd(ConcurrentHashMapState s) {
        Map<Integer, Integer> result = null;
        for (int i = 0; i < s.source_list.size(); i++) {
            result = benchmarkAdd(s.source_list.get(i), s.target);
        }
        return result;
    }

    @Benchmark
    public Object ConcurrentHashMapRemove(ConcurrentHashMapState s) {
        Map<Integer, Integer> m = null;
        for (int i = 0; i < s.source_list.size(); i++) {
            m = benchmarkRemove(s.source_list.get(i), s.target);
        }
        return m;
    }

    @Benchmark
    public Object ConcurrentLinkedDequeGet(ConcurrentLinkedDequeState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    public Object ConcurrentLinkedDequeAdd(ConcurrentLinkedDequeState s) {
        Collection<Integer> result = null;
        for (int i = 0; i < s.source_list.size(); i++) {
            result = benchmarkAdd(s.source_list.get(i), s.target);
        }
        return result;
    }

    @Benchmark
    public Object ConcurrentLinkedDequeRemove(ConcurrentLinkedDequeState s) {
        return benchmarkRemove(s.target);
    }

    @Benchmark
    public Integer ConcurrentLinkedQueueGet(ConcurrentLinkedQueueState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    public Object ConcurrentLinkedQueueAdd(ConcurrentLinkedQueueState s) {
        Collection<Integer> result = null;
        for (int i = 0; i < s.source_list.size(); i++) {
            result = benchmarkAdd(s.source_list.get(i), s.target);
        }
        return result;
    }

    @Benchmark
    public Object ConcurrentLinkedQueueRemove(ConcurrentLinkedQueueState s) {
        return benchmarkRemove(s.target);
    }

    @Benchmark
	@Measurement(iterations = 100, time = 1, timeUnit = TimeUnit.SECONDS)
    public Object ConcurrentSkipMapSetGet(ConcurrentSkipListMapState s) {
        Integer result = 0;
        for (int i = 0; i < s.source_list.size(); i++) {
            result = benchmarkGet(s.source_list.get(i), s.target);
        }
        return result;
    }

    @Benchmark
	@Measurement(iterations = 100, time = 1, timeUnit = TimeUnit.SECONDS)
    public Object ConcurrentSkipListMapAdd(ConcurrentSkipListMapState s) {
        Map<Integer, Integer> m = null;
        for (int i = 0; i < s.source_list.size(); i++) {
            m = benchmarkAdd(s.source_list.get(i), s.target);
        }
        return m;
    }

    @Benchmark
	@Measurement(iterations = 100, time = 1, timeUnit = TimeUnit.SECONDS)
    public Object ConcurrentSkipListMapRemove(ConcurrentSkipListMapState s) {
        Map<Integer,Integer> result = null;
        for (int i = 0; i < s.source_list.size(); i++) {
            result = benchmarkRemove(s.source_list.get(i), s.target);
        }
        return result;
    }

    @Benchmark
    public Object ConcurrentSkipListSetGet(ConcurrentSkipListSetState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
	@Measurement(iterations = 100, time = 1, timeUnit = TimeUnit.SECONDS)
    public Object ConcurrentSkipListSetAdd(ConcurrentSkipListSetState s) {
        Collection<Integer> result = null;
        for (int i = 0; i < s.source_list.size(); i++) {
            result =  benchmarkAdd(s.source_list.get(i), s.target);
        }
        return result;
    }

    @Benchmark
    public Object CopyOnWriteArrayListGet(CopyOnWriteArrayListState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    public Object CopyOnWriteArrayAdd(CopyOnWriteArrayListState s) {
       Collection<Integer> result = null;
        for (int i = 0; i < s.source_list.size(); i++) {
            result = benchmarkAdd(s.source_list.get(i), s.target);
        }
        return result;
    }

    @Benchmark
    public Object CopyOnWriteArrayRemove(CopyOnWriteArrayListState s) {
        return benchmarkRemove(s.target);
    }

    @Benchmark
	@Measurement(iterations = 100, time = 1, timeUnit = TimeUnit.SECONDS)
    public Object ConcurrentSkipLPistSetRemove(ConcurrentSkipListSetState s) {
        boolean result = false;
        for (int i = 0; i < s.source_list.size(); i++) {
            result = benchmarkRemove(s.source_list.get(i), s.target);
        }
        return result;
    }

    @Benchmark
    public Object CopyOnWriteArraySetGet(CopyOnWriteArraySetState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    public Object CopyOnWriteArraySetAdd(CopyOnWriteArraySetState s) {
        Collection<Integer> result = null;
        for (int i = 0; i < s.source_list.size(); i++) {
            result = benchmarkAdd(s.source_list.get(i), s.target);
        }
        return result;
    }

    @Benchmark
    public Object VectorRemove(VectorState s) {
        return benchmarkRemove(s.target);
    }

    @Benchmark
    public Object VectorGet(VectorState s) {
        return benchmarkGet(s.target);
    }

    @Benchmark
    public Object VectorAdd(VectorState s) {
        Collection<Integer> result = null;
        for (int i = 0; i < s.source_list.size(); i++) {
            result = benchmarkAdd(s.source_list.get(i), s.target);
        }
        return result;
    }

    @Benchmark
    public Object CopyOnWriteArraySetRemove(CopyOnWriteArraySetState s) {
        boolean result = false;
        for (int i = 0; i < s.source_list.size(); i++) {
            result = benchmarkRemove(s.source_list.get(i), s.target);
        }
        return result;
    }

    private Integer benchmarkGet(List<Integer> target) {
        return (target.size() > 0) ? target.get(0) : 0;
    }

    private Integer benchmarkRemove(List<Integer> target) {
        return (target.size() > 0) ? target.remove(0) : 0;
    }

    private Integer benchmarkGet(Integer key, Map<Integer, Integer> target) {
        return target.get(key);
    }

    private Integer benchmarkGet(Set<Integer> target) {
	return (target.size() > 0) ? target.iterator().next() : 0;
    }

    private Integer benchmarkGet(Queue<Integer> target) {
        return (target.size() > 0) ? target.peek() : 0;
    }

    private Integer benchmarkRemove(Queue<Integer> target) {
        return target.poll();
    }

    private Map<Integer, Integer> benchmarkRemove(Integer i, Map<Integer, Integer> target) {
        target.remove(i);
        return target;
    }

    private Integer benchmarkGet(NavigableSet<Integer> target) {
        return (target.size() > 0) ? target.first() : 0;
    }

    private Collection<Integer> benchmarkAdd(Integer i, Collection<Integer> target) {
        if (target.size() == MAX_BOUNDED_SIZE - 1) {
		return target;
	}
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
    
	@State(Scope.Benchmark)
	public static class ArrayListState extends AbstractState {
        List<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = synchronizedList(new ArrayList(MAX_BOUNDED_SIZE));
            target.addAll(initialData);
        }
    }
    
	@State(Scope.Benchmark)
	public static class ConcurrentHashMapState extends AbstractState {
        ConcurrentHashMap<Integer, Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new ConcurrentHashMap();
            target.putAll(initialData.stream().collect(toMap(identity(), identity())));
        }
    }
    
	@State(Scope.Benchmark)
	public static class ConcurrentLinkedDequeState extends AbstractState {
        ConcurrentLinkedDeque<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new ConcurrentLinkedDeque();
            target.addAll(initialData);
        }
    }
    
	@State(Scope.Benchmark)
	public static class ConcurrentLinkedQueueState extends AbstractState {
        ConcurrentLinkedQueue<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new ConcurrentLinkedQueue();
            target.addAll(initialData);
        }
    }
    
	@State(Scope.Benchmark)
	public static class ConcurrentSkipListMapState extends AbstractState {
        ConcurrentSkipListMap<Integer, Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new ConcurrentSkipListMap();
            target.putAll(initialData.stream().collect(toMap(identity(), identity())));
        }
    }
    
	@State(Scope.Benchmark)
	public static class ConcurrentSkipListSetState extends AbstractState {
        ConcurrentSkipListSet<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new ConcurrentSkipListSet();
            target.addAll(initialData);
        }
    }
    
	@State(Scope.Benchmark)
	public static class CopyOnWriteArrayListState extends AbstractState {
        CopyOnWriteArrayList<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new CopyOnWriteArrayList();
            target.addAll(initialData);
        }
    }
    
	@State(Scope.Benchmark)
	public static class CopyOnWriteArraySetState extends AbstractStateSet {
        CopyOnWriteArraySet<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new CopyOnWriteArraySet(initialDataSet);
        }
    }
    
	@State(Scope.Benchmark)
	public static class VectorState extends AbstractState {
        Vector<Integer> target;

        @Setup
        public void doSetup() {
            super.doSetup();
            target = new Vector();
            target.addAll(initialData);
        }
    }

	@State(Scope.Benchmark)
    public static abstract class AbstractState {
        static Random random = new Random();

        List<Integer> source_list = new ArrayList<Integer>();
        Collection<Integer> initialData;

        @Setup(Level.Iteration)
        public void doSetup() {
            int step = INITIAL_SIZE / 100;
            source_list = range(0, 100).map(x -> x * step).boxed().sorted(Collections.reverseOrder()).collect(toList());
            initialData = IntStream.range(MAX_BOUNDED_SIZE, MAX_BOUNDED_SIZE + INITIAL_SIZE).boxed().collect(toList());
        }
    }

	@State(Scope.Benchmark)
    public static abstract class AbstractStateSet {
        static Random random = new Random();
        CopyOnWriteArraySet<Integer> initialDataSet = new CopyOnWriteArraySet<>(IntStream.range(MAX_BOUNDED_SIZE, MAX_BOUNDED_SIZE + INITIAL_SIZE).boxed().collect(toList()));

        List<Integer> source_list = new ArrayList<Integer>();

        @Setup(Level.Iteration)
        public void doSetup() {
            int step = INITIAL_SIZE / 100;
            source_list = range(0, 100).map(x -> x * step).boxed().sorted(Collections.reverseOrder()).collect(toList());
        }
    }
}
