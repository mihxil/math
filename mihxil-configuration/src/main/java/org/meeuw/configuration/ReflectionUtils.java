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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.JarFile;
import java.util.stream.Stream;

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
        try {
            return clazz.getDeclaredMethod(name, params);
        } catch (NoSuchMethodException e) {
            log.severe("Could not find method " + name + " " + List.of(params) + " in class " + clazz.getName());
            throw e;

        }
    }

    public static Method getDeclaredBinaryMethod(Class<?> clazz, String name) {
        return getDeclaredBinaryMethod(clazz, name, clazz);
    }

    public static Method getDeclaredBinaryMethod(Class<?> clazz, String name, Class<?> param) {
        return getDeclaredMethod(clazz, name, param);
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


    public static  <C> Set<Class<? extends C>> getSubTypesOf(Class<C> type) {
        Set<Class<? extends C>> result = new HashSet<>();
        String packageName = type.getPackageName();
        String packagePath = packageName.replace('.', '/');
        try {
            Enumeration<URL> resources = ReflectionUtils.class.getClassLoader().getResources(packagePath);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if ("file".equals(resource.getProtocol())) {
                    Path root = Path.of(resource.toURI());
                    try (Stream<Path> paths = Files.walk(root)) {
                        paths.filter(path -> path.toString().endsWith(".class"))
                            .map(path -> root.relativize(path).toString())
                            .map(path -> packageName + '.' + path
                                .substring(0, path.length() - ".class".length())
                                .replace(File.separatorChar, '.'))
                            .forEach(name -> loadSubtype(type, name, result));
                    }
                } else if ("jar".equals(resource.getProtocol())) {
                    JarURLConnection connection = (JarURLConnection) resource.openConnection();
                    try (JarFile jar = connection.getJarFile()) {
                        jar.stream()
                            .map(entry -> entry.getName())
                            .filter(name -> name.startsWith(packagePath + "/") && name.endsWith(".class"))
                            .map(name -> name.substring(0, name.length() - ".class".length()).replace('/', '.'))
                            .forEach(name -> loadSubtype(type, name, result));
                    }
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException("Could not scan " + packageName, e);
        }
        return result;
    }

    private static <C> void loadSubtype(Class<C> type, String name, Set<Class<? extends C>> result) {
        if (name.endsWith("module-info") || name.endsWith("package-info")) {
            return;
        }
        try {
            Class<?> candidate = Class.forName(name, false, ReflectionUtils.class.getClassLoader());
            if (candidate != type && type.isAssignableFrom(candidate)) {
                result.add(candidate.asSubclass(type));
            }
        } catch (ClassNotFoundException | LinkageError ignored) {
            // A class on the test classpath may have optional dependencies.
        }
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
