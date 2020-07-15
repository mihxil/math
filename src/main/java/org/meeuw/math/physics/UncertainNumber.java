package org.meeuw.math.physics;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import org.meeuw.math.*;

/**
 * A number with an uncertainty {@link #getUncertainty()}, and (optionally) {@link #getUnits()}
 *
 * http://ipl.physics.harvard.edu/wp-uploads/2013/03/PS3_Error_Propagation_sp13.pdf
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public abstract class UncertainNumber extends PhysicalNumber {

    public static final BinaryOperator<UncertainNumber> TIMES = UncertainNumber::times;
    public static final UnaryOperator<UncertainNumber> UMINUS = UncertainNumber::negate;
    public static final BinaryOperator<UncertainNumber> PLUS   = UncertainNumber::plus;
    public static final BinaryOperator<UncertainNumber> MINUS = (a1, a2) -> UMINUS.apply(a2).plus(a1);

    protected UncertainNumber(Units units) {
        super(units);
    }

    public UncertainNumber measurementCopy() {
        return new Measurement(doubleValue(), getUncertainty(), units);
    }

    public UncertainNumber combined(UncertainNumber m) {
        double u= getUncertainty();
        double mu = m.getUncertainty();
        double weight = 1d / (u * u);
        double mweight = 1d / (mu * mu);
        double value = (doubleValue() * weight + m.doubleValue() * mweight) / (weight + mweight);

        // I'm not absolutely sure about this:
        double uncertaintity = 1d/ Math.sqrt((weight + mweight));
        return new Measurement(value, uncertaintity, Units.forAddition(units, m.units));

    }

    public abstract UncertainNumber plus(double value);

    public UncertainNumber minus(double value) {
        return plus(-1d * value);
    }

    public UncertainNumber plus(UncertainNumber m) {
        double u = getUncertainty();
        double mu = m.getUncertainty();
        return new Measurement(
            doubleValue() + m.doubleValue(),
            Math.sqrt(u * u + mu * mu),
            Units.forAddition(units, m.units)
        );
    }

    public UncertainNumber minus(UncertainNumber m) {
        double u = getUncertainty();
        double mu = m.getUncertainty();
        return new Measurement(
            doubleValue() - m.doubleValue(),
            Math.sqrt(u * u + mu * mu),
            Units.forAddition(units, m.units)
        );
    }
    public UncertainNumber times(UncertainNumber m) {
        double u = getUncertainty() / doubleValue();
        double mu = m.getUncertainty() / m.doubleValue();
        double newValue = doubleValue() * m.doubleValue();
        return new Measurement(
            newValue,
            Math.abs(newValue) * Math.sqrt( (u * u)  + (mu * mu)),
            //Math.abs(newValue) * (u + mu),
            Units.forMultiplication(units, m.units)
        );
    }

    public UncertainNumber dividedBy(UncertainNumber m) {
        double u = getUncertainty() / doubleValue();
        double mu = m.getUncertainty() / m.doubleValue();
        double newValue = doubleValue() / m.doubleValue();

        return new Measurement(
            newValue,
            Math.abs(newValue) * Math.sqrt( (u * u)  + (mu * mu)),
            Units.forDivision(units, m.units)
        );
    }

    @Override
    public UncertainNumber negate() {
        return times(-1);
    }


    @Override
    public UncertainNumber pow(int d) {
        return new Measurement(
            Math.pow(doubleValue(), d),
            Math.abs(d) * Math.pow(doubleValue(), d -1) * getUncertainty(),
            Units.forExponentiation(units, d));
    }

    /**
     * Creates a new {@link UncertainNumber} representing a multiple of this one.
     */
    @Override
    public UncertainNumber times(double multiplication) {
        return new Measurement(multiplication * doubleValue(),
            Math.abs(multiplication) * getUncertainty(), units);
    }

    @Override
    public UncertainNumber plus(PhysicalNumber m) {
      return new Measurement(m.doubleValue() * doubleValue(),
            Math.abs(m.doubleValue()) * getUncertainty(), Units.forMultiplication(units, m.units));
    }

    @Override
    public UncertainNumber times(PhysicalNumber m) {
        return times(m.doubleValue());
    }

    /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determin how many digits can sensibly be shown.
     */
    @Override
    public String toString() {
        return  Utils.scientificNotationWithUncertaintity(doubleValue(), getUncertainty(), minimumExponent) +
            (units == null ? "" : " " + units.toString());
    }


}
