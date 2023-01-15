/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.configuration;

import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.lang.reflect.*;
import java.util.*;
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
                    assert false : e.getMessage();
                }
            }
        }
    }

    public static <C> void forConstants(Class<C> clazz, Consumer<C> consumer) {
        forConstants(clazz, clazz, consumer);
    }

    @SneakyThrows
    public static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... params) {
        return clazz.getDeclaredMethod(name, params);
    }

    public static Method getDeclaredBinaryMethod(Class<?> clazz, String name) {
        return getDeclaredMethod(clazz, name, clazz);
    }

    /**
     * Borrowed from <a href="https://stackoverflow.com/questions/9797212/finding-the-nearest-common-superclass-or-superinterface-of-a-collection-of-classes">stackoverflow</a>
     */
    public static Deque<Class<?>> commonSuperClass(List<Class<?>> classes) {
        // start off with set from first hierarchy

        final Iterator<Class<?>> iterator = classes.iterator();
        final Set<Class<?>> rollingIntersect = new LinkedHashSet<>(
            getClassesBfs(iterator.next()));
        // intersect with next
        iterator.forEachRemaining(c -> rollingIntersect.retainAll(getClassesBfs(c)));
        return new LinkedList<>(rollingIntersect);
    }

    private static Set<Class<?>> getClassesBfs(Class<?> clazz) {
        final Set<Class<?>> classes = new LinkedHashSet<>();
        final Set<Class<?>> nextLevel = new LinkedHashSet<>();
        nextLevel.add(clazz);
        do {
            classes.addAll(nextLevel);
            final Set<Class<?>> thisLevel = new LinkedHashSet<>(nextLevel);
            nextLevel.clear();
            for (Class<?> each : thisLevel) {
                final Class<?> superClass = each.getSuperclass();
                if (superClass != null && superClass != Object.class) {
                    nextLevel.add(superClass);
                }
                Collections.addAll(nextLevel, each.getInterfaces());
            }
        } while (!nextLevel.isEmpty());
        return classes;
    }
}
