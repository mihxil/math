package org.meeuw.test.math.shapes.dim3;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.shapes.dim3.Sphere;

@Log4j2
public class SphereTest {

    Sphere<RealNumber> sphere = new Sphere<>(RealNumber.of(2.0));

    @Test
    public void area() {
        log.info("Area of {}: {}", sphere, sphere.surfaceArea());
    }


    @Test
    public void volume() {
        log.info("Volume of {}: {}", sphere, sphere.volume());


    }



}
