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
package org.meeuw.math.abstractalgebra.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

import org.meeuw.math.abstractalgebra.GroupElement;


/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
@EqualsAndHashCode
public class ProductElement<A extends GroupElement<A>, B extends GroupElement<B>> implements
    GroupElement<ProductElement<A, B>>,
    Serializable {

    private static final long serialVersionUID = 0L;

    @Getter
    private final A a;

    @Getter
    private final B b;

    private final ProductGroup<A, B> structure;


    public ProductElement(A a, B b) {
        this(ProductGroup.of(a.getStructure(), b.getStructure()), a, b);
    }

    protected ProductElement(ProductGroup<A, B> structure, A a, B b) {
        this.a = a;
        this.b = b;
        this.structure = structure;
    }


    @Override
    public ProductGroup<A, B> getStructure() {
        return structure;
    }

    @Override
    public ProductElement<A, B> operate(ProductElement<A, B> operand) {
        return new ProductElement<>(a.operate(operand.a),b.operate(operand.b));
    }

    @Override
    public ProductElement<A, B> inverse() {
        return new ProductElement<>(a.inverse(), b.inverse());
    }

    @Override
    public String toString() {
        return "(" + a + ","+ b + ")";
    }
}
