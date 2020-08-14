package org.meeuw.physics;

import org.meeuw.math.abstractalgebra.*;

/**
 * 'Physical' numbers are numbers of a {@link org.meeuw.math.abstractalgebra.Field} but with {@link Units}.
 * This means that such numbers cannot always be added to each other (because their dimensions must match).
 *
 * Physical numbers do, however, form a multiplicative group, because you <em>can</em> always multiply two.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class PhysicalNumbers extends AbstractAlgebraicStructure<PhysicalNumber> implements MultiplicativeGroup<PhysicalNumber> {

    public static final PhysicalConstant ONE = new PhysicalConstant("1", 1, Units.DIMENSIONLESS, "one");

    public static final PhysicalNumbers INSTANCE = new PhysicalNumbers();

    protected PhysicalNumbers() {
        super(PhysicalNumber.class);
    }

    @Override
    public PhysicalNumber one() {
        return ONE;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }
}
