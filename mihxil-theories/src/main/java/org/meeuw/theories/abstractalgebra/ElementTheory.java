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
package org.meeuw.theories.abstractalgebra;

import net.jqwik.api.*;
import net.jqwik.api.lifecycle.BeforeProperty;
import org.junit.jupiter.api.BeforeEach;
import org.assertj.core.api.Assertions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.meeuw.theories.BasicObjectTheory;

/**
 * The connection between {@link BasicObjectTheory} and algebra testing.
 * {@link BasicObjectTheory#datapoints()} is identified with {@link #elements()}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface ElementTheory<E>  extends BasicObjectTheory<E> {

    String ELEMENTS = "elements";

    @BeforeEach
    @BeforeProperty
    default void setupForAll() {
        Assertions.setMaxStackTraceElementsDisplayed(20);
    }

    @Provide
    Arbitrary<? extends E> elements();


    default Logger getLogger() {
        return LogManager.getLogger(this.getClass());
    }

    @Override
    default Arbitrary<Object> datapoints() {
        return elements().asGeneric();
    }
}
