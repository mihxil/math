package org.meeuw.math.abstractalgebra;

import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public interface MultiplicativeGroupTheory<F extends MultiplicativeGroupElement<F>> {

	@Property
    default void multiplicateGroupTheory(
            @ForAll("elements") F rn1,
            @ForAll("elements") F rn2) {
        F rn3 = rn1.dividedBy(rn2);
        System.out.println(rn1 + "/" + rn2 + "=" + rn3);
        assertThat(rn1.times(rn2)).isEqualTo(rn2.times(rn1));
        assertThat(rn1.times(rn1.structure().one())).isEqualTo(rn1);
    }
     @Provide
	 Arbitrary<F> elements();
}
