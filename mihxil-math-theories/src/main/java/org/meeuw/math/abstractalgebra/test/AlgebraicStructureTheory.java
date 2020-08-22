package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.Logger;
import org.meeuw.math.abstractalgebra.*;

import static org.assertj.core.api.Assertions.assertThat;

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
        if (s.getCardinality().compareTo(Cardinality.ALEPH_1) < 0) {
            assertThat(s).isInstanceOf(Streamable.class);
            Streamable<E> streamAble = (Streamable<E>) s;
            if (s.getCardinality().compareTo(new Cardinality(10000)) < 0) {
                assertThat(streamAble.stream()).doesNotHaveDuplicates().hasSize((int) s.getCardinality().getValue());
            } else {
                assertThat(streamAble.stream().limit(10001)).doesNotHaveDuplicates().hasSizeGreaterThanOrEqualTo(10000);
            }
            streamAble.stream().limit(100).forEach(e -> log.info(e::toString));
            log.info("Skipping to 1000");
            streamAble.stream().skip(1000).limit(100).forEach(e -> log.info(e::toString));
            log.info("Skipping to 1000000");
            streamAble.stream().skip(1000000).limit(100).forEach(e -> log.info(e::toString));
        } else {
            assertThat(s).isNotInstanceOf(Streamable.class);
        }
        log.info(() -> ("Cardinality of " + s  + ":" + s.getCardinality()));
    }

    @Property
    default void structureSameInstance(@ForAll(ELEMENTS) E e1, @ForAll(ELEMENT) E e2) {
        assertThat(e1.getStructure() == e2.getStructure()).isTrue();
    }

    @Property
    default void supportedOperators(@ForAll(STRUCTURE) AlgebraicStructure<E> s) {
        for (Operator o : s.getSupportedOperators()) {
            assertThat(s.supports(o)).isTrue();
        }
    }

    Map<AlgebraicStructure<?>, AtomicLong> counts = new HashMap<>();

    @Property
    default void operators(
        @ForAll(STRUCTURE) AlgebraicStructure<E> s,
        @ForAll(ELEMENTS) E e1,
        @ForAll(ELEMENTS) E e2) throws Throwable {
        AtomicLong count = counts.computeIfAbsent(s, k -> new AtomicLong(0));
        for (Operator o : s.getSupportedOperators()) {
            try {
                E result = o.apply(e1, e2);
                assertThat(result.getStructure()).isSameAs(s);
                if (count.incrementAndGet() < 200) {
                    getLogger().info(o.stringify(e1, e2) + " = " + result);
                }
            } catch (Throwable ae) {
                if (ae.getCause() instanceof ArithmeticException) {
                    getLogger().info(o.stringify(e1, e2) + " -> " + ae.getMessage());
                } else {
                    throw ae.getCause();
                }
            }

        }
    }



    @Provide
    default Arbitrary<AlgebraicStructure<? extends E>> structure() {
        return Arbitraries.of(elements().sample().getStructure());
    }
}
