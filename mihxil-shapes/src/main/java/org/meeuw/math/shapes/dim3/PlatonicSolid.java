package org.meeuw.math.shapes.dim3;

import org.meeuw.math.abstractalgebra.ScalarFieldElement;

public class PlatonicSolid<F extends ScalarFieldElement<F>> implements Polyhedron<F, PlatonicSolid<F>> {

    private final PlatonicSolidEnum platonicSolidEnum;

    public PlatonicSolid(PlatonicSolidEnum platonicSolidEnum) {
        this.platonicSolidEnum = platonicSolidEnum;
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

        return null; // TODO: implement dihedral angle calculation
    }

    @Override
    public F volume() {
        return null;
    }

    @Override
    public F surfaceArea() {
        return null;
    }



    @Override
    public boolean eq(PlatonicSolid<F> other) {
        return false;
    }

    public Sphere<F> circumscribedSphere() {
        throw new UnsupportedOperationException("Circumscribed sphere not implemented for " + platonicSolidEnum);
    }
}
