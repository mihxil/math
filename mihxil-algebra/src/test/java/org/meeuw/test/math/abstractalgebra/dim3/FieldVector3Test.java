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
package org.meeuw.test.math.abstractalgebra.dim3;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.abstractalgebra.dim3.FieldVector3;
import org.meeuw.math.abstractalgebra.dim3.FieldVector3Space;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.abstractalgebra.reals.*;
import org.meeuw.math.abstractalgebra.test.VectorSpaceTheory;
import org.meeuw.math.abstractalgebra.test.WithScalarTheory;
import org.meeuw.math.exceptions.FieldInCompleteException;
import org.meeuw.math.abstractalgebra.test.ElementTheory;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.reals.BigDecimalElement.of;
import static org.meeuw.math.uncertainnumbers.CompareConfiguration.withLooseEquals;

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
        withLooseEquals(() -> {
            FieldVector3<BigDecimalElement> v = FieldVector3.of(valueOf(3), valueOf(-4), valueOf(0));
            assertThat(v.dividedBy(of(-2))).isEqualTo(FieldVector3.of(of(-1.5), of(2), of(0)));
        });
    }

    @Test
    public void string() {
        FieldVector3<BigDecimalElement> v = FieldVector3.of(of(3), of(-4), of(0));
        assertThat(v.toString()).isEqualTo("(3.0,-4.0,0.0)");
    }

    @Test
    public void testEquals() {
        FieldVector3<BigDecimalElement> v1 = FieldVector3.of(of(3), of(-4), of(0));
        FieldVector3<BigDecimalElement> v2 = FieldVector3.of(of(3), of(-4), of(0));
        assertThat(v1).isEqualTo(v2);
        v2 = v2.withY(of(-3));
        assertThat(v1).isNotEqualTo(v2);
        v2 = v2.withY(of(-4)).withZ(of(1));
        assertThat(v1).isNotEqualTo(v2);
        v2 = v2.withZ(of(0)).withX(of(4));
        assertThat(v1).isNotEqualTo(v2);
        v2 = v2.withX(of(3));
        assertThat(v1).isEqualTo(v2);
    }

    @SuppressWarnings({"ConstantConditions", "EqualsBetweenInconvertibleTypes"})
    @Test
    public void spaceEquals() {
        Assertions.assertThat(new FieldVector3Space<>(RealField.INSTANCE).equals(new FieldVector3Space<>(RealField.INSTANCE))).isTrue();
        assertThat(new FieldVector3Space<>(RealField.INSTANCE).hashCode()).isEqualTo(new FieldVector3Space<>(RealField.INSTANCE).hashCode());
        assertThat(new FieldVector3Space<>(RealField.INSTANCE).equals(null)).isFalse();
        assertThat(new FieldVector3Space<>(RationalNumbers.INSTANCE).equals(new FieldVector3Space<>(RealField.INSTANCE))).isFalse();
    }


    @Override
    public Arbitrary<RealNumber> scalars() {
        return Arbitraries.randomValue(r ->
            new RealNumber(r.nextDouble() * 10, Math.abs(r.nextDouble()))
        )
            .injectDuplicates(0.1)
            .dontShrink()
            .edgeCases((config) -> {
                config.add(RealNumber.ZERO);
                config.add(RealNumber.ONE);
            });
    }

    @Override
    public Arbitrary<? extends FieldVector3<RealNumber>> elements() {
        return Arbitraries.doubles()
            .between(-100, 100)
            .tuple3()
            .map((t) -> {
                return FieldVector3.of(t.get1(), t.get2(), t.get3());
            })
            .injectDuplicates(0.5)
            ;
    }
}
