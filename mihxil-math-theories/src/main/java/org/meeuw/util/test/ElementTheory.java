package org.meeuw.util.test;

import net.jqwik.api.*;
import net.jqwik.api.lifecycle.BeforeProperty;
import org.junit.jupiter.api.BeforeEach;
import org.assertj.core.api.Assertions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface ElementTheory<E>  {

    String ELEMENT = "element";
    String ELEMENTS = "elements";

    @BeforeEach
    @BeforeProperty
    default void setupForAll() {
        Assertions.setMaxStackTraceElementsDisplayed(20);
    }


    @Provide
    Arbitrary<? extends E> elements();


    @Provide
    default Arbitrary<? extends E> element() {
        return Arbitraries.of(elements().sample());
    }


    @SuppressWarnings({"EqualsWithItself", "ConstantConditions"})
    @Property
    default void testEqualsSelf(@ForAll(ELEMENTS) E e) {
        assertThat(e.equals(e)).isTrue();
        assertThat(e.equals(null)).isFalse();
        assertThat(e.equals(new Object())).isFalse();
    }
    @Property
    default void testEquals(@ForAll(ELEMENTS) E e1, @ForAll(ELEMENTS) E e2) {
        assertThat(e1.equals(e2)).isEqualTo(e2.equals(e1));
    }



    @Property
    default void testHashCode(@ForAll(ELEMENTS) E e1, @ForAll(ELEMENTS) E e2) {
        if (e1.equals(e2)) {
            assertThat(e1.hashCode()).isEqualTo(e2.hashCode());
        }
        if(e1.hashCode() != e2.hashCode()) {
            assertThat(e1.equals(e2)).isFalse();
        }
    }

    default Logger getLogger() {
        return LogManager.getLogger(this.getClass());
    }
}
