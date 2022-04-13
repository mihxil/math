package org.meeuw.configuration;

import lombok.extern.java.Log;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.prefs.Preferences;

import org.meeuw.configuration.spi.ToStringProvider;

@Log
class ConfigurationPreferences {

    private static final Preferences USER_PREFERENCES = Preferences.userNodeForPackage(ConfigurationPreferences.class);



    static void addPreferenceChangeListener(Configuration.Builder configuration) {
        USER_PREFERENCES.addPreferenceChangeListener(evt -> readDefaults(configuration));
    }


    static void storeDefaults(Configuration configuration) {
        for (ConfigurationAspect aspect : configuration) {
            Preferences node = node(aspect);
            for (Method m : aspect.getClass().getDeclaredMethods()) {
                if (m.getName().startsWith("get") && m.getParameterTypes().length == 0) {
                    try {
                        String name = m.getName().substring(3);
                        Class<?> returnType = m.getReturnType();
                        Object value = m.invoke(aspect);
                        put(node, name, returnType, value);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.warning(e.getMessage());
                    }
                }
            }
            log.info(() -> "Stored " + USER_PREFERENCES);
        }
    }

     static void readDefaults(Configuration.Builder configuration) {
         for (ConfigurationAspect aspect : configuration.build()) {
             ConfigurationAspect as = aspect;
             Preferences node = node(as);
             for (Method m : aspect.getClass().getDeclaredMethods()) {
                 if (m.getName().startsWith("with") && m.getParameterTypes().length == 1) {
                     try {
                         String name = m.getName().substring(4);
                         Object currentValue = as.getClass().getDeclaredMethod("get" + name).invoke(as);
                         Object newValue = get(node, name,
                             m.getParameters()[0].getType(),
                             currentValue);

                         as = (ConfigurationAspect) m.invoke(as, newValue);
                     } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException | NoSuchMethodException e) {
                         log.log(Level.WARNING, e.getClass() + " " + e.getMessage(), e);
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

    static void put(Preferences pref, String key, Class<?> type, final Object param) {
        if (param == null) {
            pref.remove(key);
        } else if (param instanceof Integer) {
            pref.putInt(key, (Integer) param);
        } else if (param instanceof Long) {
            pref.putLong(key, (Long) param);
        } else if (param instanceof Boolean) {
            pref.putBoolean(key, (Boolean) param);
        } else if (param instanceof Float) {
            pref.putFloat(key, (Float) param);
        } else if (param instanceof Double) {
            pref.putDouble(key, (Double) param);
        } else {
            ServiceLoader<ToStringProvider> loader = ServiceLoader.load(ToStringProvider.class);
            Optional<String> o  =loader.stream()
                .sorted()
                .map(tp ->
                    tp.get().toString(param).orElse(null)
                ).findFirst();
            if (o.isPresent()) {
                pref.put(key, o.get());
            } else {
                if (!putSerializable(pref, key, param)) {
                    throw new IllegalStateException("Don't know how to put " + param);
                }
            }
        }
    }

    static boolean putSerializable(Preferences pref, String key, Object param) {
        try (
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);) {
            so.writeObject(param);
            pref.putByteArray(key, bo.toByteArray());
            return true;
        } catch (IOException e) {
            log.log(Level.WARNING, e.getMessage(), e);
            return false;
        }
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
        } else if (CharSequence.class.isAssignableFrom(type)) {
            return (C) pref.get(key, defaultValue.toString());
        } else if (type.isEnum()) {
            Class<Enum> enumClass = (Class<Enum>) type;
            String stringValue = pref.get(key, ((Enum) defaultValue).name());
            return (C) Enum.valueOf(enumClass, stringValue);
        } else if (Serializable.class.isAssignableFrom(type)) {
            byte[] bytes = pref.getByteArray(key, new byte[0]);
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

        throw new IllegalStateException("Cannot match " + type);
     }
}
