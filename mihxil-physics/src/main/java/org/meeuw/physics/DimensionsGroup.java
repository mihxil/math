package org.meeuw.physics;

import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.streams.StreamUtils;
import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 */
@Example(MultiplicativeAbelianGroup.class)
public class DimensionsGroup extends AbstractAlgebraicStructure<DimensionalAnalysis>
    implements
    MultiplicativeAbelianGroup<DimensionalAnalysis>,
    Streamable<DimensionalAnalysis> {

    private static final DimensionalAnalysis ONE = new DimensionalAnalysis();

    public static final DimensionsGroup INSTANCE = new DimensionsGroup();

    private DimensionsGroup() {
        super(DimensionalAnalysis.class);
    }

    @Override
    public DimensionalAnalysis one() {
        return ONE;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }

    @Override
    public Stream<DimensionalAnalysis> stream() {
        return StreamUtils.allIntArrayStream(Dimension.values().length)
            .map(
                DimensionalAnalysis::new
            );
    }
}
