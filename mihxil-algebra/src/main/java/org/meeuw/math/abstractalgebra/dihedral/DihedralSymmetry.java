package org.meeuw.math.abstractalgebra.dihedral;

import lombok.EqualsAndHashCode;

import java.util.function.UnaryOperator;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.dim2.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.math.Utils.TAU;
import static org.meeuw.math.abstractalgebra.dihedral.Symmetry.r;
import static org.meeuw.math.abstractalgebra.dihedral.Symmetry.s;

/**
 * @since 0.14
 * see org.meeuw.math.abstractalgebra.dihedral
 */
@EqualsAndHashCode
public class DihedralSymmetry implements GroupElement<DihedralSymmetry>, UnaryOperator<Vector2> {

    private final Symmetry symmetry;
    private final int k;

    private final DihedralGroup group;

    private transient Matrix2 asMatrix;

    static DihedralSymmetry r(int k, DihedralGroup dihedralGroup) {
        return new DihedralSymmetry(r, k, dihedralGroup);
    }

    static DihedralSymmetry s(int k, DihedralGroup dihedralGroup) {
        return new DihedralSymmetry(s, k, dihedralGroup);
    }

    public static DihedralSymmetry r(int k, int n) {
        return DihedralGroup.of(n).r(k);
    }

    public static DihedralSymmetry s(int k, int n) {
        return DihedralGroup.of(n).s(k);
    }


    private DihedralSymmetry(Symmetry symmetry, int k, DihedralGroup dihedralGroup) {
        this.symmetry = symmetry;
        if (k < 0) {
            throw new InvalidElementCreationException(symmetry.name() + k + " is impossible");
        }
        this.k = k;
        if (k >= dihedralGroup.n) {
            throw new InvalidElementCreationException(symmetry.name() + k + " is impossible for " + dihedralGroup);

        }
        this.group = dihedralGroup;
    }


    @Override
    public DihedralGroup getStructure() {
        return group;
    }

    @Override
    public DihedralSymmetry operate(DihedralSymmetry operand) {
        if (symmetry == r) {
            if (operand.symmetry == r) {
                return DihedralSymmetry.r((k + operand.k) % group.n, group);
            } else {
                return DihedralSymmetry.s((k + operand.k) % group.n, group);
            }
        } else {
            if (operand.symmetry == r) {
                return DihedralSymmetry.s((group.n + k - operand.k) % group.n, group);
            } else {
                return DihedralSymmetry.r((group.n + k - operand.k) % group.n, group);
            }
        }
    }

    @Override
    public DihedralSymmetry inverse() {
        if (symmetry == r) {
            return DihedralSymmetry.r((group.n - k) % group.n, group);
        } else {
            return DihedralSymmetry.s(k % group.n, group);
        }
    }

    @Override
    public String toString() {
        return symmetry + TextUtils.subscript(k);
    }

    public Matrix2 asMatrix2() {
        if (asMatrix == null) {
            double phi = TAU * k / group.n;
            double cos = Math.cos(phi);
            double sin = Math.sin(phi);
            if (symmetry == r) {
                asMatrix = Matrix2.of(
                    cos, -1 * sin,
                    sin, cos
                );
            } else {
                asMatrix = Matrix2.of(
                    cos, sin,
                    sin, -1 * cos
                );
            }
        }
        return asMatrix;
    }

    public <E extends CompleteScalarFieldElement<E>> FieldMatrix2<E> asFieldMatrix2(CompleteScalarField<E> field) {
        E phi = field.pi().times(2).times(k).dividedBy(group.n);
        E cos = phi.cos();
        E sin = phi.sin();
        if (symmetry == r) {
            return FieldMatrix2.of(
                cos, sin.negation(),
                sin, cos
            );
        } else {
            return FieldMatrix2.of(
                cos, sin,
                sin,  cos.negation()
            );
        }
    }

    @Override
    public Vector2 apply(Vector2 realNumbers) {
        return realNumbers.times(asMatrix2());
    }
}
