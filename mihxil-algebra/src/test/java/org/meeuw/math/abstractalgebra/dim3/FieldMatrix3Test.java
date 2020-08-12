package org.meeuw.math.abstractalgebra.dim3;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupTheory;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;

import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber.of;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
class FieldMatrix3Test implements MultiplicativeGroupTheory<FieldMatrix3<RationalNumber>> {


    @Override
    public Arbitrary<FieldMatrix3<RationalNumber>> elements() {
        return Arbitraries.of(
            FieldMatrix3.of(
                of(1), of(2), of(3),
                of(4), of(5), of(6),
                of(7), of(8), of(9)
            ),
            FieldMatrix3.of(
                of(5), of(2), of(3),
                of(4), of(2), of(6),
                of(7), of(8), of(9)
            )

        );
    }
}
