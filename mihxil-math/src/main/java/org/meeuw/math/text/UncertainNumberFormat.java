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
import java.util.OptionalLong;

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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    UncertainNumber<?> of(String v, String uncertainty, Factor factor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }




    public static String valuePlusMinError(String value, String error) {
        return value + ' ' + TextUtils.PLUSMIN + ' ' + error;
    }


    /**
     * @since 0.19
     */
    static void valuePlusMinError(StringBuffer appendable, Format format, FieldPosition position, Object value, Object error) {
        format.format(value, appendable, position);
        appendable.append(' ');
        appendable.append(TextUtils.PLUSMIN);
        appendable.append(' ');
        format.format(error, appendable, position);
    }

    public static String valueParenthesesError(String value, String error) {
        int i = 0;
        while (i < error.length() && (error.charAt(i) == '0' || error.charAt(i) == '.')) {
             i++;
         }
         return value +  "(" + error.substring(i) + ")";
    }


    /**
     * @since 0.19
     */
     static void valueParenthesesError(StringBuffer appendable, Format format, FieldPosition pos, Object value, Object error) {
        format.format(value, appendable, pos);
        appendable.append('(');
        format.format(error, appendable, pos);
        appendable.append(')');
     }

    public static String valueAndError(String value, String error, UncertaintyConfiguration.Notation uncertaintyNotation) {
        return switch (uncertaintyNotation) {
            case PARENTHESES -> valueParenthesesError(value, error);
            case PLUS_MINUS -> valuePlusMinError(value, error);
           // case ROUND_VALUE -> valueRounded(value, error);
        };
    }


    /**
     * @since 0.19
     */
    public static void valueAndError(StringBuffer appendable, Format format, FieldPosition position, Object value, Object error, UncertaintyConfiguration.Notation uncertaintyNotation) {
        switch (uncertaintyNotation) {
            case PARENTHESES -> valueParenthesesError(appendable, format, position, value, error);
            case PLUS_MINUS -> valuePlusMinError(appendable, format, position, value, error);
           // case ROUND_VALUE -> valueRounded(value, error);
        }
    }


}
