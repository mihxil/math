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

import org.meeuw.math.DoubleUtils;
import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.MetricSpaceElement;
import org.meeuw.math.exceptions.IllegalPowerException;
import org.meeuw.math.text.TextUtils;
import org.meeuw.math.uncertainnumbers.UncertainDouble;
import org.meeuw.math.uncertainnumbers.UncertainScalar;

/**
 * An element of the {@link RealField}.
 * This is the connection between {@link org.meeuw.math.uncertainnumbers.UncertainNumber} and {@link org.meeuw.math.abstractalgebra.FieldElement}s.
 * <p>
 * So, an uncertain scalar that is also an element of an algebra (a {@link org.meeuw.math.abstractalgebra.Field}, event)
 *
 * @author Michiel Meeuwissen
 * @see DoubleElement
 *  * @since 0.4
 */
public interface RealNumber
    extends
    UncertainScalar<Double, RealNumber>,
    UncertainDouble<RealNumber>,
    MetricSpaceElement<RealNumber, RealNumber>,
    CompleteScalarFieldElement<RealNumber> {

     DoubleElement ZERO = new DoubleElement(0, EXACT) {
        @Override
        public DoubleElement sqrt() {
            return this;
        }

        @Override
        public DoubleElement sqr() {
            return this;
        }

        @Override
        public DoubleElement pow(int exponent) {
            if (exponent == 0) {
                return ONE;
            }
            if (exponent < 0) {
                throw new IllegalPowerException("Cannot take negative power", this + TextUtils.superscript(  exponent));
            }
            return this;
        }
    };


     DoubleElement ONE  = new DoubleElement(1, EXACT) {
        @Override
        public DoubleElement sqrt() {
            return this;
        }
        @Override
        public DoubleElement sqr() {
            return this;
        }
        @Override
        public DoubleElement pow(int exponent) {
            return this;
        }
    };


    static RealNumber of(double value) {
        return DoubleElement.of(value, DoubleUtils.uncertaintyForDouble(value));
    }

    @Override
    RealNumber negation();

    @Override
    RealNumber times(RealNumber multiplier);

    @Override
    RealNumber pow(int exponent);

    @Override
    RealNumber plus(RealNumber summand);

    @Override
    RealNumber tan();

    @Override
    default RealNumber abs() {
        return immutableInstanceOfPrimitives(Math.abs(getValue()), getUncertainty());
    }

    @Override
    default RealNumber distanceTo(RealNumber otherElement) {
        return minus(otherElement).abs();
    }

    /**
     * For uncertain elements, an element is only zero if its value is {@link #isExact()}
     * and of course {@code 0}.
     */
    @Override
    default boolean isZero() {
        return CompleteScalarFieldElement.super.isZero();
    }

    default boolean isExactlyZero() {
        return isExact() && isZero();
    }

    @Override
    default RealField getStructure() {
        return RealField.INSTANCE;
    }


    /**
     * For uncertain elements, an element is only one if its value is {@link #isExact()}
     * and of course {@code 1}.
     */
    @Override
    default boolean isOne() {
        return isExact() && CompleteScalarFieldElement.super.isOne();
    }

}
