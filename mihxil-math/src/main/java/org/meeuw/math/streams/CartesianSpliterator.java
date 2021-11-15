package org.meeuw.math.streams;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Iterates over all values of the cartesian product of a list of other spliterators.
 *
 * It does this by first iterating 'low' values.
 *
 */
public class CartesianSpliterator<E> implements Spliterator<E[]> {
    final Supplier<Spliterator<E>>[] generators;
    final int[] i;
    int limit = 0;
    final int count;
    final Spliterator<E>[] iterators;
    final E[] result;

    @SafeVarargs
    public CartesianSpliterator(@NonNull Supplier<Spliterator<E>>... generators) {
        this.generators = generators;
        this.count = generators.length;
        this.i = new int[count];
        this.iterators = new Spliterator[count];
        for (int j = 0; j < count; j++) {
            iterators[j] = this.generators[j].get();
        }
        result = (E[]) new Object[count];
    }

    public CartesianSpliterator(Supplier<Spliterator<E>> generator, int count) {
        this(fill(generator, count));
    }

    static <E> Supplier<Spliterator<E>>[] fill(Supplier<Spliterator<E>> generator, int count) {
        Supplier<Spliterator<E>>[] generators = new Supplier[count];
        Arrays.fill(generators, generator);
        return generators;

    }


    @Override
    public boolean tryAdvance(Consumer<? super E[]> action) {
        if (result[0] == null) {// this is the first call´
            for (int j = 0; j < count; j++) {
                final int jj = j;
                if (! this.iterators[j].tryAdvance(e -> {
                    result[jj] = e;
                })) {
                    return false;
                }
            }
            action.accept(Arrays.copyOf(result, result.length));
            return true;
        } else {
            while(true) {
                for (int j = 0; j < count; j++) {
                    final int jj = j;
                    if (i[j] < limit) {
                        if (this.iterators[jj].tryAdvance(e -> {
                            i[jj]++;
                            result[jj] = e;
                        })) {
                            action.accept(Arrays.copyOf(result, result.length));
                            return true;
                        } else {
                            i[j] = Integer.MAX_VALUE;
                        }
                    }
                }
                if (Arrays.stream(i).allMatch(i -> i == Integer.MAX_VALUE)) {
                    return false;
                }
                limit++;
            }
        }
    }

    @Override
    public Spliterator<E[]> trySplit() {
        // TODO
        return null;
    }

    @Override
    public long estimateSize() {
        long result = 1;
        for (int i = 0 ; i < count; i++) {
            result *= getSpliterator(i).estimateSize();
        }
        return result;
    }

    @Override
    public int characteristics() {
        return getSpliterator(0).characteristics();
    }

    Spliterator<E> getSpliterator(int i) {
        if (iterators[i] == null) {
            iterators[i] = generators[i].get();
        }
        return iterators[i];
    }
}
