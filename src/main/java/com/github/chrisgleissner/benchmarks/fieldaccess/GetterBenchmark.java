package com.github.chrisgleissner.benchmarks.fieldaccess;

import com.github.chrisgleissner.benchmarks.AbstractBenchmark;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
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
import java.util.function.Function;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodHandles.privateLookupIn;
import static java.lang.invoke.MethodType.methodType;

public class GetterBenchmark extends AbstractBenchmark {

    @Data
    @AllArgsConstructor
    static class Foo {
        Long l;
    }

    @State(Scope.Thread)
    private static class AbstractState {

        @Setup(Level.Iteration)
        public void setupIteration() {
            foo = new Foo(System.nanoTime());
        }

        Foo foo;
    }

    // ======================

    @State(Scope.Thread)
    public static class DirectState extends AbstractState {
    }

    @Benchmark
    public void direct(Blackhole blackhole, DirectState s) {
        blackhole.consume(s.foo.getL());
    }

    // ======================

    @State(Scope.Thread)
    public static class ReflectionState extends AbstractState {

        @Setup
        public void doSetup() throws NoSuchMethodException {
            getter = Foo.class.getDeclaredMethod("getL");
            getter.setAccessible(true);
        }

        Method getter;
    }

    @Benchmark
    public void reflection(Blackhole blackhole, ReflectionState s) throws InvocationTargetException, IllegalAccessException {
        blackhole.consume(s.getter.invoke(s.foo));
    }

    // ======================

    @State(Scope.Thread)
    public static class LambdaMetaFactoryForGetterState extends AbstractState {

        @Setup
        public void doSetup() throws Throwable {
            MethodHandles.Lookup lookup = lookup();
            CallSite site = LambdaMetafactory.metafactory(lookup,
                    "apply",
                    methodType(Function.class),
                    methodType(Object.class, Object.class),
                    lookup.findVirtual(Foo.class, "getL", methodType(Long.class)),
                    methodType(Long.class, Foo.class));
            getterFunction = (Function) site.getTarget().invokeExact();
        }

        Function<Foo, Long> getterFunction;
    }

    @Benchmark
    public void lambdaMetaFactoryForGetter(Blackhole blackhole, LambdaMetaFactoryForGetterState s) {
        blackhole.consume(s.getterFunction.apply(s.foo));
    }

    // ======================

    @State(Scope.Thread)
    public static class MethodHandleForGetterState extends AbstractState {

        @Setup
        public void doSetup() throws Throwable {
            Method method = Foo.class.getMethod("getL");
            methodHandle = lookup().unreflect(method);
        }

        MethodHandle methodHandle;
    }

    @Benchmark
    public void methodHandleForGetter(Blackhole blackhole, MethodHandleForGetterState s) throws Throwable {
        blackhole.consume(s.methodHandle.invoke(s.foo));
    }

    // ======================

    @State(Scope.Thread)
    public static class MethodHandleForFieldState extends AbstractState {

        @Setup
        public void doSetup() throws Throwable {
            Field field = Foo.class.getDeclaredField("l");
            field.setAccessible(true);
            methodHandle = lookup().unreflectGetter(field);
        }

        MethodHandle methodHandle;
    }

    @Benchmark
    public void methodHandleForField(Blackhole blackhole, MethodHandleForFieldState s) throws Throwable {
        blackhole.consume(s.methodHandle.invoke(s.foo));
    }

    // ======================

    @State(Scope.Thread)
    public static class VarHandleState extends AbstractState {

        @Setup
        public void doSetup() throws Throwable {
            varHandle = privateLookupIn(SetterBenchmark.Foo.class, lookup()).findVarHandle(GetterBenchmark.Foo.class, "l", Long.class);
        }

        VarHandle varHandle;
    }

    @Benchmark
    public void varHandle(Blackhole blackhole, GetterBenchmark.VarHandleState s) {
        blackhole.consume(s.varHandle.get(s.foo));
    }

}
