package org.meeuw.test.math.shapes.dim2;

import java.util.Random;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.apache.logging.log4j.Logger;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.shapes.dim2.*;
import org.meeuw.theories.BasicObjectTheory;

import static org.meeuw.assertj.Assertions.assertThat;


public interface ShapeTheory<E extends ScalarFieldElement<E>, S extends Shape<E, S>> extends BasicObjectTheory<S> {


     /**
     * For any non-null reference values x and y, x.equals(y)
     * should return true if and only if y.equals(x) returns true.
     */
    @Property
    default void timesTwoAndTimesAHalf(@ForAll(DATAPOINTS) S x) {
        assertThat(x.times(2).times(0.5)).isEqualTo(x);
        assertThat(x.times(2).times(0.5).eq(x)).isTrue();
    }

    @Property
    default void timesRandom(@ForAll(DATAPOINTS) S x, @ForAll(RANDOMS) Random random) {
        E multiplier = x.field().nextRandom(random);
        S multiplied = x.times(multiplier);
        log().info("%s x %s = %s".formatted(x, multiplier, multiplied));
        assertThat(multiplied.times(multiplier.inverse())).isEqualTo(x);
    }


    @Property
    default void info(@ForAll(DATAPOINTS) S x) {
        Logger log = log();
        x.info().forEach(e -> log.info(e.key() + ": " + e.descriptionString()));
    }


    @Property
    default void showCircumscribedCircle(@ForAll(DATAPOINTS) S x) {
        LocatedShape<E, Circle<E>> circumscribed = x.circumscribedCircle();
        log().info("Circumscribed of %s is %s".formatted(x, circumscribed));
    }
    @Property
    default void showCircumscribedRectangle(@ForAll(DATAPOINTS) S x) {
        LocatedShape<E, Rectangle<E>> circumscribed = x.circumscribedRectangle();
        log().info("Circumscribed rectangle of %s is %s".formatted(x, circumscribed));
    }

    @Property
    default void isExact(@ForAll(DATAPOINTS) S x) {
        log().info(() -> "%s is exact: %s".formatted(x, x.isExact()));
        if (! x.field().elementsAreUncertain()) {
            assertThat(x.isExact()).isTrue();
        }
    }






}
