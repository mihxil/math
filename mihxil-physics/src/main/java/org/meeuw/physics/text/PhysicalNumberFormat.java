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
