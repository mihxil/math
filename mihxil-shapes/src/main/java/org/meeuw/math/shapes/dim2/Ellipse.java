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
    @NonExact("Integration needs to be done.")
    public F perimeter() {
        if (field instanceof CompleteScalarField) {
            CompleteScalarField<?> completeField = (CompleteScalarField) field;

            CompleteFieldElement<?> sum = (CompleteFieldElement<?>) radiusx.plus(radiusy);
            CompleteFieldElement<?> sqr = sum.sqr();
            CompleteFieldElement<?> h = ((CompleteFieldElement<?>) radiusx.minus(radiusy)).sqr();
            //h.dividedBy(sqr);

            // TODO  James Ivory,[4] Bessel[5] and Kummer,[6]
            /*
        F result = field.one();
        for (int n = 1; n <= 10; n++) {
            F term = h.pow(n).dividedBy()

            result = result.plus(term);
        }
        throw new UnsupportedOperationException("Perimeter of an ellipse is not implemented yet, see");
        */
            // Ramanujan's (second) approximation:

            //return h.times(3).dividedBy(completeField.one().times(10).plus(field.one().times(4).minus(h.times(3)).sqrt()) + 10 * h).sqrt().times(2).times(sum);
            throw new UnsupportedOperationException("Perimeter of an ellipse is not implemented yet");
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

    @Override
    public boolean strictlyEquals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Circle)) return false;
        Ellipse<?> circle = (Ellipse<?>) o;
        return radiusx instanceof Uncertain ?
            ((Uncertain) radiusx).strictlyEquals(circle.radiusx) &&
                ((Uncertain) radiusy).strictlyEquals(circle.radiusy)
            :
            radiusx.equals(circle.radiusx) &&
            radiusy.equals(circle.radiusy);

    }

    @Override
    public boolean eq(Ellipse<F> other) {
        return  this.radiusx.eq(other.radiusx);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Circle)) return false;
        Ellipse<?> ellipse = (Ellipse<?>) o;
        if (!ellipse.field.equals(field)) {
            return false;
        }
        return eq((Ellipse<F>) ellipse);
    }

    @Override
    public int hashCode() {
        return radiusx.hashCode() + radiusy.hashCode();
    }
}
