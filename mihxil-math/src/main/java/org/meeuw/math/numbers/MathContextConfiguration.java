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

    public static MathContextConfiguration get() {
        return ConfigurationService.getConfigurationAspect(MathContextConfiguration.class);
    }

    @Getter
    @With
    private final MathContext context;

    @Getter
    @With
    private final MathContext uncertaintContext;

    public MathContextConfiguration() {
        this(MathContext.DECIMAL128, null);
    }

    public MathContextConfiguration(MathContext context, MathContext uncertaintContext) {
        this.context = context;
        this.uncertaintContext = uncertaintContext == null ? new MathContext(2, RoundingMode.UP) : uncertaintContext;
    }


    @Override
    public List<Class<?>> associatedWith() {
        return Collections.singletonList(BigDecimalOperations.class);
    }
}
