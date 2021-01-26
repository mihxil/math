package org.meeuw.math.statistics.text;

import lombok.Getter;
import lombok.With;

import java.time.ZoneId;

import org.meeuw.math.text.configuration.ConfigurationAspect;

/**
 * Contains time related settings.
 * E.g. to format an instance as a {@link java.time.LocalDateTime} the prefered {@link ZoneId} is relevant.
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

}
