package org.meeuw.math.shapes;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalField;
import org.meeuw.math.abstractalgebra.circlegroup.CircleElement;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;

/**
 * An orientation represented as an exact fraction of one complete turn.
 *
 * <p>Angles are elements of the additive circle group: values differing by a whole number of
 * turns represent the same angle. Their canonical representation is therefore in the half-open
 * interval {@code [0, 1)}. For example, {@code 0}, {@code 1}, and {@code -1} all denote the
 * zero angle; {@code 1/4}, {@code 1/2}, and {@code 3/4} denote right, straight, and three-quarter
 * turns respectively.</p>
 *
 * <p>Use {@link #degrees(int)} to construct an angle from degrees, or {@link #turns(RationalNumber)}
 * when an exact turn fraction is available.</p>
 *
 * @since 0.21
 */
public class Angle extends CircleElement<RationalNumber, BigDecimalElement>  {

    /**
     * The zero angle.
     */
    public static Angle ZERO = new Angle(RationalNumber.ZERO);

    /**
     * Creates an angle from a number of degrees.
     *
     * <p>The supplied value is normalized modulo {@code 360}; negative values and values exceeding
     * one turn are accepted.</p>
     *
     * @param degrees the angle in degrees
     * @return the corresponding normalized angle
     */
    public static Angle degrees(int degrees) {
        return new Angle(RationalNumber.of(degrees).dividedBy(360));
    }

    /**
     * Creates an angle from an exact fraction of a complete turn.
     *
     * <p>The supplied value is normalized modulo one turn.</p>
     *
     * @param turns the fraction of a complete turn
     * @return the corresponding normalized angle
     */
    public static Angle turns(RationalNumber turns) {
        return new Angle(turns);
    }


    private static final RationalNumber QUARTER = RationalNumber.ONE.dividedBy(4);
    private static final RationalNumber HALF   = QUARTER.times(2);
    private static final RationalNumber THREE_QUARTER   = QUARTER.times(3);


    /**
     * Creates an angle from an exact turn fraction.
     *
     * @param turns the fraction of a complete turn
     */
    protected Angle(RationalNumber turns) {
        super(TurnsGroup.INSTANCE, turns);
    }

    /**
     * Returns this angle in radians, approximated by a {@link BigDecimalElement}.
     *
     * @return the angle in radians
     */
    public BigDecimalElement radians() {
        return radians(BigDecimalField.INSTANCE);
    }

    /**
     * Returns this angle in radians in the supplied complete field.
     *
     * @param field the field that provides the approximation of pi
     * @param <C> the element type of the complete field
     * @return {@code 2 * pi * turns()}
     */
    public <C extends CompleteFieldElement<C>> C radians(CompleteField<C> field) {
        return field.pi().times(2).times(value.getNumerator()).dividedBy(value.getDenominator());
    }

    /**
     * Tests whether this is the zero angle.
     *
     * @return whether this angle is congruent to zero turns
     */
    public boolean isZero() {
        return value.equals(RationalNumber.ZERO);
    }

    /**
     * Tests whether this is a right angle, in either direction.
     *
     * @return whether this angle is one-quarter or three-quarters of a turn
     */
    public boolean isRight() {
        return value.equals(QUARTER) || value.equals(THREE_QUARTER);
    }

    /**
     * Tests whether this is a straight angle.
     *
     * @return whether this angle is one-half turn
     */
    public boolean isStraight() {
        return value.equals(HALF);
    }

    /**
     * Returns this angle as an exact number of degrees.
     *
     * @return the normalized turn fraction multiplied by {@code 360}
     */
    public RationalNumber  degrees() {
        return value.times(360);
    }

    /**
     * Returns the sine of this angle.
     *
     * <p>Cardinal angles return exact values; other angles are approximated in the big-decimal
     * field.</p>
     *
     * @return the sine of this angle
     */
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

    /**
     * Returns the cosine of this angle.
     *
     * <p>Cardinal angles return exact values; other angles are approximated in the big-decimal
     * field.</p>
     *
     * @return the cosine of this angle
     */
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

    /**
     * Returns this angle as an exact, normalized fraction of a complete turn.
     *
     * @return a rational number in {@code [0, 1)}
     */
    public RationalNumber turns() {
        return value;
    }

    /**
     * Adds another angle, normalizing the result modulo one complete turn.
     *
     * @param other the angle to add
     * @return the normalized sum
     */
    public Angle plus(Angle other) {
        return new Angle(value.plus(other.value));
    }




}
