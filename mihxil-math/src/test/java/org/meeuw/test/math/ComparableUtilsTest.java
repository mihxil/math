package org.meeuw.test.math;

import org.junit.jupiter.api.Test;

import org.meeuw.math.ComparableUtils;

class ComparableUtilsTest {


    @Test
    void max() {
        assert ComparableUtils.max(1, 2, 3) == 3;
        assert ComparableUtils.max(3, 2, 1) == 3;
        assert ComparableUtils.max(1, 1, 1) == 1;
        assert ComparableUtils.max(1) == 1;
    }

    @Test
    void min() {
        assert ComparableUtils.min(1, 2, 3) == 1;
        assert ComparableUtils.min(3, 2, 1) == 1;
        assert ComparableUtils.min(1, 1, 1) == 1;
        assert ComparableUtils.min(1) == 1;
    }
}
