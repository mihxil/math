/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.abstractalgebra;

import lombok.Getter;

import java.lang.reflect.*;

/**
 * This abstract base class for {@link AlgebraicStructure}s takes care of the 'elementClass' property.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class AbstractAlgebraicStructure<E extends AlgebraicElement<E>>
    implements AlgebraicStructure<E> {

    @Getter
    private final Class<E> elementClass;

    protected AbstractAlgebraicStructure(Class<E> elementClass) {
        this.elementClass = elementClass;
    }

    protected AbstractAlgebraicStructure() {
        this.elementClass = toClass(((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0]);
    }

    @SuppressWarnings("unchecked")
    private Class<E> toClass(Type type) {
        if (type instanceof Class) {
            return (Class<E>) type;
        } else if (type instanceof TypeVariable<?>) {
            return toClass(((TypeVariable) type).getBounds()[0]);
        } else {
            return toClass(((ParameterizedType) type).getRawType());
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
