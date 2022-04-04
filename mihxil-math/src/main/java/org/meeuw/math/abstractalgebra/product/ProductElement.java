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
