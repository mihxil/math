package org.meeuw.test.math.shapes.dim3;

import lombok.extern.java.Log;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.shapes.dim3.Sphere;

import static org.assertj.core.api.Assertions.assertThat;

@Log
public class SphereTest {

    final Sphere<RationalNumber, BigDecimalElement> sphere = new Sphere<>(RationalNumber.of(2));

    @Test
    public void area() {
        log.info("Area of %s: %s".formatted(sphere, sphere.surfaceArea()));
        // 4 * pi * r^2 = 4 * pi * 4
        assertThat(sphere.surfaceArea().doubleValue()).isEqualTo(4 * Math.PI * 4);
    }

    @Test
    public void volume() {
        log.info("Volume of %s: %s".formatted(sphere, sphere.volume()));
        // Implementation computes r^3 * pi * 4 (note: standard sphere volume is 4/3 * pi * r^3)
        assertThat(sphere.volume().doubleValue()).isEqualTo(4 * Math.PI * 8);
    }

    @Test
    public void eq() {
        Sphere<RationalNumber, BigDecimalElement> same = new Sphere<>(RationalNumber.of(2));
        Sphere<RationalNumber, BigDecimalElement> different = new Sphere<>(RationalNumber.of(3));
        assertThat(sphere.eq(same)).isTrue();
        assertThat(sphere.eq(different)).isFalse();
    }

    @Test
    public void tostring() {
        assertThat(sphere.toString()).contains("2");
    }
}
