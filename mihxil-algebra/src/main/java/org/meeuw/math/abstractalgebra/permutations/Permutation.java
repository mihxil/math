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
package org.meeuw.math.abstractalgebra.permutations;

import java.io.Serializable;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;
import org.meeuw.math.abstractalgebra.permutations.text.PermutationConfiguration;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.text.FormatService;

import static org.meeuw.configuration.ConfigurationService.getConfigurationAspect;


/**
 * A permutation represents a certain reordering of {@link PermutationGroup#getDegree() a certain number of elements}.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Permutation  implements
    MultiplicativeGroupElement<Permutation>,
    UnaryOperator<Object[]>,
    Serializable {

    private static final long serialVersionUID = 0L;

    final int[] value;
    private transient List<Cycle> cycles;

    /**
     * Creates a new permutation object
     * @param value Precisely the numbers {@code 1..<degree>}, in a certain unique order. E.g. {@code 5,3,2,1,4}, for that permutation of degree {@code 5}
     * @see #permute(Object[])
     */
    public static Permutation of(int... value) throws InvalidElementCreationException {
        for (int i = 0; i < value.length; i++) {
            value[i]--;
        }
        return new Permutation(true, value);
    }

    public static Permutation zeroOffset(int... value) throws InvalidElementCreationException {
        return new Permutation(true, value);
    }

    Permutation(boolean validate, int... value) throws InvalidElementCreationException {
        this.value = value;
        if (validate) {
            int[] copy = Arrays.copyOf(this.value, this.value.length);
            Arrays.sort(copy);
            if (!IntStream.range(0, value.length).allMatch(i -> Arrays.binarySearch(copy, i) >= 0)) {
                throw new InvalidElementCreationException("Permutation is invalid " + listNotation(0));
            }
        }
    }

    @Override
    public @NonNull PermutationGroup getStructure() {
        return PermutationGroup.ofDegree(value.length);
    }

    @Override
    public Permutation reciprocal() {
        int[] result = new int[value.length];
        for (int i = 0; i < value.length; i++) {
            result[value[i]] = i;
        }
        return new Permutation(false, result);
    }

    /**
     * Executes this permutation on the given array of values. E.g.
     * if the permutation is {@code (2, 3, 1)} and it works on {@code ["A", "B", "C"]}
     * the result is {@code ["C", "A", "B"]}.
     *
     * @throws IndexOutOfBoundsException if the given array is too short.
     * @param <P> the type of the values
     * @param values the values
     * @return the same values but now permuted with this permutation
     */
    @SuppressWarnings("unchecked")
    public <P> P[] permute(P... values) {
        P[] result = Arrays.copyOf(values, values.length);
        for (int i = 0 ; i < value.length; i++) {
            result[value[i]] = values[i];
        }
        return result;
    }

    public  int[] permuteInts(int... values) {
        int[] result = Arrays.copyOf(values, values.length);
        for (int i = 0 ; i < value.length; i++) {
            result[value[i]] = values[i];
        }
        return result;
    }


    @Override
    public Permutation times(Permutation multiplier) {
        int[] result = new int[value.length];
        for (int i = 0 ; i < result.length; i++) {
            result[i] = value[multiplier.value[i]];
        }
        return new Permutation(false, result);
    }

    /**
     * Returns the 'cycles' of the permutation.
     * See <a href="https://en.wikipedia.org/wiki/Permutation#Cycle_notation">wikipedia</a>
     */
    public List<Cycle> getCycles() {
        if (cycles == null) {
            List<Cycle> result = new ArrayList<>();
            if (value.length > 0) {
                List<Integer> help = new ArrayList<>(value.length);
                int[] todo = Arrays.copyOf(value, value.length);

                int i = 0;
                OUTER:
                while (true) {
                    help.add(i);
                    todo[i] = -1;
                    i = value[i];
                    if (help.contains(i)) {
                        result.add(new Cycle(help.stream().mapToInt(e -> e).toArray()));
                        help.clear();
                        for (int j = 0; j < todo.length; j++) {
                            if (todo[j] != -1) {
                                i = j;
                                continue OUTER;
                            }
                        }
                        break;
                    }
                }
            }
            cycles = Collections.unmodifiableList(result);
        }
        return cycles;
    }

    /**
     * See <a href="https://en.wikipedia.org/wiki/Permutation#Cycle_notation">wikipedia</a>
     */
    public String cycleNotation(int offset) {
        String s = getCycles().stream()
            .map(c -> c.value.length == 1 ? "" : c.toString(offset))
            .collect(Collectors.joining());
        if (s.isEmpty()) {
            return "()";
        } else {
            return s;
        }
    }

    public String listNotation(int offset) {
        String join = value.length > 9 ? " " : "";
        return "(" + IntStream.of(value).mapToObj(i -> String.valueOf(i + offset)).collect(Collectors.joining(join)) + ")";
    }

    /**
     * Calls {@link #permute(Object[])}
     */
    @Override
    public Object[] apply(Object[] objects) {
        return permute(objects);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Permutation that = (Permutation) o;

        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    @Override
    public String toString() {
        return FormatService.toString(this);
    }

    public class Cycle {
        final int[] value;

        public Cycle(int... value) {
            this.value = value;
        }

        public Permutation getParent() {
            return Permutation.this;
        }

        public Cycle reciprocal() {
            int[] rec = new int[value.length];
            for (int i = 0; i < rec.length; i++) {
                rec[i] = value[value.length - i - 1];
            }
            return new Cycle(rec);
        }

        public String toString(int offset) {
            String join = Permutation.this.value.length > 9 ? " " : "";
            return "(" + IntStream.of(value).mapToObj(i -> String.valueOf(i + offset)).collect(Collectors.joining(join)) + ")";
        }

        /**
         * @see PermutationConfiguration
         */
        @Override
        public String toString() {
            return toString(getConfigurationAspect(PermutationConfiguration.class).getOffset().getAsInt());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cycle cycle = (Cycle) o;

            return Objects.equals(getParent(), cycle.getParent()) && Arrays.equals(value, cycle.value);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(value);
        }
    }

}
