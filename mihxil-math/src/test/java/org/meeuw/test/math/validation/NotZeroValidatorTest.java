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
package org.meeuw.test.math.validation;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.meeuw.math.numbers.SignedNumber;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;
import org.meeuw.math.validation.NotZero;
import org.meeuw.math.validation.NotZeroValidator;

import static org.assertj.core.api.Assertions.assertThat;

class NotZeroValidatorTest {

    static class A {
        @NotZero
        final int number;

        A(int number) {
            this.number = number;
        }
    }

    static class B {
        @NotZero
        final BigDecimal number;

        B(BigDecimal number) {
            this.number = number;
        }
    }



    final ValidatorFactory factory = Validation
            .byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();



    @Test
    public void validate() {
        assertThat(factory.getValidator().validate(new A(16))).isEmpty();
        assertThat(factory.getValidator().validate(new A(0))).hasSize(1);

        assertThat(factory.getValidator().validate(new B(new BigDecimal("16")))).isEmpty();
        assertThat(factory.getValidator().validate(new B(new BigDecimal("0")))).hasSize(1);
    }


    @Test
    public void isValid() {
        NotZeroValidator validator = new NotZeroValidator();
        assertThat(validator.isValid(0, null)).isFalse();
        assertThat(validator.isValid(0d, null)).isFalse();
        assertThat(validator.isValid(0.1d, null)).isTrue();
        assertThat(validator.isValid(UncertainDoubleElement.exactly(0), null)).isFalse();
        assertThat(validator.isValid(UncertainDoubleElement.exactly(1), null)).isTrue();
        assertThat(validator.isValid(BigDecimal.valueOf(0), null)).isFalse();
        assertThat(validator.isValid(BigDecimal.valueOf(0.001), null)).isTrue();
        assertThat(validator.isValid(null, null)).isTrue();
        assertThat(validator.isValid("foobar", null)).isTrue();
        assertThat(validator.isValid("foobar", null)).isTrue();

        assertThat(validator.isValid(new MySignedNumber(0) , null)).isFalse();
        assertThat(validator.isValid(new MySignedNumber(1) , null)).isTrue();
        assertThat(validator.isValid(new MySignedNumber(-1) , null)).isTrue();



    }


    static class MySignedNumber implements SignedNumber<MySignedNumber> {
        final int signum;

        MySignedNumber(int signum) {
            this.signum = signum;
        }

        @Override
        public int compareTo(MySignedNumber o) {
            return o.signum - signum;
        }

        @Override
        public int signum() {
            return signum;
        }
    }




}
