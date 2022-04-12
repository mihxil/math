/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
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
