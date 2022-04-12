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
import lombok.With;

import java.util.*;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.configuration.ConfigurationService;

public class RandomConfiguration implements ConfigurationAspect {


    public static long nextLong(Random random) {
        int setSize = ConfigurationService.getConfigurationAspect(RandomConfiguration.class).getSetSize();
        return random.nextInt(setSize - setSize / 2);
    }

    public static long nextNonNegativeLong(Random random) {
        int setSize = ConfigurationService.getConfigurationAspect(RandomConfiguration.class).getSetSize();
        return random.nextInt(setSize);
    }

    @With
    @Getter
    private final int setSize;

    public RandomConfiguration() {
        this(1000);
    }

    @lombok.Builder
    private RandomConfiguration(int setSize) {
        this.setSize = setSize;
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Arrays.asList(AlgebraicStructure.class);
    }
}
