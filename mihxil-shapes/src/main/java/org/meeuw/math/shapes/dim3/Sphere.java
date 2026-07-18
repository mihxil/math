package org.meeuw.math.shapes.dim3;

import lombok.Getter;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.uncertainnumbers.UncertainUtils;

@Getter
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

    @SuppressWarnings("unchecked")
    @Override
    public Sphere<C, C> complete() {
        return new Sphere<>(radius.complete());
    }

    @Override
    public ScalarField<F, C> field() {
        return field;
    }

    @Override
    public boolean eq(Sphere<F, C> other) {
        return this.radius.eq(other.radius);
    }

    @Override
    public Sphere<F, C> times(F multiplier) {
        return new Sphere<>(radius.times(multiplier));
    }

    @Override
    public Sphere<F, C> times(int multiplier) {
        return new Sphere<>(radius.times(multiplier));
    }

    @Override
    public Sphere<F, C> times(double multiplier) {
        return new Sphere<>(radius.times(multiplier));
    }

    @Override
    public String toString() {
        return "Sphere{radius=" + radius + '}';
    }

    @Override
    public boolean strictlyEquals(Object o) {
        return UncertainUtils.strictlyEqual(this, o, Sphere::radius);
    }

}
