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
package org.meeuw.math;

import java.lang.annotation.*;

/**
 * Marks a class or field as an example for something more generic. This can be used when generating documentation.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Repeatable(Examples.class)
public @interface Example {

    /**
     * For what class of interface this class or value is an example of.
     */
    Class<?> value();


    /**
     * String to use for the annotated entity when used as an example. This may default to  {@link #toString()} of the
     * object or of the singleton instance of that, but this may be overridden by this.
     */
    String string() default "";

    String prefix() default "";
}
