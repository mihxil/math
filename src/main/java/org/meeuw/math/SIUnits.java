package org.meeuw.math;

import lombok.Getter;

import static org.meeuw.math.Dimension.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public enum SIUnits {
    m(L),
    kg(M),
    s(T),
    A(I),
    K(TH),
    mol(N),
    cd(J);

    @Getter
    private final Dimension dimension;

    SIUnits(Dimension dimension) {
        this.dimension = dimension;
    }
}
