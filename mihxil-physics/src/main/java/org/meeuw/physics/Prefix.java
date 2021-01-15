package org.meeuw.physics;

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
        public String toString() {
            return "";
        }
    };

    @Override
    String toString();

}
