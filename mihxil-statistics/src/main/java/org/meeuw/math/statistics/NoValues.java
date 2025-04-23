/*
 *  Copyright 2023 Michiel Meeuwissen
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
package org.meeuw.math.statistics;

import org.meeuw.math.exceptions.DivisionByZeroException;

/**
 * This exception can be thrown when requesting the value for a {@link StatisticalNumber}, but it has no {@link StatisticalLong#enter(long...) entered values} yet
 */
public class NoValues extends DivisionByZeroException {
    public NoValues(String s, String operationString) {
        super(s, operationString);
    }
}
