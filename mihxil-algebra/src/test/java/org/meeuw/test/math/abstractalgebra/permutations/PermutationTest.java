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
package org.meeuw.test.math.abstractalgebra.permutations;

import lombok.extern.log4j.Log4j2;

import java.util.*;
import java.util.stream.Collectors;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.abstractalgebra.permutations.Permutation;
import org.meeuw.math.abstractalgebra.permutations.PermutationGroup;
import org.meeuw.math.abstractalgebra.permutations.text.PermutationConfiguration;
import org.meeuw.theories.abstractalgebra.MultiplicativeGroupTheory;
import org.meeuw.math.exceptions.InvalidElementCreationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.permutations.text.Notation.LIST;
import static org.meeuw.math.abstractalgebra.permutations.text.Offset.ZERO;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class PermutationTest implements MultiplicativeGroupTheory<Permutation> {

    @Test
    public void invalid() {
        assertThatThrownBy(() ->  Permutation.of(1, 5, 8)).isInstanceOf(InvalidElementCreationException.class);
    }

    @Test
    public void testToString() {
        Permutation permutation = Permutation.of(2, 3, 1, 5, 4);
        assertThat(permutation.toString()).isEqualTo("(123)(45)");
        ConfigurationService.withAspect(PermutationConfiguration.class, b -> b.withNotation(LIST), () ->
            assertThat(permutation.toString()).isEqualTo("(23154)")
        );
        ConfigurationService.withAspect(PermutationConfiguration.class, b -> b.withOffset(ZERO), () ->
            assertThat(permutation.toString()).isEqualTo("(012)(34)")
        );

        Permutation longPermutation = Permutation.of(10, 1, 3, 7, 5, 6, 4, 9, 8, 2);
        assertThat(longPermutation.toString()).isEqualTo("(1 10 2)(4 7)(8 9)");
        ConfigurationService.withAspect(PermutationConfiguration.class, b -> b.withNotation(LIST), () ->
            assertThat(longPermutation.toString()).isEqualTo("(10 1 3 7 5 6 4 9 8 2)")
        );
    }


    @SuppressWarnings("PointlessArithmeticExpression")
    @Test
    public void permute() {
        String[] values = { "a", "b", "c"};

        final Permutation permutation = Permutation.of(2, 3, 1);
        String[] permuted = permutation.permute(values);
        assertThat(permuted).containsExactly("c", "a", "b");
        assertThat(permutation.getStructure().one().permute(values)).containsExactly("a", "b", "c");

        assertThat(permutation.getStructure().getCardinality().getValue()).isEqualTo(3 * 2 * 1);

        List<String> test = new ArrayList<>();
        permutation.getStructure().stream().forEach(p -> {
            String s = Arrays.stream(p.apply(values)).map(Object::toString).collect(Collectors.joining(", "));
            log.info(s);
            test.add(s);
        });
        assertThat(test).containsExactly(
            "a, b, c",
            "a, c, b",
            "b, a, c",
            "c, a, b",
            "b, c, a",
            "c, b, a"
        );

    }


    @Test
    public void permuteWithDuplicates() {
        String[] values = {"a", "b", "b"};
        List<String> test = new ArrayList<>();
        PermutationGroup.ofDegree(3).stream()
            .map(p -> p.apply(values))
            .map(List::of) // List has proper equals, so distinct works as wanted.
            .distinct()
            .forEach(a -> {
                String s = a.stream().map(Object::toString).collect(Collectors.joining(", "));
            log.info(s);
            test.add(s);
        });
        assertThat(test).containsExactly(
            "a, b, b", "b, a, b", "b, b, a"

        );
    }

    @Test
    public void permuteInts() {
        final Permutation permutation = Permutation.of(2, 3, 1);
        int[] permute = permutation.permuteInts( 10, 12, 14);
        assertThat(permute).containsExactly(14, 10, 12);
    }

    @Test
    public void permuteSmall() {
        String[] values = { "a", "b", "c"};

        Permutation permutation = Permutation.of(1);
        assertThat(permutation.getStructure().stream()).hasSize(1);
        assertThat(permutation.apply(values)).isEqualTo(values);

        Permutation empty = Permutation.of();
        assertThat(empty.getStructure().stream()).hasSize(1);
        assertThat(empty.apply(values)).isEqualTo(values);
    }



    @SuppressWarnings({"ConstantConditions", "EqualsWithItself"})
    @Test
    public void cycles() {
        Permutation q = Permutation.of(5, 4, 3, 2, 1);
        assertThat(q.getCycles().toString()).isEqualTo("[(15), (24), (3)]");

        Permutation p = Permutation.of(2, 4, 1, 3, 5);
        assertThat(p.getCycles().toString()).isEqualTo("[(1243), (5)]");

        for (Permutation.Cycle c : p.getCycles()) {
            assertThat(c.getParent()).isSameAs(p);
            assertThat(c.reciprocal().reciprocal()).isEqualTo(c);
        }

        Permutation.Cycle cycle1 = p.new Cycle(1, 5);
        Permutation.Cycle cycle2 = p.new Cycle(2, 5);


        assertThat(cycle1.equals(cycle2)).isFalse();
        assertThat(cycle1.equals(cycle1)).isTrue();

        assertThat(cycle1.equals(null)).isFalse();
        assertThat(cycle1.equals(new Object())).isFalse();

        Permutation.Cycle cycle1q = q.new Cycle(1, 5);
        assertThat(cycle1.equals(cycle1q)).isFalse();

        assertThat(cycle1.hashCode()).isEqualTo(997);
    }

    @Test
    public void cycleNotation() {
        Permutation q = Permutation.of(5, 4, 3, 2, 1);
        assertThat(q.cycleNotation(1)).isEqualTo("(15)(24)");

        Permutation p = Permutation.of(2, 4, 1, 3, 5);
        assertThat(p.cycleNotation(1)).isEqualTo("(1243)");

        Permutation r = Permutation.of(2, 4, 1, 3, 5, 10, 6, 8, 9, 7);
        assertThat(r.cycleNotation(1)).isEqualTo("(1 2 4 3)(6 10 7)");

        assertThat(p.getStructure().one().toString()).isEqualTo("()");

    }


    @Test
    public void times() {
        Permutation q = Permutation.of(5, 4, 3, 2, 1);
        String[] values = {"a", "b", "c", "d", "e"};
        Permutation p = Permutation.of(2, 4, 1, 3, 5);
        String[] permutedp = p.permute(values);
        String[] permutedqp = q.permute(permutedp);
        Permutation product = q.x(p);
        assertThat(product.toString()).isEqualTo("(1435)");
        String[] permutedproduct = product.permute(values);
        assertThat(permutedproduct).containsExactly(permutedqp);
    }

    @Override
    public Arbitrary<Permutation> elements() {
        return Arbitraries.of(
            PermutationGroup.ofDegree(5).stream().collect(Collectors.toList())
        );
    }
}
