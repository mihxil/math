package org.meeuw.statistics.text;

import lombok.Getter;
import lombok.With;

import java.time.ZoneId;

import org.meeuw.math.text.configuration.ConfigurationAspect;

/**
 * Contains time related settings.
 * To format an instance the prefered {@link #getZoneId()} may be relevant.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class TimeConfiguration implements ConfigurationAspect {
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
