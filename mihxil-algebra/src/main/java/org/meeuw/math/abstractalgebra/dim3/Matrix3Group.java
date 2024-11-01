/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.abstractalgebra.dim3;

import java.util.Random;

import org.meeuw.math.Equivalence;
import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 * @see org.meeuw.math.abstractalgebra.linear.GeneralLinearGroup
 */
public class Matrix3Group extends AbstractAlgebraicStructure<Matrix3>
    implements MultiplicativeGroup<Matrix3> {

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


    @Override
    public Matrix3 nextRandom(Random r) {
        return new Matrix3(new double[][]{
            {r.nextDouble(), r.nextDouble(), r.nextDouble()},
            {r.nextDouble(), r.nextDouble(), r.nextDouble()},
            {r.nextDouble(), r.nextDouble(), r.nextDouble()}
        });
    }

    public void setDoubleEquivalence(DoubleEquivalence doubleEquivalence) {
        this.doubleEquivalence.set(doubleEquivalence);
    }
    public void clearDoubleEquivalence() {
        this.doubleEquivalence.remove();
    }
}
