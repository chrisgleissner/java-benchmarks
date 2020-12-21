package com.github.chrisgleissner.benchmarks.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
class RoundTripNotification implements Closeable {
    private final Thread receiver;
    private final Action receiveResponse;
    private final Action send;

    private final AtomicLong receivedCount = new AtomicLong();
    private final AtomicBoolean awaitingReceipt = new AtomicBoolean(true);
    private final Phaser readyToReceive = new Phaser(2);
    private final Phaser readyToRespond = new Phaser(2);

    private volatile boolean terminating;

    RoundTripNotification(Action send, Action receive, Action receiveResponse) {
        this.send = send;
        this.receiveResponse = receiveResponse;
        receiver = new Thread(() -> {
            while (!terminating) {
                try {
                    if (awaitingReceipt.compareAndSet(true, false)) {
                        log.debug("Receive");

                        receive.perform(this);

                        log.debug("Receive done");
                    }
                } catch (InterruptedException e) {
                    if (!terminating)
                        throw new RuntimeException(e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, "receiver");
        receiver.start();
    }

    public Object perform(int numberOfIterations) {
        receivedCount.set(0);
        awaitingReceipt.set(true);

        for (int i = 0; i < numberOfIterations; i++) {
            log.debug("Iteration {}", i);
            try {
                log.debug("Send");
                send.perform(this);

                log.debug("ReceiveResponse");
                receiveResponse.perform(this);
                log.debug("ReceiveResponse done");

                if (i < numberOfIterations - 1) {
                    awaitingReceipt.set(true);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (receivedCount.get() != numberOfIterations)
            throw new RuntimeException(String.format("Expected %s receipts instead of %s", numberOfIterations, receivedCount.get()));
        return this;
    }

    @Override
    public void close() {
        terminating = true;
        receiver.interrupt();
        try {
            receiver.join(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread didn't stop", e);
        }
    }

    public void received() {
        receivedCount.incrementAndGet();
    }

    public void readyToReceive() {
        readyToReceive.arriveAndAwaitAdvance();
    }

    public void readyToRespond() {
        readyToRespond.arriveAndAwaitAdvance();
    }

    interface Action {
        void perform(RoundTripNotification notification) throws Exception;
    }
}
