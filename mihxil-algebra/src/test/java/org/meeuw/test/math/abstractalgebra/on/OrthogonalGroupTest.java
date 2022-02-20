package org.meeuw.test.math.abstractalgebra.on;

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.on.OrthogonalMatrix;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.abstractalgebra.test.MultiplicativeGroupTheory;
import org.meeuw.math.abstractalgebra.test.WithScalarTheory;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
class OrthogonalGroupTest implements MultiplicativeGroupTheory<OrthogonalMatrix<RealNumber>>, WithScalarTheory<OrthogonalMatrix<RealNumber>, RealNumber> {


    @Override
    public Arbitrary<RealNumber> scalars() {
         return Arbitraries.randomValue((random) ->
            new RealNumber(random.nextDouble() * 200 - 100, random.nextDouble() * 10))
            .dontShrink()
            .edgeCases(config -> {
                config.add(RealField.INSTANCE.zero());
                config.add(RealField.INSTANCE.one());
                config.add(RealField.INSTANCE.one().times(-1));
            });
    }

    @Test
    void of() {
        OrthogonalMatrix<RealNumber> e = OrthogonalMatrix.of(
            RealNumber.of(1), RealNumber.of(2),
            RealNumber.of(3), RealNumber.of(4)
        );
        log.info("{}", e);
        assertThat(e.getStructure().getDimension()).isEqualTo(2);

        RealNumber det = e.determinant();
        assertThat(det.getValue()).isEqualTo(-2d);

    }

    @Test
    void det() {
        OrthogonalMatrix<RealNumber> e = OrthogonalMatrix.of(
            RealNumber.of(1), RealNumber.of(2), RealNumber.of(4),
            RealNumber.of(4), RealNumber.of(5), RealNumber.of(6),
            RealNumber.of(7), RealNumber.of(8), RealNumber.of(9)
        );

        RealNumber det = e.determinant();
        assertThat(det.getValue()).isEqualTo(-3d);
    }

    @Test
    void reciprocal() {
        OrthogonalMatrix<RealNumber> e = OrthogonalMatrix.of(
            RealNumber.of(1), RealNumber.of(2), RealNumber.of(4),
            RealNumber.of(4), RealNumber.of(5), RealNumber.of(6),
            RealNumber.of(7), RealNumber.of(8), RealNumber.of(9)
        );

        OrthogonalMatrix<RealNumber> rec = e.reciprocal();
        log.info("{} x {} = {}", e, rec, e.times(rec));
    }

    @Override
    public Arbitrary<OrthogonalMatrix<RealNumber>> elements() {

        return Arbitraries.randomValue(g -> OrthogonalMatrix.of(
            RealNumber.of(g.nextDouble()), RealNumber.of(g.nextDouble()),
            RealNumber.of(g.nextDouble()), RealNumber.of(g.nextDouble())
        ));
    }
}
