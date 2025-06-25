package org.meeuw.time;

import java.time.Duration;
import java.time.temporal.Temporal;

import org.meeuw.math.text.FormatService;
import org.meeuw.time.text.spi.UncertainTemporalFormatProvider;

/**
 * @since 0.18
 * @param <N>
 */
public class BasicUncertainDuration<N extends Number> implements UncertainDuration<N> {

    private final N value;
    private final N uncertainty;

    public BasicUncertainDuration(N value, N uncertainty) {
        this.value = value;
        this.uncertainty = uncertainty;
    }

    @Override
    public Duration durationValue() {
        return Duration.ofMillis(value.longValue());
    }

    @Override
    public N getValue() {
        return value;
    }

    @Override
    public N getUncertainty() {
        return uncertainty;
    }

    @Override
    public Temporal addTo(Temporal temporal) {
        throw new UnsupportedOperationException("addTo not implemented for BasicUncertainDuration");
    }

    @Override
    public Temporal subtractFrom(Temporal temporal) {
        throw new UnsupportedOperationException("addTo not implemented for BasicUncertainDuration");
    }

    @Override
    public boolean strictlyEquals(Object o) {
        return o instanceof BasicUncertainDuration b && value.equals(b.value) && uncertainty.equals(b.uncertainty);
    }
    @Override
    public String toString() {
        return FormatService.getFormat( UncertainTemporalFormatProvider.class).format(this);
    }
}
