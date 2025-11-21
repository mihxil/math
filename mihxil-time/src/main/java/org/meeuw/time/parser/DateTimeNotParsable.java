package org.meeuw.time.parser;

import lombok.Getter;

import org.meeuw.math.exceptions.NotParsable;

@Getter
public class DateTimeNotParsable extends NotParsable {
    private final ParseException parseException;

    public DateTimeNotParsable(String message, ParseException parseException) {
        super(message, parseException.getCause());
        this.parseException = parseException;
    }


}
