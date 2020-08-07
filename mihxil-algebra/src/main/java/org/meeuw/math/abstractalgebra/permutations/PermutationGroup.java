package org.meeuw.math.abstractalgebra.permutations;

import lombok.Getter;

import java.util.*;
import java.util.stream.*;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class PermutationGroup extends AbstractAlgebraicStructure<Permutation> implements MultiplicativeGroup<Permutation>, Streamable<Permutation> {

    @Getter
    private final int degree;

    public PermutationGroup(int degree) {
        super(Permutation.class);
        this.degree = degree;
    }

    @Override
    public Permutation one() {
        int[] value = new int[degree];
        for (int i = 0; i < value.length; i++) {
            value[i] = i;
        }
        return Permutation.zeroOffset(value);
    }

    @Override
    public Cardinality cardinality() {
        long i = 1;
        long answer = 1;
        while(++i <= degree) {
            answer *= i;
        }
        return new Cardinality(answer);
    }

    @Override
    public Stream<Permutation> stream() {
        int[] counters = new int[degree];
        for (int i = 0; i < counters.length; i++){
            counters[i] = i;
        }

        final Iterator<Permutation> iterator = new Iterator<Permutation>() {
            int i = 0;
            @Override
            public boolean hasNext() {
                return i < counters.length - 2;
            }

            @Override
            public Permutation next() {
                //return t = (t == Streams.NONE) ? seed : f.apply(t);
                return Permutation.of(counters);
            }
        };
        return StreamSupport.stream(Spliterators.spliterator(
                iterator,
                cardinality().getValue(),
                Spliterator.ORDERED | Spliterator.IMMUTABLE), false);

    }


    private void swap(int[] input, int a, int b) {
        int tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }
    protected void inc(int[] counters, int c) {

    }

}
