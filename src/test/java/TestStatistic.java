import clock.SettableClock;
import org.junit.Before;
import org.junit.Test;
import statistics.EventsStatisticCounter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestStatistic {

    private static final int MINUTE = 60;
    private Instant time;
    private SettableClock clock;
    private EventsStatisticCounter counter;

    @Before
    public void before() {
        time = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
        clock = new SettableClock(time);
        counter = new EventsStatisticCounter(clock);
    }

    @Test
    public void testIncrement() {
        counter.incEvent("a");
        assertEquals(counter.getEventStatisticByName("a"), 1);
        counter.incEvent("a");
        assertEquals(counter.getEventStatisticByName("a"), 2);
    }

    @Test
    public void testIncrementAndPlusHour() {
        counter.incEvent("a");
        assertEquals(counter.getEventStatisticByName("a"), 1);
        time = time.plusSeconds(30 * MINUTE);
        clock.setNow(time);
        assertEquals(counter.getEventStatisticByName("a"), 1);
        counter.incEvent("a");
        assertEquals(counter.getEventStatisticByName("a"), 2);
        time = time.plusSeconds(40 * MINUTE);
        clock.setNow(time);
        assertEquals(counter.getEventStatisticByName("a"), 1);
        time = time.plusSeconds(60 * MINUTE);
        clock.setNow(time);
        assertEquals(counter.getEventStatisticByName("a"), 0);
    }

    @Test
    public void testAllStat() {
        counter.incEvent("a");
        counter.incEvent("a");
        counter.incEvent("a");
        counter.incEvent("b");
        counter.incEvent("b");
        assertEquals(counter.getAllEventStatistic(), Map.of("a", 3, "b", 2));
        time = time.plusSeconds(30 * MINUTE);
        clock.setNow(time);
        counter.incEvent("b");
        assertEquals(counter.getAllEventStatistic(), Map.of("a", 3, "b", 3));
        time = time.plusSeconds(40 * MINUTE);
        clock.setNow(time);
        assertEquals(counter.getAllEventStatistic(), Map.of("b", 1));
    }

    @Test
    public void testOneHour() {
        counter.incEvent("a");
        assertEquals(counter.getEventStatisticByName("a"), 1);
        time = time.plusSeconds(60 * MINUTE);
        clock.setNow(time);
        assertEquals(counter.getEventStatisticByName("a"), 0);
    }
}
