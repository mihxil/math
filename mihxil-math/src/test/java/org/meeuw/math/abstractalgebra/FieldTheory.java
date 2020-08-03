package org.meeuw.math.abstractalgebra;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface FieldTheory<F extends FieldElement<F>> extends MultiplicativeGroupTheory<F>, AdditiveGroupTheory<F>  {

	@Property
    default void fieldOperators(@ForAll("element") F v1) {
        assertThat(v1.structure().supportedOperators()).contains(Operator.values());
    }

	@Property
	default void distributivity (
            @ForAll("elements") F v1,
            @ForAll("elements") F v2,
            @ForAll("elements") F v3
            ) {
		assertThat(v1.times(v2.plus(v3))).isEqualTo(v1.times(v2).plus(v1.times(v3)));
    }

}
