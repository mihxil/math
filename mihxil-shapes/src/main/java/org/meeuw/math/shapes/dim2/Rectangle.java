package org.meeuw.math.shapes.dim2;

import jakarta.validation.constraints.Min;

import java.util.stream.Stream;

import org.checkerframework.checker.units.qual.radians;
import org.meeuw.math.IntegerUtils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.dim2.FieldVector2;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.reals.RealNumber;

import static org.meeuw.math.shapes.dim2.LocatedFigure.atOrigin;
import static org.meeuw.math.uncertainnumbers.UncertainUtils.areExact;
import static org.meeuw.math.uncertainnumbers.UncertainUtils.strictlyEqual;

/**
 * Represents a rectangle defined by its width and height, both of which must be non-negative scalar.
 * This class provides methods to perform various geometric calculations such as rotation, area, perimeter,
 * diagonal length, and aspect ratio.
 *
 * @since 0.15
 */
public class Rectangle<E extends ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>> implements Polygon<E, C> {

    protected final E width;
    protected final E height;

    protected final ScalarField<E, C> field;


    /**
     *  @param width  the width of the rectangle, it must be non-negative
     *  @param height the height of the rectangle, it must be non-negative
     */
    public Rectangle(@Min(0) E width, @Min(0) E height) {
        this.width = width;
        this.height = height;
        this.field = width.getStructure();
    }


    public static Rectangle<RealNumber, RealNumber> of(double width, double height) {
        return new Rectangle<>(RealNumber.of(width), RealNumber.of(height));
    }

    public static Rectangle<RationalNumber, BigDecimalElement> of(long width, long height) {
        return new Rectangle<>(RationalNumber.of(width), RationalNumber.of(height));
    }

    public E width() {
        return width;
    }

    public E height() {
        return height;
    }


    /**
     * Checks if the rectangle is vertical, meaning its width is smaller than its height.
     *
     * @return true if the rectangle is vertical, false otherwise
     */
    public boolean vertical() {
        return width.lt(height);
    }

    /**
     * Rotates the enclosing rectangle if the current rectangle is rotated by a given angle in radians.
     *
     * @return a new Rectangle object with the rotated dimensions
     */
    @Override
    public LocatedFigure<C, C, Rectangle<C, C>> circumscribedRectangle() {
        return LocatedFigure.atOrigin(this.complete());
    }

    public LocatedFigure<E, C, Rectangle<E, C>> exactCircumscribedRectangle() {
        return LocatedFigure.atOrigin(this);
    }

    public LocatedFigure<E, C, RotatedRectangle<E, C>> circumscribedRectangle(@radians double angle) {

        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        return atOrigin(new RotatedRectangle<>(
            width.times(cos).abs().plus(height.times(sin).abs()),
            width.times(sin).abs().plus(height.times(cos).abs())
            )
        );
    }

    @Override
    public LocatedFigure<C, C, Circle<C, C>> circumscribedCircle() {
        C radius = diagonal().dividedBy(2);
        Circle<C, C> circle = new Circle<>(radius);
        return atOrigin(circle);
    }

    @Override
    public ScalarField<E, C> field() {
        return field;
    }

    /**
     *
     * Calculates the area of the rectangle by multiplying its width and height.
     */
    @Override
    public C area() {
        return exactArea().complete();
    }

    public E exactArea() {
        return  width.times(height);
    }

    @Override
    public Rectangle<C, C> complete() {
        return new Rectangle<>(width.complete(), height.complete());
    }

    /**
     * Calculates the perimeter of the rectangle by summing twice the width and twice the height.
     * The result is returned as a long to accommodate larger values.
     *
     * @return the perimeter of the rectangle
     */
    @Override
    public C perimeter() {
        return exactPerimeter().complete();
    }

    public E exactPerimeter() {
        return width.times(2).plus(height.times(2));
    }

