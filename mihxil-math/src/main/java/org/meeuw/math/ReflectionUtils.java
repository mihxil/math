/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math;

import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.lang.reflect.*;
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

    @SneakyThrows
    public static Method getBinaryOperatorMethod(Class<?> clazz, String name) {
        return clazz.getMethod(name, clazz);
    }

    @SneakyThrows
    public static Method getUnaryOperatorMethod(Class<?> clazz, String name) {
        return clazz.getDeclaredMethod(name);
    }
}
