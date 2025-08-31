package org.meeuw.math.demo;

public record ParseResult<V>(
    String input,
    V result,
    String error) {

    public boolean success() {
        return error == null;
    }
}
