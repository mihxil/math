package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.meeuw.math.abstractalgebra.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AlgebraicStructureTheory<E extends AlgebraicElement<E>>  extends ElementTheory<E> {

    String STRUCTURE = "structure";

    @SuppressWarnings("rawtypes")
    @Property()
    default void cardinality(
        @ForAll(STRUCTURE) AlgebraicStructure<E> s) {

        Logger log = LogManager.getLogger(AlgebraicStructureTheory.class);
        if (s.getCardinality().compareTo(Cardinality.ALEPH_1) < 0) {
            assertThat(s).isInstanceOf(Streamable.class);
            if (s.getCardinality().compareTo(new Cardinality(10000)) < 0) {
                assertThat(((Streamable) s).stream()).doesNotHaveDuplicates().hasSize((int) s.getCardinality().getValue());
            } else {
                assertThat(((Streamable) s).stream().limit(10001)).doesNotHaveDuplicates().hasSizeGreaterThanOrEqualTo(10000);
            }
            ((Streamable<E>) s).stream().limit(500).forEach(e -> log.info(e::toString));
        } else {
            assertThat(s).isNotInstanceOf(Streamable.class);
        }
        log.info(() -> ("Cardinality of " + s  + ":" + s.getCardinality()));
    }

    @Property
    default void structureSameInstance(@ForAll(ELEMENTS) E e1, @ForAll(ELEMENT) E e2) {
        assertThat(e1.getStructure() == e2.getStructure()).isTrue();
    }

    @Provide
    default Arbitrary<AlgebraicStructure<E>> structure() {
        return Arbitraries.of(elements().sample().getStructure());
    }
}
