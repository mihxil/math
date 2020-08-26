package org.meeuw.math.abstractalgebra.dim3;

import net.jqwik.api.*;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.abstractalgebra.test.MultiplicativeGroupTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@PropertyDefaults(tries = 100)
class FieldMatrix3Test implements MultiplicativeGroupTheory<FieldMatrix3<RationalNumber>> {


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


    @Override
    public Arbitrary<FieldMatrix3<RationalNumber>> elements() {
        return
            Arbitraries.randoms().map(RationalNumbers.INSTANCE::nextRandom).list().ofSize(9)

                .map( l -> FieldMatrix3.of(
                    l.get(0), l.get(1), l.get(2),
                    l.get(3), l.get(4), l.get(5),
                    l.get(6), l.get(7), l.get(8)
                )).filter(e -> ! e.determinant().equals(RationalNumber.ZERO))
            ;
    }
}
