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
package org.meeuw.math.uncertainnumbers.field;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.DoubleUtils;
import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.exceptions.IllegalLogarithmException;
import org.meeuw.math.exceptions.IllegalPowerException;
import org.meeuw.math.numbers.DoubleOperations;
import org.meeuw.math.numbers.UncertaintyNumberOperations;
import org.meeuw.math.text.FormatService;
import org.meeuw.math.uncertainnumbers.*;

import static org.meeuw.math.DoubleUtils.uncertaintyForDouble;

/**
 * The most basic implementation of an {@link UncertainReal}. Immutable, based on primitive {@code double}s.
 * <p>
 * The structure is {@link UncertainRealField}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainDoubleElement
    extends AbstractUncertainDouble
    implements UncertainReal {

    public static final UncertainDoubleElement ZERO = new UncertainDoubleElement(0, EXACT) {
        @Override
        public UncertainDoubleElement sqrt() {
            return this;
        }

        @Override
        public UncertainDoubleElement sqr() {
            return this;
        }

        @Override
        public UncertainDoubleElement pow(int exponent) {
            if (exponent == 0) {
                return ONE;
            }
            if (exponent < 0) {
                throw new IllegalPowerException(this + "^" + exponent);
            }
            return this;
        }
    };

    public static final UncertainDoubleElement ONE  = new UncertainDoubleElement(1, EXACT) {
        @Override
        public UncertainDoubleElement sqrt() {
            return this;
        }
        @Override
        public UncertainDoubleElement sqr() {
            return this;
        }
        @Override
        public UncertainDoubleElement pow(int exponent) {
            return this;
        }
    };

    private static final UncertaintyNumberOperations<Double> operations = DoubleOperations.INSTANCE;

    private final double value;
    private final double uncertainty;

     /**
     * Just a shortcut to {@link #UncertainDoubleElement(double, double)}, which can be statically imported.
     * <p>
     * So then you can type:
     * <pre>{@code
     *  import static org.meeuw.math.uncertainnumbers.UncertainDoubleElement.exactly;
     *  ..
     *  UncertainReal value = exactly(2d);
     *  }
     * </pre>
     */
    public static UncertainDoubleElement exactly(double value) {
        return new UncertainDoubleElement(value, EXACT);
    }

    public static UncertainDoubleElement[] exactly(double[] value) {
        UncertainDoubleElement[] result = new UncertainDoubleElement[value.length];
        for (int i = 0; i < value.length; i++) {
            result[i] = exactly(value[i]);
        }
        return result;
    }

    public static UncertainDoubleElement[][] exactly(double[][] value) {
        UncertainDoubleElement[][] result = new UncertainDoubleElement[value.length][];
        for (int i = 0; i < value.length; i++) {
            result[i] = exactly(value[i]);
        }
        return result;
    }

    /**
     * Just like {@link #uncertain(double, double)}, but you could use it like so:
     * <pre>{@code

     *   UncertainReal value = UncertainDoubleElement.of(2d, d);
     * }</pre>
     * Which just more emphasises the type that is created.
     * @see #uncertain(double, double)
     */
    public static UncertainDoubleElement of(double value, double uncertainty) {
        return new UncertainDoubleElement(value, uncertainty);
    }

    /**
     * Just a shortcut to {@link #UncertainDoubleElement(double, double)}, which can be statically imported.
     * <p>
     * So then you can type:
     * <pre>{@code
     *  import static org.meeuw.math.uncertainnumbers.UncertainDoubleElement.uncertain;
     *  ..
     *  UncertainReal value = uncertain(2d, 0.1d);
     *  }
     * </pre>
     */
    public static UncertainDoubleElement uncertain(double value, double uncertainty) {
        return of(value, uncertainty);
    }

    public UncertainDoubleElement(double value, double uncertainty) {
        this.value = value;
        this.uncertainty = uncertainty;
    }

    @Override
    public double doubleUncertainty() {
        return uncertainty;
    }

    @Override
    public UncertainRealField getStructure() {
        return UncertainRealField.INSTANCE;
    }

    @Override
    public UncertainReal dividedBy(long divisor) {
        double result = value / divisor;
        return new UncertainDoubleElement(result,
            Math.max(Math.abs(uncertainty / divisor), uncertaintyForDouble(result)));
    }

    @Override
    public UncertainDoubleElement times(long multiplier) {
        double result = value * multiplier;
        return new UncertainDoubleElement(result,
            Math.max(Math.abs(uncertainty * multiplier), uncertaintyForDouble(result)));
    }

    @Override
    public UncertainDoubleElement times(UncertainReal multiplier) {
        if (multiplier.isOne()){
            return this;
        }
        double newValue = this.doubleValue() * multiplier.doubleValue();
        return of(newValue,
            Math.max(
                operations.multiplicationUncertainty(newValue, doubleFractionalUncertainty(),  multiplier.doubleFractionalUncertainty()),
                uncertaintyForDouble(newValue)
            )
        );
    }

    @Override
    public UncertainDoubleElement plus(UncertainReal summand) {
        double v1 = this.doubleValue();
        double v2 = summand.doubleValue();
        double result  = v1 + v2;
        return of(
            result,
            DoubleUtils.max(
                operations.add(uncertainty, summand.doubleUncertainty()),
                uncertaintyForDouble(result),
                uncertaintyForDouble(v1),
                uncertaintyForDouble(v2)
            )
        );
    }

    @Override
    public UncertainDoubleElement reciprocal() {
        return pow(-1);
    }

    @Override
    public UncertainDoubleElement negation() {
        return times(-1);
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public UncertainDoubleElement sqrt() {
        return of(Math.sqrt(value), uncertainty);
    }

    @Override
    public UncertainDoubleElement sin() {
        UncertainNumber<Double> sin = operations().sin(value);
        return of(sin.getValue(), Math.max(uncertainty, sin.getUncertainty()));
    }

    @Override
    public UncertainDoubleElement cos() {
        UncertainNumber<Double> cos = operations().cos(value);
        return of(cos.getValue(), Math.max(uncertainty, cos.getUncertainty()));
    }

    @Override
    public UncertainReal pow(UncertainReal exponent) {
        double result = Math.pow(value, exponent.doubleValue());
        return of(
            result,
            DoubleUtils.max(
                uncertaintyForDouble(result),
                operations.powerUncertainty(
                    value,
                    Math.max(uncertainty, uncertaintyForDouble(value)),
                    exponent.doubleValue(),
                    Math.max(exponent.doubleUncertainty(), uncertaintyForDouble(exponent.doubleValue())),
                    result
                )
            ));
    }

    @Override
    public UncertainReal exp() {
        return of(
            Math.exp(this.doubleValue()),
            doubleUncertainty() // TODO
        );
    }

    @Override
    @NonAlgebraic
    public UncertainReal ln() throws IllegalLogarithmException {
        UncertainNumber<Double> ln = operations().ln(this.doubleValue());

        return of(
            ln.getValue(),
            ln.getUncertainty()
        );
    }

    @Override
    public  UncertainDoubleElement pow(int exponent) {
        double v = this.doubleValue();
        if (v == 0 && exponent < 0) {
            throw new IllegalPowerException(v + "^" + exponent);
        }
        return of(
            Math.pow(this.doubleValue(), exponent),
            Math.abs(exponent) * Math.pow(Math.abs(this.doubleValue()), exponent -1) * doubleUncertainty());
    }

    @Override
    public UncertainDoubleElement abs() {
        return of(Math.abs(this.doubleValue()), uncertainty);
    }

    @Override
    public UncertainDoubleElement immutableInstanceOfPrimitives(double value, double uncertainty) {
        return of(value, uncertainty);
    }

    @Override
    public boolean eq(UncertainReal other) {
        return eq(other,
            ConfigurationService.getConfigurationAspect(ConfidenceIntervalConfiguration.class).getSds());
    }

    @Override
    public boolean strictlyEquals(Object o) {
        if (!(o instanceof UncertainDoubleElement)) {
            return false;
        }
        UncertainDoubleElement uncertainDoubleElement = (UncertainDoubleElement) o;
        return value == uncertainDoubleElement.value;
    }

    @Override
    public boolean equals(Object o) {
        if ( ConfigurationService.getConfigurationAspect(CompareConfiguration.class).isEqualsIsStrict()) {
            return strictlyEquals(o);
        } else {
            return eq((UncertainReal) o);
        }
    }

    @Override
    public int hashCode() {
        // must return constant to ensure that this is consistent with equals
        return 0;
    }

    public int compareTo(Number o) {
        return Double.compare(this.doubleValue(), o.doubleValue());
    }

    /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determin how many digits can sensibly be shown.
     */
    @Override
    public String toString() {
        return FormatService.toString(this);
    }

    @Override
    public long longValue() {
        return DoubleUtils.round(doubleValue());
    }
}
