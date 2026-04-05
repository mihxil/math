package org.meeuw.math.demo;

public class MyComparable implements Comparable<MyComparable> {

    private final int value;

    public MyComparable(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(MyComparable o) {
        return Integer.compare(value, o.value);
    }
    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof MyComparable that)) return false;

        return value == that.value;
    }

    @Override
     public String toString() {
         return "MyComparable{" +
             "value=" + value +
             '}';
     }
}
