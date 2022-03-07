package org.meeuw.physics;

import java.util.Random;
import java.util.stream.Stream;

import org.meeuw.math.streams.StreamUtils;
import org.meeuw.math.abstractalgebra.*;

import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UnitsGroup extends AbstractAlgebraicStructure<Units> implements
    MultiplicativeAbelianGroup<Units>,
    Streamable<Units> {

    private static final Units ONE = CompositeUnits.DIMENSIONLESS;

    public static final UnitsGroup INSTANCE = new UnitsGroup();

    private UnitsGroup() {
        super(Units.class);
    }

    @Override
    public Units one() {
        return ONE;
    }

    @Override
    public boolean multiplicationIsCommutative() {
        return true;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }

    @Override
    public Stream<Units> stream() {
        return StreamUtils.allIntArrayStream(SIUnit.values().length).map(
            array -> {
                UnitExponent[] units = new UnitExponent[array.length];
                for (int i = 0; i < array.length; i++) {
                    units[i] = new UnitExponent(SIUnit.values()[i], array[i]);
                }
                return new CompositeUnits(exactly(1), units);
            }
        );
    }

    @Override
    public Units nextRandom(Random random) {
        throw new UnsupportedOperationException("TODO");
    }
}
