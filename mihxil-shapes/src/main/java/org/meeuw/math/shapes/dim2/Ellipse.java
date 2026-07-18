package org.meeuw.math.shapes.dim2;

import jakarta.validation.constraints.Min;
import lombok.Getter;

import java.util.stream.Stream;

import org.checkerframework.checker.units.qual.radians;
import org.meeuw.math.ComparableUtils;
import org.meeuw.math.NonExact;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.shapes.Info;
import org.meeuw.math.uncertainnumbers.Uncertain;

import static org.meeuw.math.shapes.Info.Key.*;
import static org.meeuw.math.shapes.dim2.LocatedShape.atOrigin;
import static org.meeuw.math.uncertainnumbers.UncertainUtils.areExact;
import static org.meeuw.math.uncertainnumbers.UncertainUtils.strictlyEqual;

/**
 *  An ellipse in a two-dimensional shape, defined by 2 radii, and an angle.
 */
@Getter
public class Ellipse <E extends ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>> implements Shape<E, C, Ellipse<E, C>>, Uncertain {

    private final E radiusx;
    private final E radiusy;
    private final E angle;
    private final ScalarField<E, C> field;

    /**
     */
    public Ellipse(@Min(0) E radiusx, @Min(0) E radiusy, @radians E angle) {
        this.radiusx = radiusx;
        this.radiusy = radiusy;
        this.angle = angle;
        this.field = radiusx.getStructure();
        assert ! this.radiusx.isNegative();
        assert ! this.radiusy.isNegative();
    }

    public Ellipse(@Min(0) E radiusx, @Min(0) E radiusy) {
        this(radiusx, radiusy, radiusx.getStructure().zero());
    }

    public Stream<Info> info() {
        return Stream.concat(
            Shape.super.info(),
            Stream.of(
                new Info(RADIUSX, this::radiusx),
                new Info(RADIUSY, this::radiusy),
                new Info(ANGLE, this::angle),
                new Info(LINEAR_ECCENTRICITY, this::linearEccentricity),
                new Info(ECCENTRICITY, this::eccentricity)
            )
        );
    }

    @Override
    public Ellipse<E, C> times(E multiplier) {
        return new Ellipse<>(radiusx.times(multiplier), radiusy.times(multiplier), angle);
    }
    @Override
    public Ellipse<E, C> times(int multiplier) {
        return new Ellipse<>(radiusx.times(multiplier), radiusy.times(multiplier), angle);
    }

    @Override
    public Ellipse<E, C> times(double multiplier) {
        return new Ellipse<>(radiusx.times(multiplier), radiusy.times(multiplier), angle);
    }

    @Override
    public Ellipse<E, C> rotate(E angle) {
        return new Ellipse<>(radiusx, radiusy, this.angle.plus(angle));
    }

    public C linearEccentricity() { // linear eccentricity is the distance from the center to a focus
        E maxRadius = ComparableUtils.max(radiusx, radiusy);
        E minRadius = maxRadius == radiusx ? radiusy : radiusx;
        return  maxRadius.sqr().minus(minRadius.sqr()).sqrt();
    }

    public C eccentricity() {
        E maxRadius = ComparableUtils.max(radiusx, radiusy);
        E minRadius = maxRadius == radiusx ? radiusy : radiusx;
        return  minRadius.dividedBy(maxRadius).sqr().negation().plus(field.one()).sqrt();
    }

    @Override
    @NonExact("Area can only be computed well for complete scalar fields")
    public C area() {
        return field.pi().times(radiusx.complete()).times(radiusy.complete());
    }

    @Override
    public LocatedShape<C, C, Rectangle<C, C>> circumscribedRectangle() {

        if (angle.isZero()) {
            return atOrigin(
                new Rectangle<>(radiusx.complete().times(2), radiusy.complete().times(2), field.zero().complete())
            );
        } else {
            C sin2 = angle.sin().sqr();
            C cos2 = angle.cos().sqr();
            C height = radiusx.complete().sqr().times(sin2).plus(radiusy.sqr().complete().times(cos2)).sqrt().times(2);
            C width = radiusx.complete().sqr().times(cos2).plus(radiusy.sqr().complete().times(sin2)).sqrt().times(2);

            return atOrigin(
                new Rectangle<>(
                    width,
                    height,
                    field.completedField().zero()
                )
            );
        }
    }

    @Override
    public LocatedShape<C, C, Circle<C, C>> circumscribedCircle() {
        return atOrigin(
            new Circle<>(ComparableUtils.max(radiusx, radiusy).complete())
        );
    }

    @Override
    public ScalarField<E, C> field() {
        return field;
    }

    @Override
    @NonExact("Integration needs to be done, currently only Ramanujan's approximation is implemented")
    public C perimeter() {
        return perimeterRamanujan(radiusx, radiusy);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Ellipse<C, C> complete() {
        return new Ellipse<>(radiusx.complete(), radiusy.complete(), angle.complete());
    }

    public String toString() {
        return "Ellipse{" + radiusx + ',' + radiusy + '}';
    }

    @Override
    public boolean isExact() {
        return Shape.super.isExact() || areExact(radiusx, radiusy, angle);
    }

    @Override
    public boolean strictlyEquals(Object o) {
        return strictlyEqual(this, o, Ellipse::radiusx, Ellipse::radiusy, Ellipse::angle);
    }

    @Override
    public boolean eq(Ellipse<E, C> other) {
        return  this.radiusx.eq(other.radiusx) && this.radiusy.eq(other.radiusy);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ellipse)) return false;
        Ellipse<?, ?> ellipse = (Ellipse<?, ?>) o;
        if (!ellipse.field.equals(field)) {
            return false;
        }
        return eq((Ellipse<E, C>) ellipse);
    }

    @Override
    public int hashCode() {
        return radiusx.hashCode() + 13 * radiusy.hashCode();
    }

    /**
     * Computes the perimeter of an ellipse using Ramanujan's second approximation.
     *
     * @param radiusx the semi-major axis
     * @param radiusy the semi-minor axis
     * @return the approximate perimeter of the ellipse
     */

    @NonExact
    public static <E extends  ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>> C perimeterRamanujan(E radiusx, E radiusy) {
        E sum = radiusx.plus(radiusy);
        C h = radiusx.minus(radiusy).sqr().dividedBy(sum.sqr()).complete();
        C pi = radiusx.getStructure().pi();
        C one = radiusx.getStructure().one().complete();
        C ten = radiusx.getStructure().element(10).complete();
        C four = radiusx.getStructure().element(4).complete();
        // Ramanujan's (second) approximation:

        return pi.times(sum.complete()).times(
            one.plus(
                h.times(3).dividedBy(
                    ten.plus(
                        (four.minus(h.times(3))).sqrt()
                    )
                )
            )
        );


    }
}
