package org.meeuw.math;

/**
 * @since 0.15
 */
public class ComparableUtils {

    private ComparableUtils() {
        // utility class
    }

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
