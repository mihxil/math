package org.meeuw.math.abstractalgebra.dihedral;

import lombok.EqualsAndHashCode;

import java.util.function.UnaryOperator;

import org.meeuw.math.abstractalgebra.GroupElement;
import org.meeuw.math.abstractalgebra.dim2.Matrix2;
import org.meeuw.math.abstractalgebra.dim2.Vector2;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.math.abstractalgebra.dihedral.Symmetry.r;
import static org.meeuw.math.abstractalgebra.dihedral.Symmetry.s;

/**
 * @since 0.14
 */
@EqualsAndHashCode
public class DiHedralSymmetry implements GroupElement<DiHedralSymmetry>, UnaryOperator<Vector2> {

    private final Symmetry symmetry;
    private final int k;

    private final DiHedralGroup group;

    static DiHedralSymmetry r(int k,DiHedralGroup diHedralGroup) {
        return new DiHedralSymmetry(r, k, diHedralGroup);
    }
    static DiHedralSymmetry s(int k,DiHedralGroup diHedralGroup) {
        return new DiHedralSymmetry(s, k, diHedralGroup);
    }

    public static DiHedralSymmetry r(int k, int n) {
        return DiHedralGroup.of(n).r(k);
    }

    public static DiHedralSymmetry s(int k, int n) {
        return DiHedralGroup.of(n).s(k);
    }


    private DiHedralSymmetry(Symmetry symmetry, int k, DiHedralGroup diHedralGroup) {
        this.symmetry = symmetry;
        if (k < 0) {
            throw new InvalidElementCreationException(symmetry.name() + k + " is impossible");
        }
        this.k = k;
        if (k >= diHedralGroup.n) {
            throw new InvalidElementCreationException(symmetry.name() + k + " is impossible for " + diHedralGroup);

        }
        this.group = diHedralGroup;
    }

    private DiHedralSymmetry(Symmetry symmetry, int k, int n) {
        this(symmetry, k, DiHedralGroup.of(n));
     }

    @Override
    public DiHedralGroup getStructure() {
        return group;
    }

    @Override
    public DiHedralSymmetry operate(DiHedralSymmetry operand) {
        if (symmetry == r) {
            if (operand.symmetry == r) {
                return new DiHedralSymmetry(r, (k + operand.k) % group.n, group);
            } else {
                return new DiHedralSymmetry(s, (k + operand.k) % group.n, group);
            }
        } else {
            if (operand.symmetry == r) {
                return new DiHedralSymmetry(s, (group.n + k - operand.k) % group.n, group);
            } else {
                return new DiHedralSymmetry(r, (group.n + k - operand.k) % group.n, group);
            }
        }
    }

    @Override
    public DiHedralSymmetry inverse() {
        if (symmetry == r) {
            return new DiHedralSymmetry(r, (group.n - k) % group.n, group);
        } else {
            return new DiHedralSymmetry(s, k % group.n, group);

        }
    }

    @Override
    public String toString() {
        return symmetry + TextUtils.subscript(k);
    }

    public Matrix2 asMatrix2() {
        double phi = 2 * Math.PI * k / group.n;
        double cos = Math.cos(phi);
        double sin = Math.sin(phi);
        if (symmetry == r) {
            return Matrix2.of(
                cos, -1 * sin,
                sin, cos
            );
        } else {
            return Matrix2.of(
                cos, sin,
                sin, -1 * cos
            );
        }
    }

    @Override
    public Vector2 apply(Vector2 realNumbers) {
        return realNumbers.times(asMatrix2());
    }
}
