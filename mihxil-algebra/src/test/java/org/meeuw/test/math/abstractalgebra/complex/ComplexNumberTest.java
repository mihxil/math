package org.meeuw.test.math.abstractalgebra.complex;

import net.jqwik.api.*;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.MultiplicativeSemiGroupElement;
import org.meeuw.math.abstractalgebra.complex.ComplexNumber;
import org.meeuw.math.abstractalgebra.complex.ComplexNumbers;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.abstractalgebra.test.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.reals.RealNumber.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class ComplexNumberTest implements
    CompleteFieldTheory<ComplexNumber>,
    MetricSpaceTheory<ComplexNumber, RealNumber>,
    WithScalarTheory<ComplexNumber, RealNumber> {

    static final ComplexNumbers structure = ComplexNumbers.INSTANCE;

    @Test
    public void isMultiplicativeSemiGroupElement() {
        ComplexNumber cn = new ComplexNumber(of(1), of(1));
        assertThat(cn).isInstanceOf(MultiplicativeSemiGroupElement.class);
    }


    @Override
    public Arbitrary<ComplexNumber> elements() {
        return Arbitraries.randomValue(
            (random) ->
                new ComplexNumber(
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
    public Arbitrary<RealNumber> scalars() {
        return Arbitraries.of(
            RealNumber.of(0), RealNumber.of(1), RealNumber.of(2), RealNumber.of(-1)
        );
    }

    @Test
    public void sqrt() {
        assertThat(ComplexNumber.of(RealNumber.of(-1)).sqrt()).isEqualTo(ComplexNumbers.INSTANCE.i());

        assertThat(ComplexNumber.of(RealNumber.of(0), RealNumber.of(-1)).sqrt().toString()).isEqualTo("0.70710678118655 - 0.70710678118655i");

    }
}
