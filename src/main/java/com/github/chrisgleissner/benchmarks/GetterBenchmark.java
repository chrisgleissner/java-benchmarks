package com.github.chrisgleissner.benchmarks;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 2)
@Measurement(iterations = 5, time = 5)
@Threads(4)
@Fork(2)
public class GetterBenchmark {

    @State(Scope.Thread)
    private static class AbstractState {
        @Setup(Level.Iteration)
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

        @Setup(Level.Trial)
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

        @Setup(Level.Trial)
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


    @Data
    @AllArgsConstructor
    static class Foo {
        Long l;
    }

    public static void main(String[] args) throws RunnerException {
        new Runner(new OptionsBuilder()
                .include(GetterBenchmark.class.getSimpleName())
                .forks(1)
                .build()).run();
    }
}
