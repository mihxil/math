package org.meeuw.math;

import org.junit.jupiter.api.TestInstance;

import org.meeuw.util.test.StaticUtilitiesTest;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

/**
 * @author Michiel Meeuwissen
 */
@TestInstance(PER_CLASS)
class UtilsTest implements StaticUtilitiesTest {


    @Override
    public Class<?> testClass() {
        return Utils.class;
    }
}
