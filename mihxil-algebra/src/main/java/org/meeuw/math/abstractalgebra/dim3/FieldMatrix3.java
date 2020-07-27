package org.meeuw.math.abstractalgebra.dim3;

import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@EqualsAndHashCode
public class FieldMatrix3<F extends NumberFieldElement<F, A>, A extends NumberField<F, A>>
    implements MultiplicativeGroupElement<FieldMatrix3<F, A>, FieldMatrix3Group<F, A>> {

    final F[][] values;

    final A structure;

    final F zero;

    public static <F extends NumberFieldElement<F, A>, A extends NumberField<F, A>> FieldMatrix3<F, A> of(F[][] values) {
        return new FieldMatrix3<>(values);
    }

    public FieldMatrix3(F[][] values) {
        this.structure = values[0][0].structure();
        this.values = values;
        this.zero = this.structure.zero();
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    FieldMatrix3(A structure) {
        this.structure = structure;
        this.zero = this.structure.zero();
        this.values = (F[][]) new Object[][] {
            {zero, zero, zero},
            {zero, zero, zero},
            {zero, zero, zero}
         };
     }

    @Override
    public FieldMatrix3<F, A> times(FieldMatrix3<F, A> multiplier) {
        return of(timesDouble(multiplier.values));
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public FieldVector3<F, A>[] asVectors() {
        return (FieldVector3<F, A>[]) new Object[] {
            FieldVector3.of(values[0][0], values[0][1], values[0][2]),
            FieldVector3.of(values[1][0], values[1][1], values[1][2]),
            FieldVector3.of(values[2][0], values[2][1], values[2][2])
        };
    }

    @Override
    public FieldMatrix3<F, A> pow(int exponent) {
        return null;
    }

    @Override
    public FieldMatrix3Group<F, A> structure() {
        return new FieldMatrix3Group<>(structure);
    }

    @Override
    public FieldMatrix3<F, A> self() {
        return this;
    }


    @SuppressWarnings({"unchecked", "ConstantConditions"})
    F[][] timesDouble(F[][] matrix3) {
        F[][] result = (F[][]) new Object[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                F v = structure.zero();
                for (int k = 0; k < 3; k++) {
                    v = v.plus(values[i][k].times(matrix3[k][j]));
                }
                result[i][j] = v;
            }
        }
        return result;
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    F[][] timesDouble(double multiplier) {
        F[][] result = (F[][]) new Object[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = values[i][j].times(multiplier);
            }
        }
        return result;
    }

    public FieldMatrix3<F, A> times(double multiplier) {
        return FieldMatrix3.of(timesDouble(multiplier));
    }

    double determinant() {
        //double A =(values[0][2] *  values[2][2] - );
        double B;
        double C;
        return 0;
    }

    @Override
    public String toString() {
        return "(" + Arrays.stream(asVectors()).map(FieldVector3::toString).collect(Collectors.joining(", ")) + ")";
    }
}
