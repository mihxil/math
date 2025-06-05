package org.meeuw.test.math.shapes.dim2;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.integers.ModuloField;
import org.meeuw.math.abstractalgebra.integers.ModuloFieldElement;
import org.meeuw.math.shapes.dim2.Rectangle;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;
import org.meeuw.theories.BasicObjectTheory;

import static java.lang.Math.PI;
import static org.meeuw.assertj.Assertions.assertThat;
import static org.meeuw.assertj.Assertions.assertThatAlgebraically;
import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;
import static org.meeuw.math.uncertainnumbers.field.UncertainRealField.element;

public class RectangleTest implements BasicObjectTheory<Rectangle<UncertainReal>> {

    Rectangle<UncertainReal> rectangle = new Rectangle<>(
        exactly(1024d), exactly(576d));

    ModuloField field = ModuloField.of(2002927);

    Rectangle<ModuloFieldElement> intrectangle = new Rectangle<>(
        field.element(1024), field.element(576));

    @Test
    public void aspectRatio() {
        assertThat(rectangle.aspectRatio()).isEqualTo("16:9");
    }

    @Test
    public void circumscribedRectangle() {
        assertThat(rectangle.circumscribedRectangle(
            exactly(PI / 2d) // 90 degrees
        ).shape().aspectRatio()).isEqualTo("9:16");

    }

    @Test
    public void circumscribedRectangleDegrees() {
        assertThat(rectangle.circumscribedRectangle(
            exactly(Math.toRadians(90))
        ).shape().aspectRatio()).isEqualTo("9:16");

    }


    @Test
    public void area() {
        assertThatAlgebraically(rectangle.area()).isEqTo(element(589824d));
    }

    @Test
    public void intarea() {
        assertThatAlgebraically(intrectangle.area()).isEqTo(field.element(589824));
    }

    @Test
    public void isSquare() {
        assertThat(rectangle.isSquare()).isFalse();
    }

    @Test
    public void perimeter() {
        assertThat(rectangle.perimeter().eq(element(3200))).isTrue();
    }

    @Test
    public void diagonal() {
        assertThat(rectangle.diagonal().eq(element(1174.8838240438924))).isTrue();
    }

    @Test
    public void vertical() {
        assertThat(rectangle.vertical()).isFalse();
    }

     @Test
    public void string() {
        assertThat(rectangle.toString()).isEqualTo("Rectangle{1024x576}");
    }

    @Override
    public Arbitrary<@NonNull Rectangle<UncertainReal>> datapoints() {
        return Arbitraries.doubles().ofScale(3).between(0.001, 1000)
            .flatMap(width -> Arbitraries.doubles().ofScale(3).between(0.001, 1000)
                .map(height -> new Rectangle<>(element(width), element(height))));
    }
}
