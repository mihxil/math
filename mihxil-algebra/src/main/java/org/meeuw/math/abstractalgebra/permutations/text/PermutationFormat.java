/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
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
import java.util.regex.Pattern;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.ArrayUtils;
import org.meeuw.math.abstractalgebra.permutations.Permutation;
import org.meeuw.math.abstractalgebra.permutations.PermutationGroup;
import org.meeuw.math.text.FormatService;

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

        if (obj instanceof Permutation p) {
            int o = offset.getAsInt();
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
    public Object parseObject(String source, @NonNull ParsePosition pos) {
        return switch(notation) {
            case LIST -> parseListNotation(source, pos);
            case CYCLES -> parseCycleNotation(FormatService.getCurrentStructure(), source, pos);
        };

    }
    public Permutation parseListNotation(String source, ParsePosition pos) {
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
        if (string.length == 1 && string[0].length() > 1) {
            string = subsource.split("");
        }
        for (String s : string) {
            if (!s.isEmpty()) {
                result.add(Integer.parseInt(s));
            }
        }
        pos.setIndex(close);
        return Permutation.zeroOffset(result.stream().mapToInt(in-> in - offset.getAsInt()).toArray());
    }

    public Permutation parseCycleNotation(PermutationGroup group, String source, ParsePosition pos) {
        int start = pos.getIndex();
        List<int[]> cycles = new ArrayList<>();
        int i = start;
        Pattern splitPattern = Pattern.compile(group.getDegree() > 9 ? "\\s+" : "");
        while (i < source.length()) {
            if (source.charAt(i) == '(') {
                int end = source.indexOf(')', i);
                if (end == -1) {
                    pos.setErrorIndex(i);
                    return null;
                }
                String cycleStr = source.substring(i + 1, end).trim();
                if (!cycleStr.isEmpty()) {
                    String[] elements = splitPattern.split(cycleStr);
                    List<Integer> cycle = new ArrayList<>();
                    for (String el : elements) {
                        cycle.add(Integer.parseInt(el) - offset.getAsInt());
                    }
                    cycles.add(ArrayUtils.toArray(cycle));
                }
                i = end + 1;
            } else if (Character.isWhitespace(source.charAt(i))) {
                i++;
            } else {
                break;
            }
        }
        pos.setIndex(i);
        return group.fromCycles(cycles);
    }

}
