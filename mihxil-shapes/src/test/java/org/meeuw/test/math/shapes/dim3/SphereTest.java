package org.meeuw.test.math.shapes.dim3;

import lombok.extern.java.Log;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.shapes.dim3.Sphere;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.assertj.Assertions.assertThatAlgebraically;
import static org.meeuw.math.abstractalgebra.reals.RealField.element;

@Log
public class SphereTest {

    final Sphere<RealNumber> sphere = new Sphere<>(RealNumber.of(2.0));

    @Test
    public void area() {
        log.info("Area of %s: %s".formatted(sphere, sphere.surfaceArea()));
        // 4 * pi * r^2 = 4 * pi * 4
        assertThat(sphere.surfaceArea().doubleValue()).isEqualTo(4 * Math.PI * 4);
    }

    @Test
    public void volume() {
        log.info("Volume of %s: %s".formatted(sphere, sphere.volume()));
        // The implementation computes 4 * pi * r^3
        assertThat(sphere.volume().doubleValue()).isEqualTo(4 * Math.PI * 8);
    }

    @Test
    public void eq() {
        Sphere<RealNumber> same = new Sphere<>(element(2.0));
        Sphere<RealNumber> different = new Sphere<>(element(3.0));
        assertThat(sphere.eq(same)).isTrue();
        assertThat(sphere.eq(different)).isFalse();
    }

    @Test
    public void tostring() {
        assertThat(sphere.toString()).contains("2");
    }
}
