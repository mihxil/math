package org.meeuw.test.math.abstractalgebra.linear;

import jakarta.validation.*;
import jakarta.validation.executable.ExecutableValidator;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Method;
import java.util.Set;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.RingElement;
import org.meeuw.math.abstractalgebra.integers.IntegerElement;
import org.meeuw.math.abstractalgebra.integers.Integers;
import org.meeuw.math.abstractalgebra.linear.SpecialLinearGroup;
import org.meeuw.math.abstractalgebra.linear.SpecialLinearMatrix;
import org.meeuw.math.abstractalgebra.test.MultiplicativeGroupTheory;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.exceptions.NotASquareException;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;


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
        }).isInstanceOf(NotASquareException.class);

        assertThatThrownBy(() -> {
            SpecialLinearGroup.SL_N.newElement(
                IntegerElement.of(1), IntegerElement.of(2),
                IntegerElement.of(0), IntegerElement.of(-1)
            );
        }).isInstanceOf(InvalidElementCreationException.class);

        assertThatThrownBy(() -> {
            SpecialLinearGroup.SL_N.newElement(
                IntegerElement.of(1), IntegerElement.of(2), IntegerElement.of(3),
                IntegerElement.of(2), IntegerElement.of(4), IntegerElement.of(6),
                IntegerElement.of(0), IntegerElement.of(4), IntegerElement.of(6)
            );
        }).isInstanceOf(InvalidElementCreationException.class);
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
        ExecutableValidator executableValidator = factory.getValidator().forExecutables();
        Method method = SpecialLinearGroup.class
            .getMethod("newElement", RingElement[].class);
        Object[] parameterValues = { new IntegerElement[] {IntegerElement.of(1), IntegerElement.of(2) }};
        Set<ConstraintViolation<SpecialLinearGroup>> violations
            = executableValidator.validateParameters(SpecialLinearGroup.SL_N, method, parameterValues);
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
