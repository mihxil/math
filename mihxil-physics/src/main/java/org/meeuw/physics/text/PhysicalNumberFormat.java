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
import org.meeuw.physics.PhysicalNumber;

/**
 * @author Michiel Meeuwissen
 * @since 0.5
 */
public class PhysicalNumberFormat extends Format {

    @Override
    public StringBuffer format(@NonNull Object number, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {

        StringBuffer buf = new StringBuffer();
        PhysicalNumber physicalNumber = (PhysicalNumber) number;
        buf.append(physicalNumber.getWrapped())
            .append(" ")
            .append(physicalNumber.getUnits());

        return buf;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException();
    }

}
