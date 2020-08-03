package org.meeuw.math.abstractalgebra;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AlgebraicStructureTheory<F extends AlgebraicElement<F>>  {

    String ELEMENT = "element";
    String ELEMENTS = "elements";


    @Property()
    default void cardinality(
        @ForAll(ELEMENTS) F v) {
        if (v.structure().cardinality().compareTo(Cardinality.ALEPH_1) < 0) {
            assertThat(v.structure()).isInstanceOf(Streamable.class);
            if (v.structure().cardinality().compareTo(new Cardinality(10000)) < 0) {
                assertThat(((Streamable) v.structure()).stream().count()).isEqualTo(v.structure().cardinality().getValue());
            } else {
                assertThat(((Streamable) v.structure()).stream().limit(10001)).hasSizeGreaterThanOrEqualTo(10000);
            }
        } else {
            assertThat(v.structure()).isNotInstanceOf(Streamable.class);
        }
    }

    @Provide
    Arbitrary<F> elements();

    @Provide
    default Arbitrary<F> element() {
        return Arbitraries.of(elements().sample());
    }
}
