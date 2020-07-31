package org.meeuw.math.text.spi;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

import java.text.Format;
import java.util.ServiceLoader;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class AlgebraicElementFormatProvider {

    public abstract Format getInstance(int minimumExponent);

    public abstract int weight(AlgebraicElement<?> weight);


    public static  Format getFormat(AlgebraicElement<?> object, int minimumExponent ) {
        final ServiceLoader<AlgebraicElementFormatProvider> loader = ServiceLoader.load(AlgebraicElementFormatProvider.class);
        Format format = null;
        int weight = -1;
        for (AlgebraicElementFormatProvider e : loader) {
            if (e.weight(object) > weight) {
                format = e.getInstance(minimumExponent);
                weight = e.weight(object);
            }
        }
        return format;
    }
}
