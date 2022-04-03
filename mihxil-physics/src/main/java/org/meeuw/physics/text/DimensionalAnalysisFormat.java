package org.meeuw.physics.text;

import java.text.*;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.text.TextUtils;
import org.meeuw.physics.Dimension;
import org.meeuw.physics.DimensionalAnalysis;

/**
 * @author Michiel Meeuwissen
 * @since 0.5
 */
public class DimensionalAnalysisFormat extends Format {

    @Override
    public StringBuffer format(@NonNull Object number, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {

        StringBuffer buf = new StringBuffer();
        DimensionalAnalysis dimension = (DimensionalAnalysis) number;
        if (dimension.isOne()) {
            buf.append("1");
        } else {
            buf.append(TextUtils.toString(Dimension.values(), dimension.getExponents()));
        }
        return buf;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException();
    }

}
