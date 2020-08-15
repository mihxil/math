import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.physics {
    requires org.meeuw.math;
    requires static lombok;
    exports org.meeuw.physics;

    uses AlgebraicElementFormatProvider;



}

