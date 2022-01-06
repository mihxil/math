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

    List<Class<?>> associatedWith();

}
