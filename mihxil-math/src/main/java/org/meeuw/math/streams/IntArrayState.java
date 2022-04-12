/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.streams;

/**
 * Helper class for {@link StreamUtils#allIntArrayStream(int)}
 */
class IntArrayState {
    final int degree;
    final int[] counters;
    int max = 0;
    int[] next;

    IntArrayState(int degree) {
        this.degree = degree;
        counters = new int[degree];
        makeNext();
    }

    int[] array() {
        return next;
    }

    void makeNext() {
        this.next = new int[degree];
        int offset = max / 2;
        for (int i = 0; i < degree; i++) {
            this.next[i] = counters[i] - offset;
        }
    }

    public IntArrayState next() {
        max = StreamUtils.inc(counters, max);
        makeNext();
        if (max() < max) {
            return next();
        }
        return this;
    }

    private int max() {
        int m = 0;
        for (int c : next) {
            int abs = Math.abs(c);
            if (abs > m) {
                m = abs;
            }
        }
        return m * 2;
    }
}
