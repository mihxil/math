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

import java.math.*;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.*;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.complex.BigComplexNumber;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.numbers.BigDecimalOperations;
import org.meeuw.math.numbers.MathContextConfiguration;
import org.meeuw.math.operators.BasicAlgebraicIntOperator;
import org.meeuw.math.uncertainnumbers.*;

import static org.meeuw.configuration.ConfigurationService.getConfigurationAspect;

/**
 * A real number (backend by a big decimal), element of {@link BigDecimalElement}.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class BigDecimalElement implements
    CompleteScalarFieldElement<BigDecimalElement>,
    MetricSpaceElement<BigDecimalElement, BigDecimalElement>,
    UncertainNumber<BigDecimal>,
    WithDoubleOperations<BigDecimalElement> {

    private static final MathContext UNCERTAINTY_MATH_CONTEXT= new MathContext(2, RoundingMode.HALF_UP);

    public static final BigDecimalElement ONE = new BigDecimalElement(BigDecimal.ONE, BigDecimal.ZERO);

    public static final BigDecimalElement ZERO = new BigDecimalElement(BigDecimal.ZERO,  BigDecimal.ZERO);

    /**
     * π as a {@link BigDecimalElement}
     * @see Utils#PI
     */
    public static final BigDecimalElement PI = new BigDecimalElement(new BigDecimal(Utils.PI), new BigDecimal("1e-" + (Utils.PI.length() - 1)));
     /**
     * e as a {@link BigDecimalElement}
     * @see Utils#e
     */
    public static final BigDecimalElement e = new BigDecimalElement(
        new BigDecimal(Utils.e), new BigDecimal("1e-" +  (Utils.e.length() - 1)));



    private final BigDecimal value;
    private final BigDecimal uncertainty;

    public static BigDecimalElement of(double doubleValue){
        return new BigDecimalElement(BigDecimal.valueOf(doubleValue), uncertainty(doubleValue));
    }

    public static BigDecimalElement of(String stringValue){
        return of(new BigDecimal(stringValue));
    }

    public static BigDecimalElement of(BigDecimal bigDecimal){
        return new BigDecimalElement(bigDecimal, BigDecimal.ZERO);
    }

    public static BigDecimal uncertainty(double doubleValue) {
        return BigDecimal.valueOf(DoubleUtils.uncertaintyForDouble(doubleValue));
    }

    public BigDecimalElement(BigDecimal value, BigDecimal uncertainty) {
        this.value = value;
        this.uncertainty = uncertainty;
    }
    public BigDecimalElement(UncertainNumber<BigDecimal> value) {
        this.value = value.getValue();
        this.uncertainty = value.getUncertainty();
    }


    @Override
    public BigDecimalElement plus(BigDecimalElement summand) {
        return new BigDecimalElement(UncertainNumber.super.plus(summand));
    }

    @Override
    public BigDecimalElement minus(BigDecimalElement n1) {
        return new BigDecimalElement(
            value.subtract(n1.value),
            operations().additionUncertainty(uncertainty, n1.uncertainty)
        );
    }

    @Override
    public BigDecimalElement negation() {
        return new BigDecimalElement(value.negate(), uncertainty);
    }

    @Override
    public int compareTo(@NonNull BigDecimalElement compare) {
        return value.compareTo(compare.value);
    }

    @Override
    public BigDecimalElement sqr() {
        return new BigDecimalElement(
            value.multiply(value),
            uncertainty.multiply(uncertainty)
        );
    }

    @Override
    @NonAlgebraic(reason = NonAlgebraic.Reason.SOME)
    public BigDecimalElement dividedBy(BigDecimalElement n) throws DivisionByZeroException {
        UncertainNumber<BigDecimal> newValue = operations().divide(value, n.value);
        return new BigDecimalElement(newValue.getValue(),
            operations().multiplicationUncertainty(
                newValue.getValue(),
                getFractionalUncertainty(),
                n.getFractionalUncertainty()
            ).max(newValue.getUncertainty())
        );

    }

    @Override
    public BigDecimalElement sqrt() {
        UncertainNumber<BigDecimal> sqrt = operations().sqrt(value);
        return new BigDecimalElement(sqrt.getValue(),
            operations().sqrt(uncertainty).getValue().max(sqrt.getUncertainty())
        );
    }
    @Override
    public BigDecimalElement root(int i) {
        UncertainNumber<BigDecimal> sqrt = operations().root(value,  i);
        return new BigDecimalElement(sqrt.getValue(),
            operations().sqrt(uncertainty).getValue().max(sqrt.getUncertainty())
        );
    }

    @Override
    @NonAlgebraic(reason = NonAlgebraic.Reason.ELEMENTS, value = "Not possible for negative arguments")
    public BigDecimalElement pow(BigDecimalElement bigDecimalElement) throws IllegalPowerException {
        UncertainNumber<BigDecimal> pow = operations().pow(value, bigDecimalElement.value);
        return new BigDecimalElement(pow.getValue(), uncertainty.max(pow.getUncertainty()));
    }

    @Override
    public BigDecimalElement exp() {
        UncertainNumber<BigDecimal> exp = operations().exp(value);
        return new BigDecimalElement(exp.getValue(),
            operations().expUncertainty(value, uncertainty, exp.getValue())
        );
    }

    @Override
    @NonAlgebraic
    public BigDecimalElement ln() throws IllegalLogarithmException {
        UncertainNumber<BigDecimal> ln = operations().ln(value);
        return new BigDecimalElement(
            ln.getValue(),
            operations().lnUncertainty(value, uncertainty).max(ln.getUncertainty()
            )
        );
    }

    @Override
    public BigDecimalElement sin() {
        UncertainNumber<BigDecimal> sin = operations().sin(value);
        return new BigDecimalElement(
            sin.getValue(),
            uncertainty.max(sin.getUncertainty())
        );
    }


    @Override
    public BigDecimalElement cos() {
        UncertainNumber<BigDecimal> cos = operations().cos(value);
        return new BigDecimalElement(cos.getValue(),
            uncertainty.max(cos.getUncertainty())
        );
    }

    @Override
    public BigDecimalElement times(BigDecimalElement multiplier) {
        return new BigDecimalElement(
            UncertainNumber.super.times(multiplier)
        );
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }

    @Override
    public BigDecimal getUncertainty() {
        return uncertainty.round(UNCERTAINTY_MATH_CONTEXT);
    }


    @Override
    public BigDecimalOperations operations() {
        return BigDecimalOperations.INSTANCE;
    }

    @Override
    public BigDecimalElement pow(int exponent) throws IllegalPowerException {
        if (exponent < 0) {
            try {
                return ONE.dividedBy(pow(-1 * exponent));
            } catch (DivisionByZeroException divisionByZeroException) {
                throw new IllegalPowerException(divisionByZeroException, BasicAlgebraicIntOperator.POWER.stringify(toString(), Integer.toString(exponent)));
            }
        } else {
            return new BigDecimalElement(value.pow(exponent), uncertainty);
        }
    }

    @Override
    public BigDecimalElement reciprocal() {
        //return new BigDecimalElement(UncertainNumber.super.reciprocal());
        try {
            BigDecimal newValue = BigDecimal.ONE.divide(value, getStructure().getMathContext());
            return new BigDecimalElement(
                newValue,
                getFractionalUncertainty().multiply(newValue.abs(), MathContextConfiguration.get().getUncertaintyContext()));
        } catch (ArithmeticException ae) {
            throw new DivisionByZeroException(BigDecimal.ONE, value, ae);
        }
    }

    @Override
    public BigDecimalField getStructure() {
        return BigDecimalField.INSTANCE;
    }

    @Override
    public BigDecimalElement dividedBy(long divisor) {
        UncertainNumber<BigDecimal> newValue = operations().divide(value, BigDecimal.valueOf(divisor));
        UncertainNumber<BigDecimal> uncertaintyValue = uncertainty.equals(BigDecimal.ZERO) ?
            BigDecimalField.INSTANCE.zero() :
            operations().withUncertaintyContext(() ->
                operations().divide(BigDecimal.valueOf(divisor), uncertainty)
            );
        return new BigDecimalElement(
            newValue.getValue(),
            uncertaintyValue.getValue().max(
                newValue.getUncertainty()
            )
        );
    }

    @Override
    public BigDecimalElement times(long multiplier) {
         return new BigDecimalElement(
            value.multiply(BigDecimal.valueOf(multiplier)),
            uncertainty.multiply(BigDecimal.valueOf(multiplier))
        );
    }

    @Override
    public BigDecimalElement times(double multiplier) {
        BigDecimal bigMultiplier = BigDecimal.valueOf(multiplier);
        return new BigDecimalElement(
            value.multiply(bigMultiplier),
            uncertainty.multiply(bigMultiplier)
        );

    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public long longValue() {
        return value.longValue();
    }


    @Override
    public float floatValue() {
        return value.floatValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return value;
    }

    @Override
    public boolean isOne() {
        return value.equals(BigDecimal.ONE);
    }

    @Override
    public int signum() {
        return value.signum();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public BigDecimalElement abs() {
        return new BigDecimalElement(value.abs(), uncertainty);
    }

    @Override
    public BigDecimalElement distanceTo(BigDecimalElement otherElement) {
        return new BigDecimalElement(
            getValue().subtract(otherElement.getValue()).abs(), getUncertainty());
    }


    @SuppressWarnings("unchecked")
    @Override
    public <F extends AlgebraicElement<F>> Optional<F> castDirectly(Class<F> clazz) {
        if (clazz.isAssignableFrom(BigComplexNumber.class)) {
            return Optional.of((F) new BigComplexNumber(this, getStructure().zero()));
        }
        return Optional.empty();
    }


    @Override
    public boolean eq(BigDecimalElement that) {
        return eq(that, Math.round( getConfigurationAspect(ConfidenceIntervalConfiguration.class).getSds() + 0.5f));
    }

    @Override
    public boolean strictlyEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return getValue().equals(((BigDecimalElement) o).getValue());
    }
    @Override
    public boolean equals(Object o) {
        if (getConfigurationAspect(CompareConfiguration.class).isEqualsIsStrict()) {
            return strictlyEquals(o);
        } else {
            return eq((BigDecimalElement) o);
        }
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return value.toString();
    }



}
