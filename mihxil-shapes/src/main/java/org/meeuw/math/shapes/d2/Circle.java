package org.meeuw.math.shapes.d2;

import jakarta.validation.constraints.Min;

import org.meeuw.math.abstractalgebra.CompleteScalarField;
import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.uncertainnumbers.Uncertain;

/**

 * @since 0.15
 */

public class Circle<F extends CompleteScalarFieldElement<F>> implements Shape<F, Circle<F>>, Uncertain {

    private final F radius;
    private final CompleteScalarField<F> field;

    /**
     */
    public Circle(@Min(0) F radius) {
        this.radius = radius;
        this.field = radius.getStructure();
    }

    public Circle<F> times(F multiplier) {
        return new Circle<>(radius.times(multiplier));
    }

    public F radius() {
        return radius;
    }

    @Override
    public F area() {
        return radius.sqr().times(field.pi());
    }

    @Override
    public Rectangle<F> circumscribedRectangle(F angle) {
        F diameter = diameter();
        return new Rectangle<>(diameter, diameter);
    }

    @Override
    public Circle<F> circumscribedCircle() {
        return this;
    }

    public F diameter() {
        return radius.times(2);
    }

    @Override
    public F perimeter() {
        return radius.times(2).times(field.pi());
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
