package org.meeuw.math.abstractalgebra.complex;

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.abstractalgebra.test.*;

import static org.meeuw.math.abstractalgebra.reals.RealNumber.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class ComplexNumberTest implements
    FieldTheory<ComplexNumber>,
    MetricSpaceTheory<ComplexNumber, RealNumber>,
    WithScalarTheory<ComplexNumber, RealNumber> {

    static final ComplexNumbers structure = ComplexNumbers.INSTANCE;


    @Override
    public Arbitrary<? extends ComplexNumber> elements() {
        return Arbitraries.randomValue(
            (random) ->
                new ComplexNumber(
                    of(200 * random.nextDouble() - 100),
                    of(200 * random.nextDouble() - 100)))
            .injectDuplicates(0.1)
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
}
