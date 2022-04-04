package org.meeuw.test.math.abstractalgebra.complex;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.MultiplicativeSemiGroupElement;
import org.meeuw.math.abstractalgebra.complex.BigComplexNumber;
import org.meeuw.math.abstractalgebra.complex.BigComplexNumbers;
import org.meeuw.math.abstractalgebra.reals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.test.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.reals.BigDecimalElement.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
class BigComplexNumberTest implements
    CompleteFieldTheory<BigComplexNumber>,
    MetricSpaceTheory<BigComplexNumber, BigDecimalElement>,
    WithScalarTheory<BigComplexNumber, BigDecimalElement> {

    static final BigComplexNumbers structure = BigComplexNumbers.INSTANCE;

    @Test
    public void isMultiplicativeSemiGroupElement() {
        BigComplexNumber cn = new BigComplexNumber(of(1), of(1));
        assertThat(cn).isInstanceOf(MultiplicativeSemiGroupElement.class);
    }


    @Override
    public Arbitrary<BigComplexNumber> elements() {
        return Arbitraries.randomValue(
            (random) ->
                new BigComplexNumber(
                    of(200 * random.nextDouble() - 100),
                    of(200 * random.nextDouble() - 100)))
            .injectDuplicates(0.1)
            .dontShrink()
            .edgeCases(config -> {
                config.add(structure.i());
                config.add(structure.one());
                config.add(structure.zero());
            });
    }

    @Override
    public Arbitrary<BigDecimalElement> scalars() {
        return Arbitraries.of(
            of(0),
            of(1), of(2), of(-1)
        );
    }

}