package org.meeuw.physics;

import java.util.Optional;
import java.util.function.DoubleSupplier;

/**
 * Multiplication prefix that can be used on a {@link Unit}, to get a new {@code code} that differs a certain factor only, but has the same {@link DimensionalAnalysis}
 *
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Prefix extends DoubleSupplier {

    Optional<? extends Prefix> times(Prefix prefix);

    Optional<? extends Prefix> dividedBy(Prefix prefix);

    Optional<? extends Prefix> reciprocal();

    Optional<? extends Prefix> inc();

    Optional<? extends Prefix> dec();

    @Override
    String toString();

}
