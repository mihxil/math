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

    final Circle<RealNumber, RealNumber> circle = new Circle<>(element(5.0));

    @Test
    public void atOriginShape() {
        var located = LocatedShape.atOrigin(circle);
        assertThat(located.shape()).isSameAs(circle);
    }

    @Test
    public void atOriginLocation() {
        var located = LocatedShape.atOrigin(circle);
        assertThat(located.location().isZero()).isTrue();
    }

    @Test
    public void atOriginToString() {
        var located = LocatedShape.atOrigin(circle);
        log.info("Located shape: " + located);
        // At origin, no " at " suffix
        assertThat(located.toString()).doesNotContain(" at ");
    }

    @Test
    public void withLocationToString() {
        var location = FieldVector2.of(element(1.0), element(2.0));
        var located = new LocatedShape<>(circle, location);
        log.info("Located shape at offset: " + located);
        assertThat(located.toString()).contains(" at ");
    }

    @Test
    public void equalsAndHashCode() {
        var a = LocatedShape.atOrigin(circle);
        var b = LocatedShape.atOrigin(circle);
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
