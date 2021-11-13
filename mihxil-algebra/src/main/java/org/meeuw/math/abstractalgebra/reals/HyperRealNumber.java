package org.meeuw.math.abstractalgebra.reals;

import lombok.Getter;

import java.math.BigDecimal;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.exceptions.InvalidUncertaintyException;
import org.meeuw.math.text.TextUtils;
import org.meeuw.math.text.spi.FormatService;
import org.meeuw.math.uncertainnumbers.DoubleConfidenceInterval;
import org.meeuw.math.uncertainnumbers.UncertainDouble;

import static org.meeuw.math.Utils.uncertaintyForDouble;

/**
 * A real number (backend by a double). It is uncertain, but only because of rounding errors.
 *
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public class HyperRealNumber
    implements
    FieldElement<HyperRealNumber>,
    UncertainDouble<HyperRealNumber>,
    Ordered<HyperRealNumber>
{
    public static final int EPSILON_FACTOR = 2;
    public static final double UNCERTAINTY_FOR_ONE = Utils.uncertaintyForDouble(1);
    public static final double UNCERTAINTY_FOR_ZERO = Utils.uncertaintyForDouble(0);

    public static final HyperRealNumber ONE = new HyperRealNumber(1d, 0);
    public static final HyperRealNumber ZERO = new HyperRealNumber(0d, 0);
    public static final HyperRealNumber OMEGA = new HyperRealNumber(Double.POSITIVE_INFINITY, 0);
    public static final HyperRealNumber SMALLEST = new HyperRealNumber(0d, UNCERTAINTY_FOR_ZERO);

    @Getter
    final double value;

    //@Getter final int omagas;

    @Getter
    final double uncertainty;

    public static HyperRealNumber of(double value) {
        return new HyperRealNumber(value, uncertaintyForDouble(value));
    }

    public HyperRealNumber(double value, double uncertainty) {
        this.value = value;
        if (uncertainty < 0) {
            throw new InvalidUncertaintyException("Uncertainty cannot be negative");
        }
        this.uncertainty = uncertainty;
    }

    @Override
    public HyperRealNumber plus(HyperRealNumber summand) {
        double newValue = value + summand.value;
        return _of(
            newValue,
            uncertainty + summand.uncertainty + uncertaintyForDouble(newValue)
        );
    }

    @Override
    public HyperRealNumber negation() {
        return _of(-1 * value, uncertainty);
    }

    @Override
    public HyperRealNumber minus(HyperRealNumber subtrahend) {
        return plus(subtrahend.negation());
    }

    @Override
    public HyperRealNumber times(HyperRealNumber multiplier) {
        if (multiplier == ONE) {
            return this;
        }
        return HyperRealField.INSTANCE.considerMultiplicationBySpecialValues(this, multiplier);
    }

    protected boolean isExactlyZero() {
        return value == 0 && isExact();
    }
    @Override
    public boolean isExact() {
        return uncertainty == 0;
    }

    @Override
    public HyperRealNumber pow(int exponent) {
        double newValue = Math.pow(value, exponent);
        if (value == 0 && exponent < 0) {
            throw new DivisionByZeroException("0" + TextUtils.superscript(exponent));
        }
        return new HyperRealNumber(newValue,
            uncertainty * (Math.abs(exponent) *  Math.abs(Utils.pow(value, exponent - 1))) +  uncertaintyForDouble(newValue)
        );
    }

    @Override
    public HyperRealNumber dividedBy(HyperRealNumber divisor) {
        return times(divisor.reciprocal());
    }

    @Override
    public HyperRealField getStructure() {
        return HyperRealField.INSTANCE;
    }

    @Override
    public boolean isZero() {
        return FieldElement.super.isZero();
    }

    @Override
    public HyperRealNumber reciprocal() {
        if (isZero()) {
            throw new DivisionByZeroException("Reciprocal of zero");
        }
        return pow(-1);
    }

    @Override
    public long longValue() {
        return Math.round(value);
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return new BigDecimal(value);
    }

    @Override
    public boolean isOne() {
        return isExact() && value == 1;
    }

    @Override
    public int signum() {
        return (int) Math.signum(value);
    }

    @Override
    public HyperRealNumber _of(double value, double uncertainty) {
        return new HyperRealNumber(value, uncertainty);
    }

    @Override
    public HyperRealNumber times(double multiplier) {
        return _of(value * multiplier, uncertainty * Math.abs(multiplier));
    }



    @Override
    public int compareTo(HyperRealNumber o) {
        if (confidenceEquals(o)) {
            return 0;
        }
        return Double.compare(value, o.value);
    }

    @Override
    public HyperRealNumber sqr() {
        double sq = value * value;
        return _of(sq, sq * getFractionalUncertainty() * 2);
    }


    @Override
    public HyperRealNumber abs() {
        return _of(Math.abs(value), uncertainty);
    }

    public DoubleConfidenceInterval getConfidenceInterval() {
        return DoubleConfidenceInterval.of(doubleValue(), getUncertainty(), EPSILON_FACTOR);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HyperRealNumber that = (HyperRealNumber) o;
        return confidenceEquals(that);
    }

    protected boolean confidenceEquals(HyperRealNumber that) {
        return getConfidenceInterval().contains(that.value) || that.getConfidenceInterval().contains(value);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return FormatService.toString(this);
    }

}
