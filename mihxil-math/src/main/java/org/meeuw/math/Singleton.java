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
package org.meeuw.math;

import java.lang.annotation.*;

/**
 * When an {@link org.meeuw.math.abstractalgebra.AlgebraicStructure} (or other class) is marked with this annotation, it indicates that it can only have one instance (it is a singleton). There should be a public static final field named {@code INSTANCE} that holds this instance. The one constructor should be private.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Singleton {

}
