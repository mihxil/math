package org.meeuw.math.abstractalgebra.rationalnumbers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber.of;
import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers.INSTANCE;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */

class RationalNumberTest {

    @Test
    public void test() {
        assertThat(of(1, 4).times(of(1, 2))).isEqualTo(of(1, 8));

        assertThat(of(2, 5).times(of(1, 2))).isEqualTo(of(1, 5));

        assertThat(of(3, 7).reciprocal()).isEqualTo(of(7, 3));
        assertThat(of(3, 7).reciprocal().times(of(3, 7))).isEqualTo(INSTANCE.one());

        assertThat(of(3, 7).plus(INSTANCE.zero())).isEqualTo(of(6, 14));

        assertThat(of(10).dividedBy(of(3))).isEqualTo(of(10, 3));
        assertThat(of(1).dividedBy(of(3))).isEqualTo(of(1, 3));


    }

    @Test
    public void stream() {
        assertThat(INSTANCE.stream().limit(30).map(RationalNumber::toString)).containsExactly("0",
    "1",
    "-1",
    "2",
    "-2",
    "1/2",
    "-1/2",
    "3",
    "-3",
    "1/3",
    "-1/3",
    "4",
    "-4",
    "3/2",
    "-3/2",
    "2/3",
    "-2/3",
    "1/4",
    "-1/4",
    "5",
    "-5",
    "1/5",
    "-1/5",
    "6",
    "-6",
    "5/2",
    "-5/2",
    "4/3",
    "-4/3",
    "3/4");

    }


}
