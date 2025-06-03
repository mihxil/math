package org.meeuw.math.shapes.d2;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;

/**
 * Regular polygon with n sides, all of equal length.
 */
public class NGon<F extends CompleteScalarFieldElement<F>> {

    private final int n;

    private final F size;

    public NGon(int n, F size) {
        this.n = n;
        this.size = size;
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
        return size.getStructure().pi().dividedBy(n).cot()
            .times(size.sqr()).times(n).dividedBy(4);
    }
}
