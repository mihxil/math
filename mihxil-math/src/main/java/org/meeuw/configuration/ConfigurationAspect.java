package org.meeuw.configuration;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Represent one (immutable) aspect of a {@link Configuration}
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface ConfigurationAspect extends Serializable {

    default List<Class<?>> associatedWith() {
        return Collections.emptyList();
    }

}
