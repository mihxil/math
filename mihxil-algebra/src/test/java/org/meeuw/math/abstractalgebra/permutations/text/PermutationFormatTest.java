package org.meeuw.math.abstractalgebra.permutations.text;

import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.permutations.Permutation;
import org.meeuw.math.exceptions.InvalidElementCreationException;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Michiel Meeuwissen
 */
class PermutationFormatTest {

    @Test
    public void format() {
        final PermutationFormat format = new PermutationFormat(Notation.LIST, Offset.ONE);
        assertThatThrownBy(() -> format.format(new Object())).isInstanceOf(IllegalArgumentException.class);

        Permutation permutation = Permutation.of(2, 1, 4, 3);
        assertThat(format.format(permutation)).isEqualTo("(2143)");
        PermutationFormat cycleFormat = format.withNotation(Notation.CYCLES).withOffset(Offset.ZERO);
        assertThat(cycleFormat.format(permutation)).isEqualTo("(01)(23)");
    }


    @Test
    void parseObject() throws ParseException {
        PermutationFormat format = new PermutationFormat(Notation.LIST, Offset.ONE);
        Permutation perm = (Permutation) format.parseObject("(321)");
        assertThat(perm.permute("a", "b", "c")).containsExactly("c", "b", "a");
    }

    @Test
    void parseObjectWithSpaces() throws ParseException {
        PermutationFormat format = new PermutationFormat(Notation.LIST, Offset.ONE);
        Permutation perm = (Permutation) format.parseObject("(3 2 1)");
        assertThat(perm.permute("a", "b", "c")).containsExactly("c", "b", "a");
    }

    @Test
    void parseObjectShort() throws ParseException {
        PermutationFormat format = new PermutationFormat(Notation.LIST, Offset.ONE);
        Permutation perm = (Permutation) format.parseObject("(1)");
        assertThat(perm.permute("a", "c")).containsExactly("a", "c");
    }

    @Test
    void invalidParse() {
        PermutationFormat format = new PermutationFormat(Notation.LIST, Offset.ONE);
        assertThatThrownBy(() -> format.parseObject("(453")).isInstanceOf(java.text.ParseException.class);

        assertThatThrownBy(() -> format.parseObject("453)")).isInstanceOf(java.text.ParseException.class);


        assertThatThrownBy(() -> format.parseObject("(456)")).isInstanceOf(InvalidElementCreationException.class);


    }

}
