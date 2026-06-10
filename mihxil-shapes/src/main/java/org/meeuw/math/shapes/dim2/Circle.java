package org.meeuw.math.shapes.dim2;

import jakarta.validation.constraints.Min;

import java.util.stream.Stream;

import org.meeuw.math.NonExact;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.shapes.Info;

import static org.meeuw.math.shapes.dim2.LocatedShape.atOrigin;
import static org.meeuw.math.uncertainnumbers.UncertainUtils.areExact;
import static org.meeuw.math.uncertainnumbers.UncertainUtils.strictlyEqual;

/**
 * A circle in a two-dimensional shape, defined by its radius only.
 * @since 0.15
 */

public class Circle<E extends ScalarFieldElement<E, C>,  C extends CompleteScalarFieldElement<C>> implements Shape<E, C, Circle<E, C>> {

    private final E radius;
    private final ScalarField<E, C> field;

    /**
     */
    public Circle(@Min(0) E radius) {
        this.radius = radius;
        this.field = radius.getStructure();
    }

    @Override
    public Stream<Info> info() {
        return Stream.concat(
            Shape.super.info(),
            Stream.of(
                new Info(Info.Key.RADIUS, this::radius),
                new Info(Info.Key.DIAMETER, this::diameter)
            )
        );
    }

    @Override
    public Circle<E, C> times(E multiplier) {
        return new Circle<>(radius.times(multiplier));
    }
    @Override
    public Circle<E, C> times(int multiplier) {
        return new Circle<>(radius.times(multiplier));
    }

    @Override
    public Circle<E, C> times(double multiplier) {
        return new Circle<>(radius.times(multiplier));
    }

    @Override
    public Circle<E, C> rotate(E angle) {
        return this; // a circle is invariant under rotation
    }

    public E radius() {
        return radius;
    }

    @Override
    @NonExact("Area can only be computed well for complete scalar fields")
    public C area() {
        return field.pi().times(radius.sqr().complete());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Circle<C, C> complete() {
        return new Circle<>(radius.complete());
    }

    /**
     * {@inheritDoc}
     * <p>
     * For a circle, the circumscribed rectangle is a square with the diameter as side length, at the origin.
     */
    @Override
    public LocatedShape<C, C, Rectangle<C, C>> circumscribedRectangle() {
        C diameter = diameter().complete();
        return atOrigin(
            new Rectangle<>(diameter, diameter, field.completedField().zero())
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * For a circle, the circumscribed circle is the circle itself, at the origin.
     */
    @Override
    public LocatedShape<C, C, Circle<C, C>> circumscribedCircle() {
        return atOrigin(new Circle<>(radius.complete()));
    }

    public LocatedShape<E, C, Circle<E, C>> exactCircumscribedCircle() {
        return atOrigin(new Circle<>(radius));
    }

    @Override
    public ScalarField<E, C> field() {
        return field;
    }

    public E diameter() {
        return radius.times(2);
    }

    @Override
    public C perimeter() {
        return field.pi().times(radius.times(2).complete());
    }


    public String toString() {
        return "Circle{" + radius + '}';
    }

    @Override
    public boolean isExact() {
        return Shape.super.isExact() || areExact(radius);
    }

    @Override
    public boolean strictlyEquals(Object o) {
        return strictlyEqual(this, o, Circle::radius);
    }

    @Override
    public boolean eq(Circle<E, C> other) {
        return  this.radius.eq(other.radius);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Circle<?, ?> circle)) return false;
        if (!circle.field.equals(field)) {
            return false;
        }
        return eq((Circle<E, C>) circle);
    }

    @Override
    public int hashCode() {
        return radius.hashCode();
    }
}
