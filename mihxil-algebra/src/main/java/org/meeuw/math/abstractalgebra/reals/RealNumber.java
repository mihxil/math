package org.meeuw.math.abstractalgebra.reals;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Optional;

import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.complex.ComplexNumber;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.text.spi.FormatService;
import org.meeuw.math.uncertainnumbers.*;

import static org.meeuw.math.Utils.uncertaintyForDouble;
import static org.meeuw.math.text.TextUtils.superscript;

/**
 * A real number (backend by a double). It is uncertain, but only because of rounding errors.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class RealNumber
    implements
    CompleteScalarFieldElement<RealNumber>,
    MetricSpaceElement<RealNumber, RealNumber>,
    UncertainDouble<RealNumber>
{
    public static final int EPSILON_FACTOR = 2;
    public static final double UNCERTAINTY_FOR_ONE = Utils.uncertaintyForDouble(1);
    public static final double UNCERTAINTY_FOR_ZERO = Utils.uncertaintyForDouble(0);

    public static final RealNumber ONE = new RealNumber(1d, 0) {
        @Override
        public RealNumber reciprocal() {
            return this;
        }
    };
    public static final RealNumber ZERO = new RealNumber(0d, 0);
    public static final RealNumber SMALLEST = new RealNumber(0d, UNCERTAINTY_FOR_ZERO);

    @Getter
    final double value;

    @Getter
    final double uncertainty;

    public static RealNumber of(double value) {
        return new RealNumber(value, uncertaintyForDouble(value));
    }

    public RealNumber(double value, double uncertainty) {
        this.value = value;
        if (uncertainty < 0) {
            throw new InvalidUncertaintyException("Uncertainty cannot be negative");
        }
        this.uncertainty = uncertainty;
    }

    @Override
    public RealNumber plus(RealNumber summand) {
        double newValue = value + summand.value;
        return _of(
            newValue,
            uncertainty + summand.uncertainty + uncertaintyForDouble(newValue)
        );
    }

    @Override
    public RealNumber negation() {
        return _of(-1 * value, uncertainty);
    }

    @Override
    public RealNumber minus(RealNumber subtrahend) {
        return plus(subtrahend.negation());
    }

    @Override
    public RealNumber times(RealNumber multiplier) {
        if (multiplier == ONE) {
            return this;
        }
        return RealField.INSTANCE.considerMultiplicationBySpecialValues(this, multiplier);
    }

    protected boolean isExactlyZero() {
        return value == 0 && isExact();
    }
    @Override
    public boolean isExact() {
        return uncertainty == 0;
    }

    @Override
    public RealNumber pow(int exponent) {
        double newValue = Math.pow(value, exponent);
        if (value == 0 && exponent < 0) {
            throw new DivisionByZeroException("0" + superscript(exponent));
        }
        return new RealNumber(newValue,
            uncertainty * (Math.abs(exponent) *  Math.abs(Utils.pow(value, exponent - 1))) +  uncertaintyForDouble(newValue)
        );
    }

    @Override
    public RealNumber dividedBy(RealNumber divisor) {
        return times(divisor.reciprocal());
    }

    @Override
    public RealField getStructure() {
        return RealField.INSTANCE;
    }

    @Override
    public RealNumber dividedBy(long divisor) throws DivisionByZeroException {
        double newValue = value / divisor;
        return new RealNumber(
            value / divisor,
            Math.max(Math.abs(uncertainty / divisor), Utils.uncertaintyForDouble(newValue))
        );
    }

    @Override
    public RealNumber times(long multiplier) {
        return new RealNumber(value * multiplier, Math.abs(uncertainty * multiplier));
    }

    @Override
    public RealNumber reciprocal() {
        if (isZero()) {
            throw new DivisionByZeroException("Reciprocal of zero");
        }
        return pow(-1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends AlgebraicElement<F>> Optional<F> castDirectly(Class<F> clazz) {
        if (clazz.isAssignableFrom(ComplexNumber.class)) {
            return Optional.of((F) ComplexNumber.of(this));
        }
        return Optional.empty();
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
    public RealNumber _of(double value, double uncertainty) {
        return new RealNumber(value, uncertainty);
    }

    @Override
    public RealNumber times(double multiplier) {
        return _of(value * multiplier, uncertainty * Math.abs(multiplier));
    }

    @Override
    public RealNumber sin() {
        UncertainNumber<Double> sin = operations().sin(value);
        return _of(sin.getValue(), Math.max(uncertainty, sin.getUncertainty()));
    }

    @Override
    public RealNumber cos() {
        UncertainNumber<Double> cos = operations().cos(value);

        return _of(
            cos.getValue(), Math.max(uncertainty, cos.getUncertainty()));
    }

    @Override
    public RealNumber distanceTo(RealNumber otherElement) {
        return minus(otherElement).abs();
    }

    @Override
    public int compareTo(RealNumber o) {
        if (confidenceEquals(o)) {
            return 0;
        }
        return Double.compare(value, o.value);
    }

    @Override
    public RealNumber sqr() {
        double sq = value * value;
        return _of(sq, sq * getFractionalUncertainty() * 2);
    }

    @Override
    public RealNumber sqrt() {
        return _of(Math.sqrt(value), Math.max(uncertainty, Utils.uncertaintyForDouble(value)));
    }

    @Override
    public RealNumber pow(RealNumber exponent) {
        if (value == 0 && exponent.isNegative()) {
            throw new DivisionByZeroException("0 ^ " + exponent);
        }
        return _of(
            Math.pow(value, exponent.value),
            uncertainty
        );
    }

    @Override
    public RealNumber exp() {
        return _of(Math.exp(value), uncertainty);
    }

    @Override
    @NonAlgebraic
    public RealNumber ln() throws IllegalLogException {
        UncertainNumber<Double> ln = operations().ln(value);
        return _of(ln.getValue(), ln.getUncertainty());
    }

    @Override
    public RealNumber abs() {
        return _of(Math.abs(value), uncertainty);
    }

    public DoubleConfidenceInterval getConfidenceInterval() {
        return DoubleConfidenceInterval.of(doubleValue(), getUncertainty(), EPSILON_FACTOR);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ! RealNumber.class.isAssignableFrom(o.getClass())) return false;

        RealNumber that = (RealNumber) o;
        return confidenceEquals(that);
    }

    protected boolean confidenceEquals(RealNumber that) {
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
