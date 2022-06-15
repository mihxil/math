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
package org.meeuw.math.abstractalgebra.strings;

import jakarta.validation.constraints.NotNull;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.AdditiveMonoidElement;
import org.meeuw.math.abstractalgebra.Ordered;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class StringElement implements AdditiveMonoidElement<StringElement>, CharSequence, Ordered<StringElement> {

    public static final StringElement EMPTY = new StringElement("");

    @NotNull
    private final CharSequence value;

    public StringElement(CharSequence value) {
        this.value = value;
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public StringElement subSequence(int start, int end) {
        return new StringElement(value.subSequence(start, end));
    }

    @Override
    public StringMonoid getStructure() {
        return StringMonoid.INSTANCE;
    }

    @Override
    public StringElement plus(StringElement summand) {
        return new StringElement(value.toString() + summand.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringElement that = (StringElement) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int compareTo(@NonNull StringElement o) {
        return value.toString().compareTo(o.value.toString());
    }
}
