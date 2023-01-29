package org.meeuw.math.statistics.text;

import java.time.ZoneId;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.meeuw.configuration.StringConversionService;

public class ToStringTest {

    @Test
    public void zoneId() {
        assertThat(StringConversionService.toString(ZoneId.of("Europe/Amsterdam"))).contains("Europe/Amsterdam");

        assertThat(StringConversionService.fromString("Europe/Amsterdam", ZoneId.class)).contains(ZoneId.of("Europe/Amsterdam"));

        assertThat(StringConversionService.fromString("unparseable", ZoneId.class)).isEmpty();
    }
}
