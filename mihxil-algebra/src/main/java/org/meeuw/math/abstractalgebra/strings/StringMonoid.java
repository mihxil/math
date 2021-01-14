package org.meeuw.math.abstractalgebra.strings;

import java.util.Arrays;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class StringMonoid extends AbstractAlgebraicStructure<StringElement>
    implements AdditiveMonoid<StringElement>, Streamable<StringElement> {

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
        return stream(new State());
    }

    Stream<StringElement> stream(State state) {
        return Stream.iterate(state,
            State::inc).map(State::get);
    }
    static final int FIRST_CHAR = 32;
    static final int LAST_CHAR = 195101;

    protected static boolean validChar(int i) {
        return  Character.isDefined(i) &&
            (Character.isLetterOrDigit(i) || Character.isSpaceChar(i));
    }

    static class State {
        private final int[] chars;

        public State(int... chars) {
            this.chars = chars;
        }
        public State() {
            this(new int[0]);
        }

        boolean filled() {
            for (int c : chars) {
                if (c != LAST_CHAR) {
                    return false;
                }
            }
            return true;
        }

        public State inc() {
            if (filled()) {
                int[] newChars = new int[chars.length + 1];
                Arrays.fill(newChars, FIRST_CHAR);
                return new State(newChars);
            } else {
                final int[] copy = Arrays.copyOf(chars, chars.length);
                final int lastIndex = copy.length - 1;
                if (copy[lastIndex] == LAST_CHAR) {
                    copy[lastIndex] = FIRST_CHAR;
                    int j = lastIndex - 1;
                    while (copy[j] == LAST_CHAR) {
                        j--;
                    }
                    copy[j]++;
                    return new State(copy);
                } else {
                    copy[lastIndex]++;
                    while (!validChar(copy[lastIndex])) {
                        copy[lastIndex]++;
                    }
                    return new State(copy);
                }
            }
        }

        public StringElement get() {
            return new StringElement(new String(chars, 0, chars.length));
        }

    }


}
