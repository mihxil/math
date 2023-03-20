/*
 * Copyright (C) 2012 All rights reserved
 * VPRO The Netherlands
 */

package org.meeuw.math.text;


import org.junit.jupiter.api.TestInstance;

import org.meeuw.theories.StaticUtilitiesTest;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

/**
 *
 */
@TestInstance(PER_CLASS)
public class TextUtilsTest implements StaticUtilitiesTest {


    @Override
    public Class<?> testClass() {
        return TextUtils.class;
    }
}

