package com.github.chrisgleissner.benchmarks;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodHandles.privateLookupIn;
import static java.lang.invoke.MethodType.methodType;
import static org.assertj.core.api.Assertions.assertThat;

public class LambdaTest {

    @Test
    public void lambdaGetter() throws Throwable {
        Foo foo = new Foo();
        foo.setL(1L);

        MethodHandles.Lookup lookup = lookup();
        CallSite site = LambdaMetafactory.metafactory(lookup,
                "apply",
                methodType(Function.class),
                methodType(Object.class, Object.class),
                lookup.findVirtual(Foo.class, "getL", methodType(Long.class)),
                methodType(Long.class, Foo.class));
        Function getterFunction = (Function) site.getTarget().invokeExact();

        Object o = getterFunction.apply(foo);
        assertThat(o).isInstanceOf(Long.class);
        Long l = (Long) o;
        assertThat(l).isEqualTo(1L);
    }

    @Test
    public void lambdaSetter() throws Throwable {
        Foo foo = new Foo();

        MethodHandles.Lookup lookup = privateLookupIn(Foo.class, lookup());
        MethodHandle longSetterHandle = lookup.findVirtual(Foo.class, "setL", methodType(void.class, Long.class));
        CallSite site = LambdaMetafactory.metafactory(lookup,
                "accept",
                methodType(BiConsumer.class),
                methodType(void.class, Object.class, Object.class),
                longSetterHandle,
                longSetterHandle.type());
        BiConsumer setterFunction = (BiConsumer) site.getTarget().invokeExact();

        setterFunction.accept(foo, 1L);

        assertThat(foo.l).isEqualTo(1L);
    }

    @Test
    public void lambdaPrimitiveSetter() throws Throwable {
        Foo foo = new Foo();

        MethodHandles.Lookup lookup = lookup();
        MethodHandle longSetterHandle = lookup.findVirtual(Foo.class, "setLprim", methodType(void.class, long.class));
        CallSite site = LambdaMetafactory.metafactory(lookup,
                "accept",
                methodType(ObjLongConsumer.class),
                methodType(void.class, Object.class, long.class),
                longSetterHandle,
                longSetterHandle.type());
        ObjLongConsumer setterFunction = (ObjLongConsumer) site.getTarget().invokeExact();

        setterFunction.accept(foo, 1L);

        assertThat(foo.lprim).isEqualTo(1L);
    }

    @Test
    public void lambdaPrivateFieldWrite() throws Throwable {
        Foo foo = new Foo();
        VarHandle varHandle = privateLookupIn(Foo.class, lookup()).findVarHandle(Foo.class, "l", Long.class);
        varHandle.set(foo, 1L);
        assertThat(foo.l).isEqualTo(1L);
    }


    @Data
    public static class Foo {
        Long l;
        long lprim;
    }
}
