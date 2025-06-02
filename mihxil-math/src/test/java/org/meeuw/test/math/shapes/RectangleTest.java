package org.meeuw.test.math.shapes;

import org.junit.jupiter.api.Test;

import org.meeuw.math.shapes.Rectangle;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static java.lang.Math.PI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;

public class RectangleTest {

    Rectangle<UncertainReal> rectangle = new Rectangle<>(
        exactly(1024d), exactly(576d));
    @Test
    public void aspectRatio() {
        assertThat(rectangle.aspectRatio()).isEqualTo("16:9");
    }

    @Test
    public void rotate() {
        assertThat(rectangle.rotate(
            exactly(PI / 2d) // 90 degrees
        ).aspectRatio()).isEqualTo("9:16");

    }

    @Test
    public void area() {
        assertThat(rectangle.area().eq(exactly(589824d))).isTrue();
    }

    @Test
    public void isSquare() {
        assertThat(rectangle.isSquare()).isFalse();
    }

    @Test
    public void perimeter() {
        assertThat(rectangle.perimeter().eq(exactly(3200))).isTrue();
    }

    @Test
    public void diagonal() {
        assertThat(rectangle.diagonal().eq(exactly(1174.8838240438924))).isTrue();
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
