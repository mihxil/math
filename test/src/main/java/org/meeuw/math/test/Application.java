package org.meeuw.math.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.logging.*;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.statistics.StatisticalLong;
import org.meeuw.math.windowed.WindowedEventRate;
import org.meeuw.math.windowed.WindowedStatisticalLong;


public class Application {

    static final Logger log = Logger.getLogger(Application.class.getName());
    static {
      // must set before the Logger
      // loads logging.properties from the classpath
          try (InputStream is = Application.class.getClassLoader().
              getResourceAsStream("logging.properties")) {
              LogManager.getLogManager().readConfiguration(is);
          } catch (IOException e) {
              log.log(Level.WARNING, e.getMessage(), e);
          }
      }

    public static void main(String[] arg) throws InterruptedException {
        if (arg.length > 0 && "prefs".equals(arg[0])) {
            ConfigurationService.setupUserPreferences();
        } else {
            log.info("No user preferences");
        }
        WindowedEventRate rate = WindowedEventRate.builder().build();
        StatisticalLong statisticalLong = new StatisticalLong();
        WindowedStatisticalLong windowedStatisticalLong = WindowedStatisticalLong
            .builder().build();
        Random random = new Random();

        while(true) {
            long randomLong = (long) random.nextGaussian(1000, 500);
            Thread.sleep(Math.abs(randomLong));
            rate.newEvent();
            statisticalLong.enter(randomLong);
            windowedStatisticalLong.accept(randomLong);
            log.info("rate: %s".formatted(rate));
            log.info("long: %s".formatted(statisticalLong));
            log.info("windowed long: %s".formatted(windowedStatisticalLong.getWindowValue()));
        }

    }
}
