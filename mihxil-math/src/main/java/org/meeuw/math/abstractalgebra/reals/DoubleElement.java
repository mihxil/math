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
package org.meeuw.math.abstractalgebra.reals;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.DoubleUtils;
import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.numbers.DoubleOperations;
import org.meeuw.math.numbers.UncertaintyNumberOperations;
import org.meeuw.math.operators.BasicAlgebraicIntOperator;
import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;
import org.meeuw.math.text.FormatService;
import org.meeuw.math.uncertainnumbers.*;

import static org.meeuw.math.DoubleUtils.uncertaintyForDouble;

/**
 * The most basic implementation of an {@link RealNumber}. Immutable, based on primitive {@code double}s.
 * <p>
 * The structure is {@link RealField}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class DoubleElement
    extends AbstractUncertainDouble
    implements RealNumber {

    public static final int EPSILON_FACTOR = 2;
    public static final double UNCERTAINTY_FOR_ONE = DoubleUtils.uncertaintyForDouble(1);
    public static final double UNCERTAINTY_FOR_ZERO = DoubleUtils.uncertaintyForDouble(0);




    public static final DoubleElement TWO = DoubleElement.exactly(2d);

    public static final DoubleElement SMALLEST = new DoubleElement(0d, UNCERTAINTY_FOR_ZERO);

    private static final UncertaintyNumberOperations<Double> operations = DoubleOperations.INSTANCE;

    private final double value;
    private final double uncertainty;

     /**
     * Just a shortcut to {@link #DoubleElement(double, double)}, which can be statically imported.
     * <p>
     * So then you can type:
     * <pre>{@code
     *  import static org.meeuw.math.uncertainnumbers.UncertainDoubleElement.exactly;
     *  ..
     *  UncertainReal value = exactly(2d);
     *  }
     * </pre>
     */
    public static DoubleElement exactly(double value) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException("NaN");
        }
        return new DoubleElement(value, EXACT);
    }



    public static DoubleElement[] exactly(double[] value) {
        DoubleElement[] result = new DoubleElement[value.length];
        for (int i = 0; i < value.length; i++) {
            result[i] = exactly(value[i]);
        }
        return result;
    }

    public static DoubleElement[][] exactly(double[][] value) {
        DoubleElement[][] result = new DoubleElement[value.length][];
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
    public static DoubleElement of(double value, double uncertainty) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException("NaN");
        }
        return new DoubleElement(value, uncertainty);
    }

    public static DoubleElement of(double value) {
        return of(value, uncertaintyForDouble(value));
    }


    /**
     * Just a shortcut to {@link #DoubleElement(double, double)}, which can be statically imported.
     * <p>
     * So then you can type:
     * <pre>{@code
     *  import static org.meeuw.math.uncertainnumbers.UncertainDoubleElement.uncertain;
     *  ..
     *  UncertainReal value = uncertain(2d, 0.1d);
     *  }
     * </pre>
     */
    public static DoubleElement uncertain(double value, double uncertainty) {
        return of(value, uncertainty);
    }

    public DoubleElement(double value, double uncertainty) {
        this.value = value;
        if (uncertainty < 0) {
            throw new InvalidUncertaintyException("Uncertainty cannot be negative");
        }
        this.uncertainty = uncertainty;
    }

    @Override
    public double doubleUncertainty() {
        return uncertainty;
    }

    @Override
    public DoubleElement dividedBy(long divisor) {
        if (divisor == 0) {
            throw new DivisionByZeroException("Divisor", this.toString());
         } else if (divisor == 1) {
            return this;
        }
        double result = value / divisor;
        return new DoubleElement(result,
            Math.max(Math.abs(uncertainty / divisor), uncertaintyForDouble(result)));
    }

    @Override
    public DoubleElement times(long multiplier) {
        if (multiplier == 0) {
            return DoubleElement.ZERO;
        } else if (multiplier == 1) {
            return this;
        }
        double result = value * multiplier;
        return new DoubleElement(result,
            Math.max(Math.abs(uncertainty * multiplier), uncertaintyForDouble(result)));
    }

    @Override
    public DoubleElement times(RealNumber multiplier) {
        if (multiplier.isOne()){
            return this;
        }
        if (multiplier instanceof DoubleElement doubleElement) {
            return considerMultiplicationBySpecialValues(
                this, doubleElement
            );
        } else {
            double newValue = this.doubleValue() * multiplier.doubleValue();
            return of(newValue,
                Math.max(
                    operations.multiplicationUncertainty(
                        newValue, doubleFractionalUncertainty(), multiplier.doubleFractionalUncertainty()),
                    uncertaintyForDouble(newValue)
                )
            );
        }
    }

    @Override
    public DoubleElement plus(RealNumber summand) {
        double v1 = this.doubleValue();
        double v2 = summand.doubleValue();
        assert ! Double.isNaN(v1) : "value is NaN";
        assert ! Double.isNaN(v2) : "summand is NaN";
        double result  = v1 + v2;

        return of(
            result,
            DoubleUtils.max(
                uncertainty + summand.doubleUncertainty() + uncertaintyForDouble(result),
                uncertaintyForDouble(result),
                uncertaintyForDouble(v1),
                uncertaintyForDouble(v2)
            )
        );
    }

    @Override
    @NonAlgebraic(reason = NonAlgebraic.Reason.SOME)
    public DoubleElement reciprocal() throws DivisionByZeroException {
        if (isExactlyZero()) {
            throw new DivisionByZeroException("Reciprocal of zero", BasicAlgebraicUnaryOperator.RECIPROCAL.stringify(toString()));
        }
        try {
            return pow(-1);
        } catch(IllegalPowerException illegalPowerException) {
            throw new DivisionByZeroException("Reciprocal of zero", BasicAlgebraicUnaryOperator.RECIPROCAL.stringify(toString()));
        }
    }

    @Override
    public DoubleElement negation() {
        return times(-1);
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public DoubleElement sqrt() {
        return of(Math.sqrt(value), uncertainty);
    }


    @Override
    public DoubleElement root(int i) {
        return of(Math.pow(value, 1d/i), uncertainty);
    }

    @Override
    public DoubleElement sin() {
        UncertainNumber<Double> sin = operations().sin(value);
        return of(sin.getValue(), Math.max(uncertainty, sin.getUncertainty()));
    }

    @Override
    public RealNumber asin() {
        UncertainNumber<Double> asin = operations().asin(value);
        return of(asin.getValue(), Math.max(uncertainty, asin.getUncertainty()));
    }

    @Override
    public DoubleElement cos() {
        UncertainNumber<Double> cos = operations().cos(value);
        return of(cos.getValue(), Math.max(uncertainty, cos.getUncertainty()));
    }

    @Override
    public DoubleElement tan() {
        UncertainNumber<Double> tan = operations().tan(value);
        return of(tan.getValue(), Math.max(uncertainty, tan.getUncertainty()));
    }

    @Override
    public RealNumber pow(RealNumber exponent) throws OverflowException {
        double result = Math.pow(value, exponent.doubleValue());
        if (Double.isInfinite(result)) {
            throw new OverflowException("Result is infinite  " + result,   this + "^" + exponent );
        }
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
    public RealNumber exp() {
        return of(
            Math.exp(this.doubleValue()),
            doubleUncertainty() // TODO
        );
    }

    @Override
    @NonAlgebraic
    public RealNumber ln() throws IllegalLogarithmException {
        UncertainNumber<Double> ln = operations().ln(this.doubleValue());

        return of(
            ln.getValue(),
            ln.getUncertainty()
        );
    }

    @Override
    @NonAlgebraic(reason = NonAlgebraic.Reason.SOME)
    public DoubleElement pow(int exponent) {
        double v = this.doubleValue();
        if (v == 0  && uncertainty != 0) {
            v = uncertainty;
        }
        if (v == 0 && exponent < 0) {
            throw new IllegalPowerException("Cannot take negative power of zero", BasicAlgebraicIntOperator.POWER.stringify(Double.toString(v),  Integer.toString(exponent)));
        }
        double result = Math.pow(v, exponent);
        if (Double.isInfinite(result)) {
            throw new IllegalPowerException("Resulted infinity", BasicAlgebraicIntOperator.POWER.stringify(Double.toString(v),  Integer.toString(exponent)));

        }
        return of(
            result,
            Math.abs(exponent) * Math.pow(Math.abs(v), exponent -1) * doubleUncertainty());
    }

    @Override
    public DoubleElement abs() {
        return of(Math.abs(this.doubleValue()), uncertainty);
    }

    @Override
    public DoubleElement immutableInstanceOfPrimitives(double value, double uncertainty) {
        return of(value, uncertainty);
    }

    @Override
    public boolean eq(RealNumber other) {
        return eq(other,
            ConfigurationService.getConfigurationAspect(ConfidenceIntervalConfiguration.class).getSds());
    }

    @Override
    public boolean strictlyEquals(Object o) {
        if (!(o instanceof DoubleElement uncertainDoubleElement)) {
            return false;
        }
        return value == uncertainDoubleElement.value;
    }

    @Override
    public boolean equals(Object o) {
        if ( ConfigurationService.getConfigurationAspect(CompareConfiguration.class).isEqualsIsStrict()) {
            return strictlyEquals(o);
        } else {
            return eq((RealNumber) o);
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


    public static DoubleElement considerMultiplicationBySpecialValues(DoubleElement r1, DoubleElement r2) {
        double newValue = r1.value * r2.value;
        // multiplication by zero
        if (r1.isExactlyZero() || r2.isExactlyZero()) {
            return DoubleElement.ZERO;
      /*  } else if (r1.value == 0 && r2.value != 0) {
            return new DoubleElement(newValue, r1.uncertainty);
        } else if (r2.value == 0 && r1.value != 0) {
            return new DoubleElement(newValue, r2.uncertainty);
            // NaN*/
        } else if (Double.isNaN(r1.value) || Double.isNaN(r2.value)) {
            return new DoubleElement(newValue, Double.NaN);
        } else {
            double value1 = Math.max(Math.abs(r1.value), r1.uncertainty);
            double value2 = Math.max(Math.abs(r2.value), r2.uncertainty);
            double newValueForUncertaintiy = value1 * value2;
            return new DoubleElement(newValue,
                newValueForUncertaintiy * (
                    r1.uncertainty / value1 +
                        r2.uncertainty / value2
                ) + uncertaintyForDouble(newValue)

            );
        }
    }

}
