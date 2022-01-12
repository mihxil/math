package org.meeuw.configuration;

import java.io.Serializable;
import java.util.List;

/**
 * Represents one (immutable) aspect of a {@link Configuration}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface ConfigurationAspect extends Serializable {

    /**
     * Used for implementing {@link Configuration#getConfigurationAspectsAssociatedWith(Class)}
     */
    List<Class<?>> associatedWith();

}
