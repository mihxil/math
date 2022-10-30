/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
