package org.meeuw.math.demo;

import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.LogManager;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.abstractalgebra.complex.GaussianRational;
import org.meeuw.math.abstractalgebra.permutations.PermutationGroup;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.statistics.StatisticalLong;
import org.meeuw.math.windowed.WindowedEventRate;
import org.meeuw.math.windowed.WindowedStatisticalLong;
import org.meeuw.physics.*;

import static org.meeuw.math.DigitUtils.fromDigits;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.SQR;


@Log
public class Application {

    public static void setupLogging() {
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
        System.out.println("Starting application with args: " + String.join(", ", arg));
        System.out.println("===\npermutation===");
        permutation(arg);
        System.out.println("===\ncomplex===");
        complex(arg);
        System.out.println("===\nmeasure===");
        measure(arg);
    }

    public static void permutation(String [] arg) {

        final int[] digits = new int[] {1, 2, 3, 4, 5};
        PermutationGroup permutationGroup = PermutationGroup.ofDegree(5);
        log.info(() -> "sum %s".formatted(
            permutationGroup.stream()
                .map(p -> fromDigits(p.permuteInts(digits)))
                .reduce(0L, Long::sum))
        );
    }

      public static void complex(String [] arg) {

          GaussianRational complex1 = GaussianRational.of(RationalNumber.of(1, 2), RationalNumber.of(3, 4));

          GaussianRational square = complex1.sqr();
          log.info(SQR.stringify(complex1) + "=" + square);
    }

    public static void measure(String[] arg) throws InterruptedException {

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

        Units units = SIUnit.s;
        if (arg.length > 1) {
            units = SI.INSTANCE.unitsOf(arg[1]);
        }
        Measurement m = Measurement.measurement(windowedStatisticalLong, units);


        while(true) {
            long randomLong = (long) random.nextGaussian(1000, 500);
            Thread.sleep(Math.abs(randomLong));
            rate.newEvent();
            statisticalLong.enter(randomLong);
            windowedStatisticalLong.accept(randomLong);
            log.info("rate: %s".formatted(  rate));
            log.info("long: %s".formatted(m.toUnits(SI.a)));
            log.info("windowed long: %s".formatted(windowedStatisticalLong.getWindowValue()));
        }

    }
}
