package com.github.chrisgleissner.benchmarks;

import lombok.Value;
import org.junit.Test;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class LambdaTest {

    @Test

    public void lambdaGetter() throws Throwable {
        Foo foo = new Foo(1L);

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        CallSite site = LambdaMetafactory.metafactory(lookup,
                "apply",
                MethodType.methodType(Function.class),
                MethodType.methodType(Object.class, Object.class),
                lookup.findVirtual(Foo.class, "getL", MethodType.methodType(Long.class)),
                MethodType.methodType(Long.class, Foo.class));
        Function getterFunction = (Function) site.getTarget().invokeExact();

        Object o = getterFunction.apply(foo);

        assertThat(o).isInstanceOf(Long.class);
        Long l = (Long) o;
        assertThat(l).isEqualTo(1L);
    }

    @Value
    public static class Foo {
        Long l;
    }
}
