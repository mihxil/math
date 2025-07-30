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
package org.meeuw.math.abstractalgebra.klein;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.Singleton;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.product.ProductGroup;
import org.meeuw.math.exceptions.NotParsable;

/**
 * The structure of the {@link org.meeuw.math.abstractalgebra.klein Klein 4 group}, denoted by {@code V}.
 * <p>
 * See also <a href="https://en.wikipedia.org/wiki/Klein_four-group">wikipedia</a>
 */
@Example(Group.class)
@Singleton
public class KleinGroup implements Group<KleinElement>, Streamable<KleinElement> {

    public static final KleinGroup INSTANCE = new KleinGroup();

    @Example(Group.class)
    public static final ProductGroup EXAMPLE = INSTANCE.cartesian(INSTANCE);

    private KleinGroup() {
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.of(KleinElement.values().length);
    }

    @Override
    public Class<KleinElement> getElementClass() {
        return KleinElement.class;
    }

    @Override
    public KleinElement unity() {
        return KleinElement.e;
    }

    @Override
    public Stream<KleinElement> stream() {
        return Arrays.stream(KleinElement.values());
    }

    @Override
    public KleinElement nextRandom(Random random) {
        return KleinElement.values()[random.nextInt(KleinElement.values().length)];
    }

    @Override
    public boolean operationIsCommutative() {
        return true;
    }

    @Override
    public String toString() {
        return "V";
    }

    @Override
    public KleinElement fromString(String s) throws NotParsable {
        try {
            return KleinElement.valueOf(s);
        } catch (IllegalArgumentException e) {
            throw new NotParsable(s);
        }

    }

}
