package org.meeuw.math.shapes;

import java.util.function.Supplier;

import org.meeuw.math.exceptions.FieldIncompleteException;

public record Info(Key key, Supplier<Object> description) {

    public String descriptionString() {
        try {
            return String.valueOf(description.get());
        } catch (FieldIncompleteException e) {
            return e.getMessage();
        }
    }

    public enum Key {
        AREA("area"),
        PERIMETER("perimeter"),
        CIRCUMSCRIBED_RECTANGLE("⬚"),
        CIRCUMSCRIBED_CIRCLE("◌"),
        RADIUS("radius"),
        RADIUSX("radiusx"),
        RADIUSY("radiusy"),
        DIAMETER("diameter"),
        EDGES("# edges"),
        VERTICES("# vertices"),
        ANGLE("angle"),
        LINEAR_ECCENTRICITY("linear eccentricity"),
        ECCENTRICITY("eccentricity"),
        ;

        private final String name;

        Key(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
