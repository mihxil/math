package org.meeuw.math.abstractalgebra;

import net.jqwik.api.*;

import org.junit.platform.commons.logging.LoggerFactory;

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
        if (s.getCardinality().compareTo(Cardinality.ALEPH_1) < 0) {
            assertThat(s).isInstanceOf(Streamable.class);
            if (s.getCardinality().compareTo(new Cardinality(10000)) < 0) {
                assertThat(((Streamable) s).stream()).doesNotHaveDuplicates().hasSize((int) s.getCardinality().getValue());
            } else {
                assertThat(((Streamable) s).stream().limit(10001)).doesNotHaveDuplicates().hasSizeGreaterThanOrEqualTo(10000);
            }
        } else {
            assertThat(s).isNotInstanceOf(Streamable.class);
        }
        LoggerFactory.getLogger(AlgebraicStructureTheory.class).info(() -> ("Cardinality of " + s  + ":" + s.getCardinality()));
    }

    @Provide
    default Arbitrary<AlgebraicStructure<E>> structure() {
        return Arbitraries.of(elements().sample().getStructure());
    }
}
