package org.meeuw.util.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface CharSequenceTheory<C extends CharSequence> {

    String OBJECTS = "elements";

    @Property
    default void charAt (
        @ForAll(OBJECTS) C charSequence) {
        for (int i = 0 ; i < charSequence.length(); i++) {
            assertThat(charSequence.charAt(i)).isEqualTo(charSequence.toString().charAt(i));
        }
    }

    @Property
    default void subSequence(@ForAll(OBJECTS) C charSequence) {
        for (int i = 0; i < charSequence.length(); i++) {
            for (int j = i ; j < charSequence.length(); j++) {
                CharSequence subSequence = charSequence.subSequence(i, i);
                assertThat(subSequence).hasSameClassAs(charSequence);
                assertThat(subSequence.toString()).isEqualTo(charSequence.toString().substring(i, i));
            }
        }
    }
}
