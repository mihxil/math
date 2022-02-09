package org.meeuw.util.test;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.checkerframework.checker.nullness.qual.PolyNull;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


/**
 * A test can implement this interface to and test generic property of a utility class
 */
@TestInstance(PER_CLASS)
public interface StaticUtilitiesTest {


    @ParameterizedTest
    @MethodSource("polyNullMethods")
    default void polyNull(Method m) throws InvocationTargetException, IllegalAccessException {
        Object[] parameters = new Object[m.getParameterCount()];
        for (int i = 0 ; i < m.getParameterCount(); i++) {
            if (boolean.class.equals(m.getParameterTypes()[i])) {
                parameters[i] = Boolean.FALSE;
            }
            if (int.class.equals(m.getParameterTypes()[i])) {
                parameters[i] = 10;
            }
        }
        Object invoke = m.invoke(null, parameters);
        assertThat(invoke).isNull();
    }

    @Test
    default void notInstantiatable() {
        assertThat(testClass().getConstructors()).isEmpty();
    }

    default Stream<Method> polyNullMethods() {
        return polyNullMethods(testClass());
    }

    Class<?> testClass();

    static Stream<Method> polyNullMethods(Class<?> clazz) {
        List<Method> s = Arrays.stream(clazz.getDeclaredMethods())
            .filter(m -> Modifier.isStatic(m.getModifiers()) && Modifier.isPublic(m.getModifiers()))
            .filter(m -> m.getAnnotatedReturnType().getAnnotation(PolyNull.class) != null)
            .toList();
        assumeThat(s).isNotEmpty();
        return s.stream();
    }
}
