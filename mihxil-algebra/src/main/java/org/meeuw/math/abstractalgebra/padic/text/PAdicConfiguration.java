package org.meeuw.math.abstractalgebra.padic.text;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.With;

import java.util.Collections;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.abstractalgebra.padic.PAdicNumber;

/**
 * Configures how {@link PAdicNumber p-adic numbers} are presented.
 * @since 0.24
 */
@Getter
@EqualsAndHashCode
public class PAdicConfiguration implements ConfigurationAspect {

    /**
     * How the repeating digits are indicated
     */
    @With
    private final Notation notation;

    /**
     * The maximal number of digits that are calculated to find the repetend. The repetend may be much longer than the number itself (e.g. the
     * repetend of 1/1000003 in base 5 has 1000002 digits). In that case only this number of least significant digits is shown, preceded by {@code '…'}.
     */
    @With
    private final int maximalNumberOfDigits;

    public PAdicConfiguration() {
        this(Notation.ELLIPSIS, 1000);
    }

    @lombok.Builder
    private PAdicConfiguration(Notation notation, int maximalNumberOfDigits) {
        if (maximalNumberOfDigits < 1) {
            throw new IllegalArgumentException("The maximal number of digits must be positive: " + maximalNumberOfDigits);
        }
        this.notation = notation;
        this.maximalNumberOfDigits = maximalNumberOfDigits;
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Collections.singletonList(PAdicNumberFormatProvider.class);
    }

    public enum Notation {
        /**
         * The repetend preceded by {@code ...}, and separated from the other digits by a space. E.g. {@code ...4 3.21₅}, {@code ...13 2₅} (which is 1/3),
         * {@code ...0 12₅} (which is 7)
         */
        ELLIPSIS,
        /**
         * The repetend {@link org.meeuw.math.text.TextUtils#overLine(CharSequence) overlined}. A repetend of only zeros is not shown. E.g. {@code 4̅3.21₅},
         * {@code 1̅3̅2₅} (which is 1/3), {@code 12₅} (which is 7)
         */
        OVERLINE
    }
}
