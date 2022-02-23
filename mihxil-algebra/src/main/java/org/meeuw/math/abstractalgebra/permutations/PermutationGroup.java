package org.meeuw.math.abstractalgebra.permutations;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.meeuw.math.Example;
import org.meeuw.math.MatrixUtils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.text.TextUtils;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(MultiplicativeGroup.class)
public class PermutationGroup extends AbstractAlgebraicStructure<Permutation>
    implements MultiplicativeGroup<Permutation>, Streamable<Permutation> {

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
                if (MatrixUtils.permute(values) > 0) {
                    p = new Permutation(false, Arrays.copyOf(values, degree));
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


}
