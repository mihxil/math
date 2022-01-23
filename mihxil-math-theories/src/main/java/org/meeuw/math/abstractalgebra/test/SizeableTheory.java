package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.numbers.Scalar;
import org.meeuw.math.numbers.Sizeable;
import org.meeuw.util.test.ElementTheory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public interface SizeableTheory< E extends Sizeable<SIZE>, SIZE extends Scalar<SIZE>> extends ElementTheory<E> {

  @Property
    default void abs(@ForAll(ELEMENTS) E scalar) {
        SIZE abs = scalar.abs();
        assertThat(abs.signum()).isIn(0, 1);

        getLogger().debug("abs({}) = {}", scalar, abs);
        assertThat(abs.isNegative()).withFailMessage(() -> "abs(" + scalar  + ") = " + abs + " is negative").isFalse();
  }
}
