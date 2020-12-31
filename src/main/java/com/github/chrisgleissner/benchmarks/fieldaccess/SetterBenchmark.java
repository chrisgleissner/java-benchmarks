package com.github.chrisgleissner.benchmarks.fieldaccess;

import com.github.chrisgleissner.benchmarks.AbstractBenchmark;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.openjdk.jmh.annotations.*;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.stream.LongStream;

import static com.github.chrisgleissner.benchmarks.Constants.OPERATIONS_PER_PER_INVOCATION;
import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodHandles.privateLookupIn;
import static java.lang.invoke.MethodType.methodType;
import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.toList;
import static java.util.concurrent.TimeUnit.*;

@Warmup(iterations = 50, time = 1, timeUnit = SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = SECONDS)
public class SetterBenchmark extends AbstractBenchmark {

    @Benchmark
    public Foo direct(DirectState s) {
        Foo foo = s.foo();
        foo.setL(s.l);
        return foo;
    }

    @Benchmark
    public Foo reflectionSetter(ReflectionSetterState s) throws InvocationTargetException, IllegalAccessException {
        Foo foo = s.foo();
        s.setter.invoke(foo, s.l);
        return foo;
    }

    @Benchmark
    public Foo reflectionField(ReflectionFieldState s) throws IllegalAccessException {
        Foo foo = s.foo();
        s.field.set(foo, s.l);
        return foo;
    }

    @Benchmark
    public Foo lambdaMetaFactoryForSetter(LambdaMetaFactoryForSetterState s) {
        Foo foo = s.foo();
        s.biConsumerFunction.accept(foo, s.l);
        return foo;
    }

    @Benchmark
    public Foo methodHandleForSetter(MethodHandleForSetterState s) throws Throwable {
        Foo foo = s.foo();
        s.methodHandle.invoke(foo, s.l);
        return foo;
    }

    @Benchmark
    public Foo methodHandleForField(MethodHandleForFieldState s) throws Throwable {
        Foo foo = s.foo();
        s.methodHandle.invoke(foo, s.l);
        return foo;
    }

    @Benchmark
    public Foo varHandle(VarHandleState s) {
        Foo foo = s.foo();
        s.varHandle.set(foo, s.l);
        return foo;
    }

    @Data
    @AllArgsConstructor
    public static class Foo {
        private Long l;
    }

    @State(Scope.Thread)
    private static class AbstractState {
        static Random random = new Random();
        int i;
        Foo[] foos;
        Long l;
        long primitiveL;

        @Setup(Level.Iteration)
        public void setupIteration() {
            List<Foo> foosList = LongStream.range(0, OPERATIONS_PER_PER_INVOCATION).mapToObj(Foo::new).collect(toList());
            shuffle(foosList);
            foos = foosList.toArray(Foo[]::new);
            i = 0;
            l = random.nextLong();
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
    public static class ReflectionSetterState extends AbstractState {
        Method setter;

        @Setup
        public void doSetup() throws NoSuchMethodException {
            setter = Foo.class.getDeclaredMethod("setL", Long.class);
            setter.setAccessible(true);
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
    public static class LambdaMetaFactoryForSetterState extends AbstractState {
        BiConsumer<Foo, Long> biConsumerFunction;

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
    }

    @State(Scope.Thread)
    public static class MethodHandleForSetterState extends AbstractState {
        MethodHandle methodHandle;

        @Setup
        public void doSetup() throws Throwable {
            Method method = Foo.class.getMethod("setL", Long.class);
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
            methodHandle = lookup().unreflectSetter(field);
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
