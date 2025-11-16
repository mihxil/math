package org.meeuw.time.eventsearchers;

import java.time.*;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.*;

import org.meeuw.time.Range;

@SuppressWarnings("rawtypes")
public class EventSearcherService implements EventSearcher<Temporal> {

    public static final EventSearcherService INSTANCE = new EventSearcherService();

    private final Map<Class<? extends EventSearcher>, EventSearcher> eventSearchers = new HashMap<>();


    public ServiceLoader<? extends EventSearcher> loader;

    private EventSearcherService() {
        loader = ServiceLoader.load(EventSearcher.class);
    }


    @SuppressWarnings("unchecked")
    @Override
    public Stream<Temporal> findEvents(Range<Year> range, String description) {
        Spliterator<? extends EventSearcher> splitIterator = Spliterators.spliteratorUnknownSize(loader.iterator(), Spliterator.ORDERED);

        return StreamSupport.stream(splitIterator, false)
            .flatMap(searcher -> searcher.findEvents(range, description))
            ;
    }


    public Stream<Instant> findEvents(Range<Year> range, ZoneId timeZone, String eventSummary) {
        return findEvents(range, eventSummary)
            .map(t -> {
                return toInstant(t, timeZone);
            }).filter(Optional::isPresent)
            .map(Optional::get);
    }


    public Stream<Instant> findPreviousEvents(Instant instant, ZoneId timeZone, String eventSummary) {
        Range<Year> range = Range.untilYear(instant.atZone(timeZone).getYear());
        return findEvents(range, eventSummary)
            .map(t -> toInstant(t, timeZone))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .filter(i -> i.isBefore(instant));
    }

    public Stream<Instant> findNextEvents(Instant instant, ZoneId timeZone, String eventSummary) {
        Range<Year> range = Range.fromYear(instant.atZone(timeZone).getYear());
        return findEvents(range, eventSummary)
            .map(t -> toInstant(t, timeZone))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .filter(i -> i.isAfter(instant));
    }

    public Stream<ZonedDateTime> findPreviousEvents(ZonedDateTime time,  String eventSummary) {
       return findPreviousEvents(time.toInstant(), time.getZone(),eventSummary)
            .map(i -> i.atZone(time.getZone()));
    }

    public Stream<ZonedDateTime> findNextEvents(ZonedDateTime time,  String eventSummary) {
        return findNextEvents(time.toInstant(), time.getZone(),eventSummary)
            .map(i -> i.atZone(time.getZone()));

    }



    protected Optional<Instant> toInstant(Temporal date, ZoneId timeZone) {
        // this date is at the date of the holiday at 12 AM UTC
        if (date instanceof LocalDate) {
            return Optional.of(((LocalDate) date).atStartOfDay().atZone(timeZone).toInstant());
        } else if (date instanceof OffsetDateTime) {
            return Optional.of(((OffsetDateTime) date).toInstant());
        } else if (date instanceof Instant) {
            return Optional.of((Instant) date);
        } else if (date instanceof Date) {
            return Optional.of(((Date) date).toInstant());
        } else {
            return Optional.empty();
        }
    }


    public <T extends Temporal, E extends EventSearcher<T>> Stream<E> getEventSearchers(Class<E> eventSearcherClass) {
        Spliterator<? extends EventSearcher> splitIterator = Spliterators.spliteratorUnknownSize(loader.iterator(), Spliterator.ORDERED);

        return StreamSupport.stream(splitIterator, false)
            .filter(eventSearcherClass::isInstance)
            .map(eventSearcherClass::cast);
    }

    @SuppressWarnings("unchecked")
    public <T extends Temporal, E extends EventSearcher<T>> E getEventSearcher(Class<E> eventSearcherClass) {
        return (E) eventSearchers.computeIfAbsent(eventSearcherClass, cls -> {
            List<E> searchers = getEventSearchers(eventSearcherClass).toList();
            if (searchers.size() != 1) {
                throw new IllegalStateException("Expected exactly one event searcher of type " + cls.getName() + ", but found: " + searchers);
            }
            return searchers.get(0);
        });
    }

    public <T extends Temporal, E extends EventSearcher<T>> Stream<T> findEvents(Range<Year> range, ZoneId timeZone, String eventSummary, Class<E> eventSearcherClass) {
        return getEventSearchers(eventSearcherClass)
            .flatMap(searcher -> searcher.findEvents(range, eventSummary))
            .filter(Objects::nonNull);
    }
}
