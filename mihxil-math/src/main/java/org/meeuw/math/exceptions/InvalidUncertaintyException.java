package org.meeuw.math.exceptions;

/**
 * Uncertainties are e.g. non negative. This exception gets thrown otherwise.
 * @author Michiel Meeuwissen
 */
public class InvalidUncertaintyException extends InvalidElementCreationException {
    public InvalidUncertaintyException(String s) {
        super(s);
    }
}
