package org.meeuw.math.abstractalgebra.categoryofgroups;

import org.meeuw.math.abstractalgebra.*;

/**
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
