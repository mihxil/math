/*
 *  Copyright 2022 Michiel Meeuwissen
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
package org.meeuw.math.statistics;

import lombok.Getter;

import java.util.LongSummaryStatistics;

/**
 * A 'statistical' number can receive a number of values, and can calculate the average (the value of this {@link Number} implementation) and standard deviation of those values.
 *
 * @author Michiel Meeuwissen
 * @param <N> the type of the numbers to aggregate
 */
public abstract class AbstractStatisticalNumber<
    SELF extends AbstractStatisticalNumber<SELF, N, E>,
    N extends Number,
    E
    >
    implements StatisticalNumber<SELF, N, E> {

    /**
     * The total number of values which were {@link StatisticalDoubleImpl#enter(double...)}ed.
     */
    @Getter
    protected int count = 0;

    public AbstractStatisticalNumber() {
    }
    protected AbstractStatisticalNumber(int count) {
        this.count = count;
    }

    public abstract SELF copy();

    /**
     * Enters all values of another instance of this {@link AbstractStatisticalNumber}, effectively combining the given one into this one.
     * @param m another statistical number
     * @return this
     */
    public abstract SELF enter(SELF m);


    /**
     * Synonymous to {@link #enter(AbstractStatisticalNumber)} (except the return value). Does the same though as e.g. {@link LongSummaryStatistics#combine(LongSummaryStatistics)}.
     * @param m another statistical number
     * @see #enter(AbstractStatisticalNumber)
     */
    @Override
    public void combine(SELF m) {
        enter(m);
    }

    public SELF combined(SELF m) {
        SELF copy = copy();
        return copy.enter(m);
    }

    /**
     * Resets all counters and statistics. {@link #getCount()} will return {@code 0}
     */
    public void reset() {
        count = 0;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}



