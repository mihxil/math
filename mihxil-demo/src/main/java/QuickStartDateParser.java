import org.meeuw.time.parser.DynamicDateTime;
void main() {
    DynamicDateTime dynamicDateTime = new DynamicDateTime();
    System.out.println("" + dynamicDateTime.apply("today + 5 days").toLocalDate()); // Prints the date 5 days from now
}

