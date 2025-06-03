package org.meeuw.test.math.shapes.d2;

import org.junit.jupiter.api.Test;

import org.meeuw.math.shapes.d2.IntRectangle;

import static org.assertj.core.api.Assertions.assertThat;

public class IntRectangleTest {

    IntRectangle rectangle = new IntRectangle(1024, 576);
    @Test
    public void aspectRatio() {
        assertThat(rectangle.aspectRatio()).isEqualTo("16:9");
    }

    @Test
    public void rotate() {
        assertThat(rectangle.rotateDegrees(90).aspectRatio()).isEqualTo("9:16");

        assertThat(rectangle.rotateDegrees(null)).isSameAs(rectangle);
    }

    @Test
    public void area() {
        assertThat(rectangle.area()).isEqualTo(589824L);
    }

    @Test
    public void isSquare() {
        assertThat(rectangle.isSquare()).isFalse();
    }

    @Test
    public void perimeter() {
        assertThat(rectangle.perimeter()).isEqualTo(3200L);
    }

    @Test
    public void diagonal() {
        assertThat(rectangle.diagonal()).isEqualTo(1174.8838240438924);
    }

    @Test
    public void vertical() {
        assertThat(rectangle.vertical()).isFalse();
    }

     @Test
    public void string() {
        assertThat(rectangle.toString()).isEqualTo("Rectangle{1024x576}");
    }
}
