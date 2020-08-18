package org.meeuw.math.abstractalgebra.permutations;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Permutation implements MultiplicativeGroupElement<Permutation>, UnaryOperator<Object[]> {


    final int[] value;
    private final int offset;
    private List<Cycle> cycles;

    public static Permutation of(int... value) {
        int[] v = value;
        for (int i = 0; i < value.length; i++) {
            v[i] -= 1;
        }
        return new Permutation(1, v);
    }

    public static Permutation zeroOffset(int... value) {
        return new Permutation(0, value);
    }

    private Permutation(int offset, int... value) {
        this.offset = offset;
        this.value = value;
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
        return new Permutation(offset, result);
    }

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
        return new Permutation(offset, result);
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


    protected String cycleNotation() {
        String s = getCycles().stream().map(c -> c.value.length == 1 ? "" : c.toString()).collect(Collectors.joining());
        if (s.length() == 0) {
            return "()";
        } else {
            return s;
        }
    }

    @Override
    public String toString() {
        //String join = value.length > 9 ? " " : "";
        //return "(" + IntStream.of(value).mapToObj(i -> String.valueOf(i + offset)).collect(Collectors.joining(join)) + ")";
        return cycleNotation();
    }

    @Override
    public Object[] apply(Object[] objects) {
        return permute(objects);
    }

    public class Cycle {
        final int[] value;

        public Cycle(int[] value) {
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

        public String toString() {
            String join = Permutation.this.value.length > 9 ? " " : "";
            return "(" + IntStream.of(value).mapToObj(i -> String.valueOf(i + offset)).collect(Collectors.joining(join)) + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cycle cycle = (Cycle) o;

            return Arrays.equals(value, cycle.value);
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
