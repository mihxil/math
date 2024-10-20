package org.meeuw.math.abstractalgebra.dihedral;

import lombok.EqualsAndHashCode;

import org.meeuw.math.abstractalgebra.GroupElement;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.math.abstractalgebra.dihedral.Symmetry.r;
import static org.meeuw.math.abstractalgebra.dihedral.Symmetry.s;

@EqualsAndHashCode
public class DiHedralSymmetry implements GroupElement<DiHedralSymmetry> {

    private final Symmetry symmetry;
    private final int i;

    private final DiHedralGroup group;

    public DiHedralSymmetry(Symmetry symmetry, int i, DiHedralGroup diHedralGroup) {
        this.symmetry = symmetry;
        this.i = i;
        this.group = diHedralGroup;

    }

     public DiHedralSymmetry(Symmetry symmetry, int i, int n) {
        this(symmetry, i, DiHedralGroup.of(n));
     }

    @Override
    public DiHedralGroup getStructure() {
        return group;
    }

    @Override
    public DiHedralSymmetry operate(DiHedralSymmetry operand) {
        if (symmetry == r) {
            if (operand.symmetry == r) {
                return new DiHedralSymmetry(r, (i + operand.i) % group.n, group);
            } else {
                return new DiHedralSymmetry(s, (i + operand.i) % group.n, group);
            }
        } else {
            if (operand.symmetry == r) {
                return new DiHedralSymmetry(s, (group.n + i - operand.i) % group.n, group);
            } else {
                return new DiHedralSymmetry(r, (group.n + i - operand.i) % group.n, group);
            }
        }
    }

    @Override
    public DiHedralSymmetry inverse() {
        if (symmetry == r) {
            return new DiHedralSymmetry(r, (group.n - i) % group.n, group);
        } else {
            return new DiHedralSymmetry(s, (group.n - i) % group.n, group);

        }
    }

    @Override
    public String toString() {
        return symmetry + TextUtils.subscript(i);
    }
}
