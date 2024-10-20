package org.meeuw.math.abstractalgebra.dihedral;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.text.TextUtils;

/**
 * @since 0.14
 */
public class DiHedralGroup implements Group<DiHedralSymmetry>, Streamable<DiHedralSymmetry> {

    public static Map<Integer, DiHedralGroup> CACHE = new ConcurrentHashMap<>();
    final int n;

    private DiHedralGroup(int n) {
        this.n = n;
    }
    public static DiHedralGroup of(int n) {
        return CACHE.computeIfAbsent(n, DiHedralGroup::new);
    }


    public DiHedralSymmetry r(int k) {
        if (k >= n) {
            throw new IllegalArgumentException();
        }
        return DiHedralSymmetry.r(k, this);
    }

    public DiHedralSymmetry s(int k) {
        if (k >= n) {
            throw new IllegalArgumentException();
        }
        return DiHedralSymmetry.s(k, this);
    }

    @Override
    public DiHedralSymmetry unity() {
        return DiHedralSymmetry.r(0, this);
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.of(2L * n);
    }

    @Override
    public Class<DiHedralSymmetry> getElementClass() {
        return DiHedralSymmetry.class;
    }

    @Override
    public Stream<DiHedralSymmetry> stream() {
        return Stream.concat(
            IntStream.range(0, n)
                .mapToObj(i -> DiHedralSymmetry.r(i, this)),
            IntStream.range(0, n)
                .mapToObj(i -> DiHedralSymmetry.s(i, this))
        );
    }

    @Override
    public boolean operationIsCommutative() {
        return n <= 2;
    }

    @Override
    public String toString() {
        return "D" + TextUtils.subscript(n);
    }
}
