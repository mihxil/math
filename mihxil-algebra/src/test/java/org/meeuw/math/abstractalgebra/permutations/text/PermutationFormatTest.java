package org.meeuw.math.abstractalgebra.permutations.text;

import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.permutations.Permutation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 */
class PermutationFormatTest {

    @Test
    void parseObject() throws ParseException {
        PermutationFormat format = new PermutationFormat(Notation.LIST, Offset.ONE);
        Permutation perm = (Permutation) format.parseObject("(321)");
        assertThat(perm.permute("a", "b", "c")).containsExactly("c", "b", "a");
    }

    @Test
    void invalidParse() {
        PermutationFormat format = new PermutationFormat(Notation.LIST, Offset.ONE);
        assertThatThrownBy(() -> format.parseObject("(453")).isInstanceOf(java.text.ParseException.class);

        assertThatThrownBy(() -> format.parseObject("453)")).isInstanceOf(java.text.ParseException.class);


        assertThatThrownBy(() -> format.parseObject("(456)")).isInstanceOf(IllegalArgumentException.class);


    }

}
