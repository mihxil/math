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
package org.meeuw.math.streams;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.text.TextUtils;

import static java.lang.System.Logger.Level.DEBUG;
import static org.meeuw.configuration.ReflectionUtils.commonSuperClass;

/**
 * <p>Iterates over all values of the cartesian product of a list of other spliterators.</p>
 *
 * <p>It does this by first iterating 'low' values. This makes this also feasible for products of 'infinite' streams, because
 * in that case iterating all values of one of the streams first, would result a rather uninteresting stream.</p>
 *
 * <p>So for example the cartesian product of 2 positive integers starts like so:</p>
 *
 * <pre>
 * // both max = 1
 * 1, 1
 *
 * // we increase the value of the first, and iterate all possible values of the second one &lt;= 2
 * 1, 2
 * 2, 2
 *
 * // Now, we increase only the value of the second and iterate all possible values of the first one &lt; 2
 * 2, 1
 *
 * // all combination of 1 and 2 are done, now the same algorithm but with 3
 * // set first one, second &lt;=3
 * 1, 3
 * 2, 3
 * 3, 3
 *
 * // set second one all before second &gt; 3
 * 3, 1
 * 3, 2
 *
 * // 3 is done, now 4
 * 1, 4
 * 2, 4
 * 3, 4
 * 4, 4
 *
 * 4, 1
 * 4, 2
 * 4, 3
 *
 * // etc...
 * </pre>
 *
 *
 * Another example, but now a cartesian product of <em>three</em> streams.
 *
 * <pre>
 * max = 0
 * a, a, a
 *
 * max = 1
 * b, a, a
 * b, b, a
 * b, b, b
 * b, a, b
 * a, b, a
 * b, b, a
 * b, b, b
 * a, b, a
 *
 * max = 2
 *
 * c, a, a
 * c, b, a
 * c, b, b
 * c, a, b
 * c, c, a
 * c, c, b
 * c, c, c
 * ... etc
 * </pre>
 *
 *
 */
public class CartesianSpliterator<E> implements Spliterator<E[]> {

    private static final System.Logger log = System.getLogger(CartesianSpliterator.class.getName());


    static final int maxSplit = 3;


    final Supplier<Spliterator<? extends E>>[] generators;

    /**
     * The number of spliterators, the size of the resulting combination arrays.
     */
    @Getter
    final int size;

    @Getter
    final long initiallyRemaining;


    @Getter
    final Class<E> elementClass;

    int split = 0;

    final State state;

    @SafeVarargs
    public CartesianSpliterator(
        @NonNull Class<E> elementClass,
        @NonNull Supplier<Spliterator<? extends E>>... generators) {
        this.generators = generators;
        this.size = generators.length;
        this.state = new State(this.size);
        this.elementClass = elementClass;
        this.initiallyRemaining = state.init();
    }

    private CartesianSpliterator(CartesianSpliterator<E> source) {
        this.generators = source.generators;
        this.size = source.size;
        this.state = source.state;
        this.elementClass = source.elementClass;
        this.initiallyRemaining = source.initiallyRemaining;
        this.split = source.split;
    }

    @SafeVarargs
    public CartesianSpliterator(@NonNull Supplier<Spliterator<? extends E>>... generators) {
        this(determineElementClass(generators), generators);
    }

    public CartesianSpliterator(Supplier<Spliterator<? extends E>> generator, int size) {
        this(fill(generator, size));
    }




    @Override
    public  boolean tryAdvance(final Consumer<? super E[]> action) {
        final int toAdvance = state.elementToAdvance();
        if (state.advance(toAdvance)) {
            state.index++;
            state.remaining--;
            action.accept(state.copyOfCurrent());
            return true;
        } else {
            log.log(DEBUG, "Reached end");
            return false;
        }
    }



