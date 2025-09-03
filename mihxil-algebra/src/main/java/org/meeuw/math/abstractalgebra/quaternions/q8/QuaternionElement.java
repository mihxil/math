/*
 *  Copyright 2025 Michiel Meeuwissen
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
package org.meeuw.math.abstractalgebra.quaternions.q8;

import org.meeuw.math.abstractalgebra.Group;
import org.meeuw.math.abstractalgebra.GroupElement;
import org.meeuw.math.abstractalgebra.klein.KleinGroup;

/**
 * Element of the Klein-group.
 * @since 0.19
 * @see org.meeuw.math.abstractalgebra.klein
 * @see KleinGroup
 */
public enum QuaternionElement implements GroupElement<QuaternionElement> {

    /**
     * The unity element.
     */
    e("e", "e"),
    i("i", "mi"),
    j("j", "mj"),
    k("k", "mk"),
    me("̅e", "me"),
    mi("̅i", "i"),
    mj("̅j", "j"),
    mk("̅k", "k")
    ;
  private static final QuaternionElement[][] CAYLEY_TABLE = {

    {e,  i,  j,  k, me, mi, mj, mk}, // e
    {i, me, k, mj, mi, e, mk, j},    // i
    {j, mk, me, i, mj, k, e, mi},    // j
    {k, j, mi, me, mk, mj, i, e},    // k
    {me, mi, mj, mk, e, i, j, k},    // me
    {mi, e, mk, j, i, me, k, mj},    // mi
    {mj, k, e, mi, j, mk, me, i},    // mj
    {mk, mj, i, e, k, j, mi, me}     // mk
};
    final String string;
    final String inverse;

    QuaternionElement(String s, String inverse) {
        this.string = s;
        this.inverse = inverse;
    }

    @Override
    public Group<QuaternionElement> getStructure() {
        return QuaternionGroup.INSTANCE;
    }

    @Override
    public QuaternionElement operate(QuaternionElement multiplier) {
        return CAYLEY_TABLE[ordinal()][multiplier.ordinal()];
    }

    @Override
    public QuaternionElement inverse() {
        return valueOf(inverse);
    }

    @Override
    public String toString() {
        return string;
    }


}
