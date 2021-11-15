package org.meeuw.physics.text;

import java.text.*;

import javax.validation.constraints.NotNull;

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
    public StringBuffer format(Object object, @NotNull StringBuffer toAppendTo, @NotNull FieldPosition pos) {
        StringBuffer builder = new StringBuffer();
        UnitsImpl units = (UnitsImpl) object;
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
        if ("/s".equals(source.trim())) {
            pos.setIndex(2);
            return Units.of(SIUnit.s).reciprocal();
        }
        if ("".equals(source.trim())) {
            pos.setIndex(0);
            return Units.DIMENSIONLESS;
        }
        throw new UnsupportedOperationException();
    }

}
