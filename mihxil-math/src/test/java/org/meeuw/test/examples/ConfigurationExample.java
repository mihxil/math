package org.meeuw.test.examples;

// tag::import[]
import org.meeuw.configuration.Configuration;
import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;

import static org.meeuw.configuration.ConfigurationService.*;
// end::import[]

/**
 * Code blocks used to include in asciidoc, see README-source.adoc. Asciidoc detects the tag:: comments.
 * <p>
 * Like this it is ensured that the code compiles, and is easier editable.
 *
 */
@SuppressWarnings("unused")
public class ConfigurationExample {

    public static void access() {
        // tag::access[]

        Configuration configuration = getConfiguration();
        NumberConfiguration aspect = configuration.getAspect(NumberConfiguration.class);
        int minimalExponent = aspect.getMinimalExponent();
        // end::access[]
    }


    public static void exampleConfiguration() {
        // tag::configurationService[]

        {
            //noinspection resource
            setConfiguration(builder ->
                builder.configure(NumberConfiguration.class,
                    (numberConfiguration) -> numberConfiguration.withMinimalExponent(8)
                )
            );

            //...code...
            ConfigurationService.resetToDefaults();
        }

        // or using Autocloseable
        try (Reset ignored = setConfiguration(builder ->
            builder.configure(NumberConfiguration.class,
                (numberConfiguration) -> numberConfiguration.withMinimalExponent(8)
            )
        )) {
            ;
            //...code...
        }
        // end::configurationService[]
    }

    public static void usingCloseable() {
        // tag::closable[]
        withConfiguration((con) ->
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
        defaultConfiguration((configurationBuilder) ->
            configurationBuilder.configure(NumberConfiguration.class, c -> c.withMinimalExponent(4))
                .configure(UncertaintyConfiguration.class, c -> c.withNotation(UncertaintyConfiguration.Notation.PLUS_MINUS))
        );
        // end::global[]
    }
}
