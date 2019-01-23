package com.github.chrisgleissner.benchmarks.fieldaccess;

import com.github.chrisgleissner.benchmarks.AbstractBenchmark;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.github.chrisgleissner.benchmarks.Constants.OPERATIONS_PER_PER_INVOCATION;
import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodHandles.privateLookupIn;
import static java.lang.invoke.MethodType.methodType;
import static java.util.Collections.shuffle;

public class GetterBenchmark extends AbstractBenchmark {

    @Benchmark
    public Object direct(DirectState s) {
        return s.foo().getL();
    }

    @Benchmark
    public Object reflectionGetter(ReflectionGetterState s) throws InvocationTargetException, IllegalAccessException {
        return s.getter.invoke(s.foo());
    }

    @Benchmark
    public Object reflectionField(ReflectionFieldState s) throws IllegalAccessException {
        return s.field.get(s.foo());
    }

    @Benchmark
    public Object lambdaMetaFactoryForGetter(LambdaMetaFactoryForGetterState s) {
        return s.getterFunction.apply(s.foo());
    }

    @Benchmark
    public Object methodHandleForGetter(MethodHandleForGetterState s) throws Throwable {
        return s.methodHandle.invoke(s.foo());
    }

    @Benchmark
    public Object methodHandleForField(MethodHandleForFieldState s) throws Throwable {
        return s.methodHandle.invoke(s.foo());
    }

    @Benchmark
    public Object varHandle(GetterBenchmark.VarHandleState s) {
        return s.varHandle.get(s.foo());
    }

    @Data
    @AllArgsConstructor
    static class Foo {
        Long l;
    }

    @State(Scope.Thread)
    private static class AbstractState {
        Foo[] foos;
        int i;

        @Setup(Level.Iteration)
        public void setupIteration() {
            List<Foo> foosList = LongStream.range(0, OPERATIONS_PER_PER_INVOCATION).mapToObj(Foo::new).collect(Collectors.toList());
            shuffle(foosList);
            foos = foosList.toArray(Foo[]::new);
            i = 0;
        }

        Foo foo() {
            i++;
            if (i >= OPERATIONS_PER_PER_INVOCATION)
                i = 0;
            return foos[i];
        }
    }

    @State(Scope.Thread)
    public static class DirectState extends AbstractState {
    }

    @State(Scope.Thread)
    public static class ReflectionGetterState extends AbstractState {
        Method getter;

        @Setup
        public void doSetup() throws NoSuchMethodException {
            getter = Foo.class.getDeclaredMethod("getL");
            getter.setAccessible(true);
        }
    }

    @State(Scope.Thread)
    public static class ReflectionFieldState extends AbstractState {
        Field field;

        @Setup
        public void doSetup() throws NoSuchFieldException {
            field = Foo.class.getDeclaredField("l");
            field.setAccessible(true);
        }
    }

    @State(Scope.Thread)
    public static class LambdaMetaFactoryForGetterState extends AbstractState {
        Function<Foo, Long> getterFunction;

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
    }

    @State(Scope.Thread)
    public static class MethodHandleForGetterState extends AbstractState {
        MethodHandle methodHandle;

        @Setup
        public void doSetup() throws Throwable {
            Method method = Foo.class.getMethod("getL");
            methodHandle = lookup().unreflect(method);
        }
    }

    @State(Scope.Thread)
    public static class MethodHandleForFieldState extends AbstractState {
        MethodHandle methodHandle;

        @Setup
        public void doSetup() throws Throwable {
            Field field = Foo.class.getDeclaredField("l");
            field.setAccessible(true);
            methodHandle = lookup().unreflectGetter(field);
        }
    }

    @State(Scope.Thread)
    public static class VarHandleState extends AbstractState {
        VarHandle varHandle;

        @Setup
        public void doSetup() throws Throwable {
            varHandle = privateLookupIn(Foo.class, lookup()).findVarHandle(Foo.class, "l", Long.class);
        }
    }
}
