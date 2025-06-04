package org.meeuw.math.shapes.dim2;

import jakarta.validation.constraints.Min;
import lombok.Getter;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.CompleteScalarField;
import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.dim2.FieldVector2;

/**
 * Regular polygon with n sides, all of equal length.
 */
public  class RegularPolygon<F extends CompleteScalarFieldElement<F>> implements Polygon<F, RegularPolygon<F>> {

    private final int n;
    private final F size;

    @Getter
    private final CompleteScalarField<F> field;


    public RegularPolygon(@Min(3) int n, F size) {
        this.n = n;
        this.size = size;
        this.field = size.getStructure();
    }

    public F size() {
        return size;
    }
    public int n() {
        return n;
    }
    @Override
    public F perimeter() {
        return size.times(n);
    }

    @Override
    public F area() {
        return field.pi().dividedBy(n).cot()
            .times(size.sqr()).times(n).dividedBy(4);
    }

    @Override
    public Rectangle<F> circumscribedRectangle(F angle) {
        if (angle.isZero()) {
            F inscribed = inscribedRadius();
            F circumscribed = circumscribedRadius();
            if (n == 3) {
                return new Rectangle<>(
                    size,
                    inscribed.plus(circumscribed)
                );
            }
            if (n == 4) {
                return new Rectangle<>(size, size);
            }
            if (n % 2 == 0) {
                return new Rectangle<>(
                    circumscribed.times(2),
                    inscribed.times(2)
                );
            } else {
                throw new UnsupportedOperationException("Circumscribed rectangle is not yet implemented for NGon");
            }
        }
        throw new UnsupportedOperationException("Circumscribed rectangle is not yet implemented for NGon");


    }

    @Override
    public Circle<F> circumscribedCircle() {
        return new Circle<>(circumscribedRadius());
    }

    public F interiorAngle() {
        return field.pi().times(n - 2).dividedBy(n);
    }

    public F inscribedRadius() {
        return field.pi().dividedBy(n).cot().times(size).dividedBy(2);
    }

    public F circumscribedRadius() {
        return field.pi().dividedBy(n).csc().times(size).dividedBy(2);
    }

    /**
     * Schläfli symbol
     */
    @Override
    public String toString() {
        return String.format("{%d}, size: %s", n, size.toString());
    }

    @Override
    public boolean eq(RegularPolygon<F> other) {
        return  this.size.eq(other.size) && this.n == other.n;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegularPolygon<?>)) return false;
        RegularPolygon<?> ngon = (RegularPolygon<?>) o;
        if (!ngon.field.equals(field)) {
            return false;
        }
        return eq((RegularPolygon<F>) ngon);
    }

    @Override
    public int hashCode() {
        return size.hashCode() + 31 * n + 13 * field.hashCode();
    }

    @Override
    public int numberOfEdges() {
        return n;
    }

    @Override
    public Stream<FieldVector2<F>> vertices() {
        F radius = circumscribedRadius();
        F step = field.pi().times(2).dividedBy(n);
        return IntStream.range(0, n)
            .mapToObj(i -> {
                F angle = step.times(i);
                return FieldVector2.of(angle.sin().times(size), angle.cos().times(size)
                );
            });
    }
}
