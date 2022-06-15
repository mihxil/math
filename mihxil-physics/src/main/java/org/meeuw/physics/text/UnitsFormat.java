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
package org.meeuw.physics.text;

import java.text.*;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.text.TextUtils;
import org.meeuw.physics.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.6
 */
public class UnitsFormat extends Format {

    public static final UnitsFormat INSTANCE = new UnitsFormat();

    private UnitsFormat() {

    }

    @Override
    public StringBuffer format(
        @NonNull Object object,
        @NonNull StringBuffer toAppendTo,
        @NonNull FieldPosition pos) {
        StringBuffer builder = new StringBuffer();
        CompositeUnits units = (CompositeUnits) object;
        for (UnitExponent e : units.getExponents()) {
            if (e.getExponent() != 0) {
                if (builder.length() > 0) {
                    builder.append(TextUtils.TIMES);
                }
                builder.append(e);
            }
        }
        return builder;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        // TODO, this just implements some cases we happen to need.
        // A real solution will be based on javacc or so.
        if ("/s".equals(source.substring(pos.getIndex()).trim())) {
            pos.setIndex(pos.getIndex() + 2);
            return Units.of(SIUnit.s).reciprocal();
        }
        if ("".equals(source.substring(pos.getIndex()).trim())) {
            pos.setIndex(pos.getIndex() + 1);
            return Units.DIMENSIONLESS;
        }
        throw new UnsupportedOperationException();
    }

}
