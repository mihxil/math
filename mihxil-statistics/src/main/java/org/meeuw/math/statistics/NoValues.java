package org.meeuw.math.statistics;

import org.meeuw.math.exceptions.DivisionByZeroException;

public class NoValues extends DivisionByZeroException {
    public NoValues(String s, String operationString) {
        super(s, operationString);
    }
}
