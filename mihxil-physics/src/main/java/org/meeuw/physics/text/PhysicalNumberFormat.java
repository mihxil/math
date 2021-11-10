package org.meeuw.physics.text;

import java.text.*;

import javax.validation.constraints.NotNull;

import org.meeuw.physics.PhysicalNumber;

/**
 * @author Michiel Meeuwissen
 * @since 0.5
 */
public class PhysicalNumberFormat extends Format {

    @Override
    public StringBuffer format(Object number, @NotNull StringBuffer toAppendTo, @NotNull FieldPosition pos) {

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
