package org.meeuw.test.math.abstractalgebra.gl;

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.gl.InvertibleMatrix;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.abstractalgebra.test.MultiplicativeGroupTheory;
import org.meeuw.math.abstractalgebra.test.WithScalarTheory;
import org.meeuw.math.abstractalgebra.vectorspace.NVector;
import org.meeuw.math.exceptions.InvalidElementCreationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers.INSTANCE;

@Log4j2
class GeneralLinearGroupTest implements
    MultiplicativeGroupTheory<InvertibleMatrix<RationalNumber>>,
    WithScalarTheory<InvertibleMatrix<RationalNumber>, RationalNumber> {


    @Override
    public Arbitrary<RationalNumber> scalars() {
         return Arbitraries.randomValue(INSTANCE::nextRandom)
             .dontShrink()
             .filter(f -> ! f.isZero())
             .edgeCases(config -> {
                 config.add(INSTANCE.one());
                 config.add(INSTANCE.one().times(-1));
             });
    }

    @Test
    void of() {
        InvertibleMatrix<RealNumber> e = InvertibleMatrix.of(
            RealNumber.of(1), RealNumber.of(2),
            RealNumber.of(3), RealNumber.of(4)
        );
        log.info("{}", e);
        assertThat(e.getStructure().getDimension()).isEqualTo(2);

        RealNumber det = e.determinant();
        assertThat(det.getValue()).isEqualTo(-2d);

        assertThat(e.getStructure().toString()).isEqualTo("GL₂(ℝ)");

    }

    @Test
    void det() {
        InvertibleMatrix<RealNumber> e = InvertibleMatrix.of(
            RealNumber.of(1), RealNumber.of(2), RealNumber.of(4),
            RealNumber.of(4), RealNumber.of(5), RealNumber.of(6),
            RealNumber.of(7), RealNumber.of(8), RealNumber.of(9)
        );

        RealNumber det = e.determinant();
        assertThat(det.getValue()).isEqualTo(-3d);
    }

    @Test
    void reciprocal() {
        InvertibleMatrix<RealNumber> e = InvertibleMatrix.of(
            RealNumber.of(1), RealNumber.of(2), RealNumber.of(4),
            RealNumber.of(4), RealNumber.of(5), RealNumber.of(6),
            RealNumber.of(7), RealNumber.of(8), RealNumber.of(9)
        );

        InvertibleMatrix<RealNumber> rec = e.reciprocal();
        log.info("{} x {} = {}", e, rec, e.times(rec));

    }


    @Test
    void matrixVector() {
        InvertibleMatrix<RationalNumber> e = InvertibleMatrix.of(
            RationalNumber.of(2), RationalNumber.of(5),
            RationalNumber.of(1), RationalNumber.of(3)
        );
        NVector<RationalNumber> multiplier = NVector.of(RationalNumber.of(1), RationalNumber.of(2));
        NVector<RationalNumber> result = e.times(multiplier);
        assertThat(result).isEqualTo(NVector.of(RationalNumber.of(12), RationalNumber.of(7)));
        log.info("{} x {} = {}", e, multiplier, result);

    }

    @Override
    public Arbitrary<InvertibleMatrix<RationalNumber>> elements() {

        return Arbitraries.randomValue(g -> {
            while(true) {
                try {
                    return InvertibleMatrix.of(
                        INSTANCE.nextRandom(g), INSTANCE.nextRandom(g),
                        INSTANCE.nextRandom(g), INSTANCE.nextRandom(g)
                    );
                } catch (InvalidElementCreationException invalidElementCreationException) {
                    log.info("Skipping");
                }
            }
        });
    }
}
