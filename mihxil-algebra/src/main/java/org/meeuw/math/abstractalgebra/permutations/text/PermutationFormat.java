/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.abstractalgebra.permutations.text;

import lombok.With;

import java.text.*;
import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.nullness.qual.NonNull;
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

    public PermutationFormat(@NonNull Notation notation, @NonNull Offset offset) {
        this.notation = notation;
        this.offset = offset;
    }

    @Override
    public StringBuffer format(Object obj, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {

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
