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
import org.assertj.core.api.Assertions;

import org.meeuw.math.abstractalgebra.dim3.FieldMatrix3;
import org.meeuw.math.abstractalgebra.dim3.FieldMatrix3Group;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.theories.abstractalgebra.MultiplicativeGroupTheory;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.exceptions.ReciprocalException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@PropertyDefaults(tries = 100)
@Log4j2
class FieldMatrix3Test implements MultiplicativeGroupTheory<FieldMatrix3<RationalNumber>> {


    @Test
    public void illegal() {
        Assertions.assertThatThrownBy(() -> {
            FieldMatrix3.of(
                of(3), of(0), of(2),
                of(2), of(0), of(-2),
                of(0), of(0), of(0)
            );
        }).isInstanceOf(InvalidElementCreationException.class);
        FieldMatrix3<RationalNumber> iml = new FieldMatrix3<>(
            new RationalNumber[][]{
                new RationalNumber[]{of(3), of(0), of(2)},
                new RationalNumber[]{of(2), of(0), of(-2)},
                new RationalNumber[]{of(0), of(0), of(0)}
            }
        );
        Assertions.assertThatThrownBy(iml::reciprocal)
            .isInstanceOf(ReciprocalException.class);

    }

    @Test
    public void adjugate() {
        FieldMatrix3<RationalNumber> fm = FieldMatrix3.of(
            of(3), of(0), of(2),
            of(2), of(0), of(-2),
            of(0), of(1), of(1)
        );
        assertThat(fm.adjugate()).isEqualTo(
            FieldMatrix3.of(
                of(2), of(2), of(0),
                of(-2), of(3), of(10),
                of(2), of(-3), of(0)
            )
        );
    }

    @Test
    public void determinant() {
        FieldMatrix3<RationalNumber> fm = FieldMatrix3.of(
            of(2), of(-3), of(1),
            of(2), of(0), of(-1),
            of(1), of(4), of(5)
        );
        RationalNumber determinant = fm.determinant();
        assertThat(determinant).isEqualTo(of(49));
        log.info("Determinant {}", determinant);
    }

    @Test
    public void illegalReciprocal() {

        FieldMatrix3<RationalNumber> fm = new FieldMatrix3<>(
            new RationalNumber[][]{
                new RationalNumber[]{of(2), of(0), of(-1)},
                new RationalNumber[]{of(2), of(0), of(-1)},
                new RationalNumber[]{of(1), of(4), of(5)}
            }
        );

        assertThatThrownBy(fm::reciprocal).isInstanceOf(ReciprocalException.class);
    }

    @Test
    public void determinantOfIdentity() {
        Assertions.assertThat(FieldMatrix3Group.of(RationalNumbers.INSTANCE).one()
            .determinant()).isEqualTo(of(1));
    }

    @Override
    public Arbitrary<FieldMatrix3<RationalNumber>> elements() {
        return
            Arbitraries.randoms().map(RationalNumbers.INSTANCE::nextRandom).list().ofSize(9)
                .map( l -> FieldMatrix3.of(
                    l.get(0), l.get(1), l.get(2),
                    l.get(3), l.get(4), l.get(5),
                    l.get(6), l.get(7), l.get(8)
                )
                )
            ;
    }
}
