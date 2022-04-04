package org.meeuw.test.math.abstractalgebra.product;

import lombok.extern.log4j.Log4j2;

import java.util.stream.Collectors;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.integers.ModuloFieldElement;
import org.meeuw.math.abstractalgebra.klein.KleinElement;
import org.meeuw.math.abstractalgebra.klein.KleinGroup;
import org.meeuw.math.abstractalgebra.product.ProductElement;
import org.meeuw.math.abstractalgebra.test.GroupTheory;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.integers.ModuloField.Z3Z;

@Log4j2
public class ProductGroupTest implements GroupTheory<ProductElement<KleinElement, ModuloFieldElement>> {

    @Test
    public void test() {
        KleinElement a1 = KleinElement.a;
        ModuloFieldElement b1 = Z3Z.element(2);
        ProductElement<KleinElement, ModuloFieldElement> e1 = new ProductElement<>(a1, b1);

        assertThat(e1.getStructure()).isEqualTo(KleinGroup.INSTANCE.times(Z3Z));

        KleinElement a2 = KleinElement.b;
        ModuloFieldElement b2 = Z3Z.element(1);
        ProductElement<KleinElement, ModuloFieldElement> e2 = new ProductElement<>(a2, b2);

        assertThat(e1.operate(e2)).isEqualTo(new ProductElement<>(KleinElement.c, Z3Z.element(2)));

        log.info(BasicAlgebraicBinaryOperator.OPERATION.stringify(e1, e2) + " = " + e1.operate(e2));
    }

    @Override
    @Provide
    public Arbitrary<? extends ProductElement<KleinElement, ModuloFieldElement>> elements() {

        Arbitrary<KleinElement> klein = Arbitraries.of(KleinElement.values());
        Arbitrary<ModuloFieldElement> modulo = Arbitraries.of(Z3Z.stream().collect(Collectors.toList()));
        return Combinators.combine(klein, modulo).as(ProductElement::new);


    }


}
