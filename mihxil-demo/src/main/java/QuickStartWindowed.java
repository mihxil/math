import module org.meeuw.math.statistics;

void main() {
    var windowed = WindowedStatisticalLong
        .builder()
        .bucketDuration(Duration.ofMinutes(5))
        .build();

    windowed.accept(100, 101, 102);

    IO.println(windowed.get()); // prints 101.0 ± 0.8

}

