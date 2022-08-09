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
package org.meeuw.math.text.spi;

import java.text.Format;
import java.util.List;

import org.meeuw.configuration.Configuration;
import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.abstractalgebra.AlgebraicElement;

import static org.meeuw.configuration.ConfigurationService.getConfiguration;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class AlgebraicElementFormatProvider<F extends Format> {


    public abstract F getInstance(Configuration configuration);

    public abstract int weight(Class<? extends AlgebraicElement<?>> weight);

    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append(getClass().getSimpleName());
        List<ConfigurationAspect> configurationAspects =
            getConfiguration().getConfigurationAspectsAssociatedWith(this.getClass());
        if (! configurationAspects.isEmpty()) {
            build.append(" ").append(configurationAspects);
        }
        return build.toString();
    }
}
