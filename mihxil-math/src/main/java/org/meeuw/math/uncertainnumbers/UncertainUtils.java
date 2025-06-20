package org.meeuw.math.uncertainnumbers;


import java.util.function.Function;

public final class UncertainUtils {

    private UncertainUtils() {
        // Utility class, no instances allowed
    }



    @SafeVarargs
    public static <E> boolean areExact(E... values) {
        for (E value : values) {
            if (value instanceof Uncertain uncertainValue) {
                if (!uncertainValue.isExact()) {
                    return false;
                }
            }
        }
        return true;
    }


    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <E> boolean strictlyEqual(E incoming, Object other, Function<E, Object>... value) {
        if (incoming == other) return true;
        if (!(incoming.getClass().isInstance(other))) return false;

        E casted = (E) other;
        for (Function<E, Object> f : value) {
            Object v1 = f.apply(incoming);
            Object v2 = f.apply(casted);
            if (v1 instanceof Uncertain uncertain1) {
                if (!uncertain1.strictlyEquals(v2)) {
                    return false;
                }
            } else if (!v1.equals(v2)) {
                return false;
            }
        }
        return true;
    }



}
