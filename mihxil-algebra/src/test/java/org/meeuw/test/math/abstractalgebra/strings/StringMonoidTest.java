package org.meeuw.test.math.abstractalgebra.strings;

import java.util.Random;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.abstractalgebra.Operator;
import org.meeuw.math.abstractalgebra.strings.StringElement;
import org.meeuw.math.abstractalgebra.strings.StringMonoid;
import org.meeuw.math.abstractalgebra.test.AdditiveMonoidTheory;
import org.meeuw.math.abstractalgebra.test.OrderedTheory;
import org.meeuw.math.exceptions.NoSuchOperatorException;
import org.meeuw.util.test.CharSequenceTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.strings.StringMonoid.INSTANCE;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class StringMonoidTest implements
    AdditiveMonoidTheory<StringElement>,
    OrderedTheory<StringElement>,
    CharSequenceTheory<StringElement> {

    @Test
    public void stream1() {
        Assertions.assertThat(INSTANCE.stream(new StringMonoid.State('a', 'x')).limit(10)).map(StringElement::toString)
            .containsExactly(
                "ax", "ay", "az", "a ", "aª", "aµ", "aº", "aÀ", "aÁ", "aÂ"
            );
    }
    @Test
    public void stream2() {
        assertThat(INSTANCE.stream(new StringMonoid.State('a', StringMonoid.LAST_CHAR - 1)).limit(10)).map(StringElement::toString)
            .containsExactly(
                "a鼻", "a𪘀", "b ", "b0", "b1", "b2", "b3", "b4", "b5", "b6"
            );
    }
    @Test
    public void stream3() {
        assertThat(INSTANCE.stream(new StringMonoid.State('a', StringMonoid.LAST_CHAR, StringMonoid.LAST_CHAR - 1)).limit(10)).map(StringElement::toString)
            .containsExactly(
                "a𪘀鼻",
                "a𪘀𪘀",
                "b𪘀 ",
                "b𪘀0",
                "b𪘀1",
                "b𪘀2",
                "b𪘀3",
                "b𪘀4",
                "b𪘀5",
                "b𪘀6"
            );
    }

    @Test
    public void testOperate() {
        Random random = new Random();
        assertThatThrownBy(() -> Operator.OPERATION.apply(
            INSTANCE.nextRandom(random),
            INSTANCE.nextRandom(random)
        )).isInstanceOf(NoSuchOperatorException.class);
    }

    @Override
    @Provide
    public Arbitrary<StringElement> elements() {
        return Arbitraries.of("a", "foo", "bar", "")
            .map(StringElement::new);
    }




}