    /**
     * Calculates the diagonal length of the rectangle using the Pythagorean theorem.
     * The diagonal is computed as the square root of the sum of the squares of the width and height.
     *
     * @return the length of the diagonal
     */
    public C diagonal() {
        return  width.sqr().plus(height.sqr()).sqrt();
    }

    /**
     * Checks if the rectangle is a square, meaning its width and height are equal.
     *
     * @return true if the rectangle is a square, false otherwise
     */
    public boolean isSquare() {
        return width.eq(height);
    }

    /**
     * Returns the aspect ratio of the rectangle in the format "width:height".
     * The values are reduced to their simplest form using the greatest common divisor ({@link IntegerUtils#gcd(int, int) GCD}).
     *
     * @return a string representing the aspect ratio
     */
    public String aspectRatio() {
        long gcd = IntegerUtils.gcd(width.longValue(), height.longValue());
        return String.format("%s:%s", width.dividedBy(gcd).longValue(), height.dividedBy(gcd).longValue());
    }

    /**
     * Returns the aspect ratio of the rectangle in the format "width:height".
     * The values are reduced to their simplest form using the greatest common divisor ({@link IntegerUtils#gcd(int, int) GCD}).
     *
     * @return a string representing the aspect ratio
     * @since 0.19
     */
    public RationalNumber aspectRational() {
        return RationalNumber.of(width.bigIntegerValue(), height.bigIntegerValue());
    }


    @Override
    public String toString() {
        return "Rectangle{" + parametersString() + '}';
    }

    @Override
    public String parametersString() {
        return width() + "x" +  height();
    }

    @Override
    public boolean eq(Figure<E, C> other) {
        if (!(other instanceof Rectangle<E, C> rectangle)) {
            return false;
        }
        return  this.width.eq(rectangle.width) && this.height.eq(rectangle.height);
    }

    @Override
    public Rectangle<E, C> times(E multiplier) {
        return new Rectangle<>(
            width.times(multiplier),
            height.times(multiplier)
        );
    }

    @Override
    public Rectangle<E, C> dividedBy(long divisor) {
        return new Rectangle<>(
            width.dividedBy(divisor),
            height.dividedBy(divisor)
        );
    }

    @Override
    public Rectangle<E, C> times(long multiplier) {
        return new Rectangle<>(
            width.times(multiplier),
            height.times(multiplier)
        );
    }

    @Override
    public Rectangle<E, C> times(double multiplier) {
         return new Rectangle<>(
            width.times(multiplier),
            height.times(multiplier)
        );
    }

    @Override
    public RotatedRectangle<E, C> rotate(E angle) {
        return new RotatedRectangle<>(
            width,
            height,
            angle
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rectangle<?, ?> rectangle)) return false;
        if (!rectangle.field.equals(field)) {
            return false;
        }
        return eq((Rectangle<E, C>) rectangle);
    }

    @Override
    public int hashCode() {
        return width.hashCode()  + 31 * height.hashCode() + 13 * field.hashCode();
    }

    @Override
    public int numberOfEdges() {
        return 4;
    }

    @Override
    public Stream<FieldVector2<C, C>> vertices() {
        return exactVertices().map(FieldVector2::complete);
    }

    public Stream<FieldVector2<E, C>> exactVertices() {
        E halfWidth = width.dividedBy(2);
        E halfHeight = height.dividedBy(2);
        return Stream.of(
            FieldVector2.of(halfWidth.negation(), halfHeight.negation()),
            FieldVector2.of(halfWidth, halfHeight.negation()),
            FieldVector2.of(halfWidth, halfHeight),
            FieldVector2.of(halfWidth.negation(), halfHeight)
        );
    }

    public FieldVector2<E, C> asVector() {
        return FieldVector2.of(width, height);
    }

    @Override
    public boolean isExact() {
        return Polygon.super.isExact() || areExact(width, height);
    }



    @Override
    public boolean strictlyEquals(Object o) {
        return strictlyEqual(this, o, Rectangle::width, Rectangle::height);
    }


}
