package org.meeuw.math.streams;

import lombok.Getter;
import lombok.extern.java.Log;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.text.TextUtils;

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
 * // we increase the value of the first, and iterate all possible values of the second one <= 2
 * 1, 2
 * 2, 2
 *
 * // Now, we increase only the value of the second and iterate all possible values of the first one < 2
 * 2, 1
 *
 * // all combination of 1 and 2 are done, now the same algorithm but with 3
 * // set first one, second <=3
 * 1, 3
 * 2, 3
 * 3, 3
 *
 * // set second one all before second < 3
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
 * Another example, but now a cartesian product of _three_ streams.
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
@Log
public class CartesianSpliterator<E> implements Spliterator<E[]> {

    final Supplier<Spliterator<? extends E>>[] generators;

    final int[] positions;

    /**
     * The current maximal index in a spliterator. If all combinations with this value are exhausted it will be increased
     *
     * We start at minus one, which is exhausted, and it well be increased to 0, which should result in exactly one
     * combination (consisting of all first elements of the provided spliterators)
     */
    int currentLimit = -1; // not yet started

    /**
     * The number of spliterators, the size of the resulting combination arrays.
     */
    @Getter
    final int size;

    @Getter
    final long initiallyRemaining;

    /**
     * The number of remaining elements to return
     */
    long remaining;

    @Getter
    long index = -1;

    int elementCurrentlyFixedAtLimit;

    /**
     * For each position an iterator can be needed, they are reassigned often (besides the one at size - 1)
     */
    final Spliterator<? extends E>[] iterators;

    /**
     * If for a certain position it is its turn to contain the limit value, its value will be gotton from the 'limit iterators'.
     *
     * These are only advanced, nevery reassigned.
     */
    final Spliterator<? extends E>[] limitIterators;


    /**
     * The array used to calculate new items. This is updated in every {@link #tryAdvance(Consumer)}.
     *
     * A copy is created by {@link #copyOfCurrent()}
     */
    final Object[] current;

    @Getter
    final Class<E> elementClass;

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public CartesianSpliterator(
        @NonNull Class<E> elementClass,
        @NonNull Supplier<Spliterator<? extends E>>... generators) {
        this.generators = generators;
        this.size = generators.length;
        this.positions = new int[size];
        this.iterators = new Spliterator[size];
        this.limitIterators = new Spliterator[size];
        this.current =  new Object[size];
        this.elementClass = elementClass;
        this.initiallyRemaining = init();
    }

    @SafeVarargs
    public CartesianSpliterator(@NonNull Supplier<Spliterator<? extends E>>... generators) {
        this(determineElementClass(generators), generators);
    }

