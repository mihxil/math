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
package org.meeuw.math.text;

import lombok.*;

import java.text.*;

import org.meeuw.math.text.configuration.UncertaintyConfiguration;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 * @author Michiel Meeuwissen
 * @since 0.9
 */
public class UncertainNumberFormat extends Format {

    @Getter
    @Setter
    private UncertaintyConfiguration.Notation uncertaintyNotation = UncertaintyConfiguration.Notation.PLUS_MINUS;

    @Override
    public StringBuffer format(Object number, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
        if (number instanceof UncertainNumber<?>) {
            UncertainNumber<?> uncertainNumber = (UncertainNumber<?>) number;

            toAppendTo.append(valueAndError(uncertainNumber.getValue().toString(), uncertainNumber.getUncertainty().toString(), getUncertaintyNotation()));
            return toAppendTo;
        } else {
            throw new IllegalArgumentException("Cannot format given Object " + number.getClass() + " as a Number");
        }
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        return null;
    }



    public static String valuePlusMinError(String value, String error) {
        return value + ' ' + TextUtils.PLUSMIN + ' ' + error;
    }

    public static String valueParenthesesError(String value, String error) {
        int i = 0;
        while (i < error.length() && (error.charAt(i) == '0' || error.charAt(i) == '.')) {
             i++;
         }
         return value +  "(" + error.substring(i) + ")";
    }

    public static String valueAndError(String value, String error, UncertaintyConfiguration.Notation uncertaintyNotation) {
        return switch (uncertaintyNotation) {
            case PARENTHESES -> valueParenthesesError(value, error);
            case PLUS_MINUS -> valuePlusMinError(value, error);
        };
    }


}
