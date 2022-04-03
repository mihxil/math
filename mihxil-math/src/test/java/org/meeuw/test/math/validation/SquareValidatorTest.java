package org.meeuw.test.math.validation;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.meeuw.math.numbers.Scalar;
import org.meeuw.math.validation.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SquareValidatorTest {

    static class A {
        @Square(value = 4)
        final int number;

        A(int number) {
            this.number = number;
        }
  }
    SquareValidator validator = new SquareValidator();

    ValidatorFactory factory = Validation
            .byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();


    @Test
    public void  test() {
        assertThat(validator.isValid(new String[]{"a", "b", "c", "d"}, null)).isTrue();

        assertThat(validator.isValid(new String[]{"a", "b", "c"}, null)).isFalse();

        assertThat(validator.isValid(new String[][]{new String[]{"a", "b"}, new String[]{"c", "d"}}, null)).isTrue();

        assertThat(validator.isValid(new String[][]{new String[]{"a", "b"}, new String[]{"c", "d", "e"}}, null)).isFalse();

        assertThat(validator.isValid(3, null)).isFalse();

        assertThat(validator.isValid(new Scalar.Number(4), null)).isTrue();

        assertThat(validator.isValid(Arrays.asList(1, 2, 3), null)).isFalse();

        assertThatThrownBy(() -> {
            validator.isValid("", null);
        }).isInstanceOf(IllegalArgumentException.class);

    }
    @Test
    public void validate() {
        assertThat(factory.getValidator().validate(new A(16))).isEmpty();
        assertThat(factory.getValidator().validate(new A(9))).hasSize(1);
    }

}
