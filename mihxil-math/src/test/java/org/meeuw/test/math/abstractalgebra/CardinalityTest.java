package org.meeuw.test.math.abstractalgebra;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.abstractalgebra.Cardinality;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class CardinalityTest {

    @Test
    public void alephs() {
        Assertions.assertThat(Cardinality.ALEPH_0).isGreaterThan(new Cardinality(Long.MAX_VALUE));
        assertThat(Cardinality.ALEPH_1).isGreaterThan(Cardinality.ALEPH_0);
        assertThat(Cardinality.ALEPH_0.toString()).isEqualTo("א\u200E₀");
        assertThat(Cardinality.ALEPH_1.toString()).isEqualTo("א\u200E₁");
    }
    @Test
    public void test() {
        assertThat(new Cardinality(10)).isGreaterThan(new Cardinality(9));
        assertThat(new Cardinality(9)).isLessThan(new Cardinality(10));
        assertThat(new Cardinality(9)).isLessThan(Cardinality.ALEPH_0);

        assertThat(new Cardinality(10)).isEqualTo(new Cardinality(10));
        assertThat(new Cardinality(10).toString()).isEqualTo("10");
        assertThat(new Cardinality(10).getValue()).isEqualTo(10);

    }

    @Property
    public void hash(@ForAll("cardinalities") Cardinality c1, @ForAll("cardinalities") Cardinality c2) {
        if (c1.equals(c2)) {
            assertThat(c2).isEqualTo(c1);
            assertThat(c1.hashCode()).isEqualTo(c2.hashCode());
        }
        assertThat(c1.compareTo(c2)).isEqualTo(-1 * c2.compareTo(c1));
    }

    @Property
    public void moreEquals(@ForAll("cardinalities") Cardinality c1) {
        assertThat(c1).isNotEqualTo(null);
        assertThat(c1).isNotEqualTo(new Object());
    }



    @Provide
    public Arbitrary<Cardinality> cardinalities() {
        return Arbitraries.of(Cardinality.ALEPH_0, Cardinality.ALEPH_1, new Cardinality(0), new Cardinality(9), new Cardinality(10), new Cardinality(Long.MAX_VALUE));
    }

}
