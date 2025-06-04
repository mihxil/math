package org.meeuw.math.shapes.d2;

import jakarta.validation.constraints.Min;

import org.meeuw.math.abstractalgebra.CompleteScalarField;
import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;

/**

 * @since 0.15
 */
public class Circle<F extends CompleteScalarFieldElement<F>> {

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

    public F area() {
        return radius.sqr().times(field.pi());
    }

    public F diameter() {
        return radius.times(2);
    }
    public F perimeter() {
        return radius.times(2).times(field.pi());
    }


    public String toString() {
        return "Circle{" + radius + '}';
    }
}
