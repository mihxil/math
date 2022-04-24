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
package org.meeuw.test.configuration.spi;


import lombok.ToString;
import lombok.With;

import java.util.Collections;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;

/**
 * Has no no-args constructor
 * @author Michiel Meeuwissen
 */
@ToString
public class InvalidConfigurationAspect implements ConfigurationAspect {

    @With
    final int someInt;

    public InvalidConfigurationAspect() {
        someInt = 1;
        //throw new IllegalStateException();
    }


    public InvalidConfigurationAspect(int someInt) {
        this.someInt = someInt;
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Collections.singletonList(TestProvider.class);
    }
}
