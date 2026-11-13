/*
 *  Copyright 2026 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.windowed;

import java.util.Map;

/**
 * JMX management interface for a {@link KeyedWindowedRateLimiter}. An implementation can be obtained via {@link KeyedWindowedRateLimiter#getMXBean()},
 * which can then be registered at an {@code MBeanServer}, e.g.:
 * <pre>{@code
 *  ManagementFactory.getPlatformMBeanServer().registerMBean(
 *      limiter.getMXBean(), new ObjectName("org.example:type=RateLimiter,name=api"));
 * }</pre>
 *
 * @author Michiel Meeuwissen
 * @since 0.23
 */
public interface KeyedWindowedRateLimiterMXBean {

    /**
     * @return the window, as an ISO-8601 duration
     */
    String getWindow();

    /**
     * Changes the window. Events in the current window remain accounted for.
     * @param window the new window. Leniently parsed, so e.g. {@code 1h} is accepted too (see {@link org.meeuw.time.TimeUtils#parseDuration(CharSequence)})
     */
    void setWindow(String window);

    long getAllowedPerWindow();

    /**
     * Changes the number of allowed events per window. Events in the current window remain accounted for.
     * @param allowedPerWindow the new value, must be positive
     */
    void setAllowedPerWindow(long allowedPerWindow);

    /**
     * @return per key (as a string) the number of events in the current window
     */
    Map<String, Long> getCounts();

    /**
     * Forgets about all events, so all keys can start afresh.
     */
    void clear();
}
