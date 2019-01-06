package com.github.chrisgleissner.benchmarks;

import lombok.Data;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import static java.lang.invoke.MethodType.methodType;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(1)
public class SetterBenchmark {

    @State(Scope.Thread)
    private static class AbstractState {

        @Setup
        public void setupIteration() {
            foo = new Foo();
            l = System.nanoTime();
        }

        Foo foo;
        Long l;
    }

    @State(Scope.Thread)
    public static class DirectState extends AbstractState {
    }

    @Benchmark
    public void direct(Blackhole blackhole, DirectState s) {
        s.foo.setL(s.l);
        blackhole.consume(s.foo.l);
    }

    @State(Scope.Thread)
    public static class ReflectionState extends AbstractState {

        @Setup
        public void doSetup() throws NoSuchMethodException {
            setter = Foo.class.getDeclaredMethod("setL", Long.class);
            setter.setAccessible(true);
        }

        Method setter;
    }

    @Benchmark
    public void reflection(Blackhole blackhole, ReflectionState s) throws InvocationTargetException, IllegalAccessException {
        s.setter.invoke(s.foo, new Object[] { s.l });
        blackhole.consume(s.l);

    }

    @State(Scope.Thread)
    public static class LambdaMetaFactoryState extends AbstractState {

        @Setup
        public void doSetup() throws Throwable {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodHandle longSetterHandle = lookup.findVirtual(Foo.class, "setL", methodType(void.class, Long.class));
            CallSite site = LambdaMetafactory.metafactory(lookup,
                    "accept",
                    methodType(BiConsumer.class),
                    methodType(void.class, Object.class, Object.class),
                    longSetterHandle,
                    longSetterHandle.type());
            biConsumerFunction = (BiConsumer) site.getTarget().invokeExact();
        }

        BiConsumer<Foo, Long> biConsumerFunction;
    }

    @Benchmark
    public void lambdaMetaFactory(Blackhole blackhole, LambdaMetaFactoryState s) {
        s.biConsumerFunction.accept(s.foo, s.l);
        blackhole.consume(s.l);
    }

    @Data
    static class Foo {
        Long l;
    }
}
