package org.meeuw.math.demo;

import lombok.extern.java.Log;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.LogManager;


public class DemoUtils {

    public static void setupLogging(String level) {

        try (InputStream is = Application.class.getClassLoader().
            getResourceAsStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(is);

          /*  LogManager.getLogManager().updateConfiguration((a) -> {
                if (".level".equals(a)) {
                    return (x, y) -> level;
                } else {
                    return (x, y) -> y;
                }
            });*/
        } catch (IOException e) {

          }
    }

}
