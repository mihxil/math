package org.meeuw.math.text;

import java.text.*;

/**
 * @since 0.19
 */
class ToStringFormat extends Format {

    static final ToStringFormat INSTANCE = new ToStringFormat();

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        return obj == null ? toAppendTo : toAppendTo.append(obj);
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        pos.setErrorIndex(pos.getIndex());
        return null;
    }
}
