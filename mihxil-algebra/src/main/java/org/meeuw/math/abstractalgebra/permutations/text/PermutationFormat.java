package org.meeuw.math.abstractalgebra.permutations.text;

import java.text.*;

import org.meeuw.math.abstractalgebra.permutations.Permutation;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class PermutationFormat extends Format {

    private final Notation notation;
    private final Offset offset;

    public PermutationFormat(Notation notation, Offset offset) {
        this.notation = notation;
        this.offset = offset;
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        int o = offset.getAsInt();
        switch (notation) {
            case CYCLES:
                toAppendTo.append(((Permutation) obj).cycleNotation(o));
                break;
            case LIST:
                toAppendTo.append(((Permutation) obj).listNotation(o));
        }
        return toAppendTo;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException();
    }
}
