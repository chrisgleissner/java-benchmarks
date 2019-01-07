package com.github.chrisgleissner.benchmarks;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.invoke.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodHandles.privateLookupIn;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(1)
public class GetterBenchmark {

    @Data
    @AllArgsConstructor
    static class Foo {
        Long l;
    }

    @State(Scope.Thread)
    private static class AbstractState {

        @Setup
        public void setupIteration() {
            foo = new Foo(System.nanoTime());
        }

        Foo foo;
    }

    @State(Scope.Thread)
    public static class DirectState extends AbstractState {
    }

    @Benchmark
    public void direct(Blackhole blackhole, DirectState s) {
        blackhole.consume(s.foo.getL());
    }

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


    @State(Scope.Thread)
    public static class LambdaMetaFactoryState extends AbstractState {

        @Setup
        public void doSetup() throws Throwable {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            CallSite site = LambdaMetafactory.metafactory(lookup,
                    "apply",
                    MethodType.methodType(Function.class),
                    MethodType.methodType(Object.class, Object.class),
                    lookup.findVirtual(Foo.class, "getL", MethodType.methodType(Long.class)),
                    MethodType.methodType(Long.class, Foo.class));
            getterFunction = (Function) site.getTarget().invokeExact();
        }

        Function<Foo, Long> getterFunction;
    }

    @Benchmark
    public void lambdaMetaFactory(Blackhole blackhole, LambdaMetaFactoryState s) {
        blackhole.consume(s.getterFunction.apply(s.foo));
    }

    @State(Scope.Thread)
    public static class VarHandleState extends AbstractState {

        @Setup
        public void doSetup() throws Throwable {
            varHandle = privateLookupIn(SetterBenchmark.Foo.class, lookup()).findVarHandle(SetterBenchmark.Foo.class, "l", Long.class);
        }

        VarHandle varHandle;
    }

    @Benchmark
    public void varHandle(Blackhole blackhole, SetterBenchmark.VarHandleState s) {
        blackhole.consume(s.varHandle.get(s.foo));
    }

}
