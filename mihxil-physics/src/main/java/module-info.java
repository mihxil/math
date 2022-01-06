import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.physics.text.spi.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.physics {
    requires static lombok;
    requires org.meeuw.math;
    requires java.validation;
    requires org.checkerframework.checker.qual;
    requires org.meeuw.configuration;
    requires java.logging;

    exports org.meeuw.physics;

    uses AlgebraicElementFormatProvider;

    provides AlgebraicElementFormatProvider with
        DimensionsFormatProvider,
        PhysicalNumberFormatProvider,
        UnitsFormatProvider
        ;

}

