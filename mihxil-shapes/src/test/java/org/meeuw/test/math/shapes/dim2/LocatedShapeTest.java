package org.meeuw.test.math.shapes.dim2;

import lombok.extern.java.Log;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.dim2.FieldVector2;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.shapes.dim2.Circle;
import org.meeuw.math.shapes.dim2.LocatedShape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.reals.RealField.element;

@Log
public class LocatedShapeTest {

    final Circle<RealNumber> circle = new Circle<>(element(5.0));

    @Test
    public void atOriginShape() {
        LocatedShape<RealNumber, Circle<RealNumber>> located = LocatedShape.atOrigin(circle);
        assertThat(located.shape()).isSameAs(circle);
    }

    @Test
    public void atOriginLocation() {
        LocatedShape<RealNumber, Circle<RealNumber>> located = LocatedShape.atOrigin(circle);
        assertThat(located.location().isZero()).isTrue();
    }

    @Test
    public void atOriginToString() {
        LocatedShape<RealNumber, Circle<RealNumber>> located = LocatedShape.atOrigin(circle);
        log.info("Located shape: " + located);
        // At origin, no " at " suffix
        assertThat(located.toString()).doesNotContain(" at ");
    }

    @Test
    public void withLocationToString() {
        FieldVector2<RealNumber> location = FieldVector2.of(element(1.0), element(2.0));
        LocatedShape<RealNumber, Circle<RealNumber>> located = new LocatedShape<>(circle, location);
        log.info("Located shape at offset: " + located);
        assertThat(located.toString()).contains(" at ");
    }

    @Test
    public void equalsAndHashCode() {
        LocatedShape<RealNumber, Circle<RealNumber>> a = LocatedShape.atOrigin(circle);
        LocatedShape<RealNumber, Circle<RealNumber>> b = LocatedShape.atOrigin(circle);
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
