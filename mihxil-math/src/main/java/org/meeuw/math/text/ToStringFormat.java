package org.meeuw.math.text;

import java.text.*;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @since 0.19
 */
class ToStringFormat extends Format {

    static final ToStringFormat INSTANCE = new ToStringFormat();

    @Override
    public StringBuffer format(Object obj, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
        return obj == null ? toAppendTo : toAppendTo.append(obj);
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        pos.setErrorIndex(pos.getIndex());
        return null;
    }
}
