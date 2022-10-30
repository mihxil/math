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
package org.meeuw.test.math.abstractalgebra.linear;

import jakarta.validation.*;
import jakarta.validation.executable.ExecutableValidator;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Method;
import java.util.Set;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.meeuw.math.abstractalgebra.RingElement;
import org.meeuw.math.abstractalgebra.integers.IntegerElement;
import org.meeuw.math.abstractalgebra.integers.Integers;
import org.meeuw.math.abstractalgebra.linear.SpecialLinearGroup;
import org.meeuw.math.abstractalgebra.linear.SpecialLinearMatrix;
import org.meeuw.math.abstractalgebra.test.MultiplicativeGroupTheory;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.exceptions.NotASquareException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Log4j2
public class SpecialLinearGroupTest {

    @Test
    void of() {
        SpecialLinearMatrix<IntegerElement> e = SpecialLinearMatrix.of(
            IntegerElement.of(1), IntegerElement.of(2),
            IntegerElement.of(0), IntegerElement.of(-1)
        );
        log.info("{}", e);
        assertThat(e.getStructure().getDimension()).isEqualTo(2);

        IntegerElement det = e.determinant();
        assertThat(det.getValue()).isEqualTo(-1);

        assertThat(e.getStructure().toString()).isEqualTo("SL₂(ℤ)");

        assertThatThrownBy(() -> {
            SpecialLinearMatrix.of(
                IntegerElement.of(1), IntegerElement.of(2),
                IntegerElement.of(0)
            );
        }).isInstanceOf(NotASquareException.class).hasMessage("3 is not a square");

        assertThatThrownBy(() -> {
            SpecialLinearMatrix.of(
                IntegerElement.of(1), IntegerElement.of(2),
                IntegerElement.of(-1), IntegerElement.of(-2)
            );
        }).isInstanceOf(InvalidElementCreationException.class).hasMessage("The matrix ((1,2),(-1,-2)) is not invertible. Determinant: 0");

        assertThatThrownBy(() -> {
            SpecialLinearMatrix.of(
                IntegerElement.of(1), IntegerElement.of(2),
                IntegerElement.of(3), IntegerElement.of(4)
            );
        }).isInstanceOf(InvalidElementCreationException.class).hasMessage("The matrix ((1,2),(3,4)) is not invertible. Determinant: -2");
    }

    @Test
    void sl_n() {
        assertThatThrownBy(() -> {
            SpecialLinearGroup.SL_N.newElement(
                IntegerElement.of(1), IntegerElement.of(2),
                IntegerElement.of(0), IntegerElement.of(-1)
            );
        }).isInstanceOf(InvalidElementCreationException.class).hasMessage("Dimension of matrix is not 3 (but 2)");

        assertThatThrownBy(() -> {
            SpecialLinearGroup.SL_N.newElement(
                IntegerElement.of(1), IntegerElement.of(2), IntegerElement.of(3),
                IntegerElement.of(2), IntegerElement.of(4), IntegerElement.of(6),
                IntegerElement.of(0), IntegerElement.of(4), IntegerElement.of(6)
            );
        }).isInstanceOf(InvalidElementCreationException.class).hasMessage("The matrix ((1,2,3),(2,4,6),(0,4,6)) is not invertible. Its determinant is zero");
    }

    //@Test  //TODO
    public void stream() {
        SpecialLinearGroup<IntegerElement> e = SpecialLinearGroup.of(3, Integers.INSTANCE);
        e.stream().limit(100).forEach(m -> {
            IntegerElement det = m.determinant();
            log.info("det({})={}", m, det);
            assertThat(m.determinant().abs()).isIn(IntegerElement.ONE);
        });
    }

    @Test
    public void validation() throws NoSuchMethodException {

        ValidatorFactory factory = Validation
            .byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();
        System.err.println("" + factory);;
        ExecutableValidator executableValidator = factory.getValidator().forExecutables();
        Method method = SpecialLinearGroup.class
            .getMethod("newElement", RingElement[].class);
        Object[] parameterValues = { new IntegerElement[] {IntegerElement.of(1), IntegerElement.of(2) }};
        //assertThat(new SquareValidator().isValid(parameterValues[0], null)).isFalse();


        Set<ConstraintViolation<SpecialLinearGroup<IntegerElement>>> violations
            = executableValidator.validateParameters(
            SpecialLinearGroup.SL_N, method, parameterValues);
        log.info("{}", violations);
        assertThat(violations).hasSize(1);

    }

    public static class IntegersTest implements
        MultiplicativeGroupTheory<SpecialLinearMatrix<IntegerElement>> {


        @Property
        public void determinant(@ForAll(ELEMENTS) SpecialLinearMatrix<IntegerElement> matrix) {
            log.info("det({}) = {}", matrix, matrix.determinant());
            assertThat(matrix.determinant()).isIn(IntegerElement.ONE, IntegerElement.ONE.negation());

        }
        @Override
        public Arbitrary<SpecialLinearMatrix<IntegerElement>> elements() {
            SpecialLinearGroup<IntegerElement> e = SpecialLinearGroup.of(3, Integers.INSTANCE);
            return Arbitraries.randomValue(e::nextRandom);
        }
    }

}
