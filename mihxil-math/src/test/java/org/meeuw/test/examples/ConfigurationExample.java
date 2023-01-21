package org.meeuw.test.examples;
// tag::import[]

import org.meeuw.configuration.Configuration;
import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;

// end::import[]

/**
 * Code blocks used to include in assciidoc.
 *
 *
 *
 */
public class ConfigurationExample {

    public static void access() {
        // tag::access[]

        Configuration configuration = ConfigurationService.getConfiguration();
        NumberConfiguration aspect = configuration.getAspect(NumberConfiguration.class);
        int minimalExponent = aspect.getMinimalExponent();
        // end::access[]
    }

    public static void setConfiguration() {
        // tag::configurationService[]
        ConfigurationService.setConfiguration(builder ->
            builder.configure(NumberConfiguration.class,
                (numberConfiguration) -> numberConfiguration.withMinimalExponent(8)
            )
        );

        //...code...
        ConfigurationService.resetToDefaults();
        // end::configurationService[]
    }

    public static void usingCloseable() {
        // tag::closable[]
        ConfigurationService.withConfiguration((con) ->
                con.configure(UncertaintyConfiguration.class,
                        (uncertaintyConfiguration) -> uncertaintyConfiguration.withNotation(UncertaintyConfiguration.Notation.PARENTHESES))
                    .configure(NumberConfiguration.class,
                        (numberConfiguration) -> numberConfiguration.withMinimalExponent(3))
            , () -> {
                // code

            });
        // end::closable[]

    }

    public static void global() {
        // tag::global[]
        ConfigurationService.defaultConfiguration((con) ->
            con.configure(NumberConfiguration.class, c -> c.withMinimalExponent(4))
                .configure(UncertaintyConfiguration.class, c -> c.withNotation(UncertaintyConfiguration.Notation.PLUS_MINUS))
        );
        // end::global[]

    }
}
