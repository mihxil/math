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
package org.meeuw.math.numbers;

import lombok.Getter;
import lombok.With;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.configuration.ConfigurationService;

public class MathContextConfiguration implements ConfigurationAspect {

    /**
     * The default value for {@link #getUncertaintyContext()} . A math context with precision 2, and {@link RoundingMode#HALF_UP}.
     *
     */
    public static MathContext DEFAULT_UNCERTAINTY_CONTEXT =  new MathContext(2, RoundingMode.HALF_UP);

    public static MathContextConfiguration get() {
        return ConfigurationService.getConfigurationAspect(MathContextConfiguration.class);
    }

    @Getter
    @With
    private final MathContext context;

    /**
     * The {@link MathContext} to be used for representing uncertainties. Uncertainties themselves are
     * not, and need not be, very precise. This defaults to {@link #DEFAULT_UNCERTAINTY_CONTEXT}.
     */
    @Getter
    @With
    private final MathContext uncertaintyContext;


    /**
     */
    @Getter
    @With
    private final long maxBits;

    public MathContextConfiguration() {
        this(  new MathContext(100), null, 100_000);
    }

    public MathContextConfiguration(MathContext context, MathContext uncertaintyContext, long maxBits) {
        this.context = context;
        this.uncertaintyContext = uncertaintyContext == null ? DEFAULT_UNCERTAINTY_CONTEXT : uncertaintyContext;
        this.maxBits = maxBits;
    }


    @Override
    public List<Class<?>> associatedWith() {
        return Collections.singletonList(BigDecimalOperations.class);
    }
}
