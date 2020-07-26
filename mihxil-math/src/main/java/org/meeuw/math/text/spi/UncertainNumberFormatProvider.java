package org.meeuw.math.text.spi;

import java.text.Format;
import java.util.ServiceLoader;

import org.meeuw.math.UncertainNumber;

/**
 * @author Michiel Meeuwissen
 * @since -.4
 */
public abstract class UncertainNumberFormatProvider {


    public abstract Format getInstance(int minimumExponent);

    public abstract int weight(UncertainNumber<?> weight);


    public static  Format getFormat(UncertainNumber<?> object, int minimumExponent ) {
        final ServiceLoader<UncertainNumberFormatProvider> loader = ServiceLoader.load(UncertainNumberFormatProvider.class);
        Format format = null;
        int weight = -1;
        for (UncertainNumberFormatProvider e : loader) {
            if (e.weight(object) > weight) {
                format = e.getInstance(minimumExponent);
                weight = e.weight(object);
            }
        }
        return format;
    }
}
