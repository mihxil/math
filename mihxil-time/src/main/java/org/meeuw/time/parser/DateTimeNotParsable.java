package org.meeuw.time.parser;

import lombok.Getter;

import org.meeuw.math.exceptions.NotParsable;

@Getter
public class DateTimeNotParsable extends NotParsable {
    private final ParseException parseException;

    public DateTimeNotParsable(String message, ParseException parseException, String value) {
        super(message + ":\t" + value, parseException.getCause(), value);
        this.parseException = parseException;
    }


}
