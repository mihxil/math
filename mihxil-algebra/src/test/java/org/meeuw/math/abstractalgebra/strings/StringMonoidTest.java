package org.meeuw.math.abstractalgebra.strings;

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.test.AdditiveMonoidTheory;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class StringMonoidTest implements AdditiveMonoidTheory<StringElement> {

    @Override
    @Provide
    public Arbitrary<StringElement> elements() {
        return Arbitraries.of("a", "foo", "bar", "").map(StringElement::new);
    }
}
