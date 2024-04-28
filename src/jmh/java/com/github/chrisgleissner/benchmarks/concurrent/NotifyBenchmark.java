package com.github.chrisgleissner.benchmarks.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@OperationsPerInvocation(100)
public class NotifyBenchmark {

    public static final int OPERATIONS_PER_PER_INVOCATION = 100;

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
            final Phaser request = new Phaser();
            request.bulkRegister(2);

            final Phaser response = new Phaser();
            response.bulkRegister(2);

            notification = new RoundTripNotification(
                    n -> {
                        n.readyToReceive();
                        request.arriveAndAwaitAdvance();
                    },
                    n -> {
                        n.readyToReceive();
                        request.arriveAndAwaitAdvance();
                        n.received();

                        n.readyToRespond();
                        response.arriveAndAwaitAdvance();
                    },
                    n -> {
                        n.readyToRespond();
                        response.arriveAndAwaitAdvance();
                    });
        }
    }

    @State(Scope.Benchmark)
    public static class CyclicBarrierState extends AbstractNotifyState {

        @Setup(Level.Iteration)
        public void doSetup() {
            final CyclicBarrier request = new CyclicBarrier(2);
            final CyclicBarrier response = new CyclicBarrier(2);

            notification = new RoundTripNotification(
                    n -> {
                        n.readyToReceive();
                        request.await();
                    },
                    n -> {
                        n.readyToReceive();
                        request.await();
                        n.received();

                        n.readyToRespond();
                        response.await();
                    },
                    n -> {
                        n.readyToRespond();
                        response.await();
                    });
        }
    }

    @State(Scope.Benchmark)
    public static class WaitNotifyState extends AbstractNotifyState {

        @Setup(Level.Iteration)
        public void doSetup() {
            final Object request = new Object();
            final Object response = new Object();
            notification = new RoundTripNotification(
                    n -> {
                        n.readyToReceive();
                        synchronized (request) {
                            request.notify();
                        }
                    },
                    n -> {
                        synchronized (request) {
                            n.readyToReceive();
                            request.wait();
                            n.received();
                        }
                        n.readyToRespond();
                        synchronized (response) {
                            response.notify();
                        }
                    },
                    n -> {
                        synchronized (response) {
                            n.readyToRespond();
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
                    n -> {
                        n.readyToReceive();
                        request.get().countDown();
                    },
                    n -> {
                        n.readyToReceive();
                        // TODO Investigate how a thread suspension can be enforced to make benchmark more realistic
                        // CountDownLatch is much faster than Wait/Notify because it never awaits. Instead, the
                        // latch is counted down before the await.
                        request.get().await();
                        n.received();

                        n.readyToRespond();
                        response.get().countDown();
                    },
                    n -> {
                        try {
                            n.readyToRespond();
                            response.get().await();
                        } finally {
                            request.set(new CountDownLatch(1));
                            response.set(new CountDownLatch(1));
                        }
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
