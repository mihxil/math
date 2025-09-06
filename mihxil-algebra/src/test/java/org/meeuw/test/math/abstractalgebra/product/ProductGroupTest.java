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
package org.meeuw.test.math.abstractalgebra.product;

import lombok.extern.java.Log;

import java.util.stream.Collectors;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.integers.ModuloField;
import org.meeuw.math.abstractalgebra.integers.ModuloFieldElement;
import org.meeuw.math.abstractalgebra.klein.KleinElement;
import org.meeuw.math.abstractalgebra.klein.KleinGroup;
import org.meeuw.math.abstractalgebra.product.ProductElement;
import org.meeuw.theories.abstractalgebra.GroupTheory;
import org.meeuw.math.exceptions.AlgebraicStructureException;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.integers.ModuloField.Z3Z;

@Log
public class ProductGroupTest implements GroupTheory<ProductElement> {

    @Test
    public void test() {
        KleinElement a1 = KleinElement.a;
        ModuloFieldElement b1 = Z3Z.element(2);
        ProductElement e1 = ProductElement.of(a1, b1);

        assertThat(e1.getStructure()).isEqualTo(KleinGroup.INSTANCE.times(Z3Z));

        KleinElement a2 = KleinElement.b;
        ModuloFieldElement b2 = Z3Z.element(1);
        ProductElement e2 = ProductElement.of(a2, b2);

        assertThat(e1.operate(e2)).isEqualTo(ProductElement.of(KleinElement.c, Z3Z.element(2)));

        log.info(BasicAlgebraicBinaryOperator.OPERATION.stringify(e1, e2) + " = " + e1.operate(e2));
    }

    @Test
    public void incompatible() {
        ProductElement e1 = ProductElement.of(KleinElement.a, Z3Z.element(2));
        ProductElement e2 = ProductElement.of(KleinElement.b, ModuloField.of(13).element(2));

        assertThatThrownBy(() -> e1.operate(e2)).isInstanceOf(AlgebraicStructureException.class);
    }

    @Override
    @Provide
    public Arbitrary<ProductElement> elements() {
        Arbitrary<KleinElement> klein = Arbitraries.of(KleinElement.values());
        Arbitrary<ModuloFieldElement> modulo = Arbitraries.of(Z3Z.stream().collect(Collectors.toList()));
        return Combinators.combine(klein, modulo).as(ProductElement::of);
    }

}
