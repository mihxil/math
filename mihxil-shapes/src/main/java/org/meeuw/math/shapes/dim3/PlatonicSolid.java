package org.meeuw.math.shapes.dim3;

import lombok.Getter;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.uncertainnumbers.UncertainUtils;

@Getter
public class PlatonicSolid<E extends ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>> implements Polyhedron<E, C, PlatonicSolid<E, C>> {

    private final PlatonicSolidEnum type;
    private final ScalarField<E, C> field;
    private final E size;


    public PlatonicSolid(PlatonicSolidEnum type, E size) {
        this.type = type;
        this.size = size;
        this.field = size.getStructure();
    }

    @Override
    public int vertices() {
        return type.vertices();
    }

    @Override
    public int edges() {
        return type.edges();
    }

    @Override
    public int faces() {
        return type.faces();
    }

    public C dihedralAngle() {
        var p = type.p();
        var q = type.q();
        return
            ((field.pi().dividedBy(q)).cos())
                .dividedBy(
                    field.pi().dividedBy(p).sin()
                ).asin().times(2);
    }

    public C inradius() {
        var theta = dihedralAngle();
        return field.complete(size()).dividedBy(2).dividedBy(
            field.pi().dividedBy(type.p()).tan()
        ).times(theta.dividedBy(2).tan());
    }

    public C circumradius() {
        var theta = dihedralAngle();
        return size().complete().dividedBy(2).times(
            field.pi().dividedBy(type.q()).tan()
        ).times(theta.dividedBy(2).tan());
    }

    @Override
    public C volume() {
        return inradius().times(surfaceArea()).dividedBy(3);
    }

    @Override
    public C surfaceArea() {
        return size().complete().dividedBy(2).sqr().times((long) faces() * type.p())
            .dividedBy(field.pi().dividedBy(type.p()).tan());
    }

    @Override
    public PlatonicSolid<C, C> complete() {
        return new PlatonicSolid<>(type, size.complete());
    }

    @Override
    public boolean eq(PlatonicSolid<E, C> other) {
        return false;
    }

    @Override
    public PlatonicSolid<E, C> times(E multiplier) {
        return new PlatonicSolid<>(type, size.times(multiplier));
    }

    @Override
    public PlatonicSolid<E, C> times(int multiplier) {
        return new PlatonicSolid<>(type, size.times(multiplier));
    }

    @Override
    public PlatonicSolid<E, C> times(double multiplier) {
        return new PlatonicSolid<>(type, size.times(multiplier));
    }

    public Sphere<E, C> circumscribedSphere() {
        throw new UnsupportedOperationException("Circumscribed sphere not implemented for " + type);
    }

    @Override
    public String toString() {
        return String.format("%s, edge size: %s", type.name(), size);
    }

    @Override
    public boolean strictlyEquals(Object o) {
        return UncertainUtils.strictlyEqual(this, o, PlatonicSolid::size);
    }
}
