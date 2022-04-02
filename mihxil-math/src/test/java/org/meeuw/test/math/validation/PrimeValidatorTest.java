package org.meeuw.test.math.validation;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.Test;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.meeuw.math.validation.Prime;

import static org.assertj.core.api.Assertions.assertThat;

class PrimeValidatorTest {

    static class A {
        @Prime
        final int number;

        A(int number) {
            this.number = number;
        }
    }
  ValidatorFactory factory = Validation
            .byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();

    @Test
    public void  test() {
        assertThat(factory.getValidator().validate(new A(3))).isEmpty();
        assertThat(factory.getValidator().validate(new A(4))).hasSize(1);
    }

}
