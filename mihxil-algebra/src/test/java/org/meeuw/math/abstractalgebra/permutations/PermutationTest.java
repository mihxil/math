package org.meeuw.math.abstractalgebra.permutations;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.MultiplicativeGroupTheory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class PermutationTest implements MultiplicativeGroupTheory<Permutation> {


    @Test
    public void permute() {
        String[] values = new String[] { "a", "b", "c"};

        Permutation permutation = Permutation.of(2, 3, 1);
        String[] permuted = permutation.permute(values);
        assertThat(permuted).containsExactly("c", "a", "b");
        assertThat(permutation.structure().one().permute(values)).containsExactly("a", "b", "c");

        assertThat(permutation.structure().cardinality().getValue()).isEqualTo(3 * 2 * 1);

        permutation.structure().stream().forEach(p -> {
            System.out.println(IntStream.of(p.value).boxed().map(Object::toString).collect(Collectors.joining(", ")));
        });

    }

    @Test
    public void cycles() {
        Permutation q = Permutation.of(5, 4, 3, 2, 1);
        assertThat(q.getCycles().toString()).isEqualTo("[(15), (24), (3)]");

        Permutation p = Permutation.of(2, 4, 1, 3, 5);
        assertThat(p.getCycles().toString()).isEqualTo("[(1243), (5)]");
    }

    @Test
    public void cycleNotation() {
        Permutation q = Permutation.of(5, 4, 3, 2, 1);
        assertThat(q.cycleNotation()).isEqualTo("(15)(24)");

        Permutation p = Permutation.of(2, 4, 1, 3, 5);
        assertThat(p.cycleNotation()).isEqualTo("(1243)");

        Permutation r = Permutation.of(2, 4, 1, 3, 5, 10, 6, 8, 9, 7);
        assertThat(r.cycleNotation()).isEqualTo("(1 2 4 3)(6 10 7)");
    }


    @Test
    public void times() {
        Permutation q = Permutation.of(5, 4, 3, 2, 1);
        String[] values = new String[] {"a", "b", "c", "d", "e"};
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
        return Arbitraries.of(Permutation.of(2, 4, 1, 3, 5));
    }
}
