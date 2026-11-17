package org.meeuw.math.abstractalgebra;

import java.net.URI;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A description of an {@link AlgebraicStructure algebraic structure} (see {@link AlgebraicStructure#getDescription()}): a text, and optionally a link
 * to more information (like a wikipedia page). Similar to 'external documentation' in OpenAPI.
 *
 * @param text a (short) text describing the structure
 * @param url a link to more information, or {@code null}
 * @since 0.24
 */
public record Description(@NonNull String text, @Nullable URI url) {

    public Description {
        if (text == null) {
            throw new IllegalArgumentException("The text of a description cannot be null");
        }
    }

    public static Description of(@NonNull String text) {
        return new Description(text, null);
    }

    public static Description of(@NonNull String text, @NonNull String url) {
        return new Description(text, URI.create(url));
    }

    /**
     * @return the link to more information, if there is one
     */
    public Optional<URI> getUrl() {
        return Optional.ofNullable(url);
    }

    @Override
    public String toString() {
        return text;
    }
}
