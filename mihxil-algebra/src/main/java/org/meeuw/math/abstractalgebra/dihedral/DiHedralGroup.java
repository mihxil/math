package org.meeuw.math.abstractalgebra.dihedral;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

import static org.meeuw.math.abstractalgebra.dihedral.Symmetry.r;
import static org.meeuw.math.abstractalgebra.dihedral.Symmetry.s;

public class DiHedralGroup implements Group<DiHedralSymmetry>, Streamable<DiHedralSymmetry> {

    public static Map<Integer, DiHedralGroup> CACHE = new ConcurrentHashMap<>();
    final int n;

    private DiHedralGroup(int n) {
        this.n = n;
    }
    public static DiHedralGroup of(int n) {
        return CACHE.computeIfAbsent(n, DiHedralGroup::new);
    }

    @Override
    public DiHedralSymmetry unity() {
        return new DiHedralSymmetry(r, 0, this);
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
                .mapToObj(i -> new DiHedralSymmetry(r, i, this)),
            IntStream.range(0, n)
                .mapToObj(i -> new DiHedralSymmetry(s, i, this))
        );
    }
}
