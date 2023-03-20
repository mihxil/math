package org.meeuw.assertj;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

public class Assertions extends org.assertj.core.api.Assertions {

    public static <E extends AlgebraicElement<E>> AlgebraicElementAssert<E> assertThat(E actual) {
        return new AlgebraicElementAssert<>(actual);
    }

}
