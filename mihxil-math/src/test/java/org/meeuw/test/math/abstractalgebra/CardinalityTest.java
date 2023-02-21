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
package org.meeuw.test.math.abstractalgebra;

import java.math.BigInteger;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.Cardinality;
import org.meeuw.math.exceptions.CardinalityNotFiniteException;

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
        assertThat(Cardinality.ALEPH_0).isEqualTo(Cardinality.ALEPH_0.times(Cardinality.of(3)));
        assertThat(Cardinality.ALEPH_0).isEqualTo(Cardinality.ALEPH_0.pow(3));


        assertThat(Cardinality.ALEPH_1).isGreaterThan(Cardinality.ALEPH_0);
        assertThat(Cardinality.ALEPH_0.toString()).isEqualTo("א\u200E₀");
        assertThat(Cardinality.ALEPH_0.pow(3).toString()).isEqualTo("א\u200E₀");
        assertThat(Cardinality.ALEPH_0.times(Cardinality.of(2)).toString()).isEqualTo("א\u200E₀");
        assertThat(Cardinality.ALEPH_1.toString()).isEqualTo("א\u200E₁");

        assertThatThrownBy(Cardinality.C::getValue).isInstanceOf(CardinalityNotFiniteException.class);
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
