package org.meeuw.physics;

import java.util.stream.Stream;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 */
public class DimensionsGroup extends AbstractAlgebraicStructure<Dimensions> implements MultiplicativeAbelianGroup<Dimensions>, Streamable<Dimensions> {

    private static final Dimensions ONE = new Dimensions();

    public static final DimensionsGroup INSTANCE = new DimensionsGroup();

    private DimensionsGroup() {
        super(Dimensions.class);
    }

    @Override
    public Dimensions one() {
        return ONE;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }

    @Override
    public Stream<Dimensions> stream() {
        return Utils.stream(Dimension.values().length).map(
            Dimensions::new
        );
    }
}
