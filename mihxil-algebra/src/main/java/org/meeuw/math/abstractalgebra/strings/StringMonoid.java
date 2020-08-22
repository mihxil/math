package org.meeuw.math.abstractalgebra.strings;

import java.util.Arrays;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class StringMonoid extends AbstractAlgebraicStructure<StringElement> implements AdditiveMonoid<StringElement>, Streamable<StringElement> {

    public static final StringMonoid INSTANCE = new StringMonoid();

    protected StringMonoid() {
        super(StringElement.class);
    }

    @Override
    public StringElement zero() {
        return StringElement.EMPTY;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }

    @Override
    public Stream<StringElement> stream() {
        return Stream.iterate(new State(),
            State::inc).map(State::get);

    }

    public static class State {
        private final int i;
        private final char[] chars;

        public State(char[] chars, int i) {
            this.chars = chars;
            this.i = i;
        }
        public State() {
            this.chars = new char[0];
            this.i = -1;
        }

        boolean filled() {
            for (char c : chars) {
                if (c != Character.MAX_VALUE) {
                    return false;
                }
            }
            return true;
        }

        public State inc() {
            if (filled()) {
                char[] newChars = new char[chars.length + 1];
                Arrays.fill(newChars, Character.MIN_VALUE);
                return new State(newChars, newChars.length - 1);
            } else {
                char[] copy = Arrays.copyOf(chars, chars.length);
                if (copy[i] == Character.MAX_VALUE) {
                    copy[i] = Character.MIN_VALUE;
                    return new State(copy, i - 1);
                } else {
                    copy[i]++;
                    return new State(copy, i);
                }

            }
        }

        public StringElement get() {
            return new StringElement(new String(chars));
        }
    }


}
