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

import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.meeuw.math.ArrayUtils;
import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.permutations.text.PermutationConfiguration;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.configuration.ConfigurationService.getConfigurationAspect;
import static org.meeuw.math.IntegerUtils.factorial;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(MultiplicativeGroup.class)
public class PermutationGroup extends AbstractAlgebraicStructure<Permutation>
    implements MultiplicativeGroup<Permutation>, Streamable<Permutation> {

    private static final Map<Integer, PermutationGroup>  INSTANCES = new ConcurrentHashMap<>();

    /**
     * The <em>degree</em> of a permutation group is the number of elements it is working on.
     */
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
    public Permutation nextRandom(Random random) {
        List<Integer> list = new ArrayList<>(degree);
        for (int i = 0; i < degree; i++) {
            list.add(i);
        }
        Collections.shuffle(list, random);
        return Permutation.zeroOffset(list.stream().mapToInt(i -> i).toArray());
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.of(factorial(degree));
    }

    public Permutation fromCycles(List<int[]> cycles) {
        int offset = getConfigurationAspect(PermutationConfiguration.class).getOffset().getAsInt();
        int[] mapping = new int[degree];
        for (int i = 0; i < degree; i++) {
            mapping[i] = i;
        }
        for (int[] cycle : cycles) {
            int[] elements = cycle;
            int len = elements.length;
            for (int i = 0; i < len; i++) {
                mapping[elements[i]] = elements[(i + 1) % len];
            }
        }
        return Permutation.zeroOffset(mapping);
    }


    @Override
    public Stream<Permutation> stream() {
        final Iterator<Permutation> iterator = new Iterator<>() {
            Permutation p = one();
            final int[] values = Arrays.copyOf(p.value, degree);

            @Override
            public boolean hasNext() {
                return p != null;
            }

            @Override
            public Permutation next() {
                Permutation value = p;
                if (ArrayUtils.permute(values) > 0) {
                    p = new Permutation(false, Arrays.copyOf(values, degree));
                } else {
                    p = null;
                }
                return value;
            }
        };
        return StreamSupport.stream(Spliterators.spliterator(
                iterator,
                getCardinality().getValue().intValue(),
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
