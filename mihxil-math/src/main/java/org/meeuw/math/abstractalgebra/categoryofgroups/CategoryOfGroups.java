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

import org.meeuw.math.abstractalgebra.*;

/**
 * All groups themselves form the 'category of groups'.
 *
 * For we just made it a {@link MultiplicativeSemiGroup}. Groups can be 'multiplied' to form
 * {@link org.meeuw.math.abstractalgebra.product.ProductGroup}
 * @author Michiel Meeuwissen
 * @since 0.8
 */

public class CategoryOfGroups extends AbstractAlgebraicStructure<Element>
    implements MultiplicativeSemiGroup<Element> {

    static final CategoryOfGroups INSTANCE = new CategoryOfGroups();


    private CategoryOfGroups() {
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }

    @Override
    public String toString() {
        return "group";
    }


}
