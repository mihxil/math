package org.meeuw.math.abstractalgebra;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeMonoidTheory<E extends MultiplicativeMonoidElement<E>> extends MultiplicativeSemiGroupTheory<E> {

 @Property
    default void powExponentZero(
         @ForAll(ELEMENTS) E v1
    )  {
        assertThat(v1.pow(0)).isEqualTo(v1.getStructure().one());
    }
}
