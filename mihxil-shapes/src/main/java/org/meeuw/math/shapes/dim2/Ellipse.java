package org.meeuw.math.shapes.dim2;

import jakarta.validation.constraints.Min;

import org.meeuw.math.ComparableUtils;
import org.meeuw.math.NonExact;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.FieldIncompleteException;
import org.meeuw.math.uncertainnumbers.Uncertain;

import static org.meeuw.math.shapes.dim2.LocatedShape.atOrigin;

public class Ellipse <F extends ScalarFieldElement<F>> implements Shape<F, Ellipse<F>>, Uncertain {

    private final F radiusx;
    private final F radiusy;
    private final ScalarField<F> field;

    /**
     */
    public Ellipse(@Min(0) F radiusx, @Min(0) F radiusy) {
        this.radiusx = radiusx;
        this.radiusy = radiusy;
        this.field = radiusx.getStructure();
    }

    @Override
    public Ellipse<F> times(F multiplier) {
        return new Ellipse<>(radiusx.times(multiplier), radiusy.times(multiplier));
    }
    @Override
    public Ellipse<F> times(int multiplier) {
        return new Ellipse<>(radiusx.times(multiplier), radiusy.times(multiplier));
    }

    @Override
    public Ellipse<F> times(double multiplier) {
        return new Ellipse<>(radiusx.times(multiplier), radiusy.times(multiplier));
    }

    public F radiusx() {
        return radiusx;
    }

    public F radiusy() {
        return radiusy;
    }

    @SuppressWarnings("unchecked")
    public F linearEccentricity() { // linear eccentricity is the distance from the center to a focus
        if (field instanceof CompleteScalarField) {
            F maxRadius = ComparableUtils.max(radiusx, radiusy);
            F minRadius = maxRadius == radiusx ? radiusy : radiusx;
            return (F) ((CompleteFieldElement<?>) maxRadius.sqr().minus(minRadius.sqr())).sqrt();
        } else {
            throw new FieldIncompleteException("linearEccentricity can only be computed well for complete scalar fields");
        }
    }


    @SuppressWarnings("unchecked")
    public F eccentricity() {
        if (field instanceof CompleteScalarField) {
            F maxRadius = ComparableUtils.max(radiusx, radiusy);
            F minRadius = maxRadius == radiusx ? radiusy : radiusx;
            return (F) ((CompleteFieldElement<?>) minRadius.dividedBy(maxRadius).sqr().negation().plus(field.one())).sqrt();
        } else {
            throw new FieldIncompleteException("eccentricity can only be computed well for complete scalar fields");
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    @NonExact("Area can only be computed well for complete scalar fields")
    public F area() {
        if (field instanceof CompleteScalarField) {
            CompleteScalarField<?> completeField = (CompleteScalarField) field;
            return ((F) completeField.pi()).times(radiusx).times(radiusy);
        } else {
            return radiusx.times(radiusy).times(Math.PI);
        }
    }

    @Override
    public LocatedShape<F, Rectangle<F>> circumscribedRectangle(F angle) {
        return atOrigin(
            new Rectangle<>(radiusx.times(2), radiusy.times(2))
        );
    }

    @Override
    public LocatedShape<F, Circle<F>> circumscribedCircle() {
        return atOrigin(
            new Circle<>(ComparableUtils.max(radiusx, radiusy))
        );
    }

    @Override
    public ScalarField<F> field() {
        return field;
    }



    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    @NonExact("Integration needs to be done, currently only Ramanujan's approximation is implemented")
    public F perimeter() {
        if (field instanceof CompleteScalarField) {
            return (F) perimeterRamanujan((CompleteScalarFieldElement) radiusx, (CompleteScalarFieldElement) radiusy);
        } else {
            throw new FieldIncompleteException("Perimeter can only be computed well for complete scalar fields");
        }

    }


    public String toString() {
        return "Ellipse{" + radiusx + ',' + radiusy + '}';
    }

    @Override
    public boolean isExact() {
        return !(radiusx instanceof Uncertain) || ((Uncertain) radiusx).isExact();
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public boolean strictlyEquals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ellipse<?>)) return false;
        Ellipse<?> ellipse = (Ellipse<?>) o;
        return radiusx instanceof Uncertain ?
            ((Uncertain) radiusx).strictlyEquals(ellipse.radiusx) &&
                ((Uncertain) radiusy).strictlyEquals(ellipse.radiusy)
            :
            radiusx.equals(ellipse.radiusx) &&
            radiusy.equals(ellipse.radiusy);

    }

    @Override
    public boolean eq(Ellipse<F> other) {
        return  this.radiusx.eq(other.radiusx) && this.radiusy.eq(other.radiusy);
    }


    @SuppressWarnings({"DataFlowIssue", "unchecked"})
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ellipse)) return false;
        Ellipse<?> ellipse = (Ellipse<?>) o;
        if (!ellipse.field.equals(field)) {
            return false;
        }
        return eq((Ellipse<F>) ellipse);
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
    public static <E extends  CompleteScalarFieldElement<E>> E perimeterRamanujan(E radiusx, E radiusy) {
        E sum = radiusx.plus(radiusy);
        E h = radiusx.minus(radiusy).sqr().dividedBy(sum.sqr());
        E pi = radiusx.getStructure().pi();
        E one = radiusx.getStructure().one();
        E ten = radiusx.getStructure().element(10);
        E four = radiusx.getStructure().element(4);
        // Ramanujan's (second) approximation:

        return pi.times(sum).times(
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
