package org.meeuw.test.examples;
// tag::import[]

import org.meeuw.configuration.Configuration;
import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.text.configuration.NumberConfiguration;
// end::import[]
public class ConfigurationExample {

    public static void access() {
        // tag::access[]

        Configuration configuration = ConfigurationService.getConfiguration();
        NumberConfiguration aspect = configuration.getAspect(NumberConfiguration.class);
        int minimalExponent = aspect.getMinimalExponent();
        // end::access[]
    }

    public static void setConfiguration(String[] argv) {
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
}
