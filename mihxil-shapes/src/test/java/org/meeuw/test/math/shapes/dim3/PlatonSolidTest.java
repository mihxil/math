package org.meeuw.test.math.shapes.dim3;

import lombok.extern.java.Log;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.shapes.dim3.PlatonicSolid;

import static org.meeuw.assertj.Assertions.assertThat;
import static org.meeuw.math.shapes.dim3.PlatonicSolidEnum.*;

@Log
public class PlatonSolidTest {

    @SuppressWarnings("unchecked")
    public static PlatonicSolid<RealNumber, RealNumber>[] platonics() {
        return new PlatonicSolid[]{
            new PlatonicSolid<>(TETRAHEDRON, RealNumber.of(2.0)),
            new PlatonicSolid<>(CUBE, RealNumber.of(2.0)),
            new PlatonicSolid<>(OCTAHEDRON, RealNumber.of(2.0)),
            new PlatonicSolid<>(DODECAHEDRON, RealNumber.of(2.0)),
            new PlatonicSolid<>(ICOSAHEDRON, RealNumber.of(2.0))
        };
    }

    @ParameterizedTest
    @MethodSource("platonics")
    public void dihedralAngle(PlatonicSolid<RealNumber, RealNumber> platonicSolid) {
        log.info("Dihedral angle of %s: %s".formatted(platonicSolid, Math.toDegrees(platonicSolid.dihedralAngle().doubleValue())));
    }

    @ParameterizedTest
    @MethodSource("platonics")
    public void area(PlatonicSolid<RealNumber, RealNumber> platonicSolid) {
        log.info("Area of %s: %s".formatted(platonicSolid, platonicSolid.surfaceArea()));
        if (platonicSolid.type() == TETRAHEDRON) {
            assertThat(platonicSolid.surfaceArea().doubleValue()).isEqualTo(6.928203230275511);
        }
    }

    @ParameterizedTest
    @MethodSource("platonics")
    public void volume(PlatonicSolid<RealNumber, RealNumber> platonicSolid) {
        log.info("Volume of %s: %s".formatted(platonicSolid, platonicSolid.volume()));
        if (platonicSolid.type() == TETRAHEDRON) {
            assertThat(platonicSolid.volume().doubleValue()).isEqualTo(0.9428090415820645);
        }
    }

    @ParameterizedTest
    @MethodSource("platonics")
    public void inradius(PlatonicSolid<RealNumber, RealNumber> platonicSolid) {
        log.info("Inradius of %s: %s".formatted(platonicSolid, platonicSolid.inradius()));
        if (platonicSolid.type() == TETRAHEDRON) {
            assertThat(platonicSolid.inradius().doubleValue()).isEqualTo(0.40824829046386335);
        }
    }

    @ParameterizedTest
    @MethodSource("platonics")
    public void circumradius(PlatonicSolid<RealNumber, RealNumber> platonicSolid) {
        log.info("circumradiusius of %s: %s".formatted(platonicSolid, platonicSolid.circumradius()));
        if (platonicSolid.type() == TETRAHEDRON) {
            assertThat(platonicSolid.circumradius().doubleValue()).isEqualTo(1.2247448713915894);
        }
    }

}
