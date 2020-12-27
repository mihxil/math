package org.meeuw.math.abstractalgebra.dim3;

import org.meeuw.math.Equivalence;
import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Matrix3Group extends AbstractAlgebraicStructure<Matrix3> implements MultiplicativeGroup<Matrix3> {

    public static final Matrix3Group INSTANCE = new Matrix3Group();

    @FunctionalInterface
    public interface DoubleEquivalence {
        boolean test(double t, double u);
    }

    final ThreadLocal<DoubleEquivalence> doubleEquivalence = ThreadLocal.withInitial(() -> (t, u) -> t == u);


    private Matrix3Group() {
        super(Matrix3.class);
    }

    @Override
    public Matrix3 one() {
        return new Matrix3(new double[][] {
            {1, 0, 0},
            {0, 1, 0},
            {0 , 0, 1}
        });
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.C;
    }

    @Override
    public Equivalence<Matrix3> getEquivalence() {
        return (m1, m2 ) -> {
            DoubleEquivalence equivalence = this.doubleEquivalence.get();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (!equivalence.test(m1.values[i][j], m2.values[i][j])) {
                        return false;
                    }
                }
            }
            return true;
        };
    }

    public void setDoubleEquivalence(DoubleEquivalence doubleEquivalence) {
        this.doubleEquivalence.set(doubleEquivalence);
    }
    public void clearDoubleEquivalence() {
        this.doubleEquivalence.remove();
    }
}
