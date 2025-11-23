import org.meeuw.math.windowed.*;
@SuppressWarnings("resource")
void main() {
    WindowedStatisticalLong windowed = WindowedStatisticalLong
        .builder()
        .build();

    windowed.accept(100, 101, 102);
    IO.println(windowed.get()); // prints 101.0 ± 0.8
}

