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
package org.meeuw.math.abstractalgebra.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.Group;
import org.meeuw.math.abstractalgebra.GroupElement;
import org.meeuw.math.exceptions.AlgebraicStructureException;


/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
@EqualsAndHashCode
public class ProductElement implements GroupElement<ProductElement>, Serializable {

    private static final long serialVersionUID = 0L;

    @Getter
    private final List<GroupElement<?>> multiplicands;

    private final ProductGroup structure;

    public static ProductElement of(GroupElement<?>... groupElement) {
        List<GroupElement<?>> list = asList(groupElement);
        return withGroup(productGroupFor(list), list);
    }

    static ProductElement withGroup(ProductGroup group, List<GroupElement<?>> groupElements) {
        return new ProductElement(group, groupElements);
    }

    private static List<GroupElement<?>> asList(GroupElement<?>... elements) {
        List<GroupElement<?>> result = new ArrayList<>();
        for (GroupElement<?> e : elements) {
            if (e instanceof ProductElement) {
                result.addAll(((ProductElement) e).multiplicands);
            } else {
                result.add(e);
            }
        }
        return result;
    }

    private ProductElement(ProductGroup structure, List<GroupElement<?>> multiplicands) {
        this.multiplicands = multiplicands;
        this.structure = structure;
    }

    protected static ProductGroup productGroupFor(List<GroupElement<?>> multiplicands) {
        return ProductGroup.of(multiplicands.stream().map(GroupElement::getStructure).toArray(Group[]::new));
    }


    @Override
    public @NonNull ProductGroup getStructure() {
        return structure;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public ProductElement operate(ProductElement operand) {
        if (! operand.getStructure().equals(getStructure())) {
            throw new AlgebraicStructureException(this, operand);
        }

        List<GroupElement<?>>  result =  new ArrayList<>();
        for (int i = 0; i < multiplicands.size(); i++) {
            GroupElement groupElement = multiplicands.get(i);
            GroupElement otherElement = operand.multiplicands.get(i);
            GroupElement operate = (GroupElement) groupElement.operate(otherElement);
            result.add(operate);
        }
        return ProductElement.withGroup(structure, result);
    }

    @Override
    public ProductElement inverse() {
        return ProductElement.withGroup(getStructure(),
            multiplicands.stream().map(GroupElement::inverse)
                .collect(Collectors.toList())
        );
    }

    @Override
    public String toString() {
        return "(" + multiplicands.stream().map(Object::toString).collect(Collectors.joining(","))  + ")";
    }
}
