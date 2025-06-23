package org.meeuw.test.math.shapes.dim3;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.shapes.dim3.PlatonicSolid;
import org.meeuw.math.shapes.dim3.PlatonicSolidEnum;

import static org.meeuw.assertj.Assertions.assertThat;

@Log4j2
public class PlatonSolidTest {




    public static PlatonicSolid<RealNumber>[] platonics() {
        return new PlatonicSolid[]{
            new PlatonicSolid<>(PlatonicSolidEnum.TETRAHEDRON, RealNumber.of(2.0)),
            new PlatonicSolid<>(PlatonicSolidEnum.CUBE, RealNumber.of(2.0)),
            new PlatonicSolid<>(PlatonicSolidEnum.OCTAHEDRON, RealNumber.of(2.0)),
            new PlatonicSolid<>(PlatonicSolidEnum.DODECAHEDRON, RealNumber.of(2.0)),
            new PlatonicSolid<>(PlatonicSolidEnum.ICOSAHEDRON, RealNumber.of(2.0))
        };
    }

    @ParameterizedTest
    @MethodSource("platonics")
    public void dihedralAngle(PlatonicSolid<RealNumber> platonicSolid) {
        log.info("Dihedral angle of {}: {}", platonicSolid, Math.toDegrees(platonicSolid.dihedralAngle().doubleValue()));
    }

    @ParameterizedTest
    @MethodSource("platonics")
    public void area(PlatonicSolid<RealNumber> platonicSolid) {
        log.info("Area of {}: {}", platonicSolid, platonicSolid.surfaceArea());
        if (platonicSolid.platonicSolidEnum() == PlatonicSolidEnum.TETRAHEDRON) {
            assertThat(platonicSolid.surfaceArea().doubleValue()).isEqualTo(6.928203230275511);
        }

    }


    @ParameterizedTest
    @MethodSource("platonics")
    public void volume(PlatonicSolid<RealNumber> platonicSolid) {
        log.info("Volume of {}: {}", platonicSolid, platonicSolid.surfaceArea());
        if (platonicSolid.platonicSolidEnum() == PlatonicSolidEnum.TETRAHEDRON) {
            assertThat(platonicSolid.surfaceArea().doubleValue()).isEqualTo(6.928203230275511);
        }

    }

    @ParameterizedTest
    @MethodSource("platonics")
    public void inradius(PlatonicSolid<RealNumber> platonicSolid) {
        log.info("Inradius of {}: {}", platonicSolid, platonicSolid.inradius());
        if (platonicSolid.platonicSolidEnum() == PlatonicSolidEnum.TETRAHEDRON) {
            assertThat(platonicSolid.inradius().doubleValue()).isEqualTo(0.40824829046386335);
        }
    }


    @ParameterizedTest
    @MethodSource("platonics")
    public void circumradius(PlatonicSolid<RealNumber> platonicSolid) {
        log.info("circumradiusius of {}: {}", platonicSolid, platonicSolid.circumradius());
        if (platonicSolid.platonicSolidEnum() == PlatonicSolidEnum.TETRAHEDRON) {

            assertThat(platonicSolid.circumradius().doubleValue()).isEqualTo(1.2247448713915894);
        }
    }

}
