package org.meeuw.test.math.shapes.dim2;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.shapes.dim2.Shape;

import static org.meeuw.assertj.Assertions.assertThat;



public interface ShapeTheory<S extends Shape<?, S>> extends org.meeuw.theories.BasicObjectTheory<S> {


     /**
     * For any non-null reference values x and y, x.equals(y)
     * should return true if and only if y.equals(x) returns true.
     */
    @Property
    default void times(@ForAll(DATAPOINTS) S x) {
        assertThat(x.times(2).times(0.5)).isEqualTo(x);
        assertThat(x.times(2).times(0.5).eq(x)).isTrue();
    }


    @Property
    default void info(@ForAll(DATAPOINTS) S x) {
        x.info().forEach(e -> System.out.println(e[0] + ": " + e[1]));
    }


}
