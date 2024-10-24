package org.meeuw.math.exceptions;

public class OverflowException extends OperationException {
    public OverflowException(String s, String operationString) {
        super(s, operationString);
    }
}
