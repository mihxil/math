package org.meeuw.math.abstractalgebra.dihedral;

import lombok.Getter;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;

import static org.meeuw.math.text.TextUtils.subscript;

/**
 * @since 0.14
 * @see org.meeuw.math.abstractalgebra.dihedral
 */
public class DihedralGroup implements Group<DihedralSymmetry>, Streamable<DihedralSymmetry> {

    private static final Map<Integer, DihedralGroup> CACHE = new ConcurrentHashMap<>();

    @Getter
    final int n;

    public static DihedralGroup of(int n) {
        return CACHE.computeIfAbsent(n, DihedralGroup::new);
    }

    @Example(Group.class)
    public static DihedralGroup D3 = of(3);

    private final DihedralSymmetry[] rcache;
    private final DihedralSymmetry[] scache;


    private DihedralGroup(int n) {
        this.n = n;
        rcache = new DihedralSymmetry[n];
        scache = new DihedralSymmetry[n];
    }

    public DihedralSymmetry r(int k) {
        if (k < 0 || k >= n) {
            throw new InvalidElementCreationException("! 0 <= " + k + "  < " + n);
        }
        if (rcache[k] == null) {
            rcache[k] = DihedralSymmetry.r(k, this);
        }
        return rcache[k];
    }

    public DihedralSymmetry s(int k) {
        if (k < 0 || k >= n) {
            throw new InvalidElementCreationException("! 0 <= " + k + "  < " + n);
        }
        if (scache[k] == null) {
            scache[k] = DihedralSymmetry.s(k, this);
        }
        return scache[k];
    }

    @Override
    public DihedralSymmetry unity() {
        return r(0);
    }

    @Override
    public DihedralSymmetry nextRandom(Random random) {
        return random.nextBoolean() ?
            r(random.nextInt(n)) :
            s(random.nextInt(n));
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
                .mapToObj(this::r),
            IntStream.range(0, n)
                .mapToObj(this::s)
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
