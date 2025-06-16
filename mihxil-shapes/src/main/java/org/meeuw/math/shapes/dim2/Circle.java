package org.meeuw.math.shapes.dim2;

import jakarta.validation.constraints.Min;

import org.checkerframework.checker.units.qual.radians;
import org.meeuw.math.NonExact;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.uncertainnumbers.Uncertain;

import static org.meeuw.math.shapes.dim2.LocatedShape.atOrigin;

/**

 * @since 0.15
 */

public class Circle<F extends ScalarFieldElement<F>> implements Shape<F, Circle<F>>, Uncertain {

    private final F radius;
    private final ScalarField<F> field;

    /**
     */
    public Circle(@Min(0) F radius) {
        this.radius = radius;
        this.field = radius.getStructure();
    }

    @Override
    public Circle<F> times(F multiplier) {
        return new Circle<>(radius.times(multiplier));
    }
    @Override
    public Circle<F> times(int multiplier) {
        return new Circle<>(radius.times(multiplier));
    }

    @Override
    public Circle<F> times(double multiplier) {
        return new Circle<>(radius.times(multiplier));
    }

    public F radius() {
        return radius;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    @NonExact("Area can only be computed well for complete scalar fields")
    public F area() {
        if (field instanceof CompleteScalarField) {
            CompleteScalarField<?> completeField = (CompleteScalarField) field;
            return ((F) completeField.pi()).times(radius.sqr());
        } else {
            return radius().sqr().times(Math.PI);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * For a circle, the circumscribed rectangle is a square with the diameter as side length, at the origin.
     */
    @Override
    public LocatedShape<F, Rectangle<F>> circumscribedRectangle(@radians F angle) {
        F diameter = diameter();
        return atOrigin(
            new Rectangle<>(diameter, diameter)
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * For a circle, the circumscribed circle is the circle itself, at the origin.
     */
    @Override
    public LocatedShape<F, Circle<F>> circumscribedCircle() {
        return atOrigin(this);
    }

    @Override
    public ScalarField<F> field() {
        return field;
    }

    public F diameter() {
        return radius.times(2);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    @NonExact("Perimeter can only be computed well for complete scalar fields")
    public F perimeter() {
         if (field instanceof CompleteScalarField) {
             CompleteScalarField<?> completeField = (CompleteScalarField) field;
             return radius.times(2).times((F) completeField.pi());
         } else {
             return radius.times(2).times(Math.PI);
         }
    }


    public String toString() {
        return "Circle{" + radius + '}';
    }

    @Override
    public boolean isExact() {
        return !(radius instanceof Uncertain) || ((Uncertain) radius).isExact();
    }

    @Override
    public boolean strictlyEquals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Circle)) return false;
        Circle<?> circle = (Circle<?>) o;
        return radius instanceof Uncertain ? ((Uncertain) radius).strictlyEquals(circle.radius) : radius.equals(circle.radius);
    }

    @Override
    public boolean eq(Circle<F> other) {
        return  this.radius.eq(other.radius);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Circle)) return false;
        Circle<?> circle = (Circle<?>) o;
        if (!circle.field.equals(field)) {
            return false;
        }
        return eq((Circle<F>) circle);
    }

    @Override
    public int hashCode() {
        return radius.hashCode();
    }
}
