package org.meeuw.math.statistics.text;

import lombok.Getter;
import lombok.With;

import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;

/**
 * Contains time related settings.
 * E.g. to format an instance as a {@link java.time.LocalDateTime} the preferred {@link ZoneId} is relevant.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class TimeConfiguration implements ConfigurationAspect {

    /**
     * The zone id used for representing instances when that is relevant.
     */
    @Getter
    @With
    final ZoneId zoneId;

    @lombok.Builder
    private TimeConfiguration(ZoneId zoneId) {
        this.zoneId = zoneId;
    }
    public TimeConfiguration() {
        this(ZoneId.systemDefault());
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Collections.singletonList(StatisticalLongNumberFormat.class);
    }
}
