package org.meeuw.math.abstractalgebra.dihedral;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.text.TextUtils;

/**
 * @since 0.14
 * @see org.meeuw.math.abstractalgebra.dihedral
 */
public class DihedralGroup implements Group<DihedralSymmetry>, Streamable<DihedralSymmetry> {

    public static Map<Integer, DihedralGroup> CACHE = new ConcurrentHashMap<>();
    final int n;

    private DihedralGroup(int n) {
        this.n = n;
    }
    public static DihedralGroup of(int n) {
        return CACHE.computeIfAbsent(n, DihedralGroup::new);
    }


    public DihedralSymmetry r(int k) {
        return DihedralSymmetry.r(k, this);
    }

    public DihedralSymmetry s(int k) {
        return DihedralSymmetry.s(k, this);
    }

    @Override
    public DihedralSymmetry unity() {
        return DihedralSymmetry.r(0, this);
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.of(2L * n);
    }

    @Override
    public Class<DihedralSymmetry> getElementClass() {
        return DihedralSymmetry.class;
    }

    @Override
    public Stream<DihedralSymmetry> stream() {
        return Stream.concat(
            IntStream.range(0, n)
                .mapToObj(i -> DihedralSymmetry.r(i, this)),
            IntStream.range(0, n)
                .mapToObj(i -> DihedralSymmetry.s(i, this))
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
