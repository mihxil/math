package org.meeuw.physics.text;

import java.text.*;

import javax.validation.constraints.NotNull;

import org.meeuw.math.Utils;
import org.meeuw.physics.Dimension;
import org.meeuw.physics.DimensionalAnalysis;

/**
 * @author Michiel Meeuwissen
 * @since 0.5
 */
public class DimensionalAnalysisFormat extends Format {

    @Override
    public StringBuffer format(Object number, @NotNull StringBuffer toAppendTo, @NotNull FieldPosition pos) {

        StringBuffer buf = new StringBuffer();
        DimensionalAnalysis dimension = (DimensionalAnalysis) number;
        if (dimension.isOne()) {
            buf.append("1");
        } else {
            buf.append(Utils.toString(Dimension.values(), dimension.getExponents()));
        }
        return buf;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException();
    }

}
