import org.meeuw.math.windowed.WindowedStatisticalLong;

void main() {
    // a sliding window of 5 minutes
    var windowed = WindowedStatisticalLong
        .builder()
        .window(Duration.ofMinutes(5))
        .bucketCount(10) // divided in 10 buckets
        .build();

    windowed.accept(100, 101, 102);
    IO.println(windowed.get()); // prints 101.0 ± 0.8
}
