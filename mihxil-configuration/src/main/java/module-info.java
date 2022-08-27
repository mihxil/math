import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.configuration.spi.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.configuration {
    requires static lombok;
    requires org.checkerframework.checker.qual;

    requires java.logging;
    requires java.prefs;

    exports org.meeuw.configuration;
    exports org.meeuw.configuration.spi;

    uses ConfigurationAspect;
    uses ToStringProvider;

    provides ToStringProvider with
        EnumToString,
        StringToString,
        DoubleToString,
        FloatToString,
        LongToString,
        IntegerToString,
        BooleanToString
        ;
}

