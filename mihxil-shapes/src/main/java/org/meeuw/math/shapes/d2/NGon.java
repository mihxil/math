package org.meeuw.math.shapes.d2;

import org.meeuw.math.abstractalgebra.CompleteScalarField;
import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;

/**
 * Regular polygon with n sides, all of equal length.
 */
public class NGon<F extends CompleteScalarFieldElement<F>> {

    private final int n;

    private final F size;
    private final CompleteScalarField<F> field;


    public NGon(int n, F size) {
        this.n = n;
        this.size = size;
        this.field = size.getStructure();
    }

    public F size() {
        return size;
    }
    public int n() {
        return n;
    }
    public F perimeter() {
        return size.times(n);
    }
    public F area() {
        return field.pi().dividedBy(n).cot()
            .times(size.sqr()).times(n).dividedBy(4);
    }
    public F interiorAngle() {
        return field.pi().times(n - 2).dividedBy(n);
    }

    public F inscribedRadius() {
        return field.pi().dividedBy(n).cot().times(size).dividedBy(2);
    }

    public F circumscribedRadius() {
        return field.pi().dividedBy(n).csc().times(size).dividedBy(2);
    }

    /**
     * Schläfli symbol
     */
    public String toString() {
        return String.format("{%d}, size: %s", n, size.toString());
    }
}
