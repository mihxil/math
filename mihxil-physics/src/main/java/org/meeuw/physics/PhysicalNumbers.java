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

import java.util.NavigableSet;

import org.meeuw.math.Singleton;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.NotParsable;
import org.meeuw.math.operators.AlgebraicComparisonOperator;
import org.meeuw.math.operators.BasicComparisonOperator;

import static org.meeuw.math.CollectionUtils.navigableSet;

/**
 * 'Physical' numbers are numbers of a {@link org.meeuw.math.abstractalgebra.Field} but with {@link Units}.
 * This means that such numbers cannot always be added to each other (because their dimensions must match).
 * <p>
 * Physical numbers do, however, form a multiplicative group, because you <em>can</em> always multiply two.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Singleton
public class PhysicalNumbers extends AbstractAlgebraicStructure<PhysicalNumber>
    implements MultiplicativeAbelianGroup<PhysicalNumber> {

    public static final PhysicalConstant ONE = new PhysicalConstant("1", 1, Units.DIMENSIONLESS, "one");

    public static final PhysicalNumbers INSTANCE = new PhysicalNumbers();

    private PhysicalNumbers() {
        super(PhysicalNumber.class);
    }

    @Override
    public PhysicalNumber one() {
        return ONE;
    }

    @Override
    public NavigableSet<AlgebraicComparisonOperator> getSupportedComparisonOperators() {
        return navigableSet(BasicComparisonOperator.values());
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }

    @Override
    public boolean multiplicationIsCommutative() {
        return true;
    }

    @Override
    public PhysicalNumber fromString(String s) {
        throw new NotParsable.NotImplemented("not implemented");
    }

}
