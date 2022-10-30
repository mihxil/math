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

import java.io.Serializable;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.product.ProductGroup;


/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public interface Element extends
    MultiplicativeSemiGroupElement<Element>,
    Serializable {

    @Override
    default CategoryOfGroups getStructure() {
        return CategoryOfGroups.INSTANCE;
    }

    @SuppressWarnings("rawtypes")
    @Override
    default ProductGroup<?, ?> times(Element operand) {
        return ProductGroup.ofGeneric((org.meeuw.math.abstractalgebra.Group) this, (org.meeuw.math.abstractalgebra.Group) operand);
    }

    @SuppressWarnings("unchecked")
    default <A extends GroupElement<A>, B extends GroupElement<B>> ProductGroup<A, B> cartesian(Group<B> operand) {
        return (ProductGroup<A, B>) times(operand);

    }
}
