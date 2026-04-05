package org.meeuw.test.math.shapes.dim3;

import lombok.extern.java.Log;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.shapes.dim3.RectangularCuboid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.assertj.Assertions.assertThatAlgebraically;
import static org.meeuw.math.abstractalgebra.reals.RealField.element;

@Log
public class RectangularCuboidTest {

    final RectangularCuboid<RealNumber> cuboid = new RectangularCuboid<>(
        element(2.0), element(3.0), element(4.0));

    final RectangularCuboid<RealNumber> cube = new RectangularCuboid<>(
        element(3.0), element(3.0), element(3.0));

    @Test
    public void volume() {
        assertThatAlgebraically(cuboid.volume()).isEqTo(element(24.0));
    }

    @Test
    public void cubeVolume() {
        assertThatAlgebraically(cube.volume()).isEqTo(element(27.0));
    }

    @Test
    public void surfaceArea() {
        // 2*(w*h + w*d + h*d) = 2*(6 + 8 + 12) = 52
        assertThatAlgebraically(cuboid.surfaceArea()).isEqTo(element(52.0));
    }

    @Test
    public void cubeSurfaceArea() {
        // 6 * s^2 = 6 * 9 = 54
        assertThatAlgebraically(cube.surfaceArea()).isEqTo(element(54.0));
    }

    @Test
    public void vertices() {
        assertThat(cuboid.vertices()).isEqualTo(8);
    }

    @Test
    public void edges() {
        assertThat(cuboid.edges()).isEqualTo(12);
    }

    @Test
    public void faces() {
        assertThat(cuboid.faces()).isEqualTo(6);
    }

    @Test
    public void eulerCharacteristic() {
        assertThat(cuboid.eulerCharacteristic()).isEqualTo(2);
    }

    @Test
    public void tostring() {
        log.info("Cuboid: " + cuboid);
        assertThat(cuboid.toString()).contains("2").contains("3").contains("4");
    }

    @Test
    public void widthHeightDepth() {
        assertThatAlgebraically(cuboid.width()).isEqTo(element(2.0));
        assertThatAlgebraically(cuboid.height()).isEqTo(element(3.0));
        assertThatAlgebraically(cuboid.depth()).isEqTo(element(4.0));
    }
}
