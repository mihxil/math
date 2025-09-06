package org.meeuw.test.math.shapes.dim3;

import lombok.extern.java.Log;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.shapes.dim3.Sphere;

@Log
public class SphereTest {

    final  Sphere<RealNumber> sphere = new Sphere<>(RealNumber.of(2.0));
    @Test
    public void area() {
        log.info("Area of %s: %s".formatted(sphere, sphere.surfaceArea()));
    }

    @Test
    public void volume() {
        log.info("Volume of %s: %s".formatted(sphere, sphere.volume()));
    }
}
