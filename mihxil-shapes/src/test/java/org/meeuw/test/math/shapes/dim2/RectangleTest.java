package org.meeuw.test.math.shapes.dim2;

import java.util.stream.Collectors;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.jupiter.Rounding;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.dim2.FieldVector2;
import org.meeuw.math.abstractalgebra.integers.ModuloField;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.exceptions.FieldIncompleteException;
import org.meeuw.math.shapes.dim2.Rectangle;

import static java.lang.Math.PI;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.assertj.Assertions.assertThat;
import static org.meeuw.assertj.Assertions.assertThatAlgebraically;
import static org.meeuw.math.abstractalgebra.reals.DoubleElement.exactly;
import static org.meeuw.math.abstractalgebra.reals.RealField.element;

public class RectangleTest implements FigureTheory<RealNumber, RealNumber, Rectangle<RealNumber, RealNumber>> {

    Rectangle<RealNumber, RealNumber> rectangle = new Rectangle<>(
        exactly(1024d), exactly(576d));

    ModuloField field = ModuloField.of(2002927);

    Rectangle<RationalNumber, BigDecimalElement> rationalRectangle = Rectangle.of(1024, 576);

    @Test
    public void aspectRatio() {
        assertThat(rectangle.aspectRatio()).isEqualTo("16:9");
    }

    @Test
    public void aspectRational() {
        assertThat(rectangle.aspectRational().toString()).isEqualTo("¹⁶⁄₉");
    }

    @Test
    public void times() {
        assertThat(rectangle.times(2).toString()).isEqualTo("Rectangle{2048x1152}");
        assertThat(rectangle.times(exactly(2)).toString()).isEqualTo("Rectangle{2048x1152}");
        assertThat(rectangle.times(2.5).toString()).isEqualTo("Rectangle{2560x1440}");
        assertThat(rationalRectangle.times(2).toString()).isEqualTo("Rectangle{2048x1152}");
        assertThat(rationalRectangle.times(2.5).toString()).isEqualTo("Rectangle{2560x1440}");
    }

    @Test
    public void circumscribedRectangle() {
        // 90 degrees
        assertThat(rectangle.rotate(exactly(PI / 2d)).circumscribedRectangle(
        ).shape().aspectRatio()).isEqualTo("9:16");

    }

    @Test
    public void circumscribedRectangleDegrees() {
        assertThat(rectangle.rotate( exactly(Math.toRadians(90))).circumscribedRectangle().shape().aspectRatio()).isEqualTo("9:16");

        assertThat(rationalRectangle.circumscribedRectangle(Math.toRadians(90)).shape().aspectRatio()).isEqualTo("9:16");
    }


    @Test
    public void area() {
        assertThatAlgebraically(rectangle.area()).isEqTo(element(589824d));
    }

    @Test
    public void intarea() {
        assertThatAlgebraically(rationalRectangle.exactArea()).isEqTo(RationalNumber.of(589824));
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
        assertThatThrownBy(() -> rationalRectangle.diagonal()).isInstanceOf(FieldIncompleteException.class);
    }

    @Test
    public void vertical() {
        assertThat(rectangle.vertical()).isFalse();
    }

    @Test
    public void string() {
        assertThat(rectangle.toString()).isEqualTo("Rectangle{1024x576}");
    }

    @Test
    public void notEquals() {
        assertThat(rectangle.equals(rationalRectangle)).isFalse();
    }

    @Test
    @Rounding
    public void vertices() {
        assertThat(rectangle.vertices()
                .map(FieldVector2::toString)
                .collect(Collectors.joining(" "))
        ).isEqualTo("(-512,-288) (512,-288) (512,288) (-512,288)");
        assertThat(rationalRectangle.numberOfEdges()).isEqualTo(4);
    }

    @Override
    public Arbitrary<@NonNull Rectangle<RealNumber, RealNumber>> datapoints() {
        return Arbitraries.doubles().ofScale(3).between(0.001, 1000)
            .flatMap(width -> Arbitraries.doubles().ofScale(3).between(0.001, 1000)
                .map(height -> new Rectangle<>(element(width), element(height))));
    }
}
