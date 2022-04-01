package org.meeuw.math.abstractalgebra.strings;

import jakarta.validation.constraints.NotNull;

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
    public int compareTo(StringElement o) {
        return value.toString().compareTo(o.value.toString());
    }
}
