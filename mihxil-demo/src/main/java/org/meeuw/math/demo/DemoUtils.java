package org.meeuw.math.demo;

import java.io.*;
import java.util.logging.*;


public class DemoUtils {

    public static void setupLogging(String level) {
        setupLogging(Level.parse(level));
    }
    public static void setupLogging(Level level) {


        try (InputStream is = Application.class.getClassLoader().
            getResourceAsStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(is);

            Logger rootLogger = Logger.getLogger("org.meeuw");
            rootLogger.setLevel(level);
        } catch (IOException e) {
            System.err.println(e.getMessage());
          }
    }

}
