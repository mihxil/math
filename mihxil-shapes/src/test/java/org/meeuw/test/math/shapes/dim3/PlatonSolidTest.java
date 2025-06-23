package org.meeuw.test.math.shapes.dim3;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.shapes.dim3.PlatonicSolid;
import org.meeuw.math.shapes.dim3.PlatonicSolidEnum;

import static org.meeuw.assertj.Assertions.assertThat;

@Log4j2
public class PlatonSolidTest {

    PlatonicSolid<RealNumber> tetrahedron = new PlatonicSolid<>(PlatonicSolidEnum.TETRAHEDRON, RealNumber.of(2.0));


    @Test
    public void dihedralAngle() {
        log.info("Dihedral angle of tetrahedron: {}", Math.toDegrees(tetrahedron.dihedralAngle().doubleValue()));
    }

    @Test
    public void area() {
        log.info("Area of tetrahedron: {}", tetrahedron.surfaceArea());
        assertThat(tetrahedron.surfaceArea().doubleValue()).isEqualTo(6.928203230275511);

    }

    @Test
    public void inradius() {
        log.info("Inradius of tetrahedron: {}", tetrahedron.inradius());
        assertThat(tetrahedron.inradius().doubleValue()).isEqualTo(0.40824829046386335);
    }


    @Test
    public void circumradius() {
        log.info("Inradcircumradiusius of tetrahedron: {}", tetrahedron.circumradius());
        assertThat(tetrahedron.circumradius().doubleValue()).isEqualTo(1.2247448713915894);
    }

}
