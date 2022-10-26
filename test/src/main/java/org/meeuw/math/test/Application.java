package org.meeuw.math.test;

import org.meeuw.math.windowed.WindowedEventRate;

public class Application {


    public static void main(String[] arg) throws InterruptedException {
        WindowedEventRate rate = WindowedEventRate.builder().build();

        rate.newEvent();
        Thread.sleep(10);
        rate.newEvent();

        System.out.println(rate);
    }
}
