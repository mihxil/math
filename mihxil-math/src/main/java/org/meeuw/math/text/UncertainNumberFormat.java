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
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.exceptions.NotParsable;
import org.meeuw.math.numbers.*;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 * @author Michiel Meeuwissen
 * @since 0.9
 */
public class UncertainNumberFormat<N extends Number> extends AbstractUncertainFormat<UncertainNumber<?>, RealNumber, N> {

    public UncertainNumberFormat() {
        super(UncertainNumber.class, null);
    }

    @Override
    DoubleElement of(String v, Factor factor) {
        throw new NotParsable.NotImplemented("Not supported yet. to parse number " + v + " (" + factor + ")");
    }

    @Override
    DoubleElement of(String v, String uncertainty, Factor factor) {
        return DoubleElement.of(Double.parseDouble(v), Double.parseDouble(uncertainty));
    }

    @Override
    protected void valueParenthesesError(StringBuffer appendable, FieldPosition position, UncertainNumber<?> value) {
        UncertainFormatUtils.valueParenthesesError(appendable, ToStringFormat.INSTANCE, position, value.getValue(), value.getUncertainty());
    }

    @Override
    protected void valuePlusMinError(StringBuffer appendable, FieldPosition position, UncertainNumber<?> value) {
        UncertainFormatUtils.valuePlusMinError(appendable, ToStringFormat.INSTANCE, position, value.getValue(), value.getUncertainty());
    }

    @Override
    protected void valueRound(StringBuffer appendable, FieldPosition position, UncertainNumber<?> value) {
        ToStringFormat.INSTANCE.format(value.getValue(), appendable, position);
    }


}
