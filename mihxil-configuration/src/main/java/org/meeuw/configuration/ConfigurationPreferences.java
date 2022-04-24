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

    private static final Preferences USER_PREFERENCES = Preferences.userNodeForPackage(ConfigurationPreferences.class);

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
                    Class<?> returnType = m.getReturnType();
                    try {
                        Object value = m.invoke(aspect);
                        put(node, name, returnType, value);
                    } catch (IllegalAccessException | InvocationTargetException | IOException | IllegalStateException e) {
                        log.warning(String.format("%s for %s (%s): %s", m.getDeclaringClass(), m, aspect, e.getMessage()));
                    }
                }
            }
            log.fine(() -> "Stored " + USER_PREFERENCES);
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

         log.info(() -> "Read " + USER_PREFERENCES);
    }

    private static Preferences node(ConfigurationAspect aspect) {
        return USER_PREFERENCES.node(aspect.getClass().getCanonicalName());
    }

    static void put(Preferences pref, String key, Class<?> type, final Object paramValue) throws IOException {
        if (paramValue == null) {
            pref.remove(key);
        } else if (paramValue instanceof Integer) {
            pref.putInt(key, (Integer) paramValue);
        } else if (paramValue instanceof Long) {
            pref.putLong(key, (Long) paramValue);
        } else if (paramValue instanceof Boolean) {
            pref.putBoolean(key, (Boolean) paramValue);
        } else if (paramValue instanceof Float) {
            pref.putFloat(key, (Float) paramValue);
        } else if (paramValue instanceof Double) {
            pref.putDouble(key, (Double) paramValue);
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

    static Stream<ToStringProvider> stream() {
        ServiceLoader<ToStringProvider> loader = ServiceLoader.load(ToStringProvider.class);
        //return loader.stream();  java 9, damn.
        List<ToStringProvider> list = new ArrayList<>();
        loader.iterator().forEachRemaining(list::add);
        return list.stream();
    }


     @SuppressWarnings("unchecked")
     static <C> C get(Preferences pref, String key, Class<? extends C> type, C defaultValue) {
        if (Integer.class.isAssignableFrom(type) || Integer.TYPE.isAssignableFrom(type)) {
            return (C) Integer.valueOf(pref.getInt(key, (int) defaultValue));
        } else if (Long.class.isAssignableFrom(type) || Long.TYPE.isAssignableFrom(type)) {
            return (C) Long.valueOf(pref.getLong(key, (long) defaultValue));
        } else if (Boolean.class.isAssignableFrom(type) || Boolean.TYPE.isAssignableFrom(type)) {
            return (C) Boolean.valueOf(pref.getBoolean(key, (Boolean) defaultValue));
        } else if (Float.class.isAssignableFrom(type) || Float.TYPE.isAssignableFrom(type)) {
            return (C) Float.valueOf(pref.getFloat(key, (Float) defaultValue));
        } else if (Double.class.isAssignableFrom(type) || Double.TYPE.isAssignableFrom(type)) {
            return (C) Double.valueOf(pref.getDouble(key, (Double) defaultValue));
        } else {
            String v = pref.get(key, toString(defaultValue).orElse(null));
            Optional<C> o = (Optional<C>) stream()
                .sorted()
                .map(tp -> {
                    Object nv =  tp.fromString(type, v).orElse(null);
                    return nv;
                })
                .filter(Objects::nonNull)
                .findFirst();
            return
                o.orElseGet(() ->
                    getSerializable(pref, key, defaultValue)
                );
        }
     }


    static void putSerializable(Preferences pref, String key, Serializable paramValue) throws IOException {
        try (
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);) {
            so.writeObject(paramValue);
            pref.putByteArray(key, bo.toByteArray());
        }
    }

    static <C> C getSerializable(Preferences pref, String key, C defaultValue) {
        final byte[] bytes = pref.getByteArray(key, new byte[0]);
        //assert defaultValue instanceof Serializable;
        if (bytes.length > 0) {
            try (
                ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
                ObjectInputStream si = new ObjectInputStream(bi)) {
                return (C) si.readObject();
            } catch (IOException | ClassNotFoundException e) {
                log.log(Level.WARNING, e.getMessage(), e);
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }
}
