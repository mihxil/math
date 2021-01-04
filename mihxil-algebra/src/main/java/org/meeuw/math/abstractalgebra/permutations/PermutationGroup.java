package org.meeuw.math.abstractalgebra.permutations;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.text.TextUtils;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class PermutationGroup extends AbstractAlgebraicStructure<Permutation> implements MultiplicativeGroup<Permutation>, Streamable<Permutation> {

    private static final Map<Integer, PermutationGroup>  INSTANCES = new ConcurrentHashMap<>();

    @Getter
    private final int degree;

    private Permutation one;

    public static PermutationGroup ofDegree(int degrees) {
        return INSTANCES.computeIfAbsent(degrees, PermutationGroup::new);
    }

    private PermutationGroup(int degree) {
        super(Permutation.class);
        this.degree = degree;

    }

    @Override
    public Permutation one() {
        if (one == null) {
            int[] value = new int[degree];
            for (int i = 0; i < value.length; i++) {
                value[i] = i;
            }
            one = Permutation.zeroOffset(value);
        }
        return one;
    }

    @Override
    public Cardinality getCardinality() {
        long i = 1;
        long answer = 1;
        while(++i <= degree) {
            answer *= i;
        }
        return new Cardinality(answer);
    }

    @Override
    public Stream<Permutation> stream() {
        final Iterator<Permutation> iterator = new Iterator<Permutation>() {
            Permutation p = one();
            final int[] values = Arrays.copyOf(p.value, degree);

            @Override
            public boolean hasNext() {
                return p != null;
            }

            @Override
            public Permutation next() {
                Permutation value = p;
                if (permute(values)) {
                    p = Permutation.zeroOffset(Arrays.copyOf(values, degree));
                } else {
                    p = null;
                }
                return value;
            }
        };
        return StreamSupport.stream(Spliterators.spliterator(
                iterator,
                getCardinality().getValue(),
                Spliterator.ORDERED | Spliterator.IMMUTABLE), false);

    }

    @Override
    public String getDescription() {
        return "permutation group of degree " + degree;
    }

    @Override
    public String toString() {
        return "S" + TextUtils.subscript(degree);
    }

    /**
     * Knuth's L algorithm. (taken from https://codereview.stackexchange.com/questions/158798/on-knuths-algorithm-l-to-generate-permutations-in-lexicographic-order).
     */
    private static boolean permute(int[] values) {
        // Nothing to do for empty or single-element arrays:
        if (values.length <= 1) {
            return false;
        }

        // L2: Find last j such that self[j] < self[j+1]. Terminate if no such j
        // exists.
        int j = values.length - 2;
        while (j >= 0 && values[j] >= values[j+1]) {
            j -= 1;
        }
        if (j == -1) {
            return false;
        }

        // L3: Find last l such that self[j] < self[l], then exchange elements j and l:
        int l = values.length - 1;
        while (values[j] >= values[l]) {
            l -= 1;
        }
        swap(values, j, l);

        // L4: Reverse elements j+1 ... count-1:
        int lo = j + 1;
        int hi = values.length - 1;
        while (lo < hi) {
        swap(values, lo, hi);
            lo += 1;
            hi -= 1;
        }
        return true;
    }


    private static void swap(int[] input, int a, int b) {
        int tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }

}
