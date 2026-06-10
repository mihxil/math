package org.meeuw.math.shapes.dim3;

import org.meeuw.math.abstractalgebra.*;

public class Sphere<F extends ScalarFieldElement<F, C>, C extends CompleteScalarFieldElement<C>> implements Solid<F, C, Sphere<F, C>> {

    private final F radius;
    private final ScalarField<F, C> field;

    public Sphere(F radius) {
        this.radius = radius;
        this.field = radius.getStructure();
    }

    @Override
    public C volume() {
        return radius.pow(3).complete().times(field.pi().times(4));

    }
    @Override
    public C surfaceArea() {
        return radius.sqr().complete().times(field.pi()).times(4);
    }

    @Override
    public boolean eq(Sphere<F, C> other) {
        return this.radius.eq(other.radius);
    }

    @Override
    public String toString() {
        return "Sphere{radius=" + radius + '}';
    }
}
