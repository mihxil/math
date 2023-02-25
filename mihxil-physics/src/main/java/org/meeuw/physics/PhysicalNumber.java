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
package org.meeuw.physics;

import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Objects;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.WithUnits;
import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;
import org.meeuw.math.exceptions.IllegalPowerException;
import org.meeuw.math.exceptions.ReciprocalException;
import org.meeuw.math.numbers.Scalar;
import org.meeuw.math.numbers.SignedNumber;
import org.meeuw.math.text.FormatService;
import org.meeuw.math.uncertainnumbers.*;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

/**
 * <p>
 * An uncertain number but also with {@link Units}
 *</p>
 * <p>
 * This makes it not a {@link org.meeuw.math.abstractalgebra.FieldElement}, but merely a {@link MultiplicativeGroupElement}, since you cannot add any physical number to another.
 * </p>
 * <p>
 * There are two basic implementations
 * </p>
 * <ul>
 *  <li>{@link Measurement}</li>
 *  <li>{@link PhysicalConstant}</li>
 * </ul>

 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class PhysicalNumber
    extends Number
    implements MultiplicativeGroupElement<PhysicalNumber>,
    UncertainDouble<PhysicalNumber>,
    Scalar<PhysicalNumber>,
    Comparable<PhysicalNumber>,
    SignedNumber<PhysicalNumber>,
    WithUnits {

    @Getter
    protected final UncertainReal wrapped;

    @Getter
    protected final Units units;

    PhysicalNumber(@NonNull UncertainReal wrapped, @NonNull Units units) {
        this.units = units;
        this.wrapped = wrapped;
    }

    @Override
    public long longValue() {
        return wrapped.longValue();
    }
    @Override
    public int intValue() {
        return wrapped.intValue();
    }

    @Override
    public float floatValue() {
        return wrapped.floatValue();
    }

    @Override
    public double doubleValue() {
        return wrapped.doubleValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return wrapped.bigDecimalValue();
    }

    @Override
    public double doubleUncertainty() {
        return wrapped.doubleUncertainty();
    }

    @Override
    public PhysicalNumber weightedAverage(UncertainDouble<?> combinand) {
        if (combinand instanceof PhysicalNumber) {
            combinand = ((PhysicalNumber) combinand).toUnits(this.getUnits());
        }
        return copy(wrapped.weightedAverage(combinand), units);
    }

    @Override
    public PhysicalNumber times(double multiplier) {
        return copy(wrapped.times(multiplier), units);
    }

    @Override
    public PhysicalNumber times(PhysicalNumber multiplier) {
        return copy(
            wrapped.times(multiplier.wrapped),
            Units.forMultiplication(units, multiplier.getUnits())
        );
    }

    public PhysicalNumber times(UncertainReal multiplier) {
        return copy(wrapped.times(multiplier), units);
    }

    @Override
    @NonAlgebraic(reason = NonAlgebraic.Reason.SOME)
    public PhysicalNumber dividedBy(PhysicalNumber  divisor) throws ReciprocalException {
        return times(divisor.reciprocal());
    }

    @Override
    public PhysicalNumber pow(int exponent) {
        return copy(
            wrapped.pow(exponent),
            Units.forExponentiation(units, exponent));
    }

    /**
     * Units will implicitly be converted, and the resulting value will have the units of this.
     *
     * @throws DimensionsMismatchException If the summand has dimensions incompatible with the dimensions of this. (e.g. you cannot add meters to seconds).
     * @param summand the physical number to add to this one
     * @return  a new physical number which is the sum of this one and another one.
     */
    @Override
    @NonAlgebraic(reason = NonAlgebraic.Reason.ELEMENTS, value="dimensions must match")
    public PhysicalNumber plus(PhysicalNumber summand) throws DimensionsMismatchException {
        summand = summand.toUnits(this.getUnits());
        return copy(wrapped.plus(summand.wrapped), Units.forAddition(units, summand.getUnits()));
    }

    @Override
    public PhysicalNumber reciprocal() throws ReciprocalException {
        try {
            return pow(-1);
        } catch (IllegalPowerException illegalPowerException) {
            throw new ReciprocalException(illegalPowerException);
        }
    }

    /**
     * Converts this to a new physical number but represented in the given units.
     *
     * @throws DimensionsMismatchException if the target units are not compatible (have different dimensions)
     * @param target the new units
     * @return a new physical number representing the same value as this one, only in different units
     */
    public PhysicalNumber toUnits(Units target) throws DimensionsMismatchException {
        if (getUnits().equals(target)) {
            return this;
        }
        UncertainReal factor = getUnits().conversionFactor(target);
        return copy(wrapped.times(factor), target);
    }

    public PhysicalNumber toUnits(Unit... units) throws DimensionsMismatchException {
        return toUnits(Units.of(units));
    }

    /**
     * Converts the current physical number to a different {@link SystemOfMeasurements}.
     */
    public PhysicalNumber toUnits(SystemOfMeasurements systemOfMeasurements) {
        return toUnits(systemOfMeasurements.forDimensions(units.getDimensions()));
    }

    /**
     * Just adds {@link #plus(PhysicalNumber)}{@link #negation()}.
     * @see #plus(PhysicalNumber)
     * @param subtrahend to physical number to subtract from this one
     * @return the current number minus the subtrahend
     */
    @NonAlgebraic(reason = NonAlgebraic.Reason.ELEMENTS, value="dimensions must match")
    public PhysicalNumber minus(PhysicalNumber subtrahend) throws DimensionsMismatchException {
        return plus(subtrahend.negation());
    }

    @Override
    public PhysicalNumber plus(double summand) {
        return copy(wrapped.plus(summand), units);
    }

    protected abstract PhysicalNumber copy(UncertainReal wrapped, Units units);

    @Override
    public PhysicalNumbers getStructure() {
        return PhysicalNumbers.INSTANCE;
    }

    @Override
    @NonAlgebraic
    public PhysicalNumber negation() {
        return times(-1);
    }

    @Override
    public int signum() {
        return wrapped.signum();
    }

    @Override
    @NonAlgebraic
    public PhysicalNumber abs() {
        if (isPositive()) {
            return this;
        } else {
            return new Measurement(wrapped.abs(), getUnits());
        }
    }

    @Override
    public PhysicalNumber immutableInstanceOfPrimitives(double value, double uncertainty) {
        return new Measurement(value, uncertainty, units);
    }

    @Override
    public boolean eq(PhysicalNumber of) {
        if (! units.getDimensions().eq(of.units.getDimensions())) {
            return  false;
        }
        PhysicalNumber sameUnits = of.toUnits(units);
        return wrapped.eq(sameUnits.wrapped, ConfigurationService.getConfigurationAspect(ConfidenceIntervalConfiguration.class).getSds());
    }

    @Override
    public boolean strictlyEquals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhysicalNumber)) return false;
        PhysicalNumber of = (PhysicalNumber) o;
        if (! units.getDimensions().equals(of.units.getDimensions())) {
            return  false;
        }
        return Objects.equals(getValue(), of.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if ( ConfigurationService.getConfigurationAspect(CompareConfiguration.class).isEqualsIsStrict()) {
            return strictlyEquals(o);
        } else {
            return eq((PhysicalNumber) o);
        }
    }

    @Override
    public int hashCode() {
        return  (units != null ? units.hashCode() : 0);
    }

    @Override
    public int compareTo(@org.checkerframework.checker.nullness.qual.NonNull PhysicalNumber f) {
        if (equals(f)) {
            return 0;
        }

        return Double.compare(this.doubleValue(), f.toUnits(getUnits()).doubleValue());
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
    public String getUnitsAsString() {
        return FormatService.toString(units);
    }

}

