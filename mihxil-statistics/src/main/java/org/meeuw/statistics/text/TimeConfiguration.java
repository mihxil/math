package org.meeuw.statistics.text;

import lombok.Getter;
import lombok.With;

import java.time.ZoneId;

import org.meeuw.math.text.configuration.Configuration;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class TimeConfiguration implements Configuration {
    @Getter
    @With
    final ZoneId zoneId;

    public TimeConfiguration(ZoneId zoneId) {
        this.zoneId = zoneId;
    }


}
