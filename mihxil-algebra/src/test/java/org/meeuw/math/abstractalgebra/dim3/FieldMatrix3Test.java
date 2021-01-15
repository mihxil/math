package org.meeuw.math.abstractalgebra.dim3;

import lombok.extern.log4j.Log4j2;
import net.jqwik.api.*;

import org.junit.jupiter.api.Test;
import org.meeuw.math.exceptions.MathException;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.abstractalgebra.test.MultiplicativeGroupTheory;

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
    public void reciprocal() {
        FieldMatrix3<RationalNumber> fm = FieldMatrix3.of(
            of(2), of(0), of(-1),
            of(2), of(0), of(-1),
            of(1), of(4), of(5)
        );

        assertThat(fm.determinant()).isEqualTo(RationalNumber.ZERO);
        assertThatThrownBy(fm::reciprocal).isInstanceOf(MathException.class);
    }

    @Test
    public void determinantOfIdentity() {
        assertThat(FieldMatrix3Group.of(RationalNumbers.INSTANCE).one().determinant()).isEqualTo(of(1));
    }

    @Override
    public Arbitrary<FieldMatrix3<RationalNumber>> elements() {
        return
            Arbitraries.randoms().map(RationalNumbers.INSTANCE::nextRandom).list().ofSize(9)
                .map( l -> FieldMatrix3.of(
                    l.get(0), l.get(1), l.get(2),
                    l.get(3), l.get(4), l.get(5),
                    l.get(6), l.get(7), l.get(8)
                )).filter(e -> ! e.determinant().equals(RationalNumber.ZERO)
            )
            ;
    }
}
