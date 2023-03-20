/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.theories;

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
    default void charAtIsConsistentWithToStringCharAt(
        @ForAll(OBJECTS) C charSequence) {
        for (int i = 0 ; i < charSequence.length(); i++) {
            assertThat(charSequence.charAt(i)).isEqualTo(charSequence.toString().charAt(i));
        }
    }

    @Property
    default void subSequenceIsConsistent(@ForAll(OBJECTS) C charSequence) {
        for (int i = 0; i < charSequence.length(); i++) {
            for (int j = i ; j < charSequence.length(); j++) {
                CharSequence subSequence = charSequence.subSequence(i, i);
                assertThat(subSequence).hasSameClassAs(charSequence);
                assertThat(subSequence.toString()).isEqualTo(charSequence.toString().substring(i, i));
            }
        }
    }
}
