package org.meeuw.math;

/**
 * Utilities related to {@link Comparable comparable} objects. {@link #max(Comparable, Comparable[])}, {@link #min(Comparable, Comparable[])}
 * @since 0.15
 */
public class ComparableUtils {

    private ComparableUtils() {
        // utility class
    }

    /**
     * returns the biggest of the given values. The first value is required, the rest is optional.
     * @param value
     * @param values
     * @return
     * @param <T>
     */
    @SafeVarargs
    public static <T extends Comparable<T>> T max(T value, T... values) {
        T result = value;;
        for (T v : values) {
            if (v.compareTo(result) > 0) {
                result = v;
            }
        }
        return result;
    }

    /**
     * returns the smallest of the given values. The first value is required, the rest is optional.
     * @param value
     * @param values
     * @return
     * @param <T>
     */
    @SafeVarargs
    public static <T extends Comparable<T>> T min(T value, T... values) {
        T result = value;;
        for (T v : values) {
            if (v.compareTo(result) < 0) {
                result = v;
            }
        }
        return result;
    }
}
