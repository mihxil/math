/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.numbers.test;

import net.jqwik.api.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
public interface NumberTheory<N extends Number> {

    @Provide
    Arbitrary<? extends N> numbers();


    @Property
    default void intValue( @ForAll("numbers") N number) {
        assertThat(number.intValue()).isInstanceOf(Integer.class);
    }

    @Property
    default void longValue( @ForAll("numbers") N number) {
        assertThat(number.longValue()).isInstanceOf(Long.class);
    }


    @Property
    default void floatValue( @ForAll("numbers") N number) {
        assertThat(number.floatValue()).isInstanceOf(Float.class);
    }

    @Property
    default void doubleValue( @ForAll("numbers") N number) {
        assertThat(number.doubleValue()).isInstanceOf(Double.class);
    }

    @Property
    default void byteValue( @ForAll("numbers") N number) {
        assertThat(number.byteValue()).isInstanceOf(Byte.class);
    }

    @Property
    default void shortValue( @ForAll("numbers") N number) {
        assertThat(number.shortValue()).isInstanceOf(Short.class);
    }
}
