package org.meeuw.test.math.abstractalgebra;

import java.math.BigInteger;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.Cardinality;
import org.meeuw.math.exceptions.CardinalityException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class CardinalityTest {

    @Test
    public void alephs() {
        assertThat(Cardinality.ALEPH_0).isGreaterThan(Cardinality.of(Long.MAX_VALUE));
        assertThat(Cardinality.ALEPH_1).isGreaterThan(Cardinality.ALEPH_0);
        assertThat(Cardinality.ALEPH_0.toString()).isEqualTo("א\u200E₀");
        assertThat(Cardinality.ALEPH_1.toString()).isEqualTo("א\u200E₁");

        assertThatThrownBy(Cardinality.C::getValue).isInstanceOf(CardinalityException.class);
    }
    @Test
    public void test() {
        assertThat(Cardinality.of(10)).isGreaterThan(Cardinality.of(9));
        assertThat(Cardinality.of(9)).isLessThan( Cardinality.of(10));
        assertThat(Cardinality.of(9)).isLessThan(Cardinality.ALEPH_0);

        assertThat(Cardinality.of(10)).isEqualTo(Cardinality.of(10));
        assertThat(Cardinality.of(10).toString()).isEqualTo("10");
        assertThat(Cardinality.of(10).getValue()).isEqualTo(10);

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
        return Arbitraries.of(
            Cardinality.ALEPH_0, Cardinality.ALEPH_1, Cardinality.of(0), Cardinality.of(9), Cardinality.of(10), Cardinality.of(Long.MAX_VALUE), Cardinality.of(BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.TWO)));
    }

}
