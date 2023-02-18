/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.abstractalgebra.test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntConsumer;

import net.jqwik.api.*;

import org.apache.logging.log4j.Logger;
import org.meeuw.math.Example;
import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.NotStreamable;
import org.meeuw.math.exceptions.OperationException;
import org.meeuw.math.operators.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.meeuw.math.operators.BasicComparisonOperator.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AlgebraicStructureTheory<E extends AlgebraicElement<E>>  extends ElementTheory<E> {

    String STRUCTURE = "structure";

    @SuppressWarnings("unchecked")
    @Property()
    default void cardinalityAndStreaming(
        @ForAll(STRUCTURE) AlgebraicStructure<E> s) {

        Logger log = getLogger();

        log.info("Testing {} ({})", s.toString(), s.getDescription());
        AtomicLong count = new AtomicLong(0);
        if (s.getCardinality().compareTo(Cardinality.ALEPH_1) < 0) {
            assertThat(s).isInstanceOf(Streamable.class);
            try {
                Streamable<E> streamAble = (Streamable<E>) s;
                if (s.getCardinality().compareTo(Cardinality.of(10000)) < 0) {
                    assertThat(streamAble.stream()).doesNotHaveDuplicates().hasSize(s.getCardinality().getValue().intValue());
                } else {
                    assertThat(streamAble.stream().limit(10001)).doesNotHaveDuplicates().hasSizeGreaterThanOrEqualTo(10000);
                }
                streamAble.stream().limit(1000).forEach(e -> {
                    if (count.incrementAndGet() < 20) {
                        log.info(e::toString);
                    }
                    }
                );
                IntConsumer  skipAndStream = (skip) ->
                    streamAble.stream().skip(skip).limit(20).forEach(e -> {
                        if (count.get() < skip) {
                            count.set(skip);
                            log.info("Skipping to {}", skip);
                        }
                        count.incrementAndGet();
                        log.info(e::toString);
                    }
                    );
                ;
                skipAndStream.accept(5000);
                skipAndStream.accept(1_000_000);

            } catch (NotStreamable ns) {
                log.warn(ns.getMessage());
            }
        } else {
            assertThat(s).isNotInstanceOf(Streamable.class);
        }
        log.info(() -> ("Cardinality of " + s  + ":" + s.getCardinality()));
        if (count.get() <= 1_000 && count.get() > 0) {
            assertThat(s.getCardinality().getValue().intValue()).isEqualTo(count.get());
        }
    }

    @Property
    default void nextRandom(@ForAll(STRUCTURE) AlgebraicStructure<E> s) {
        Random random = new Random();
        try {
            for (int i = 0; i < 10; i++) {
                getLogger().info("randomvalue {}: {}", i, s.nextRandom(random));
            }
        } catch (UnsupportedOperationException use) {
            getLogger().info(use.getMessage());
        }
    }

    @Property
    default void structureSameInstance(@ForAll(ELEMENTS) E e1, @ForAll(ELEMENT) E e2) {
        assertThat(e1.getStructure() == e2.getStructure()).isTrue();
        assertThat(e1.getStructure().equals(e2.getStructure())).isTrue();
    }

    @Property
    default void elementClass(
        @ForAll(STRUCTURE) AlgebraicStructure<E> s,
        @ForAll(ELEMENTS) E e
        ) {
        assertThat(e).isInstanceOf(s.getElementClass());
    }


    Map<AlgebraicStructure<?>, AtomicLong> COUNTS = new HashMap<>();
    Map<AlgebraicStructure<?>, AtomicLong> ERROR_COUNTS = new HashMap<>();

    @Property
    default void algebraicBinaryOperators(
        @ForAll(STRUCTURE) AlgebraicStructure<E> s,
        @ForAll(ELEMENTS) E e1,
        @ForAll(ELEMENTS) E e2) throws Throwable {

        AtomicLong count = COUNTS.computeIfAbsent(s, k -> new AtomicLong(0));
        AtomicLong error = ERROR_COUNTS.computeIfAbsent(s, k -> new AtomicLong(0));
        int size = s.getSupportedOperators().size();
        for (AlgebraicBinaryOperator o : s.getSupportedOperators()) {
            try {
                E result = o.apply(e1, e2);
                assertThat(result)
                    .withFailMessage("operator " + o + "(" + e1 + ", " + e2 + ") resulted null").isNotNull();
                assertThat(result.getStructure()).isSameAs(s);
                if (count.incrementAndGet() < (size * 3L)) { // show three example of every operator
                    getLogger().info(o.stringify(e1, e2) + " = " + result);
                } else {
                    getLogger().debug(o.stringify(e1, e2) + " = " + result);
                }
            } catch (OperationException ae) {
                if (error.incrementAndGet() < 3L) {
                    getLogger().info(o.stringify(e1, e2) + " -> " + ae.getMessage());
                } else {
                    getLogger().debug(o.stringify(e1, e2) + " -> " + ae.getMessage());
                }
                assertThat(o.isAlgebraicFor(e1))
                    .withFailMessage(ae.getClass().getName() + ":" + ae.getMessage() + " but %s is algebraic for %s %s", o, e1.getClass().getSimpleName(), e1).isFalse();

                Method methodFor = o.getMethodFor(e1);
                assertThat(Arrays.stream(methodFor.getExceptionTypes()).filter(e -> e.isInstance(ae)).findFirst()).withFailMessage(ae + " is not in throws of " + methodFor).isNotEmpty();

            } catch (Throwable ae) {
                if (ae.getCause() != null) {
                    throw ae.getCause();
                } else {
                    throw ae;
                }
            }

        }
    }

    Map<AlgebraicStructure<?>, AtomicLong> UCOUNTS = new HashMap<>();
    Map<AlgebraicStructure<?>, AtomicLong> ERROR_UCOUNTS = new HashMap<>();

    @Property
    default void algebraicUnaryOperators(
        @ForAll(STRUCTURE) AlgebraicStructure<E> s,
        @ForAll(ELEMENTS) E e1) throws Throwable {

        AtomicLong count = UCOUNTS.computeIfAbsent(s, k -> new AtomicLong(0));
        AtomicLong countError = ERROR_UCOUNTS.computeIfAbsent(s, k -> new AtomicLong(0));
        int size = s.getSupportedOperators().size();
        for (AlgebraicUnaryOperator o : s.getSupportedUnaryOperators()) {
            try {
                E result = o.apply(e1);
                assertThat(result)
                    .withFailMessage("operator " + o + "(" + e1 + ") resulted null").isNotNull();
                assertThat(result.getStructure()).withFailMessage("Result of operator " + o + " (" + e1 + ") has structure " + result.getClass() + " " + result.getStructure() + " which is not " + s).isSameAs(s);
                if (count.incrementAndGet() < (size * 3L)) { // show three example of every operator
                    getLogger().info(o.stringify(e1) + " = " + result);
                } else {
                    getLogger().debug(o.stringify(e1) + " = " + result);
                }
            } catch (OperationException ae) {
                //Assume.that(! o.isAlgebraicFor(e1));
                if (countError.incrementAndGet() < 3L) {
                    getLogger().info(o.stringify(e1) + " -> " + ae.getMessage());
                } else {
                    getLogger().debug(o.stringify(e1) + " -> " + ae.getMessage());
                }
            } catch (Throwable ae) {
                getLogger().info(o.stringify(e1) + " -> " + ae.getMessage());
                if (ae.getCause() != null) {
                    throw ae.getCause();
                } else {
                    throw ae;
                }
            }
        }
    }

    @Property
    default void functions(
        @ForAll(STRUCTURE) AlgebraicStructure<E> s,
        @ForAll(ELEMENTS) E e1) throws Throwable {

        for (GenericFunction o : s.getSupportedFunctions()) {
            try {
                Object result = o.apply(e1);
                getLogger().info(o.stringify(e1) + " = " + result);
                assertThat(result)
                    .withFailMessage("operator " + o + "(" + e1 + ") resulted null").isNotNull();
            } catch (OperationException ae) {
                getLogger().info(o.stringify(e1) + " -> " + ae.getMessage());
            } catch (Throwable ae) {
                getLogger().info(o.stringify(e1) + " -> " + ae.getMessage());
                if (ae.getCause() != null) {
                    throw ae.getCause();
                } else {
                    throw ae;
                }
            }

        }
    }

    @Provide
    default Arbitrary<AlgebraicStructure<? extends E>> structure() {
        return Arbitraries.of(elements().sample().getStructure());
    }

    @Property
    default void getComparisonOperators(@ForAll(STRUCTURE) AlgebraicStructure<E> struct) {
        if (Ordered.class.isAssignableFrom(struct.getElementClass())) {
            assertThat(struct.getSupportedComparisonOperators())
                .contains(LT, LTE, GT, GTE);
        }
        assertThat(struct.getSupportedComparisonOperators()).contains(EQ);
    }

    @Property
    default void examples(@ForAll(STRUCTURE) AlgebraicStructure<E> struct) {
        Example[] annotation = struct.getClass().getAnnotationsByType(Example.class);
        for (Example example : annotation) {
            assertThat(example.value()).isAssignableFrom(struct.getClass());
        }
    }

    @Property
    default void toString(@ForAll(STRUCTURE) AlgebraicStructure<E> struct) {
        getLogger().info(struct.getClass().getSimpleName() + " -> " + struct);
    }


    @Property
    default void staticOperators(
        @ForAll(STRUCTURE) AlgebraicStructure<E> structure,
        @ForAll BasicAlgebraicBinaryOperator o) {

        try {
            Method method = structure.getElementClass().getMethod(o.getMethod().getName(), o.getMethod().getParameterTypes());
            if (!structure.getSupportedOperators().contains(o)) {
                if (method.getAnnotation(NonAlgebraic.class) == null) {
                    fail("Not supported operation %s  is on %s", o, structure.getElementClass());
                } else {
                    getLogger().info("Not supported operation {} is on {}, but it is marked non algebraic", o, structure.getElementClass());

                }
            } else {
                getLogger().info("Ok {} on {}", o, structure.getElementClass());
            }
        } catch (NoSuchMethodException e) {
            if (structure.getSupportedOperators().contains(o)) {
                fail("Supported operation %s not on %s", o, structure.getElementClass());
            } else {
                getLogger().info("Ok {}: {}", o, e.getMessage());
            }
        }
    }

    @Property
    default void staticUnaryOperators(
        @ForAll(STRUCTURE) AlgebraicStructure<E> structure,
        @ForAll BasicAlgebraicUnaryOperator o) {

        try {
            Method method = structure.getElementClass().getMethod(o.getMethod().getName(), o.getMethod().getParameterTypes());

            if (!structure.getSupportedUnaryOperators().contains(o)) {
                if (method.getAnnotation(NonAlgebraic.class) == null) {
                    fail("Not supported operation %s  is on %s", o, structure.getElementClass());
                } else {
                    getLogger().info("Not supported operation {}  is on {}, but it is marked non algebraic", o, structure.getElementClass());
                }
            } else {
                getLogger().info("Ok {} on {}", o, structure.getElementClass());
            }
        } catch (NoSuchMethodException e) {
            if (structure.getSupportedUnaryOperators().contains(o)) {
                fail("Supported operation %s not on %s", o, structure.getElementClass());
            } else {
                getLogger().info("Ok {}: No such method {}", o, e.getMessage());
            }
        }
    }

    @Property
    default void castDirectly(@ForAll(ELEMENTS) E v) {
        for (AlgebraicStructure<? extends AlgebraicElement<?>> c : v.getStructure().getSuperGroups()) {
            Optional<? extends AlgebraicElement<?>> casted = v.castDirectly(c.getElementClass());
            assertThat(casted).isPresent();
            getLogger().info("{} -{}-> {}", v, c, casted.get());
        }
    }
    @Property
    default void cast(@ForAll(ELEMENTS) E v) {
        for (AlgebraicStructure<? extends AlgebraicElement<?>> c : v.getStructure().getAncestorGroups()) {
            AlgebraicElement<?> casted = v.cast(c.getElementClass());
            getLogger().info("{} -{}-> {}", v, c, casted);
        }
    }

    @Property
    default void eqMethod(@ForAll(ELEMENTS) E element) {
        Set<Method> eqMethods = new HashSet<>();
        for (Method m : element.getClass().getMethods()) {
            int modifiers = m.getModifiers();
            if (m.getName().equals("eq") && Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers) && m.getParameterCount() == 1) {
                eqMethods.add(m);
            }
        }
        assertThat(eqMethods).hasSize(1);
    }

}
