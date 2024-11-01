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
package org.meeuw.test.math.abstractalgebra.dim2;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.abstractalgebra.dim2.FieldVector2;
import org.meeuw.math.abstractalgebra.dim2.FieldVector2Space;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.abstractalgebra.reals.*;
import org.meeuw.math.exceptions.FieldIncompleteException;
import org.meeuw.theories.abstractalgebra.*;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.reals.BigDecimalElement.of;
import static org.meeuw.math.uncertainnumbers.CompareConfiguration.withLooseEquals;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class FieldVector2Test implements
    WithScalarTheory<FieldVector2<RealNumber>, RealNumber>,
    VectorSpaceTheory<FieldVector2<RealNumber>, RealNumber>,
    ElementTheory<FieldVector2<RealNumber>> {

    @Test
    public void abs() {
        FieldVector2<RealNumber> v = FieldVector2.of(3, -4);
        assertThat(v.abs()).isEqualTo(new RealNumber(5, 0));
    }

    @Test
    public void absOfRational() {
        FieldVector2<RationalNumber> v = FieldVector2.of(RationalNumber.of(3), RationalNumber.of(-4));
        assertThatThrownBy(v::abs).isInstanceOf(FieldIncompleteException.class);
    }

    @Test
    public void times() {
        FieldVector2<RationalNumber> v = FieldVector2.of(RationalNumber.of(3), RationalNumber.of(-4));
        assertThat(v.times(RationalNumber.of(3, 2))).isEqualTo(FieldVector2.of(RationalNumber.of(9, 2), RationalNumber.of(-6)));
    }

    @Test
    public void dividedBy() {
        withLooseEquals(() -> {
            FieldVector2<BigDecimalElement> v = FieldVector2.of(valueOf(3), valueOf(-4));
            assertThat(v.dividedBy(of(-2))).isEqualTo(FieldVector2.of(of(-1.5), of(2)));
        });
    }

    @Test
    public void string() {
        FieldVector2<BigDecimalElement> v = FieldVector2.of(of(3), of(-4));
        assertThat(v.toString()).isEqualTo("(3.0,-4.0)");
    }

    @Test
    public void testEquals() {
        FieldVector2<BigDecimalElement> v1 = FieldVector2.of(of(3), of(-4));
        FieldVector2<BigDecimalElement> v2 = FieldVector2.of(of(3), of(-4));
        assertThat(v1).isEqualTo(v2);
        v2 = v2.withY(of(-3));
        assertThat(v1).isNotEqualTo(v2);
        v2 = v2.withY(of(-4));
        assertThat(v1).isEqualTo(v2);
    }

    @SuppressWarnings({"ConstantConditions", "EqualsBetweenInconvertibleTypes"})
    @Test
    public void spaceEquals() {
        Assertions.assertThat(new FieldVector2Space<>(RealField.INSTANCE).equals(new FieldVector2Space<>(RealField.INSTANCE))).isTrue();
        assertThat(new FieldVector2Space<>(RealField.INSTANCE).hashCode()).isEqualTo(new FieldVector2Space<>(RealField.INSTANCE).hashCode());
        assertThat(new FieldVector2Space<>(RealField.INSTANCE).equals(null)).isFalse();
        assertThat(new FieldVector2Space<>(RationalNumbers.INSTANCE).equals(new FieldVector2Space<>(RealField.INSTANCE))).isFalse();
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
    public Arbitrary<FieldVector2<RealNumber>> elements() {
        return Arbitraries.doubles()
            .between(-100, 100)
            .tuple2()
            .map((t) -> {
                return FieldVector2.of(t.get1(), t.get2());
            })
            .injectDuplicates(0.5)
            ;
    }
}
