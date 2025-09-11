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

import lombok.NonNull;

import java.text.FieldPosition;
import java.text.Format;

import org.meeuw.math.abstractalgebra.reals.DoubleElement;
import org.meeuw.math.exceptions.NotParsable;
import org.meeuw.math.numbers.Factor;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 * @author Michiel Meeuwissen
 * @since 0.9
 */
public class UncertainNumberFormat extends AbstractUncertainFormat<UncertainNumber<?>> {

    @Override
    public StringBuffer format(Object number, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
        if (number instanceof UncertainNumber<?> uncertainNumber) {
            valueAndError(toAppendTo, ToStringFormat.INSTANCE,  pos, uncertainNumber.getValue(), uncertainNumber.getUncertainty(),getUncertaintyNotation());
            return toAppendTo;
        } else {
            throw new IllegalArgumentException("Cannot format given Object " + number.getClass() + " as a Number");
        }
    }

    @Override
    UncertainNumber<?> of(String v, Factor factor) {
        throw new NotParsable.NotImplemented("Not supported yet. to parse number " + v + " (" + factor + ")");
    }

    @Override
    UncertainNumber<?> of(String v, String uncertainty, Factor factor) {
        return DoubleElement.of(Double.parseDouble(v), Double.parseDouble(uncertainty));
    }




}
