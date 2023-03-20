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

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.dim3.FieldVector3;
import org.meeuw.math.abstractalgebra.dim3.Rotation;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.theories.abstractalgebra.MultiplicativeGroupTheory;
import org.meeuw.math.text.TextUtils;

import static java.lang.Math.PI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.dim3.FieldVector3.of;
import static org.meeuw.math.abstractalgebra.dim3.Rotation.*;
import static org.meeuw.math.uncertainnumbers.CompareConfiguration.withLooseEquals;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class RotationTest implements MultiplicativeGroupTheory<Rotation> {

    @Test
    public void rotx() {
        withLooseEquals(() -> {
            Rotation half = Rx(PI);
            Rotation quarter = Rx(PI / 2);
            FieldVector3<RealNumber> v = of(0, 1, 0);
            FieldVector3<RealNumber> rotated = half.apply(v);
            assertThat(rotated).isEqualTo(of(0, -1, 0));

            FieldVector3<RealNumber> rotatedQuarter = quarter.apply(v);
            assertThat(rotatedQuarter).isEqualTo(of(0, 0, 1));

            RealNumber det = half.asMatrix().determinant();
            log.info("Determinant of {}: {}", half.asMatrix(), det);
            Rotation reciprocal = half.reciprocal();
            log.info("{}", reciprocal);

            Rotation pow = half.pow(-1);
            log.info("{}", pow);
        });
    }

    @Test
    public void roty() {
        withLooseEquals(() -> {
            Rotation y = Ry(PI);
            FieldVector3<RealNumber> v = of(1, 0, 0);
            FieldVector3<RealNumber> rotated = y.apply(v);
            assertThat(rotated).isEqualTo(of(-1, 0, 0));
        });
    }

    @Test
    public void rotz() {
        withLooseEquals(() -> {
            Rotation z = Rotation.Rz(PI);
            FieldVector3<RealNumber> v = of(0, 1, 0);
            FieldVector3<RealNumber> rotated = z.apply(v);
            assertThat(rotated).isEqualTo(of(0, -1, 0));
        });
    }

    @Property
    public void determinantShouldBeOne(@ForAll(ELEMENTS) Rotation r) {
        RealNumber determinant = r.asMatrix().determinant();
        assertThat(determinant.eq(RealNumber.ONE))
            .withFailMessage("det(" + r.asMatrix() + ") = " + determinant + " " + TextUtils.NOT_EQUALS + " 1").isTrue();
    }

    @Override
    public Arbitrary<Rotation> elements() {
        return Arbitraries.doubles().ofScale(20).between(0, 2d * PI)
            .tuple3()
            .map(t -> Rx(t.get1()).times(Ry(t.get2())).times(Rz(t.get3())))
            ;
    }
}
