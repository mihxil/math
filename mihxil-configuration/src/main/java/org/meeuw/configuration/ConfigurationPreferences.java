package org.meeuw.configuration;

import java.io.*;
import java.lang.reflect.*;
import java.util.Optional;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static java.lang.System.Logger.Level.*;


/**
 * This ia a wrapper around {@link Preferences}
 */
class ConfigurationPreferences {
    private static final System.Logger log = System.getLogger(ConfigurationPreferences.class.getName());

    private static final Preferences USER_PREFERENCES = createUserPreferences();

    static Preferences getUserPreferences() {
        return USER_PREFERENCES;
    }

    private static synchronized Preferences createUserPreferences() {
        log.log(DEBUG, "Creating user preferences");
        Preferences userPreferences = null;
        try {
            userPreferences = Preferences.userNodeForPackage(ConfigurationPreferences.class);
        } catch (Exception e) {
            log.log(WARNING, () -> "fooar:" +  e.getClass().getName() + ":" + e.getMessage(), e);
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
                if (m.getParameterTypes().length == 0 && !Modifier.isStatic(m.getModifiers())) {
                    String name;
                    if (m.getName().length() > 3 && m.getName().startsWith("get")) {
                        name = m.getName().substring(3);
                    } else if (m.getName().startsWith("is") && m.getReturnType().equals(Boolean.TYPE)) {
                        name = m.getName().substring(2);
                    } else {
                        name = null;
                    }
                    if (name != null) {
                        try {
                            Object value = m.invoke(aspect);
                            put(node, name, value);
                        } catch (IllegalAccessException | InvocationTargetException | IOException |
                                 IllegalStateException e) {
                            log.log(WARNING, String.format("%s for %s (%s): %s", m.getDeclaringClass(), m, aspect, e.getMessage()));
                        }
                    }
                }
            }
            log.log(DEBUG, () -> "Stored " + USER_PREFERENCES);
        }
    }

     static void readDefaults(Configuration.Builder configuration) {
         for (ConfigurationAspect aspect : configuration.build()) {
             ConfigurationAspect as = aspect;
             Preferences node = node(as);
             for (Method m : aspect.getClass().getDeclaredMethods()) {
                 if (m.getName().startsWith("with") && m.getParameterTypes().length == 1) {
                     final String name =  m.getName().substring(4);
                     final String methodName;
                     if (m.getParameterTypes()[0] == Boolean.TYPE) {
                         methodName = "is" + name;
                     } else {
                         methodName = "get" + name;
                     }
                     Object newValue = null;
                     try {
                         Object currentValue = as.getClass().getDeclaredMethod(methodName).invoke(as);
                         newValue = get(node, name,
                             m.getParameters()[0].getType(),
                             currentValue);

                         as = (ConfigurationAspect) m.invoke(as, newValue);
                     } catch (NoSuchMethodException e) {
                         log.log(INFO, "No method " + methodName + " + for wither " + m + ". Ignored.");
                     } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                         log.log(WARNING, "For " + as + "#" + m + "(" + (newValue == null ? "NULL" : (newValue.getClass() + " " + newValue)) + ")" + e.getClass() + " " + e.getMessage(), e);
                     }
                 }
             }
             configuration.aspectValue(as);
         }

         log.log(INFO, () -> "Read " + USER_PREFERENCES);
    }

    private static Preferences node(ConfigurationAspect aspect) {
        return USER_PREFERENCES.node(aspect.getClass().getCanonicalName());
    }

    static void put(final Preferences pref, final String key, final Object paramValue) throws IOException {
        if (paramValue == null) {
            pref.remove(key);
        } else {
            Optional<String> o = StringConversionService.toString(paramValue);
            if (o.isPresent()) {
                pref.put(key, o.get());
            } else if (paramValue instanceof Serializable) {
                putSerializable(pref, key, (Serializable) paramValue);
            } else {
                throw new IllegalStateException("Don't know how to put " + paramValue);
            }
        }
    }

    static <C> C get(Preferences pref, String key, Class<? extends C> type, C defaultValue) {

         String v = pref.get(key, StringConversionService.toString(defaultValue).orElse(null));
         Optional<C> o = StringConversionService.fromString(v, type);

         return
             o.orElseGet(() ->
                 getSerializable(pref, key, defaultValue)
             );
     }


    static void putSerializable(Preferences pref, String key, Serializable paramValue) throws IOException {
        try (
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo)) {
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
                log.log(WARNING, "For byte array with length " + bytes.length + ":" + e.getClass().getName() + ": " + e.getMessage() + ", defaulting to "+ defaultValue, e);
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }
}
