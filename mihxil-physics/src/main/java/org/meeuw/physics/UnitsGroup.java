package org.meeuw.physics;

import java.util.stream.Stream;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UnitsGroup extends AbstractAlgebraicStructure<Units> implements MultiplicativeGroup<Units>, Streamable<Units> {

    private static final Units ONE = UnitsImpl.DIMENSIONLESS;

    public static final UnitsGroup INSTANCE = new UnitsGroup();

    private UnitsGroup() {
        super(Units.class);
    }

    @Override
    public Units one() {
        return ONE;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }

    @Override
    public Stream<Units> stream() {
        return Utils.stream(SIUnit.values().length).map(
            array -> {
                UnitExponent[] units = new UnitExponent[array.length];
                for (int i = 0; i < array.length; i++) {
                    units[i] = new UnitExponent(SIUnit.values()[i], array[i]);
                }
                return new UnitsImpl(units);
            }
        );
    }
}
