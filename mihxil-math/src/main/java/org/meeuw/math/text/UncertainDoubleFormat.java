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
package org.meeuw.math.text;


import java.text.FieldPosition;

import org.meeuw.math.abstractalgebra.reals.DoubleElement;
import org.meeuw.math.numbers.DoubleOperations;
import org.meeuw.math.numbers.Factor;
import org.meeuw.math.uncertainnumbers.UncertainDouble;

import static org.meeuw.math.DoubleUtils.uncertaintyForDouble;

/**
 * Can format {@link DoubleElement}s.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainDoubleFormat extends AbstractUncertainFormat<UncertainDouble<?>, DoubleElement, Double> {



    DoubleOperations ops;


    public UncertainDoubleFormat() {
        super(UncertainDouble.class, DoubleOperations.INSTANCE);
    }

    private boolean roundingErrorsOnly(double value, double uncertainty) {
        return uncertainty < uncertaintyForDouble(value) * considerRoundingErrorFactor;
    }

    @Override
    DoubleElement of(String valueStr, Factor factor) {
        double value = scientific.fromString(valueStr);
        return (DoubleElement) factor.apply(DoubleElement.of(value, considerRoundingErrorFactor * uncertaintyForDouble(value)));

    }

    @Override
    DoubleElement of(String valueStr, String uncertaintyStr, Factor factor) {
        double value = scientific.fromString(valueStr);
        double uncertainty = scientific.fromString(uncertaintyStr);

        return (DoubleElement) factor.apply(DoubleElement.of(value, uncertainty));

    }

    @Override
    protected void valueParenthesesError(StringBuffer appendable, FieldPosition position, UncertainDouble<?> uncertainNumber) {
        valueAndError(appendable, position, uncertainNumber);
    }

    @Override
    protected void valuePlusMinError(StringBuffer appendable, FieldPosition position, UncertainDouble<?> uncertainNumber) {
        valueAndError(appendable, position, uncertainNumber);
    }

    @Override
    protected void valueRound(StringBuffer appendable, FieldPosition position, UncertainDouble<?> value) {
        valueAndError(appendable, position, value);

    }

    protected void valueAndError(StringBuffer appendable, FieldPosition position, UncertainDouble<?> uncertainNumber) {
        if (uncertainNumber.isExact() || roundingErrorsOnly(uncertainNumber.doubleValue(), uncertainNumber.doubleUncertainty())) {
            scientific.format(
                uncertainNumber.getValue(),
                uncertainNumber.getUncertainty(),
                appendable, position);
        } else {
            scientific.formatWithUncertainty(
                uncertainNumber.getValue(),
                uncertainNumber.getUncertainty(),
                appendable,
                position
            );
        }
    }




}
