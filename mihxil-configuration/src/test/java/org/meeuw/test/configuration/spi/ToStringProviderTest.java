package org.meeuw.test.configuration.spi;

import java.util.ServiceLoader;

import org.junit.jupiter.api.Test;

import org.meeuw.configuration.spi.ToStringProvider;

public class ToStringProviderTest {

    @Test
    public void testInt() {
        ServiceLoader.load(ToStringProvider.class);
    }
}
