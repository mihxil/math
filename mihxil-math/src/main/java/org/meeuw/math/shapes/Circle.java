package org.meeuw.math.shapes;

import jakarta.validation.constraints.Min;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;

/**

 * @since 0.15
 */
public class Circle<F extends CompleteScalarFieldElement<F>> {

    private final F radius;

    /**
     */
    public Circle(@Min(0) F radius) {
        this.radius = radius;
    }

    public F radius() {
        return radius;
    }

    public F area() {
        return radius.sqr().times(radius.getStructure().pi());
    }

    public F diameter() {
        return radius.times(2);
    }
    public F perimeter() {
        return radius.times(2).times(radius.getStructure().pi());
    }


    public String toString() {
        return "Circle{" + radius + '}';
    }
}
