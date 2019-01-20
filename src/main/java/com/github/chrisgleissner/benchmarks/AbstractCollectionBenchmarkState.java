package com.github.chrisgleissner.benchmarks;

import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@State(Scope.Benchmark)
public abstract class AbstractCollectionBenchmarkState {

    public static int ADDS_PER_INVOCATION = 10_000;
    public static int MAX_NUMBER_OF_INVOCATIONS_EXPECTED_PER_ITERATION = 50_000_000;

    Integer[] ints = IntStream.range(0, ADDS_PER_INVOCATION).boxed().toArray(Integer[]::new);

    ArrayBlockingQueue<Integer> abq;
    ArrayDeque<Integer> ad;
    ArrayList<Integer> al;
    ArrayList<Integer> alnr;
    ConcurrentHashMap<Integer, Integer> chm;
    ConcurrentLinkedDeque<Integer> cld;
    ConcurrentSkipListSet<Integer> csls;
    CopyOnWriteArrayList<Integer> cowal;
    CopyOnWriteArraySet<Integer> cowas;
    HashMap<Integer, Integer> hm;
    HashSet<Integer> hs;
    LinkedBlockingDeque<Integer> lbd;
    LinkedBlockingQueue<Integer> lbq;
    LinkedHashSet<Integer> lhs;
    LinkedHashMap<Integer, Integer> lhm;
    LinkedList<Integer> ll;
    LinkedTransferQueue<Integer> ltq;
    PriorityBlockingQueue<Integer> pbq;
    PriorityQueue<Integer> pq;
    Stack<Integer> s;
    TreeSet<Integer> ts;
    Vector<Integer> v;
    Vector<Integer> vnr;

    @Setup(Level.Iteration)
    public void doSetup() {
        abq = new ArrayBlockingQueue<>(MAX_NUMBER_OF_INVOCATIONS_EXPECTED_PER_ITERATION);
        ad = new ArrayDeque<>();
        al = new ArrayList<>();
        alnr = new ArrayList<>(MAX_NUMBER_OF_INVOCATIONS_EXPECTED_PER_ITERATION);
        chm = new ConcurrentHashMap<>();
        cld = new ConcurrentLinkedDeque<>();
        csls = new ConcurrentSkipListSet<>();
        cowal = new CopyOnWriteArrayList<>();
        cowas = new CopyOnWriteArraySet<>();
        hm = new HashMap<>();
        hs = new HashSet<>();
        lbd = new LinkedBlockingDeque<>();
        lbq = new LinkedBlockingQueue<>();
        lhs = new LinkedHashSet<>();
        lhm = new LinkedHashMap<>();
        ll = new LinkedList<>();
        ltq = new LinkedTransferQueue<>();
        pbq = new PriorityBlockingQueue<>();
        pq = new PriorityQueue<>();
        s = new Stack<>();
        ts = new TreeSet<>();
        v = new Vector<>();
        vnr = new Vector<>(MAX_NUMBER_OF_INVOCATIONS_EXPECTED_PER_ITERATION);
    }

    @TearDown(Level.Iteration)
    public void doTearDown() {
        abq = null;
        ad = null;
        al = null;
        alnr = null;
        chm = null;
        cld = null;
        csls = null;
        cowal = null;
        cowas = null;
        hm = null;
        hs = null;
        lbd = null;
        lbq = null;
        lhs = null;
        lhm = null;
        ll = null;
        ltq = null;
        pbq = null;
        pq = null;
        s = null;
        ts = null;
        v = null;
        vnr = null;
    }
}
