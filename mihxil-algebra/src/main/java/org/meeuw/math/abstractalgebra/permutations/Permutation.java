package org.meeuw.math.abstractalgebra.permutations;

import java.io.Serializable;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.*;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.abstractalgebra.permutations.text.Offset;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.math.text.spi.Configuration;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Permutation  implements
    MultiplicativeGroupElement<Permutation>,
    UnaryOperator<Object[]>,
    Serializable
{
    private static final long serialVersionUID = 0L;

    final int[] value;
    private transient List<Cycle> cycles;

    public static Permutation of(int... value) {
        for (int i = 0; i < value.length; i++) {
            value[i]--;
        }
        return new Permutation(true, value);
    }

    public static Permutation zeroOffset(int... value) {
        return new Permutation(true, value);
    }

    Permutation(boolean validate, int... value) {
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
    public PermutationGroup getStructure() {
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
     * Executes this permutation on the given array of values.
     * @throws IndexOutOfBoundsException if the given array is too short.
     */
    @SuppressWarnings("unchecked")
    public <P> P[] permute(P... values) {
        P[] result = Arrays.copyOf(values, values.length);
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

    public List<Cycle> getCycles() {
        if (cycles == null) {
            List<Cycle> result = new ArrayList<>();
            List<Integer> help = new ArrayList<>(value.length);
            int[] todo = Arrays.copyOf(value, value.length);

            int i = 0;
            OUTER:
            while(true) {
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
            cycles = Collections.unmodifiableList(result);
        }
        return cycles;
    }

    public String cycleNotation(int offset) {
        String s = getCycles().stream().map(c -> c.value.length == 1 ? "" : c.toString(offset)).collect(Collectors.joining());
        if (s.length() == 0) {
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
    public String toString() {
        return AlgebraicElementFormatProvider.toString(this);
    }

    public class Cycle {
        final int[] value;

        Cycle(int... value) {
            this.value = value;
        }

        Permutation getParent() {
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

        @Override
        public String toString() {
            return toString(Configuration.get().getPropertyOrDefault(Offset.class.getName(), Offset.ONE).getAsInt());
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
}
