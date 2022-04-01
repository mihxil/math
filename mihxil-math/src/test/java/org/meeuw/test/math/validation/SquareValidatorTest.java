package org.meeuw.test.math.validation;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import org.meeuw.math.numbers.Scalar;
import org.meeuw.math.validation.SquareValidator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SquareValidatorTest {


    SquareValidator validator = new SquareValidator();
    @Test
    public void  test() {
        assertThat(validator.isValid(new String[]{"a", "b", "c", "d"}, null)).isTrue();

        assertThat(validator.isValid(new String[]{"a", "b", "c"}, null)).isFalse();

        assertThat(validator.isValid(3, null)).isFalse();

        assertThat(validator.isValid(new Scalar.Number(4), null)).isTrue();

        assertThat(validator.isValid(Arrays.asList(1, 2, 3), null)).isFalse();

        assertThatThrownBy(() -> {
            validator.isValid("", null);
        }).isInstanceOf(IllegalArgumentException.class);



    }

}
