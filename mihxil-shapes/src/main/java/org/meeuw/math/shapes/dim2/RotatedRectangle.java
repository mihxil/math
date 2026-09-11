package org.meeuw.math.shapes.dim2;

import jakarta.validation.constraints.Min;

import java.util.stream.Stream;

import org.checkerframework.checker.units.qual.radians;
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
public class RotatedRectangle<E extends ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>> extends Rectangle<E, C> {


    private final E angle ;

    /**
     *  @param width  the width of the rectangle, it must be non-negative
     *  @param height the height of the rectangle, it must be non-negative
     */
    public RotatedRectangle(@Min(0) E width, @Min(0) E height, @radians E angle) {
        super(width, height);
        this.angle = angle;
    }

    public RotatedRectangle(@Min(0) E width, @Min(0) E height) {
        this(width, height, height.getStructure().zero());

    }

    public static RotatedRectangle<RealNumber, RealNumber> of(double width, double height) {
        return new RotatedRectangle<>(RealNumber.of(width), RealNumber.of(height), RealNumber.ZERO);
    }

    public static RotatedRectangle<RationalNumber, BigDecimalElement> of(long width, long height) {
        return new RotatedRectangle<>(RationalNumber.of(width), RationalNumber.of(height), RationalNumber.ZERO);
    }

    public E angle() {
        return angle;
    }

    /**
     * Checks if the rectangle is vertical, meaning its width is smaller than its height.
     *
     * @return true if the rectangle is vertical, false otherwise
     */
    public boolean vertical() {
        return circumscribedRectangle().shape().vertical();
    }

    /**
     * Rotates the enclosing rectangle if the current rectangle is rotated by a given angle in radians.
     *
     * @return a new Rectangle object with the rotated dimensions
     */
    @Override
    public LocatedFigure<C, C, Rectangle<C, C>> circumscribedRectangle() {
        return exactCircumscribedRectangle().complete();
    }

    public LocatedFigure<E, C, Rectangle<E, C>> exactCircumscribedRectangle() {

        if (angle.isZero()) {
            return atOrigin(this);
        }

        E sin = field.approx(angle.sin());
        assert sin != null;
        E cos = field.approx(angle.cos());
        assert cos != null;
        return atOrigin(new RotatedRectangle<>(
            width.times(cos).abs().plus(height.times(sin).abs()),
            width.times(sin).abs().plus(height.times(cos).abs()),
            field.zero()
        ));

    }

    public LocatedFigure<E, C, RotatedRectangle<E, C>> circumscribedRectangle(@radians double angle) {

        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        return atOrigin(new RotatedRectangle<>(
            width.times(cos).abs().plus(height.times(sin).abs()),
            width.times(sin).abs().plus(height.times(cos).abs()),
            field().zero()
        ));
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


    @Override
    public RotatedRectangle<C, C> complete() {
        return new RotatedRectangle<>(width.complete(), height.complete(), angle.complete());
    }




    @Override
    public String toString() {
        return "RotatedAngle{" + width() + "x" +  height() + ' ' + angle() + '}';
    }


    @Override
    public boolean eq(Figure<E, C> other) {
        if (!(other instanceof RotatedRectangle<E, C> rectangle)) {
            return false;
        }
        return  super.eq(other) && this.angle.eq(rectangle.angle);
    }

    @Override
    public RotatedRectangle<E, C> times(E multiplier) {
        return new RotatedRectangle<>(
            width.times(multiplier),
            height.times(multiplier),
            angle
        );
    }

    @Override
    public RotatedRectangle<E, C> dividedBy(long divisor) {
        return new RotatedRectangle<>(
            width.times(divisor),
            height.times(divisor),
            angle
        );
    }

    @Override
    public RotatedRectangle<E, C> times(long multiplier) {
        return new RotatedRectangle<>(
            width.times(multiplier),
            height.times(multiplier),
            angle
        );
    }

    @Override
    public RotatedRectangle<E, C> times(double multiplier) {
         return new RotatedRectangle<>(
            width.times(multiplier),
            height.times(multiplier),
             angle
        );
    }

    @Override
    public RotatedRectangle<E, C> rotate(E angle) {
        return new RotatedRectangle<>(
            width,
            height,
            this.angle.plus(angle)
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RotatedRectangle<?, ?> rectangle)) return false;
        if (!rectangle.field.equals(field)) {
            return false;
        }
        return eq((RotatedRectangle<E, C>) rectangle);
    }

    @Override
    public int hashCode() {
        return 13 * super.hashCode() + angle.hashCode();
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

    @Override
    public boolean isExact() {
        return super.isExact() && areExact(angle);
    }

    @Override
    public boolean strictlyEquals(Object o) {
        return super.strictlyEquals(o) && strictlyEqual(this, o, RotatedRectangle::angle);
    }


}