    public String currentAsString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            String v = String.valueOf(state.current[i]);
            if (i == state.elementCurrentlyFixedAtLimit) {
                v = TextUtils.underLineDouble(v);
            }
            if (i == state.elementToAdvance()) {
                v = TextUtils.underLine(v);
            }
            builder.append(v);
            if (state.positions[i] >= 0) {
                builder.append(TextUtils.subscript(state.positions[i]));
            }
            if (i < size - 1) {
                builder.append(", ");
            }
        }
        builder.append(" (").append(state.currentLimit).append(')');
        return builder.toString();
    }


    @Override
    public Spliterator<E[]> trySplit() {
        // just split the first iterator.
        if (split > maxSplit || true) {
            // but not too often.
            return null;
        }

        this.split++;
        CartesianSpliterator<E> result = new CartesianSpliterator<>(
            this);

        return result;
    }

    @Override
    public long estimateSize() {
        return state.remaining;
    }

    @Override
    public int characteristics() {
        return state.limitIterators[0].characteristics();
    }

    public long getIndex() {
        return state.getIndex();
    }


    /**
     * Utility to determine the common super class of all generators.
     */

    @SuppressWarnings("unchecked")
    private static <E> Class<E> determineElementClass(Supplier<Spliterator<? extends E>>... generators) {
        final Class<?>[] container = new Class<?>[1];
        Class<E> result = (Class<E>) Object.class;
        final List<Class<?>> classes = new ArrayList<>();
        if (generators.length > 0) {
            for (Supplier<Spliterator<? extends E>> generator : generators) {
                if (generator.get().tryAdvance(e ->
                    container[0] = e.getClass())
                ) {
                    classes.add(container[0]);
                }
            }
            result = (Class<E>) commonSuperClass (classes).getFirst();
        }
        log.log(DEBUG,"Found common super class " + classes + " -> " + result.getCanonicalName());
        return result;
    }

    @SuppressWarnings("unchecked")
    private static <E> Supplier<Spliterator<? extends E>>[] fill(Supplier<Spliterator<? extends E>> generator, int count) {
        Supplier<Spliterator<? extends E>>[] generators = new Supplier[count];
        Arrays.fill(generators, generator);
        return generators;
    }


    class State {

        /**
         * For each position an iterator can be needed, they are reassigned often (besides the one at size - 1)
         */
        final Spliterator<? extends E>[] iterators;

        /**
         * If for a certain position it is its turn to contain the limit value, its value will be gotten from the 'limit iterators'.
         * <p>
         * These are only advanced, never reassigned.
         */
        final Spliterator<? extends E>[] limitIterators;


        /**
         * The array used to calculate new items. This is updated in every {@link #tryAdvance(Consumer)}.
         * <p>
         * A copy is created by {@link #copyOfCurrent()}
         */
        final E[] current;

        final int[] positions;

        /**
         * The current maximal index in a spliterator. If all combinations with this value are exhausted it will be increased
         * <p>
         * We start at minus one, which is exhausted, and it will be increased to 0, which should result in exactly one
         * combination (consisting of all first elements of the provided spliterators)
         */
        int currentLimit = -1; // not yet started

        /**
         * The number of remaining elements to return (may be {@link Long#MAX_VALUE} for infinite streams)
         */
        long remaining;

        @Getter
        long index = -1;

        int elementCurrentlyFixedAtLimit;

        @SuppressWarnings("unchecked")
        State(int size) {
            this.positions = new int[size];
            this.iterators = new Spliterator[size];
            this.limitIterators = new Spliterator[size];
            this.current = (E[]) new Object[size];
        }

        /**
         * Sets up everything as it needs to be before iteration starts
         */
        private long init() {
            Arrays.fill(this.positions, -1);     // nothing is advanced yet
            elementCurrentlyFixedAtLimit = size - 1; // as if we would be on the -1'th entry,

            // calculate remaining. cartesian product, so multiply them all.
            remaining = size > 0 ? 1 : 0; // unless no iterators, then the combine one results zero
            for (int i = 0; i < size; i++) {
                limitIterators[i] = generators[i].get();
                long subSize = limitIterators[i].estimateSize();
                if (subSize == 0) {
                    remaining = 0;
                    break;
                }
                try {
                    remaining = Math.multiplyExact(subSize, remaining);
                } catch (ArithmeticException oe) {
                    remaining = Long.MAX_VALUE;
                }
            }
            return remaining;
        }

        /**
         * Shifts the current fixed value one to the right, if possible.
         * Wraps around otherwise, and try to advance one there.
         *
         * @return {@code true} if success. The {@link State#current} array is updated. {@code false} at and of stream.
         */
        private boolean shiftCurrentFix() {
            int skips = 0;
            while(skips < size) {
                elementCurrentlyFixedAtLimit++;
                if (elementCurrentlyFixedAtLimit == size) {
                    currentLimit++;
                    elementCurrentlyFixedAtLimit = 0;
                }
                if (limitIterators[elementCurrentlyFixedAtLimit] != null) {
                    // this proposal is not finished, so we can use it.
                    if (limitIterators[elementCurrentlyFixedAtLimit].tryAdvance(a ->
                        current[elementCurrentlyFixedAtLimit] = a)
                    ) {
                        if (initializeForNewFix()) {
                            return true;
                        }
                    }

                }
                skips++;
            }
            log.log(DEBUG,"Cannot shift. End of stream");
            return false;
        }
        /**
         *
         */
        private boolean resetIteratorAt(final int i) {
            final int currentMax = i < elementCurrentlyFixedAtLimit ? currentLimit - 1 : currentLimit;
            iterators[i] = generators[i].get();
            positions[i] = -1;
            if (currentMax > positions[i]) {
                if (iterators[i].tryAdvance(e -> {
                    current[i] = e;
                    positions[i] = 0;
                })) {
                    return true;
                } else {
                    log.log(DEBUG,"Empty iterator found");
                    return false;
                }
            } else {
                log.log(DEBUG,() -> "Couldn't initialize"); // this can only happen at the first few elements
                return false;
            }
        }
        /**
         * Return this position that must be increased. This is basically just the first entry, unless that happens to be
         * the currently fixed element, in which case the second element is the one to try to advance now.
         */
        private int elementToAdvance() {
            return elementCurrentlyFixedAtLimit == 0 ? 1 : 0;
        }

        /**
         * Tries to advance the value of the element at given index
         * <p>
         * If this is impossible, either because the underlying spliterator is at its end,
         * or if it limited by the current value of {@link State#currentLimit}, the value of this
         * will be reset and the incrementation will be 'carried'.
         * <p>
         * This can be called recursively. In case a certain iterator is 'exhausted', advancing will be 'carried' to
         * the next one.
         *
         */
        private synchronized boolean advance(final int elementToAdvance) {
            assert elementToAdvance != elementCurrentlyFixedAtLimit : "Cannot increase current fix";
            assert elementToAdvance >= 0;

            if (elementToAdvance < size) {
                final int elementLimit = elementToAdvance < elementCurrentlyFixedAtLimit ? currentLimit - 1 : currentLimit;

                if (positions[elementToAdvance] < elementLimit) {
                    // all right, just try to advance
                    assert iterators[elementToAdvance] != null : "Current iterator is null";
                    if (iterators[elementToAdvance].tryAdvance(e -> {
                        current[elementToAdvance] = e;
                        positions[elementToAdvance]++;
                    }
                    )) {
                        // success, so ok
                        log.log(DEBUG,() -> "Advanced iterator at " + elementToAdvance + " " + current[elementToAdvance]);
                        return true;
                    } else {
                        log.log(DEBUG,() -> "Element at " + elementToAdvance + " could not be advanced because the iterator is exhausted");
                        return carry(elementToAdvance);
                    }
                } else {
                    log.log(DEBUG,() -> "Element at " + elementToAdvance + " could not be advanced because that would advance it after element limit " + elementLimit);
                    return carry(elementToAdvance);
                }
            } else {
                return shiftCurrentFix();
            }
        }

        private boolean carry(final int elementToAdvance) {

            // the next iterator, unless is fixed.
            final int to = elementCurrentlyFixedAtLimit - elementToAdvance == 1 ? elementCurrentlyFixedAtLimit + 1 : elementToAdvance + 1;

            if (to >= size) {
                // not possible the carry, all possibilities for this fix are exhausted.
                return shiftCurrentFix();
            } else {
                resetIteratorAt(elementToAdvance);
                return advance(to);
            }
        }



        private boolean initializeForNewFix() {
            for (int i = 0; i < size; i++) {
                if (i != elementCurrentlyFixedAtLimit) {
                    // all iterators must be set to first value
                    if (! resetIteratorAt(i)) {
                        return false;
                    }
                }
            }
            return true;
        }



        @SuppressWarnings({"unchecked"})
        E[] copyOfCurrent() {
            E[] copy = (E[]) Array.newInstance(elementClass, size);
            System.arraycopy(current, 0, copy, 0, size);
            return copy;
        }



    }


}
