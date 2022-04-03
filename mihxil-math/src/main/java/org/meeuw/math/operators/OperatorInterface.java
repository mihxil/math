package org.meeuw.math.operators;

import java.util.Comparator;

public interface OperatorInterface {

    Comparator<OperatorInterface> COMPARATOR = Comparator
        .comparing(OperatorInterface::ordinal)
        .thenComparing(OperatorInterface::name);

    String name();

    default int ordinal() {
        return Integer.MAX_VALUE;
    }
}
