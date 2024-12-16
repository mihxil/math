package org.meeuw.math.abstractalgebra.dihedral;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.math.text.TextUtils.subscript;

/**
 * @since 0.14
 * @see org.meeuw.math.abstractalgebra.dihedral
 */
public class DihedralGroup implements Group<DihedralSymmetry>, Streamable<DihedralSymmetry> {


    private static final Map<Integer, DihedralGroup> CACHE = new ConcurrentHashMap<>();
    final int n;

    public static DihedralGroup of(int n) {
        return CACHE.computeIfAbsent(n, DihedralGroup::new);
    }

    @Example(Group.class)
    public static DihedralGroup D3 = of(3);


    private DihedralGroup(int n) {
        this.n = n;
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
    public DihedralSymmetry nextRandom(Random random) {
        return random.nextBoolean() ?
            DihedralSymmetry.r(random.nextInt(n), this) :
            DihedralSymmetry.s(random.nextInt(n), this);
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
        return "D" + subscript(n);
    }
}
