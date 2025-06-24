package org.meeuw.math.time;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.*;

/**
 * @since 0.18
 * @param <N>
 */
public class BasicUncertainInstant<N extends Number> implements UncertainInstant<N> {

    private final N value;
    private final N uncertainty;

    public BasicUncertainInstant(N value, N uncertainty) {
        this.value = value;
        this.uncertainty = uncertainty;
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
    public Instant instantValue() {
        return Instant.ofEpochMilli(value.longValue());
    }

    @Override
    public UncertainDuration<N> until(Temporal endExclusive) {
        // Implementation of until method
        //return new BasicUncertainDuration<>(instantValue().until(endExclusive).get(ChronoUnit.MILLIS), uncertainty); // Placeholder implementation
        throw new UnsupportedOperationException("until not implemented for BasicUncertainInstant");
    }

    @Override
    public String toString() {
        return instantValue() + " ± " + Duration.ofMillis(uncertainty.longValue());
    }

    @Override
    public Temporal plus(long amountToAdd, TemporalUnit unit) {
        throw new UnsupportedOperationException("until not implemented for BasicUncertainInstant");
    }

    @Override
    public boolean strictlyEquals(Object o) {
        return o instanceof BasicUncertainInstant b && value.equals(b.value) && uncertainty.equals(b.uncertainty);
    }
}
