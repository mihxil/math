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
package org.meeuw.math.abstractalgebra.dim2;

import java.util.Random;

import org.meeuw.math.Example;
import org.meeuw.math.Singleton;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.text.TextUtils;

/**
 * @author Michiel Meeuwissen
 * @since 0.14
 */
@Example(VectorSpace.class)
@Singleton
public class Vector2Space implements VectorSpace<RealNumber, Vector2>, AbelianRing<Vector2> {

    public static final Vector2Space INSTANCE = new Vector2Space();

    private  Vector2Space() {

    }

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public Vector2 zero() {
        return Vector2.of(0, 0);
    }

    @Override
    public RealField getField() {
        return RealField.INSTANCE;
    }

    @Override
    public Vector2 one() {
        return Vector2.of(1, 1);
    }

    @Override
    public Vector2 nextRandom(Random random) {
        return Vector2.of(random.nextDouble(), random.nextDouble());
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }

    @Override
    public Class<Vector2> getElementClass() {
        return Vector2.class;
    }

    @Override
    public String toString() {
        return getField() + TextUtils.superscript(3);
    }

    @Override
    public Vector2 fromString(String s) {
        String stripped = TextUtils.stripParentheses(s);
        String[] parts = stripped.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Cannot parse " + s + " as " + this);
        }
        return Vector2.of(
            getField().fromString(parts[0]).doubleValue(),
            getField().fromString(parts[1]).doubleValue()
        );

    }
}
