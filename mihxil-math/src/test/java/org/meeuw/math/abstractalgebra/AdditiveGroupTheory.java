package org.meeuw.math.abstractalgebra;

import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveGroupTheory<F extends AdditiveGroupElement<F>> {

	@Property
    default void additiveGroupTheory(
            @ForAll("elements") F rn1,
            @ForAll("elements") F rn2) {
        F rn3 = rn1.minus(rn2);
        System.out.println(rn1 + " - " + rn2 + "=" + rn3);
        assertThat(rn1.plus(rn2)).isEqualTo(rn2.plus(rn1));
        assertThat(rn1.plus(rn1.structure().zero())).isEqualTo(rn1);
    }

    @Provide
    Arbitrary<F> elements();
}
