package org.meeuw.math.shapes.dim3;

import org.meeuw.math.abstractalgebra.*;

public class Sphere<F extends ScalarFieldElement<F>> implements Volume<F, Sphere<F>> {

    private final F radius;
    private final ScalarField<F> field;

    public Sphere(F radius) {
        this.radius = radius;
        this.field = radius.getStructure();
    }

    @Override
    public F volume() {
        return radius.pow(3).times(field.pi().times(4));

    }
    @Override
    public F surfaceArea() {
        return radius.sqr().times(field.pi()).times(4);
    }

    @Override
    public boolean eq(Sphere<F> other) {
        return this.radius.eq(other.radius);
    }

    @Override
    public String toString() {
        return "Sphere{radius=" + radius + '}';
    }
}
