package org.meeuw.math.abstractalgebra.permutations;

import lombok.extern.log4j.Log4j2;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.permutations.text.Notation;
import org.meeuw.math.abstractalgebra.permutations.text.Offset;
import org.meeuw.math.abstractalgebra.test.MultiplicativeGroupTheory;
import org.meeuw.math.text.spi.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class PermutationTest implements MultiplicativeGroupTheory<Permutation> {

    @Test
    public void testToString() {
        Permutation permutation = Permutation.of(2, 3, 1, 5, 4);
        assertThat(permutation.toString()).isEqualTo("(123)(45)");
        Configuration.with(b -> b.property(Notation.class.getName(), Notation.LIST), () -> {
            assertThat(permutation.toString()).isEqualTo("(23154)");
        });
        Configuration.with(b -> b.property(Offset.class.getName(), Offset.ZERO), () -> {
            assertThat(permutation.toString()).isEqualTo("(012)(34)");
        });

    }


    @Test
    public void permute() {
        String[] values = new String[] { "a", "b", "c"};

        Permutation permutation = Permutation.of(2, 3, 1);
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
    public void permuteSmall() {
        String[] values = new String[] { "a", "b", "c"};

        Permutation permutation = Permutation.of(1);
        assertThat(permutation.getStructure().stream()).hasSize(1);
        assertThat(permutation.apply(values)).isEqualTo(values);

        Permutation empty = Permutation.of();
        assertThat(empty.getStructure().stream()).hasSize(1);
        assertThat(empty.apply(values)).isEqualTo(values);
    }



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
        return Arbitraries.of(
            Permutation.of(2, 4, 1, 3, 5),
            Permutation.of(1, 2, 3, 4, 5),
            Permutation.of(1, 2, 3, 5, 4)
        );
    }
}
