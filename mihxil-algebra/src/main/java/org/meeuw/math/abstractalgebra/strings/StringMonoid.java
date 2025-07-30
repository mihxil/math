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
package org.meeuw.math.abstractalgebra.strings;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.Singleton;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.operators.AlgebraicComparisonOperator;
import org.meeuw.math.operators.BasicComparisonOperator;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(AdditiveMonoid.class)
@Singleton
public class StringMonoid extends AbstractAlgebraicStructure<StringElement>
    implements AdditiveMonoid<StringElement>, Streamable<StringElement> {

    public static final StringMonoid INSTANCE = new StringMonoid();

    private StringMonoid() {
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
    public NavigableSet<AlgebraicComparisonOperator> getSupportedComparisonOperators() {
        return BasicComparisonOperator.ALL;
    }

    @Override
    public StringElement nextRandom(Random random) {
        byte[] array = new byte[(int) RandomConfiguration.nextNonNegativeLong(random)]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        return new StringElement(generatedString);
    }

    @Override
    public Stream<StringElement> stream() {
        return stream(new State());
    }

    @Override
    public StringElement fromString(String string) {
        return new StringElement(string);
    }


    public Stream<StringElement> stream(State state) {
        return Stream.iterate(state,
            State::inc).map(State::get);
    }
    static final int FIRST_CHAR = 32;
    public static final int LAST_CHAR = 195101;

    protected static boolean validChar(int i) {
        return  Character.isDefined(i) &&
            (Character.isLetterOrDigit(i) || Character.isSpaceChar(i));
    }

    public static class State {
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
                } else {
                    copy[lastIndex]++;
                    while (!validChar(copy[lastIndex])) {
                        copy[lastIndex]++;
                    }
                }
                return new State(copy);
            }
        }

        public StringElement get() {
            return new StringElement(new String(chars, 0, chars.length));
        }

    }


}
