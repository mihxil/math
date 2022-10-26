package org.meeuw.configuration;

import lombok.extern.java.Log;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.logging.Level;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Stream;

import org.meeuw.configuration.spi.ToStringProvider;

@Log
class ConfigurationPreferences {

    private static final Preferences USER_PREFERENCES = getUserPreferences();

    private static synchronized Preferences getUserPreferences() {
        log.finer("Creating user preferences");
        Preferences userPreferences = null;
        try {
            userPreferences = Preferences.userNodeForPackage(ConfigurationPreferences.class);
        } catch (Exception e) {
            log.log(Level.WARNING, e, () -> "fooar:" +  e.getClass().getName() + ":" + e.getMessage());
        }
        return userPreferences;
    }

    private ConfigurationPreferences() {
    }

    static void sync() throws BackingStoreException {
        USER_PREFERENCES.flush();
        USER_PREFERENCES.sync();
    }

    static void addPreferenceChangeListener(Configuration.Builder configuration) {
        USER_PREFERENCES.addPreferenceChangeListener(evt -> readDefaults(configuration));
    }


    static void storeDefaults(Configuration configuration) {
        for (ConfigurationAspect aspect : configuration) {
            Preferences node = node(aspect);
            for (Method m : aspect.getClass().getDeclaredMethods()) {
                if (m.getName().length() > 3 && m.getName().startsWith("get") && m.getParameterTypes().length == 0 && !Modifier.isStatic(m.getModifiers())) {
                    String name = m.getName().substring(3);
                    try {
                        Object value = m.invoke(aspect);
                        put(node, name, value);
                    } catch (IllegalAccessException | InvocationTargetException | IOException | IllegalStateException e) {
                        log.warning(String.format("%s for %s (%s): %s", m.getDeclaringClass(), m, aspect, e.getMessage()));
                    }
                }
            }
            log.finer(() -> "Stored " + USER_PREFERENCES);
        }
    }

     static void readDefaults(Configuration.Builder configuration) {
         for (ConfigurationAspect aspect : configuration.build()) {
             ConfigurationAspect as = aspect;
             Preferences node = node(as);
             for (Method m : aspect.getClass().getDeclaredMethods()) {
                 if (m.getName().startsWith("with") && m.getParameterTypes().length == 1) {
                     String name = m.getName().substring(4);
                     Object newValue = null;
                     try {
                         Object currentValue = as.getClass().getDeclaredMethod("get" + name).invoke(as);
                         newValue = get(node, name,
                             m.getParameters()[0].getType(),
                             currentValue);

                         as = (ConfigurationAspect) m.invoke(as, newValue);
                     } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException | NoSuchMethodException e) {
                         log.log(Level.WARNING, "For " + as + "#" + m + "(" + (newValue == null ? "NULL" : (newValue.getClass() + " " + newValue)) + ")" + e.getClass() + " " + e.getMessage(), e);
                     }
                 }
             }
             configuration.aspectValue(as);
         }

         log.fine(() -> "Read " + USER_PREFERENCES);
    }

    private static Preferences node(ConfigurationAspect aspect) {
        return USER_PREFERENCES.node(aspect.getClass().getCanonicalName());
    }

    static void put(Preferences pref, String key, final Object paramValue) throws IOException {
        if (paramValue == null) {
            pref.remove(key);
        } else {
            Optional<String> o = toString(paramValue);
            if (o.isPresent()) {
                pref.put(key, o.get());
            } else if (paramValue instanceof Serializable) {
                putSerializable(pref, key, (Serializable) paramValue);
            } else {
                throw new IllegalStateException("Don't know how to put " + paramValue);
            }
        }
    }

    static <C> Optional<String> toString(C value) {
        return  stream()
            .sorted()
            .map(tp ->
                tp.toString(value).orElse(null)
            )
            .filter(Objects::nonNull)
            .findFirst();
    }

    @SuppressWarnings("unchecked")
    static Stream<ToStringProvider<?>> stream() {
        ServiceLoader<ToStringProvider<?>> loader = (ServiceLoader) ServiceLoader.load(ToStringProvider.class);
        //return loader.stream();  java 9, damn.
        List<ToStringProvider<?>> list = new ArrayList<>();
        loader.iterator().forEachRemaining(list::add);
        return list.stream();
    }


     @SuppressWarnings("unchecked")
     static <C> C get(Preferences pref, String key, Class<? extends C> type, C defaultValue) {

         String v = pref.get(key, toString(defaultValue).orElse(null));
         Optional<C> o = (Optional<C>) stream()
             .sorted()
             .map(tp -> {
                 return tp.fromString(type, v).orElse(null);
             })
             .filter(Objects::nonNull)
             .findFirst();
         return
             o.orElseGet(() ->
                 getSerializable(pref, key, defaultValue)
             );
     }


    static void putSerializable(Preferences pref, String key, Serializable paramValue) throws IOException {
        try (
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);) {
            so.writeObject(paramValue);
            pref.putByteArray(key, bo.toByteArray());
        }
    }

    @SuppressWarnings("unchecked")
    static <C> C getSerializable(Preferences pref, String key, C defaultValue) {
        final byte[] bytes = pref.getByteArray(key, new byte[0]);
        //assert defaultValue instanceof Serializable;
        if (bytes.length > 0) {
            try (
                final ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
                final ObjectInputStream si = new ObjectInputStream(bi)) {
                return (C) si.readObject();
            } catch (IOException | ClassNotFoundException e) {
                log.log(Level.WARNING, "For byte array with length " + bytes.length + ":" + e.getClass().getName() + ": " + e.getMessage() + ", defaulting to "+ defaultValue);
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }
}
