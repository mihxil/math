package org.meeuw.test.math.abstractalgebra.vectorspace;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.abstractalgebra.vectorspace.NVectorSpace;
import org.meeuw.math.exceptions.NotStreamable;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NVectorSpaceTest {

    @Test
    public void nonstreamable() {
        NVectorSpace<RealNumber> realSpace = NVectorSpace.of(3, RealField.INSTANCE);
        assertThatThrownBy(realSpace::stream).isInstanceOf(NotStreamable.class);
    }
}
