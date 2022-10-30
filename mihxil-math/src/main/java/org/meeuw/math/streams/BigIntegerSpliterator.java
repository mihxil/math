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
package org.meeuw.math.streams;

import java.math.BigInteger;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static java.math.BigInteger.ONE;

/**
 * A {@link Spliterator} on all {@link BigInteger}s.
 *
 * @author Michiel Meeuwissen
 */
public class BigIntegerSpliterator implements Spliterator<BigInteger> {
    private BigInteger current;
    private boolean negatives;
    private BigInteger step;
    private boolean acceptNegative;
    private final BigInteger stepSignum;
    private final AtomicInteger thread;

    @lombok.Builder
    private BigIntegerSpliterator(BigInteger start, boolean includeNegatives, BigInteger step, AtomicInteger thread) {
        this.current = start;
        this.negatives = includeNegatives;
        this.step = step == null ? ONE : step;
        this.stepSignum = BigInteger.valueOf(this.step.signum());
        this.acceptNegative = false;
        this.thread = thread == null ? new AtomicInteger(1) : thread;
    }

    @lombok.Builder
    public BigIntegerSpliterator(BigInteger start, boolean includeNegatives, BigInteger step) {
        this(start, includeNegatives, step, null);
    }

    protected BigIntegerSpliterator copy() {
        BigIntegerSpliterator c = new BigIntegerSpliterator(current, negatives, step, thread);
        c.acceptNegative = acceptNegative;
        return c;
    }

    protected void accept(Consumer<? super BigInteger> action) {
        if (acceptNegative) {
            action.accept(current.negate().multiply(stepSignum));
        } else {
            action.accept(current.multiply(stepSignum));
        }
    }

    protected void advance() {
        if (negatives) {
            if (current.intValue() > 0 && !acceptNegative) {
                acceptNegative = true;
            } else {
                acceptNegative = false;
                current = current.add(step);
            }
        } else {
            current = current.add(step);
        }
    }

    @Override
    public boolean tryAdvance(Consumer<? super BigInteger> action) {
        accept(action);
        advance();
        return true;
    }

    @Override
    public BigIntegerSpliterator trySplit() {
        if (thread.get() >= StreamUtils.getMaxThreads()) {
            return null;
        }
        thread.incrementAndGet();
        if (negatives) {
            negatives = false;
            BigIntegerSpliterator otherStream = copy();
            otherStream.acceptNegative = true;
            if (current.intValue() == 0) {
                otherStream.advance();
            }
            return otherStream;
        } else {
            BigInteger prevStep = step;
            step = step.multiply(BigInteger.valueOf(2));
            BigIntegerSpliterator otherStream = copy();
            otherStream.current = otherStream.current.add(prevStep);
            return otherStream;
        }
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
        return DISTINCT | NONNULL | IMMUTABLE;
    }

}
