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
package org.meeuw.test.configuration;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.configuration.*;


class ConfigurationServiceTest {

    public static class Unregistered implements ConfigurationAspect {

        @Override
        public List<Class<?>> associatedWith() {
            return Collections.emptyList();
        }
    }

    @Test
    public void invalidConfigurationAspect() {
        Assertions.assertThatThrownBy(() -> ConfigurationService.getConfiguration().getAspect(Unregistered.class)).isInstanceOf(ConfigurationException.class);


    }

}
