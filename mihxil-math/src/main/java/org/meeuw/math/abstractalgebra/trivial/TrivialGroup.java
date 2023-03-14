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
package org.meeuw.math.abstractalgebra.trivial;

import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.text.TextUtils;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 * @see org.meeuw.math.abstractalgebra.trivial
 */
@Example(Group.class)
public class TrivialGroup extends AbstractAlgebraicStructure<TrivialGroupElement>
    implements Group<TrivialGroupElement>, Streamable<TrivialGroupElement> {

    public  static final TrivialGroup INSTANCE = new TrivialGroup();

    private TrivialGroup() {
    }

    @Override
    public TrivialGroupElement unity() {
        return TrivialGroupElement.e;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ONE;
    }

    @Override
    public Stream<TrivialGroupElement> stream() {
        return Stream.of(TrivialGroupElement.e);
    }

    @Override
    public boolean operationIsCommutative() {
        return true;
    }

    @Override
    public String toString() {
        return "C" + TextUtils.subscript(1);
    }

}
