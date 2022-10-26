/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.statistics;

import lombok.Getter;

import java.util.LongSummaryStatistics;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.Utils;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.numbers.DoubleOperations;
import org.meeuw.math.numbers.UncertaintyNumberOperations;
import org.meeuw.math.text.FormatService;
import org.meeuw.math.uncertainnumbers.ConfidenceIntervalConfiguration;
import org.meeuw.math.uncertainnumbers.UncertainNumber;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

/**
 * A 'statistical' number, can receive a number of values, and can calculate the average (the value of this {@link Number} implementation) and standard deviation of those values.
 * <p>
 * The idea is that the '{@link #getUncertainty()}' will simply be determined heuristically, and be given by the {@link #getStandardDeviation()}
 *
 * @author Michiel Meeuwissen
 * @param <T> the type of the numbers to aggregate
 */
public abstract class StatisticalNumber<T extends StatisticalNumber<T> & UncertainReal> implements UncertainReal {

    /**
     * The total number of values which were {@link StatisticalDouble#enter(double...)}ed.
     */
    @Getter
    protected int count = 0;

    static final UncertaintyNumberOperations<Double> operations = DoubleOperations.INSTANCE;

    public StatisticalNumber() {
    }
    protected StatisticalNumber(int count) {
        this.count = count;
    }

    public abstract T copy();

    abstract double getStandardDeviation();

    @Override
    public double getUncertainty() {
        return getStandardDeviation();
    }

    @Override
    public boolean isExact() {
        return false;
    }

    /**
     * Enters all values of another instance of this {@link StatisticalNumber}, effectively combining the given one into this one.
     * @param m another statistical number
     * @return this
     */
    public abstract T enter(T m);

    /**
     * Synonymous to {@link #enter(StatisticalNumber)} (except the return value). Does the same though as e.g. {@link LongSummaryStatistics#combine(LongSummaryStatistics)}.
     * @param m anothe statistical number
     * @see #enter(StatisticalNumber)
     */
    public void combine(T m) {
        enter(m);
    }

    public T combined(T m) {
        T copy = copy();
        return copy.enter(m);
    }

    /**
     * Divides the current instant by a value, and returns {@code this}
     * @param divisor divisor
     * @return this
     */
    public T divide(double divisor) {
        return multiply(1 / divisor);
    }

    /**
     * Multiplies the current instant by a value, and returns {@code this}
     *
     * @see #times(double)
     * @param multiplier multiplier
     * @return this
     */
    public abstract T multiply(double multiplier);


    @Override
    public T times(double multiplier) {
        return copy().multiply(multiplier);
    }
    @Override
    public T dividedBy(double divisor) {
        return copy().divide(divisor);
    }

    @Override
    public T negation() {
        return times(-1d);
    }


    @Override
    public UncertainDoubleElement pow(int exponent) {
        return new UncertainDoubleElement(Math.pow(getValue(), exponent), getUncertainty());
    }

    @Override
    public UncertainDoubleElement dividedBy(long divisor) {
        double newValue = getValue() / divisor;
        return new UncertainDoubleElement(
            newValue,
            Math.max(
                getUncertainty() / divisor,
                Utils.uncertaintyForDouble(newValue)
            )
        );
    }


    @Override
    public UncertainDoubleElement times(long multiplier) {
        return new UncertainDoubleElement(getValue() * multiplier, getUncertainty() * multiplier);
    }


    public void reset() {
        count = 0;
    }

    public UncertainDoubleElement immutableCopy() {
        return _of(getValue(), getUncertainty());
    }

    @Override
    public UncertainDoubleElement _of(double value, double uncertainty) {
        return new UncertainDoubleElement(value, uncertainty);
    }

    @Override
    public UncertainReal sqrt() {
        UncertainNumber<Double> sqrt = operations.sqrt(getValue());
        return _of(sqrt.getValue(), Math.max(getUncertainty(), sqrt.getValue()));
    }

    @Override
    public UncertainReal sin() {
        UncertainNumber<Double> sin = operations.sin(getValue());
        return _of(sin.getValue(), Math.max(getUncertainty(), sin.getValue()));
    }

    @Override
    public UncertainReal cos() {
        UncertainNumber<Double> cos = operations.cos(getValue());
        return _of(cos.getValue(), Math.max(getUncertainty(), cos.getValue()));
    }

    @Override
    public UncertainReal pow(UncertainReal exponent) {
        UncertainNumber<Double> result = operations.pow(getValue(), exponent.getValue());
        return _of(
            result.getValue(),
            Math.max(operations.powerUncertainty(
                getValue(), getUncertainty(), exponent.getValue(), exponent.getUncertainty(),
                result.getValue()),
                result.getUncertainty()
            )
        );
    }

    @Override
    public UncertainReal times(UncertainReal multiplier) {
        double v = getValue() * multiplier.getValue();
        return _of(v,
            Math.max(
                operations.multiplicationUncertainty(v, getFractionalUncertainty(), multiplier.getFractionalUncertainty()),
                Utils.uncertaintyForDouble(v)
            )
        );
    }

    @Override
    public UncertainReal plus(UncertainReal summand) {
        return _of(getValue() + summand.getValue(), operations.additionUncertainty(getUncertainty(), summand.getUncertainty()));
    }

    @Override
    public int compareTo(@NonNull UncertainReal o) {
        if (equals(o)) {
            return 0;
        } else {
            return Double.compare(getValue(), o.getValue());
        }
    }

    @SuppressWarnings({"EqualsWhichDoesntCheckParameterClass", "com.haulmont.jpb.EqualsDoesntCheckParameterClass"})
    @Override
    public boolean equals(Object o) {
        if (count == 0) {
            return ((StatisticalNumber) o).count == 0;
        }
        return equals(o,
            ConfigurationService.getConfigurationAspect(ConfidenceIntervalConfiguration.class).getSds()
        );
    }

    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determin how many digits can sensibly be shown.
     */
    @Override
    public String toString() {
        try {
            return FormatService.toString(this);
        } catch (DivisionByZeroException divisionByZeroException) {
            return "NaN";
        }
    }
}



