/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.abstractalgebra.dim3;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.text.TextUtils;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(VectorSpace.class)
public class Vector3Space implements VectorSpace<RealNumber, Vector3>, AbelianRing<Vector3> {

    public static final Vector3Space INSTANCE = new Vector3Space();

    @Override
    public int getDimension() {
        return 3;
    }

    @Override
    public Vector3 zero() {
        return Vector3.of(0, 0, 0);
    }

    @Override
    public RealField getField() {
        return RealField.INSTANCE;
    }

    @Override
    public Vector3 one() {
        return Vector3.of(1, 1, 1);
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }

    @Override
    public Class<Vector3> getElementClass() {
        return Vector3.class;
    }

    @Override
    public String toString() {
        return getField() + TextUtils.superscript(3);
    }
}
