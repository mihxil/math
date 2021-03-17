package org.meeuw.physics.text;

import java.text.*;

import javax.validation.constraints.NotNull;

import org.meeuw.math.text.TextUtils;
import org.meeuw.physics.UnitExponent;
import org.meeuw.physics.UnitsImpl;

/**
 * @author Michiel Meeuwissen
 * @since 0.6
 */
public class UnitsFormat extends Format {

    @Override
    public StringBuffer format(Object number, @NotNull StringBuffer toAppendTo, @NotNull FieldPosition pos) {

        StringBuffer builder = new StringBuffer();
        UnitsImpl units = (UnitsImpl) number;
        for (UnitExponent e : units.getExponents()) {
            if (e.getExponent() != 0) {
                if (builder.length() > 0) {
                    builder.append(TextUtils.TIMES);
                }
                builder.append(e.toString());
            }
        }
        return builder;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException();
    }

}
