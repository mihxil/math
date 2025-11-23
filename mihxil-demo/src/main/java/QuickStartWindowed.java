import org.meeuw.math.windowed.*;
void main() {
    WindowedStatisticalLong windowed = WindowedStatisticalLong
        .builder()
        .build();

    windowed.accept(100, 101, 102);

    System.out.println("" + windowed.get()); // prints 101.0 ± 0.8
}

