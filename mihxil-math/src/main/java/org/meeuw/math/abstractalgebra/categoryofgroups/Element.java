package org.meeuw.math.abstractalgebra.categoryofgroups;

import java.io.Serializable;

import org.meeuw.math.abstractalgebra.MultiplicativeSemiGroupElement;
import org.meeuw.math.abstractalgebra.product.ProductGroup;


/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public interface Element extends
    MultiplicativeSemiGroupElement<Element>,
    Serializable {

    @Override
    default Group getStructure() {
        return Group.INSTANCE;
    }

    @SuppressWarnings("rawtypes")
    @Override
    default ProductGroup<?, ?> times(Element operand) {
        return ProductGroup.ofGeneric((org.meeuw.math.abstractalgebra.Group) this, (org.meeuw.math.abstractalgebra.Group) operand);
    }
}
