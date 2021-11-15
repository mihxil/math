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
