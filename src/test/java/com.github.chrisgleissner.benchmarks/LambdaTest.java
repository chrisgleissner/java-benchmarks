package com.github.chrisgleissner.benchmarks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.junit.Test;

import java.lang.invoke.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.lang.invoke.MethodType.methodType;
import static org.assertj.core.api.Assertions.assertThat;

public class LambdaTest {

    @Test
    public void lambdaGetter() throws Throwable {
        Foo foo = new Foo(1L);

        MethodHandles.Lookup lookup = MethodHandles.lookup();
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

        MethodHandles.Lookup lookup = MethodHandles.lookup();
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

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class Foo {
        Long l;
    }
}
