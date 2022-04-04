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
