import module org.meeuw.time;

void main() {
    var dynamicDateTime = new DynamicDateTime();
    IO.println(
        // Prints the date 5 days from now
        dynamicDateTime.apply("today + 5 days").toLocalDate()
    );
}

