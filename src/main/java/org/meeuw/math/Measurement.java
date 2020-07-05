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
public abstract class Measurement extends Number implements Comparable<Number> {

    @Getter
    @Setter
    protected int minimumExponent = 4;

    @Getter
    @Setter
    protected Units units = null;

    public abstract double getUncertainty();


    /**
     * Assuming that this measurement is from a different set (the mean is <em>principally
     * different</em>)
     *
     */
    public Measurement plus(Measurement m) {
        if (! Objects.equals(units, m.units)) {
            throw new IllegalArgumentException();
        }
        double u = getUncertainty();
        double mu = m.getUncertainty();
        return new MeasurementImpl(
            doubleValue() + m.doubleValue(),
            Math.sqrt(u * u + mu * mu),
            units
        );
    }

    public Measurement minus(Measurement m) {
        if (! Objects.equals(units, m.units)) {
            throw new IllegalArgumentException();
        }
        double u = getUncertainty();
        double mu = m.getUncertainty();
        return new MeasurementImpl(
            doubleValue() - m.doubleValue(),
            Math.sqrt(u * u + mu * mu),
            units
        );
    }
    public Measurement times(Measurement m) {
        Units newUnits = null;
        if (units != null) {
            newUnits = units.times(m.units);
        }
        double u = getUncertainty() / doubleValue();
        double mu = m.getUncertainty() / m.doubleValue();
        return new MeasurementImpl(
            doubleValue() * m.doubleValue(),
            Math.sqrt( (u * u)  + (mu * mu)),
            newUnits
        );
    }

    public Measurement dividedBy(Measurement m) {
        Units newUnits = null;
        if (units != null) {
            newUnits = units.dividedBy(m.units);
        }
        double u = getUncertainty() / doubleValue();
        double mu = m.getUncertainty() / m.doubleValue();
        return new MeasurementImpl(
            doubleValue() / m.doubleValue(),
            Math.sqrt( (u * u)  + (mu * mu)),
            newUnits
        );
    }
    public Measurement pow(int d) {
        Units newUnits = null;
        if (units != null) {
            newUnits = units.pow(d);
        }
        return new MeasurementImpl(
            Math.pow(doubleValue(), d),
            Math.abs(d) * Math.pow(doubleValue(), d -1) * getUncertainty(),
            newUnits);
    }

    public Measurement negate() {
        return times(-1f);
    }

    public Measurement times(double multiplication) {
        return new MeasurementImpl(multiplication * doubleValue(), getUncertainty(), units);
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

    public static final class Plus implements BinaryOperator<Measurement> {
        public static final Measurement.Plus PLUS = new Measurement.Plus();

        @Override
        public Measurement apply(Measurement a1, Measurement a2) {
            return a1.plus(a2);
        }
    }

    public static class Times implements BinaryOperator<Measurement> {
        public static final Measurement.Times TIMES = new Measurement.Times();


        @Override
        public Measurement apply(Measurement a1, Measurement a2) {
            return a1.times(a2);
        }
    }

}
