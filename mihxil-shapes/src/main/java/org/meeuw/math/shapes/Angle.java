package org.meeuw.math.shapes;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalField;
import org.meeuw.math.abstractalgebra.circlegroup.CircleElement;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;

/**
 * representation of an angle
 */
public class Angle extends CircleElement<RationalNumber, BigDecimalElement>  {

    public static Angle ZERO = new Angle(RationalNumber.ZERO);

    public static Angle degrees(int degrees) {
        return new Angle(RationalNumber.of(degrees).dividedBy(360));
    }

    public static Angle turns(RationalNumber turns) {
        return new Angle(turns);
    }


    private static final RationalNumber QUARTER = RationalNumber.ONE.dividedBy(4);
    private static final RationalNumber HALF   = QUARTER.times(2);
    private static final RationalNumber THREE_QUARTER   = QUARTER.times(3);


    protected Angle(RationalNumber turns) {
        super(TurnsGroup.INSTANCE, turns);
    }

    public BigDecimalElement radians() {
        return radians(BigDecimalField.INSTANCE);
    }

    public <C extends CompleteFieldElement<C>> C radians(CompleteField<C> field) {
        return field.pi().times(2).times(value.getNumerator()).dividedBy(value.getDenominator());
    }


    public boolean isZero() {
        return value.equals(RationalNumber.ZERO);
    }

    public boolean isRight() {
        return value.equals(QUARTER) || value.equals(THREE_QUARTER);
    }

    public boolean isStraight() {
        return value.equals(HALF);
    }
    public RationalNumber  degrees() {
        return value.times(360);
    }

    public BigDecimalElement sin() {
        if (isZero() || isStraight()) {
            return BigDecimalElement.ZERO;
        }
        if (value.equals(QUARTER)) {
            return BigDecimalElement.ONE;
        }
        if (value.equals(THREE_QUARTER)) {
            return BigDecimalElement.ONE.negation();
        }
        return radians().sin();
    }

    public BigDecimalElement cos() {
        if (isZero()) {
            return BigDecimalElement.ONE;
        }
        if (isRight()) {
            return BigDecimalElement.ZERO;
        }
        if (isStraight()) {
            return BigDecimalElement.ONE.negation();
        }
        return radians().cos();
    }

    public RationalNumber  turns() {
        return value;
    }

    public Angle plus(Angle other) {
        return new Angle(value.plus(other.value));
    }




}
