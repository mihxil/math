package org.meeuw.math.shapes;

import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.circlegroup.CircleGroup;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;

public class TurnsGroup extends CircleGroup<RationalNumber, BigDecimalElement> {
    static TurnsGroup INSTANCE = new TurnsGroup();

    private TurnsGroup() {
        super(RationalNumbers.INSTANCE, RationalNumber.ZERO, RationalNumber.ONE);
    }
}
