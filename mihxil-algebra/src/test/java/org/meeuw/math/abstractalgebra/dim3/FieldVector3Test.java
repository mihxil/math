package org.meeuw.math.abstractalgebra.dim3;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.junit.jupiter.api.Test;
import org.meeuw.math.exceptions.FieldInCompleteException;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.abstractalgebra.reals.*;
import org.meeuw.math.abstractalgebra.test.VectorSpaceTheory;
import org.meeuw.math.abstractalgebra.test.WithScalarTheory;
import org.meeuw.util.test.ElementTheory;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.reals.BigDecimalElement.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class FieldVector3Test  implements
    WithScalarTheory<FieldVector3<RealNumber>, RealNumber>,
    VectorSpaceTheory<FieldVector3<RealNumber>, RealNumber>,
    ElementTheory<FieldVector3<RealNumber>> {

    @Test
    public void abs() {
        FieldVector3<RealNumber> v = FieldVector3.of(3, -4, 0);
        assertThat(v.abs()).isEqualTo(new RealNumber(5, 0));
    }

    @Test
    public void absOfRational() {
        FieldVector3<RationalNumber> v = FieldVector3.of(RationalNumber.of(3), RationalNumber.of(-4), RationalNumber.of(0));
        assertThatThrownBy(v::abs).isInstanceOf(FieldInCompleteException.class);
    }

    @Test
    public void times() {
        FieldVector3<RationalNumber> v = FieldVector3.of(RationalNumber.of(3), RationalNumber.of(-4), RationalNumber.of(0));
        assertThat(v.times(RationalNumber.of(3, 2))).isEqualTo(FieldVector3.of(RationalNumber.of(9, 2), RationalNumber.of(-6), RationalNumber.ZERO));
    }

    @Test
    public void dividedBy() {
        FieldVector3<BigDecimalElement> v = FieldVector3.of(valueOf(3), valueOf(-4), valueOf(0));
        assertThat(v.dividedBy(of(-2))).isEqualTo(FieldVector3.of(of(-1.5), of(2), of(0)));
    }

    @Test
    public void string() {
        FieldVector3<BigDecimalElement> v = FieldVector3.of(of(3), of(-4), of(0));
        assertThat(v.toString()).isEqualTo("(3.0,-4.0,0.0)");
    }

    @SuppressWarnings({"ConstantConditions", "EqualsBetweenInconvertibleTypes"})
    @Test
    public void spaceEquals() {
        assertThat(new FieldVector3Space<>(RealField.INSTANCE).equals(new FieldVector3Space<>(RealField.INSTANCE))).isTrue();
        assertThat(new FieldVector3Space<>(RealField.INSTANCE).hashCode()).isEqualTo(new FieldVector3Space<>(RealField.INSTANCE).hashCode());
        assertThat(new FieldVector3Space<>(RealField.INSTANCE).equals(null)).isFalse();
        assertThat(new FieldVector3Space<>(RationalNumbers.INSTANCE).equals(new FieldVector3Space<>(RealField.INSTANCE))).isFalse();
    }


    @Override
    public Arbitrary<RealNumber> scalars() {
        return Arbitraries.randomValue(r ->
            new RealNumber(r.nextDouble() * 10, Math.abs(r.nextDouble()))
        ).injectDuplicates(0.1)
            .edgeCases((config) -> {
                config.add(RealNumber.ZERO);
                config.add(RealNumber.ONE);
            });
    }

    @Override
    public Arbitrary<? extends FieldVector3<RealNumber>> elements() {
        return Arbitraries.randomValue(r ->
            FieldVector3.of(r.nextDouble() * 100, r.nextDouble() * 100, r.nextDouble()* 100)
        ).injectDuplicates(0.1);
    }
}
