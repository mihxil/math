package org.meeuw.math.abstractalgebra.dim3;

import lombok.EqualsAndHashCode;
import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;
import org.meeuw.math.abstractalgebra.NumberField;
import org.meeuw.math.abstractalgebra.NumberFieldElement;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Collectors;

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

    private static <F extends NumberFieldElement<F>> FieldMatrix3<F> of(F[] array) {
        return of(array[0], array[1],array[2],array[3],array[4], array[5], array[6], array[7], array[8]);
    }

    @SuppressWarnings("unchecked")
    public static <F extends NumberFieldElement<F>> FieldMatrix3<F> of(
        F v00, F v01, F v02,
        F v10, F v11, F v12,
        F v20, F v21, F v22
        ) {
        F[][] fs = (F[][]) Array.newInstance(v11.getClass(), 3, 3);
        fs[0][0]  = v00;
        fs[0][1]  = v01;
        fs[0][2]  = v02;

        fs[1][0]  = v10;
        fs[1][1]  = v11;
        fs[1][2]  = v12;

        fs[2][0]  = v20;
        fs[2][1]  = v21;
        fs[2][2]  = v22;
        return new FieldMatrix3<F>(fs);
    }

    private FieldMatrix3(F[][] values) {
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
        return new FieldMatrix3Group<>(elementStructure);
    }

    @Override
    public FieldMatrix3<F> self() {
        return this;
    }


    @SuppressWarnings({"unchecked", "ConstantConditions"})
    F[] timesDouble(F[][] matrix3) {
        F[] result = (F[]) new Object[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                F v = elementStructure.zero();
                for (int k = 0; k < 3; k++) {
                    v = v.plus(values[i][k].times(matrix3[k][j]));
                }
                result[i * 3 + j] = v;
            }
        }
        return result;
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    F[] elementTimes(F multiplier) {
        F[]result = (F[]) new Object[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i * 3 + j] = values[i][j].times(multiplier);
            }
        }
        return result;
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
