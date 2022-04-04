package org.meeuw.math.abstractalgebra.categoryofgroups;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */

public class Group extends AbstractAlgebraicStructure<Element>
    implements MultiplicativeSemiGroup<Element> {

    static final Group INSTANCE = new Group();


    private Group() {
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
