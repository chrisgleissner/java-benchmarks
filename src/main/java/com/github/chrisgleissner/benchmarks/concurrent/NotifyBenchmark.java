package com.github.chrisgleissner.benchmarks.concurrent;

import com.github.chrisgleissner.benchmarks.AbstractBenchmark;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@OperationsPerInvocation(10)
public class NotifyBenchmark extends AbstractBenchmark {

    public static final int OPERATIONS_PER_PER_INVOCATION = 10;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(NotifyBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public Object waitNotify(WaitNotifyState state) {
        return state.notification.perform(OPERATIONS_PER_PER_INVOCATION);
    }

    @Benchmark
    public Object phaser(PhaserState state) {
        return state.notification.perform(OPERATIONS_PER_PER_INVOCATION);
    }

    @Benchmark
    public Object cyclicBarrier(CyclicBarrierState state) {
        return state.notification.perform(OPERATIONS_PER_PER_INVOCATION);
    }

    @Benchmark
    public Object countdownLatch(CountDownLatchState state) {
        return state.notification.perform(OPERATIONS_PER_PER_INVOCATION);
    }

    @State(Scope.Benchmark)
    public static class PhaserState extends AbstractNotifyState {

        @Setup(Level.Iteration)
        public void doSetup() {
            Phaser request = new Phaser();
            request.bulkRegister(2);

            Phaser response = new Phaser();
            response.bulkRegister(2);

            notification = new RoundTripNotification(
                    t -> {
                        t.readyToReceive();
                        request.arriveAndAwaitAdvance();
                    },
                    t -> {
                        t.readyToReceive();
                        request.arriveAndAwaitAdvance();
                        t.received();

                        t.readyToRespond();
                        response.arriveAndAwaitAdvance();
                    },
                    t -> {
                        t.readyToRespond();
                        response.arriveAndAwaitAdvance();
                    });
        }
    }

    @State(Scope.Benchmark)
    public static class CyclicBarrierState extends AbstractNotifyState {

        @Setup(Level.Iteration)
        public void doSetup() {
            CyclicBarrier request = new CyclicBarrier(2);
            CyclicBarrier response = new CyclicBarrier(2);

            notification = new RoundTripNotification(
                    t -> {
                        try {
                            t.readyToReceive();
                            request.await();
                        } catch (BrokenBarrierException e) {
                        }
                    },
                    t -> {
                        try {
                            t.readyToReceive();
                            request.await();
                            t.received();

                            t.readyToRespond();
                            response.await();
                        } catch (BrokenBarrierException e) {
                        }
                    },
                    t -> {
                        try {
                            t.readyToRespond();
                            response.await();
                        } finally {
                            request.reset();
                            response.reset();
                        }
                    });
        }
    }

    @State(Scope.Benchmark)
    public static class WaitNotifyState extends AbstractNotifyState {

        @Setup(Level.Iteration)
        public void doSetup() {
            Object request = new Object();
            Object response = new Object();
            notification = new RoundTripNotification(
                    t -> {
                        t.readyToReceive();
                        synchronized (request) {
                            request.notify();
                        }
                    },
                    t -> {
                        synchronized (request) {
                            t.readyToReceive();
                            request.wait();
                            t.received();
                        }
                        t.readyToRespond();
                        synchronized (response) {
                            response.notify();
                        }
                    },
                    t -> {
                        synchronized (response) {
                            t.readyToRespond();
                            response.wait();
                        }
                    });
        }
    }

    @State(Scope.Benchmark)
    public static class CountDownLatchState extends AbstractNotifyState {

        @Setup(Level.Iteration)
        public void doSetup() {
            AtomicReference<CountDownLatch> request = new AtomicReference<>();
            request.set(new CountDownLatch(1));

            AtomicReference<CountDownLatch> response = new AtomicReference<>();
            response.set(new CountDownLatch(1));

            notification = new RoundTripNotification(
                    t -> {
                        t.readyToReceive();
                        request.get().countDown();
                    },
                    t -> {
                        t.readyToReceive();
                        request.get().await();
                        t.received();

                        t.readyToRespond();
                        response.get().countDown();
                    },
                    t -> {
                        t.readyToRespond();
                        response.get().await();
                        request.set(new CountDownLatch(1));
                        response.set(new CountDownLatch(1));
                    });
        }
    }

    protected abstract static class AbstractNotifyState {
        protected RoundTripNotification notification;

        public abstract void doSetup();

        @TearDown(Level.Iteration)
        public void doTeardown() {
            notification.close();
        }
    }
}
