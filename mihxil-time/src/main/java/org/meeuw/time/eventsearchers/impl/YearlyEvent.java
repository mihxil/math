package org.meeuw.time.eventsearchers.impl;

import java.time.LocalDate;
import java.time.Year;
import java.util.function.Function;

/*
 * @author Michiel Meeuwissen
 * @since 0.19
 */
public interface YearlyEvent extends Function<Year, LocalDate>, Event {


}
