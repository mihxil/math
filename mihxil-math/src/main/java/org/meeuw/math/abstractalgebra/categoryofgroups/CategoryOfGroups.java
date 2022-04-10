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