    public CartesianSpliterator(Supplier<Spliterator<? extends E>> generator, int size) {
        this(fill(generator, size));
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
            limitIterators[i] = this.generators[i].get();
            remaining *= limitIterators[i].estimateSize();
        }
        return remaining;
    }


    @Override
    public boolean tryAdvance(final Consumer<? super E[]> action) {
        final int toAdvance = elementToAdvance();
        if (advance(toAdvance)) {
            index++;
            remaining--;
            action.accept(copyOfCurrent());
            return true;
        } else {
            log.fine("Reached end");
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
     *
     * If this is impossible, either because the underlying spliterator is at its end,
     * or if it limited by the current value of {@link #currentLimit}, the value of this
     * will be reset and the incrementation will be 'carried'.
     *
     * This can be called recursively. In case a certain iterator is 'exhausted', advancing will be 'carried' to
     * the next one.
     *
     */
    private boolean advance(final int elementToAdvance) {
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
                    log.fine(() -> "Advanced iterator at " + elementToAdvance + " " + current[elementToAdvance]);
                    return true;
                } else {
                    log.fine(() -> "Element at " + elementToAdvance + " could not be advanced because the iterator is exhausted");
                    return carry(elementToAdvance);
                }
            } else {
                log.fine(() -> "Element at " + elementToAdvance + " could not be advanced because that would advance it after element limit " + elementLimit);
                return carry(elementToAdvance);
            }
        } else {
            return shiftCurrentFix();
        }
    }

    public boolean carry(final int elementToAdvance) {

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

    /**
     * Shifts the current fixed value one to the right, if possible.
     * Wraps around otherwise, and try to advance one there.
     *
     * @return {@code true} if success. The {@link #current} array is updated. {@code false} at and of stream.
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
        log.fine("Cannot shift. End of stream");
        return false;
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

    /**
     *
     */
    private boolean resetIteratorAt(final int i) {
        final int currentMax = i < elementCurrentlyFixedAtLimit ? currentLimit - 1 : currentLimit;
        iterators[i] = this.generators[i].get();
        positions[i] = -1;
        if (currentMax > positions[i]) {
            if (iterators[i].tryAdvance(e -> {
                current[i] = e;
                positions[i] = 0;
            })) {
                return true;
            } else {
                log.fine("Empty iterator found");
                return false;
            }
        } else {
            log.fine(() -> "Couldn't initialize"); // this can only happen at the first few elements
            return false;
        }
    }



    @SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy"})
    public E[] copyOfCurrent() {
        E[] copy = (E[]) Array.newInstance(elementClass, size);
        System.arraycopy(current, 0, copy, 0, this.size);
        return copy;
    }


    public String currentAsString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            String v = String.valueOf(current[i]);
            if (i == elementCurrentlyFixedAtLimit) {
                v = TextUtils.underLineDouble(v);
            }
            if (i == elementToAdvance()) {
                v = TextUtils.underLine(v);
            }
            builder.append(v);
            if (positions[i] >= 0) {
                builder.append(TextUtils.subscript(positions[i]));
            }
            if (i < size - 1) {
                builder.append(", ");
            }
        }
        builder.append(" (").append(currentLimit).append(')');
        return builder.toString();
    }


    @Override
    public Spliterator<E[]> trySplit() {
        // TODO, may be it _can_ be split
        return null; // cannot be split
    }

    @Override
    public long estimateSize() {
        return remaining;
    }

    @Override
    public int characteristics() {
        return limitIterators[0].characteristics();
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
        log.fine("Found common super class " + classes + " -> " + result.getCanonicalName());
        return result;
    }

    @SuppressWarnings("unchecked")
    private static <E> Supplier<Spliterator<? extends E>>[] fill(Supplier<Spliterator<? extends E>> generator, int count) {
        Supplier<Spliterator<? extends E>>[] generators = new Supplier[count];
        Arrays.fill(generators, generator);
        return generators;
    }


    /**
     * Borrowed from https://stackoverflow.com/questions/9797212/finding-the-nearest-common-superclass-or-superinterface-of-a-collection-of-classes
     */
    private static Deque<Class<?>> commonSuperClass(List<Class<?>> classes) {
        // start off with set from first hierarchy

        final Iterator<Class<?>> iterator = classes.iterator();
        final Set<Class<?>> rollingIntersect = new LinkedHashSet<>(
            getClassesBfs(iterator.next()));
        // intersect with next
        iterator.forEachRemaining(c -> rollingIntersect.retainAll(getClassesBfs(c)));
        return new LinkedList<>(rollingIntersect);
    }



    private static Set<Class<?>> getClassesBfs(Class<?> clazz) {
        final Set<Class<?>> classes = new LinkedHashSet<>();
        final Set<Class<?>> nextLevel = new LinkedHashSet<>();
        nextLevel.add(clazz);
        do {
            classes.addAll(nextLevel);
            final Set<Class<?>> thisLevel = new LinkedHashSet<>(nextLevel);
            nextLevel.clear();
            for (Class<?> each : thisLevel) {
                final Class<?> superClass = each.getSuperclass();
                if (superClass != null && superClass != Object.class) {
                    nextLevel.add(superClass);
                }
                Collections.addAll(nextLevel, each.getInterfaces());
            }
        } while (!nextLevel.isEmpty());
        return classes;
    }


}
