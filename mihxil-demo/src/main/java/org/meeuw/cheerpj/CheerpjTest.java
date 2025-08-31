package org.meeuw.cheerpj;

import org.meeuw.math.abstractalgebra.klein.KleinGroup;

public class CheerpjTest {


    public static String getSupportedOperators() {
        return KleinGroup.INSTANCE.getSupportedOperators().toString();
    }
}
