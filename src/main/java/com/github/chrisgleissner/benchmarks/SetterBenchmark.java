package com.github.chrisgleissner.benchmarks;

import lombok.Data;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodHandles.privateLookupIn;
import static java.lang.invoke.MethodType.methodType;

public class SetterBenchmark extends AbstractBenchmark {

    @Data
    public static class Foo {
        private Long l;
    }

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

    // ======================

    @State(Scope.Thread)
    public static class DirectState extends AbstractState {
    }

    @Benchmark
    public void direct(Blackhole blackhole, DirectState s) {
        s.foo.setL(s.l);
        blackhole.consume(s.foo.l);
    }

    // ======================

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
        s.setter.invoke(s.foo, s.l);
        blackhole.consume(s.foo.l);

    }

    // ======================

    @State(Scope.Thread)
    public static class LambdaMetaFactoryForSetterState extends AbstractState {

        @Setup
        public void doSetup() throws Throwable {
            MethodHandles.Lookup lookup = lookup();
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
    public void lambdaMetaFactoryForSetter(Blackhole blackhole, LambdaMetaFactoryForSetterState s) {
        s.biConsumerFunction.accept(s.foo, s.l);
        blackhole.consume(s.foo.l);
    }

    // ======================

    @State(Scope.Thread)
    public static class MethodHandleForSetterState extends AbstractState {

        @Setup
        public void doSetup() throws Throwable {
            Method method = Foo.class.getMethod("setL", Long.class);
            methodHandle = lookup().unreflect(method);
        }

        MethodHandle methodHandle;
    }

    @Benchmark
    public void methodHandleForSetter(Blackhole blackhole, MethodHandleForSetterState s) throws Throwable {
        s.methodHandle.invoke(s.foo, s.l);
        blackhole.consume(s.foo.l);
    }

    // ======================

    @State(Scope.Thread)
    public static class MethodHandleForFieldState extends AbstractState {
        @Setup
        public void doSetup() throws Throwable {
            Field field = Foo.class.getDeclaredField("l");
            field.setAccessible(true);
            methodHandle = lookup().unreflectSetter(field);
        }

        MethodHandle methodHandle;
    }

    @Benchmark
    public void methodHandleForField(Blackhole blackhole, MethodHandleForFieldState s) throws Throwable {
        s.methodHandle.invoke(s.foo, s.l);
        blackhole.consume(s.foo.l);
    }

    // ======================

    @State(Scope.Thread)
    public static class VarHandleState extends AbstractState {

        @Setup
        public void doSetup() throws Throwable {
            varHandle = privateLookupIn(Foo.class, lookup()).findVarHandle(Foo.class, "l", Long.class);

        }

        VarHandle varHandle;
    }

    @Benchmark
    public void varHandle(Blackhole blackhole, VarHandleState s) {
        s.varHandle.set(s.foo, s.l);
        blackhole.consume(s.foo.l);
    }
}
