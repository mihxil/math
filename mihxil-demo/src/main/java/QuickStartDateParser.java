import org.meeuw.time.parser.DynamicDateTime;
void main() {
    DynamicDateTime dynamicDateTime = new DynamicDateTime();
    IO.println(
        // Prints the date 5 days from now
        dynamicDateTime.apply("today + 5 days").toLocalDate()
    );
}

