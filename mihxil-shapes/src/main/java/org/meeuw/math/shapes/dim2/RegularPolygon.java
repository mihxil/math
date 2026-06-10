package org.meeuw.math.shapes.dim2;

import jakarta.validation.constraints.Min;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.checkerframework.checker.units.qual.radians;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.dihedral.DihedralGroup;
import org.meeuw.math.abstractalgebra.dim2.FieldVector2;

import static org.meeuw.math.shapes.dim2.LocatedShape.atOrigin;
import static org.meeuw.math.uncertainnumbers.UncertainUtils.areExact;
import static org.meeuw.math.uncertainnumbers.UncertainUtils.strictlyEqual;

/**
 * Regular polygon with n sides, all of equal length.
 */
public  class RegularPolygon<E extends ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>> implements Polygon<E, C,  RegularPolygon<E, C>> {

    private final int n;
    private final E size;
    private final E angle;
    private final ScalarField<E, C> field;

    public RegularPolygon(@Min(3) int n, E size, @radians E angle) {
        this.n = n;
        this.size = size;
        this.angle = angle;
        this.field = size.getStructure();
    }
    public RegularPolygon(@Min(3) int n, E size) {
        this(n, size, size.getStructure().zero());
    }


    public static <E extends ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>> RegularPolygon<C, C> withCircumScribedRadius(int n, E radius) {
        return new RegularPolygon<>(n, radius.complete().times(2).times(radius.getStructure().pi().dividedBy(n).sin()), radius.getStructure().zero().complete());
    }

    public E size() {
        return size;
    }
    public int n() {
        return n;
    }
    @Override
    public C perimeter() {
        return exactPerimeter().complete();
    }

    public E exactPerimeter() {
        return size.times(n);
    }

    @Override
    public C area() {
        return field.pi().dividedBy(n).cot()
            .times(size.sqr().complete()).times(n).dividedBy(4);
    }

    @SuppressWarnings("unchecked")
    @Override
    public RegularPolygon<C, C> complete() {
        return new RegularPolygon<>(n, size.complete(), angle.complete());
    }

    public DihedralGroup dihedralGroup() {
        return DihedralGroup.of(n);
    }
    public static <E extends ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>> RegularPolygon<E, C> of(DihedralGroup group, E size) {
        return new RegularPolygon<>(group.getN(), size);
    }

    @Override
    public LocatedShape<C, C, Circle<C, C>> circumscribedCircle() {
        return atOrigin(new Circle<>(circumscribedRadius()));
    }

    @Override
    public ScalarField<E, C> field() {
        return field;
    }

    public C interiorAngle() {
        return field.pi().times(n - 2).dividedBy(n);
    }

    public C inscribedRadius() {
        return field.pi().dividedBy(n).cot().times(size.complete()).dividedBy(2);
    }

    public Circle<C, C> inscribedCircle() {
        return new Circle<>(inscribedRadius());
    }

    public C circumscribedRadius() {
        return size.complete().dividedBy(field.pi().dividedBy(n).sin().times(2));
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
    public boolean eq(RegularPolygon<E, C> other) {
        return  this.size.eq(other.size) && this.n == other.n;
    }

    @Override
    public RegularPolygon<E, C> times(E multiplier) {
        E newSize = size.times(multiplier);
        return new RegularPolygon<>(n, newSize, angle);
    }

    @Override
    public RegularPolygon<E, C> times(int multiplier) {
        return new RegularPolygon<>(n, size.times(multiplier), angle);
    }

    @Override
    public RegularPolygon<E, C> times(double multiplier) {
        return new RegularPolygon<>(n, size.times(multiplier), angle);
    }

    @Override
    public RegularPolygon<E, C> rotate(E angle) {
        return new RegularPolygon<>(n, size, this.angle.plus(angle));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegularPolygon<?, ?> ngon)) return false;
        if (!ngon.field.equals(field)) {
            return false;
        }
        return eq((RegularPolygon<E, C>) ngon);
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
    public Stream<FieldVector2<C, C>> vertices() {
        C radius = circumscribedRadius();
        C step = field.pi().times(2).dividedBy(n);
        C offset;
        if (n % 2 == 1) {
            offset = field.pi().dividedBy(2).negation();
        } else if (n % 4 == 0) {
            offset = field.pi().dividedBy(n).negation();
        } else {
            offset = field.zero().complete();
        }
        return IntStream.range(0, n)
            .mapToObj(i -> {
                @radians C angle = offset.plus(step.times(i)).plus(this.angle.complete());
                return FieldVector2.of(
                    angle.cos().times(radius), angle.sin().times(radius)
                );
            });
    }


    @Override
    public boolean isExact() {
        return Polygon.super.isExact() || areExact(size);
    }

    @Override
    public boolean strictlyEquals(Object o) {
        return strictlyEqual(this, o, RegularPolygon::size);
    }

}
