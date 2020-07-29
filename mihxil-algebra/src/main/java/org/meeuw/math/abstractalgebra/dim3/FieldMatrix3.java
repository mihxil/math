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
public class FieldMatrix3<F extends NumberFieldElement<F>>
    implements MultiplicativeGroupElement<FieldMatrix3<F>> {

    final F[][] values;

    final NumberField<F> elementStructure;

    final F zero;

    public static <F extends NumberFieldElement<F>> FieldMatrix3<F> of(F[][] values) {
        return new FieldMatrix3<>(values);
    }

    public FieldMatrix3(F[][] values) {
        this.elementStructure = values[0][0].structure();
        this.values = values;
        this.zero = this.elementStructure.zero();
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    FieldMatrix3(NumberField<F> structure) {
        this.elementStructure = structure;
        this.zero = this.elementStructure.zero();
        this.values = (F[][]) new Object[][] {
            {zero, zero, zero},
            {zero, zero, zero},
            {zero, zero, zero}
         };
     }

    @Override
    public FieldMatrix3<F> times(FieldMatrix3<F> multiplier) {
        return of(timesDouble(multiplier.values));
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public FieldVector3<F>[] asVectors() {
        return (FieldVector3<F>[]) new Object[] {
            FieldVector3.of(values[0][0], values[0][1], values[0][2]),
            FieldVector3.of(values[1][0], values[1][1], values[1][2]),
            FieldVector3.of(values[2][0], values[2][1], values[2][2])
        };
    }

    @Override
    public FieldMatrix3<F> pow(int exponent) {
        return null;
    }

    @Override
    public FieldMatrix3Group<F> structure() {
        return new FieldMatrix3Group<F>(elementStructure);
    }

    @Override
    public FieldMatrix3<F> self() {
        return this;
    }


    @SuppressWarnings({"unchecked", "ConstantConditions"})
    F[][] timesDouble(F[][] matrix3) {
        F[][] result = (F[][]) new Object[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                F v = elementStructure.zero();
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

    public FieldMatrix3<F> times(double multiplier) {
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
