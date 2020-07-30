package org.meeuw.math.abstractalgebra.integers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.integers.EvenIntegerElement.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class EvenIntegerElementTest {

    @Test
    public void test() {
        assertThatThrownBy(() -> of(1)).isInstanceOf(IllegalArgumentException.class);

        assertThat(of(2).times(of(4))).isEqualTo(of(8));
        assertThat(of(2).plus(of(4))).isEqualTo(of(6));
        assertThat(of(2).plus(of(4).negation())).isEqualTo(of(-2));

        assertThat(of(2).plus(of(2).structure().zero())).isEqualTo(of(2));


    }

}
