package org.meeuw.math;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.function.BinaryOperator;

/**
 * A number with an uncertainty.
 *
 * http://ipl.physics.harvard.edu/wp-uploads/2013/03/PS3_Error_Propagation_sp13.pdf
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public abstract class UncertainNumber extends Number implements Comparable<Number> {

    @Getter
    @Setter
    protected int minimumExponent = 4;

    @Getter
    @Setter
    protected Units units = null;

    public abstract double getUncertainty();

    public UncertainNumber measurementCopy() {
        return new Measurement(doubleValue(), getUncertainty(), units);
    }

    public UncertainNumber combine(UncertainNumber m) {
        if (! Objects.equals(units, m.units)) {
            throw new IllegalArgumentException();
        }
        double u= getUncertainty();
        double mu = m.getUncertainty();
        double weight = 1d / (u * u);
        double mweight = 1d / (mu * mu);
        double value = (doubleValue() * weight + m.doubleValue() * mweight) / (weight + mweight);

        // I'm not absolutely sure about this:
        double uncertaintity = 1d/ Math.sqrt((weight + mweight));
        return new Measurement(value, uncertaintity, units);

    }

    public UncertainNumber plus(UncertainNumber m) {
        if (! Objects.equals(units, m.units)) {
            throw new IllegalArgumentException();
        }
        double u = getUncertainty();
        double mu = m.getUncertainty();
        return new Measurement(
            doubleValue() + m.doubleValue(),
            Math.sqrt(u * u + mu * mu),
            units
        );
    }

    public UncertainNumber minus(UncertainNumber m) {
        if (! Objects.equals(units, m.units)) {
            throw new IllegalArgumentException();
        }
        double u = getUncertainty();
        double mu = m.getUncertainty();
        return new Measurement(
            doubleValue() - m.doubleValue(),
            Math.sqrt(u * u + mu * mu),
            units
        );
    }
    public UncertainNumber times(UncertainNumber m) {
        Units newUnits = null;
        if (units != null) {
            newUnits = units.times(m.units);
        }
        double u = getUncertainty() / doubleValue();
        double mu = m.getUncertainty() / m.doubleValue();
        double newValue = doubleValue() * m.doubleValue();
        return new Measurement(
            newValue,
            Math.abs(newValue) * Math.sqrt( (u * u)  + (mu * mu)),
            //Math.abs(newValue) * (u + mu),
            newUnits
        );
    }

    public UncertainNumber dividedBy(UncertainNumber m) {
        Units newUnits = null;
        if (units != null) {
            newUnits = units.dividedBy(m.units);
        }
        double u = getUncertainty() / doubleValue();
        double mu = m.getUncertainty() / m.doubleValue();
        double newValue = doubleValue() / m.doubleValue();

        return new Measurement(
            newValue,
            Math.abs(newValue) * Math.sqrt( (u * u)  + (mu * mu)),
            newUnits
        );
    }
    public UncertainNumber pow(int d) {
        Units newUnits = null;
        if (units != null) {
            newUnits = units.pow(d);
        }
        return new Measurement(
            Math.pow(doubleValue(), d),
            Math.abs(d) * Math.pow(doubleValue(), d -1) * getUncertainty(),
            newUnits);
    }

    public UncertainNumber negate() {
        return times(-1f);
    }

    public UncertainNumber times(double multiplication) {
        return new Measurement(multiplication * doubleValue(),
            Math.abs(multiplication) * getUncertainty(), units);
    }


    /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determin how many digits can sensibly be shown.
     */
    @Override
    public String toString() {
        return  Utils.scientificNotation(doubleValue(), getUncertainty(), minimumExponent) +
            (units == null ? "" : " " + units.toString());
    }


    @Override
    public int compareTo(Number o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }

    public static final class Plus implements BinaryOperator<UncertainNumber> {
        public static final UncertainNumber.Plus PLUS = new UncertainNumber.Plus();

        @Override
        public UncertainNumber apply(UncertainNumber a1, UncertainNumber a2) {
            return a1.plus(a2);
        }
    }

    public static class Times implements BinaryOperator<UncertainNumber> {
        public static final UncertainNumber.Times TIMES = new UncertainNumber.Times();


        @Override
        public UncertainNumber apply(UncertainNumber a1, UncertainNumber a2) {
            return a1.times(a2);
        }
    }

}
