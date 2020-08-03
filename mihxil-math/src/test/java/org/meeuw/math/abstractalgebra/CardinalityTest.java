package org.meeuw.math.abstractalgebra;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
class CardinalityTest {

    @Test
    public void test() {
        assertThat(Cardinality.ALEPH_0).isGreaterThan(new Cardinality(Long.MAX_VALUE));
        assertThat(Cardinality.ALEPH_1).isGreaterThan(Cardinality.ALEPH_0);
        assertThat(new Cardinality(10)).isGreaterThan(new Cardinality(9));
    }

}
