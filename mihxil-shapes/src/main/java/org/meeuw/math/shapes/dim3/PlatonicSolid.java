package org.meeuw.math.shapes.dim3;

import lombok.Getter;

import org.meeuw.math.abstractalgebra.ScalarField;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;

@Getter
public class PlatonicSolid<F extends ScalarFieldElement<F>> implements Polyhedron<F, PlatonicSolid<F>> {

    private final PlatonicSolidEnum platonicSolidEnum;

    private final ScalarField<F> field;
    private final F size;


    public PlatonicSolid(PlatonicSolidEnum platonicSolidEnum, F size) {
        this.platonicSolidEnum = platonicSolidEnum;
        this.size = size;
        this.field = size.getStructure();
    }

    @Override
    public int vertices() {
        return platonicSolidEnum.vertices();
    }

    @Override
    public int edges() {
        return platonicSolidEnum.edges();
    }

    @Override
    public int faces() {
        return platonicSolidEnum.faces();
    }

    public F dihedralAngle() {
        var p = platonicSolidEnum.p();
        var q = platonicSolidEnum.q();
        return
            ((field.pi().dividedBy(q)).cos())
                .dividedBy(
                    field.pi().dividedBy(p).sin()
                ).asin().times(2);
    }

    public F inradius() {
        var theta = dihedralAngle();
        return size().dividedBy(2).dividedBy(
            field.pi().dividedBy(platonicSolidEnum.p()).tan()
        ).times(theta.dividedBy(2).tan());
    }

    public F circumradius() {
        var theta = dihedralAngle();
        return size().dividedBy(2).times(
            field.pi().dividedBy(platonicSolidEnum.q()).tan()
        ).times(theta.dividedBy(2).tan());
    }

    @Override
    public F volume() {
        return inradius().times(surfaceArea()).dividedBy(3);
    }



    @Override
    public F surfaceArea() {
        return size().dividedBy(2).sqr().times((long) faces() * platonicSolidEnum.p())
            .dividedBy(field.pi().dividedBy(platonicSolidEnum.p()).tan());
    }



    @Override
    public boolean eq(PlatonicSolid<F> other) {
        return false;
    }

    public Sphere<F> circumscribedSphere() {
        throw new UnsupportedOperationException("Circumscribed sphere not implemented for " + platonicSolidEnum);
    }

    @Override
    public String toString() {
        return String.format("%s, edge size: %s", platonicSolidEnum.name(), size);
    }
}
