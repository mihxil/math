package org.meeuw.math.svg.dim3;

import java.util.Objects;

import org.meeuw.math.abstractalgebra.dim3.Vector3;

public record Plane(Vector3 point, Vector3 normal) {

    public Plane {
        Objects.requireNonNull(point, "point must not be null");
        Objects.requireNonNull(normal, "normal must not be null");
    }
}
