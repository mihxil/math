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
package org.meeuw.math.abstractalgebra;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An element of {@link Group}, where it is not defined whether the operation is addition or multiplication (or some other operation).
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 * @param <E> Self type
 */
public interface GroupElement<E extends GroupElement<E>>
    extends MagmaElement<E> {

    @Override
    @NonNull
    Group<E> getStructure();

    /**
     * @return The inverse element for {@link #operate(MagmaElement)}, such that {@code self.operate(self.inverse())} = {@link Group#unity()}
     */
    E inverse();


}
