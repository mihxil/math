package org.meeuw.math.shapes.dim2;

import jakarta.validation.constraints.Min;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.CompleteScalarField;
import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.dihedral.DihedralGroup;
import org.meeuw.math.abstractalgebra.dim2.FieldVector2;

import static org.meeuw.math.shapes.dim2.LocatedShape.atOrigin;

/**
 * Regular polygon with n sides, all of equal length.
 */
public  class RegularPolygon<F extends CompleteScalarFieldElement<F>> implements Polygon<F, RegularPolygon<F>> {

    private final int n;
    private final F size;
    private final CompleteScalarField<F> field;

    public RegularPolygon(@Min(3) int n, F size) {
        this.n = n;
        this.size = size;
        this.field = size.getStructure();
    }

    public static <F extends CompleteScalarFieldElement<F>> RegularPolygon<F> withCircumScribedRadius(int n, F radius) {
        return new RegularPolygon<>(n, radius.times(2).times(radius.getStructure().pi().dividedBy(n).sin()));
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

    public DihedralGroup dihedralGroup() {
        return DihedralGroup.of(n);
    }
    public static <F extends CompleteScalarFieldElement<F>> RegularPolygon<F> of(DihedralGroup gropu, F size) {
        return new RegularPolygon<>(gropu.getN(), size);
    }

/*    @Override
    public LocatedShape<F, Rectangle<F>> circumscribedRectangle(F angle) {
        if (angle.isZero()) {
            F inscribed = inscribedRadius();
            F circumscribed = circumscribedRadius();
            if (n == 3) {
                return atOrigin(new Rectangle<>(
                    size,
                    inscribed.plus(circumscribed)
                ));
            }
            if (n == 4) {
                return atOrigin(new Rectangle<>(size, size));
            }
            if (n % 2 == 0) {
                return atOrigin(new Rectangle<>(
                    circumscribed.times(2),
                    inscribed.times(2)
                ));
            } else {
                throw new UnsupportedOperationException("Circumscribed rectangle is not yet implemented for NGon");
            }
        }
        throw new UnsupportedOperationException("Circumscribed rectangle is not yet implemented for NGon");


    }*/

    @Override
    public LocatedShape<F, Circle<F>> circumscribedCircle() {
        return atOrigin(new Circle<>(circumscribedRadius()));
    }

    @Override
    public CompleteScalarField<F> field() {
        return field;
    }

    public F interiorAngle() {
        return field.pi().times(n - 2).dividedBy(n);
    }

    public F inscribedRadius() {
        return field.pi().dividedBy(n).cot().times(size).dividedBy(2);
    }

    public Circle<F> inscribedCircle() {
        return new Circle<>(inscribedRadius());
    }

    public F circumscribedRadius() {
        return size.dividedBy(field.pi().dividedBy(n).sin().times(2));
    }

    /**
     * Schläfli symbol
     */
    @Override
    public String toString() {
        if (size.isOne()) {
            return String.format("{%d}", n);
        } else {
            return String.format("{%d}, size: %s", n, size);
        }
    }

    @Override
    public boolean eq(RegularPolygon<F> other) {
        return  this.size.eq(other.size) && this.n == other.n;
    }

    @Override
    public RegularPolygon<F> times(F multiplier) {
        return new RegularPolygon<>(n, size.times(multiplier));
    }

    @Override
    public RegularPolygon<F> times(int multiplier) {
        return new RegularPolygon<>(n, size.times(multiplier));
    }

    @Override
    public RegularPolygon<F> times(double multiplier) {
        return new RegularPolygon<>(n, size.times(multiplier));
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
        F offset;
        if (n % 2 == 1) {
            offset = field.pi().dividedBy(2).negation();
        } else if (n % 4 == 0) {
            offset = field.pi().dividedBy(n).negation();
        } else {
            offset = field.zero();
        }
        return IntStream.range(0, n)
            .mapToObj(i -> {
                F angle = offset.plus(step.times(i));
                return FieldVector2.of(
                    angle.cos().times(radius), angle.sin().times(radius)
                );
            });
    }
}
