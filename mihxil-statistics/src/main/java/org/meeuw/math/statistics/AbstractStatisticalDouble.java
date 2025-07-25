/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.statistics;

import java.math.BigDecimal;
import java.util.Objects;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.DoubleUtils;
import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.exceptions.IllegalPowerException;
import org.meeuw.math.numbers.DoubleOperations;
import org.meeuw.math.numbers.UncertaintyNumberOperations;
import org.meeuw.math.text.FormatService;
import org.meeuw.math.uncertainnumbers.*;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.meeuw.configuration.ConfigurationService.getConfigurationAspect;

/**
 * A 'statistical' number can receive a number of values, and can calculate the average (the value of this {@link Number} implementation) and standard deviation of those values.
 * <p>
 * The idea is that the '{@link #doubleUncertainty()}' will simply be determined heuristically, and be given by the {@link #getStandardDeviation()}
 *
 * @author Michiel Meeuwissen
 */
public abstract class AbstractStatisticalDouble
    <SELF extends AbstractStatisticalDouble<SELF>>
    extends AbstractStatisticalNumber<SELF, Double, UncertainReal>
    implements
    UncertainNumber<Double>,
    StatisticalDouble<SELF> {

    static final UncertaintyNumberOperations<Double> operations = DoubleOperations.INSTANCE;

    public AbstractStatisticalDouble() {
    }

    protected AbstractStatisticalDouble(int count) {
        super(count);
    }

    @Override
    public double doubleValue() throws NoValues {
        return optionalDoubleMean()
            .orElseThrow(() ->
                new NoValues("No values entered, cannot calculate mean",   ".mean")
            );
    }

    /**
     * Divides the current instant by a value, and returns {@code this}
     * @param divisor divisor
     * @return this
     */
    public SELF divide(double divisor) {
        return multiply(1 / divisor);
    }

    /**
     * Multiplies the current instant by a value, and returns {@code this}
     *
     * @see #times(double)
     * @param multiplier multiplier
     * @return this
     */
    public abstract SELF  multiply(double multiplier);


    @Override
    public SELF times(double multiplier) {
        return copy().multiply(multiplier);
    }
    @Override
    public SELF dividedBy(double divisor) {
        return copy().divide(divisor);
    }

    @Override
    public SELF negation() {
        return times(-1d);
    }

    @Override
    public UncertainReal pow(int exponent) {
        Double pow = operations().pow(doubleValue(), exponent);
        Double powerUncertainty = operations().powerUncertainty(doubleValue(), getUncertainty(), (double) exponent, 0d, pow);
        return immutableInstance(pow, powerUncertainty);
    }

    @Override
    public UncertainDoubleElement dividedBy(long divisor) {
        double newValue = doubleValue() / divisor;
        return new UncertainDoubleElement(
            newValue,
            Math.max(
                doubleUncertainty() / divisor,
                DoubleUtils.uncertaintyForDouble(newValue)
            )
        );
    }

    @Override
    public UncertainDoubleElement times(long multiplier) {
        return immutableInstance(getValue() * multiplier, getUncertainty() * multiplier);
    }


    @Override
    public UncertainDoubleElement immutableInstanceOfPrimitives(double value, double uncertainty) {
        return new UncertainDoubleElement(value, uncertainty);
    }

    @Override
    public UncertainDoubleElement immutableInstance(@NonNull Double value, @NonNull Double uncertainty) {
        return immutableInstanceOfPrimitives(value, uncertainty);
    }

    @Override
    public UncertainReal sqrt() {
        UncertainNumber<Double> sqrt = operations.sqrt(doubleValue());
        return immutableInstance(sqrt.getValue(), Math.max(doubleUncertainty(), sqrt.getValue()));
    }

    @Override
    public UncertainReal root(int base) {
        UncertainNumber<Double> sqrt = operations.root(doubleValue(), base);
        return immutableInstance(sqrt.getValue(), Math.max(doubleUncertainty(), sqrt.getValue()));
    }

    @Override
    public UncertainReal sin() {
        UncertainNumber<Double> sin = operations.sin(doubleValue());
        return immutableInstance(sin.getValue(), Math.max(doubleUncertainty(), sin.getValue()));
    }

    @Override
    public UncertainReal asin() {
        UncertainNumber<Double> asin = operations.asin(doubleValue());
        return immutableInstance(asin.getValue(), Math.max(doubleUncertainty(), asin.getValue()));
    }


    @Override
    public UncertainReal cos() {
        UncertainNumber<Double> cos = operations.cos(doubleValue());
        return immutableInstance(cos.getValue(), Math.max(doubleUncertainty(), cos.getValue()));
    }
    @Override
    public UncertainReal tan() {
        UncertainNumber<Double> tan = operations.tan(doubleValue());
        return immutableInstance(tan.getValue(), Math.max(doubleUncertainty(), tan.getValue()));
    }

    @Override
    @NonAlgebraic(reason = NonAlgebraic.Reason.SOME, value="Can't be taken of 0 for negative arguments")
    public UncertainReal pow(UncertainReal exponent) throws IllegalPowerException {
        UncertainNumber<Double> result = operations.pow(doubleValue(), exponent.doubleValue());
        return immutableInstance(
            result.getValue(),
            Math.max(operations.powerUncertainty(
                doubleValue(), doubleUncertainty(), exponent.doubleValue(), exponent.doubleUncertainty(),
                result.getValue()),
                result.getUncertainty()
            )
        );
    }

    @Override
    public UncertainReal times(UncertainReal multiplier) {
        double v = doubleValue() * multiplier.doubleValue();
        return immutableInstanceOfPrimitives(v,
            Math.max(
                operations.multiplicationUncertainty(v, doubleFractionalUncertainty(), multiplier.doubleFractionalUncertainty()),
                DoubleUtils.uncertaintyForDouble(v)
            )
        );
    }

    @Override
    public UncertainReal plus(UncertainReal summand) {
        return immutableInstance(doubleValue() + summand.doubleValue(), operations.additionUncertainty(doubleUncertainty(), summand.doubleUncertainty()));
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(getMean());
    }

    @Override
    public int compareTo(@NonNull UncertainReal o) {
        if (equals(o)) {
            return 0;
        } else {
            return Double.compare(doubleValue(), o.doubleValue());
        }
    }

    @Override
    public boolean eq(UncertainReal o) {
        if (getCount() == 0 && o instanceof StatisticalNumber<?,?,?>) {
            return ((StatisticalNumber<?, ?, ?>) o).getCount() == 0;
        }
        return eq(o,
            getConfigurationAspect(ConfidenceIntervalConfiguration.class).getSds()
        );
    }

    @Override
    public boolean strictlyEquals(Object o) {
        if (! (o instanceof AbstractStatisticalDouble<?>)) {
            return false;
        }
        AbstractStatisticalDouble<?> number = (AbstractStatisticalDouble<?>) o;

        return getCount() == 0 ? number.getCount() == 0 : Objects.equals(getValue(), number.getValue());
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if ( getConfigurationAspect(CompareConfiguration.class).isEqualsIsStrict()) {
            return strictlyEquals(o);
        } else {
            return eq((SELF) o);
        }
    }

    /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determine how many digits can sensibly be shown.
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



