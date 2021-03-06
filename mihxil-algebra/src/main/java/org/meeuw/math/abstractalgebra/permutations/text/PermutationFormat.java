package org.meeuw.math.abstractalgebra.permutations.text;

import lombok.With;

import java.text.*;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.meeuw.math.abstractalgebra.permutations.Permutation;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class PermutationFormat extends Format {

    @With
    private final Notation notation;
    @With
    private final Offset offset;

    public PermutationFormat(@NotNull Notation notation, @NotNull Offset offset) {
        this.notation = notation;
        this.offset = offset;
    }

    @Override
    public StringBuffer format(Object obj, @NotNull StringBuffer toAppendTo, @NotNull FieldPosition pos) {

        if (obj instanceof Permutation) {
            int o = offset.getAsInt();
            Permutation p = (Permutation) obj;
            switch (notation) {
                case CYCLES:
                    toAppendTo.append(p.cycleNotation(o));
                    break;
                case LIST:
                    toAppendTo.append(p.listNotation(o));
            }
            return toAppendTo;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * only support list notation for now.
     */
    @Override
    public Object parseObject(String source, ParsePosition pos) {
        int open = pos.getIndex();
        if (source.charAt(open) != '(') {
            pos.setErrorIndex(open);
            return null;
        }
        List<Integer> result = new ArrayList<>();
        int close = source.indexOf(')', open + 1);
        if (close == -1) {
            pos.setErrorIndex(open);
            return null;
        }
        String subsource = source.substring(open + 1, close);
        String[] string = subsource.split(" ");
        if (string.length == 1 && string[0].length() > 1) {
            string = subsource.split("");
        }
        for (String s : string) {
            result.add(Integer.parseInt(s));
        }
        pos.setIndex(close);
        return Permutation.zeroOffset(result.stream().mapToInt(in-> in - offset.getAsInt()).toArray());
    }
}
