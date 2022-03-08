package org.meeuw.math.abstractalgebra.test;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import net.jqwik.api.*;

import org.apache.logging.log4j.Logger;
import org.meeuw.math.Example;
import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.NotStreamable;
import org.meeuw.math.exceptions.ReciprocalException;
import org.meeuw.util.test.ElementTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.meeuw.math.abstractalgebra.ComparisonOperator.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AlgebraicStructureTheory<E extends AlgebraicElement<E>>  extends ElementTheory<E> {

    String STRUCTURE = "structure";

    @SuppressWarnings("unchecked")
    @Property()
    default void cardinality(
        @ForAll(STRUCTURE) AlgebraicStructure<E> s) {

        Logger log = getLogger();

        log.info("Testing {} ({})", s.toString(), s.getDescription());
        if (s.getCardinality().compareTo(Cardinality.ALEPH_1) < 0) {
            assertThat(s).isInstanceOf(Streamable.class);
            try {
                Streamable<E> streamAble = (Streamable<E>) s;
                if (s.getCardinality().compareTo(new Cardinality(10000)) < 0) {
                    assertThat(streamAble.stream()).doesNotHaveDuplicates().hasSize((int) s.getCardinality().getValue());
                } else {
                    assertThat(streamAble.stream().limit(10001)).doesNotHaveDuplicates().hasSizeGreaterThanOrEqualTo(10000);
                }
                streamAble.stream().limit(20).forEach(e -> log.info(e::toString));
                log.info("Skipping to 1000");
                streamAble.stream().skip(1000).limit(20).forEach(e -> log.info(e::toString));
                log.info("Skipping to 1000000");
                streamAble.stream().skip(1000000).limit(20).forEach(e ->
                    log.info(e::toString));
            } catch (NotStreamable ns) {
                log.warn(ns.getMessage());
            }
        } else {
            assertThat(s).isNotInstanceOf(Streamable.class);
        }
        log.info(() -> ("Cardinality of " + s  + ":" + s.getCardinality()));
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

    Map<AlgebraicStructure<?>, AtomicLong> COUNTS = new HashMap<>();

    @Property
    default void elementClass(
        @ForAll(STRUCTURE) AlgebraicStructure<E> s,
        @ForAll(ELEMENTS) E e
        ) {
        assertThat(e).isInstanceOf(s.getElementClass());
    }

    @Property
    default void operators(
        @ForAll(STRUCTURE) AlgebraicStructure<E> s,
        @ForAll(ELEMENTS) E e1,
        @ForAll(ELEMENTS) E e2) throws Throwable {

        AtomicLong count = COUNTS.computeIfAbsent(s, k -> new AtomicLong(0));
        int size = s.getSupportedOperators().size();
        for (Operator o : s.getSupportedOperators()) {
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
            } catch (ReciprocalException ae) {
                getLogger().info(o.stringify(e1, e2) + " -> " + ae.getMessage());
            } catch (Throwable ae) {
                if (ae.getCause() != null) {
                    throw ae.getCause();
                } else {
                    throw ae;
                }
            }

        }
    }

    @Property
    default void unaryOperators(
        @ForAll(STRUCTURE) AlgebraicStructure<E> s,
        @ForAll(ELEMENTS) E e1) throws Throwable {

        AtomicLong count = COUNTS.computeIfAbsent(s, k -> new AtomicLong(0));
        int size = s.getSupportedOperators().size();
        for (UnaryOperator o : s.getSupportedUnaryOperators()) {
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
            } catch (ReciprocalException ae) {
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
        @ForAll Operator o) {

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
        @ForAll UnaryOperator o) {

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

}
