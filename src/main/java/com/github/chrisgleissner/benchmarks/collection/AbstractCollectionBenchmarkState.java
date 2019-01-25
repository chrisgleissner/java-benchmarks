package com.github.chrisgleissner.benchmarks.collection;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.PriorityBlockingQueue;

import static com.github.chrisgleissner.benchmarks.Constants.MAX_OPERATIONS_PER_ITERATION;

@State(Scope.Benchmark)
public abstract class AbstractCollectionBenchmarkState {
    // Not examined:
    // DelayQueue: Requires mix-in interface 'Delayed'
    // SynchronousQueue: Requires removal after add
    ArrayBlockingQueue<Integer> abq;
    ArrayDeque<Integer> ad;
    ArrayList<Integer> al;
    ArrayList<Integer> alnr;
    ConcurrentHashMap<Integer, Integer> chm;
    ConcurrentLinkedDeque<Integer> cld;
    ConcurrentLinkedQueue<Integer> clq;
    ConcurrentSkipListMap<Integer, Integer> cslm;
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
    TreeMap<Integer, Integer> tm;
    Vector<Integer> v;
    Vector<Integer> vnr;

    @Setup(Level.Iteration)
    public void doSetup() {
        abq = new ArrayBlockingQueue<>(MAX_OPERATIONS_PER_ITERATION);
        ad = new ArrayDeque<>();
        al = new ArrayList<>();
        alnr = new ArrayList<>(MAX_OPERATIONS_PER_ITERATION);
        chm = new ConcurrentHashMap<>();
        cld = new ConcurrentLinkedDeque<>();
        clq = new ConcurrentLinkedQueue<>();
        cslm = new ConcurrentSkipListMap<>();
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
        tm = new TreeMap<>();
        ts = new TreeSet<>();
        v = new Vector<>();
        vnr = new Vector<>(MAX_OPERATIONS_PER_ITERATION);
    }

    @TearDown(Level.Iteration)
    public void doTearDown() {
        abq = null;
        ad = null;
        al = null;
        alnr = null;
        chm = null;
        cld = null;
        clq = null;
        cslm = null;
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
        tm = null;
        ts = null;
        v = null;
        vnr = null;
    }
}
