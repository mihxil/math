package org.meeuw.physics;

import java.util.Optional;
import java.util.function.DoubleSupplier;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Prefix extends DoubleSupplier {

    Prefix NONE = new Prefix() {
        @Override
        public double getAsDouble() {
            return 1;
        }

        @Override
        public Optional<? extends Prefix> times(Prefix prefix) {
            return Optional.of(prefix);
        }

        @Override
        public Optional<? extends Prefix> dividedBy(Prefix prefix) {
            return prefix.reciprocal();
        }

        @Override
        public Optional<? extends Prefix> reciprocal() {
            return Optional.of(this);
        }

        @Override
        public String toString() {
            return "";
        }
    };

    Optional<? extends Prefix> times(Prefix prefix);

    Optional<? extends Prefix> dividedBy(Prefix prefix);

    Optional<? extends Prefix> reciprocal();

    @Override
    String toString();

}
