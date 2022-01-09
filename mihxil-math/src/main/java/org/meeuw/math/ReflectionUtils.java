package org.meeuw.math;

import lombok.extern.java.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

@Log
public class ReflectionUtils {

    private ReflectionUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <C, D> void forConstants(Class<C> clazz, Class<D> constantClass, Consumer<D> consumer) {
        for (Field f : clazz.getDeclaredFields()) {
            if (Modifier.isPublic(f.getModifiers()) &&
                Modifier.isStatic(f.getModifiers()) &&
                constantClass.isAssignableFrom(f.getType())) {
                try {
                    consumer.accept((D) f.get(null));
                } catch (IllegalAccessException e) {
                    log.warning(e.getMessage());
                }
            }
        }
    }

    public static <C> void forConstants(Class<C> clazz, Consumer<C> consumer) {
        forConstants(clazz, clazz, consumer);
    }
}
