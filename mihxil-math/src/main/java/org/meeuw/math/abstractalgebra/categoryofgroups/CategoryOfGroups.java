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
package org.meeuw.math.abstractalgebra.categoryofgroups;

import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.NotStreamable;

/**
 * All groups themselves form the 'category of groups'. I.e, structures are elements of this thing.
 * <p>
 * For now, we just made it a {@link MultiplicativeSemiGroup}. Groups can be 'multiplied' to form
 * {@link org.meeuw.math.abstractalgebra.product.ProductGroup}
 * @author Michiel Meeuwissen
 * @since 0.8
 */

public class CategoryOfGroups extends AbstractAlgebraicStructure<Element>
    implements MultiplicativeSemiGroup<Element>, Streamable<Element> {

    static final CategoryOfGroups INSTANCE = new CategoryOfGroups();

    private CategoryOfGroups() {
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }

    @Override
    public String toString() {
        return "GRP";
    }


    @Override
    public Stream<Element> stream() {
        throw new NotStreamable("I suppose the number of possible groups is countable, but I wouldn't know now...");
    }
}
