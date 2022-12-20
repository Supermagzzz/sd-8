package statistics;

import clock.Clock;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class EventsStatisticCounter implements EventsStatistic {
    private final Deque<TimeWithName> deque = new ArrayDeque<>();
    private final HashMap<String, Integer> counter = new HashMap<>();
    private final Clock clock;

    public EventsStatisticCounter(Clock clock) {
        this.clock = clock;
    }

    public void incEvent(String name) {
        clearOldEvents();
        deque.addLast(new TimeWithName(clock.now(), name));
        counter.put(name, counter.getOrDefault(name, 0) + 1);
    }

    public int getEventStatisticByName(String name) {
        clearOldEvents();
        return counter.getOrDefault(name, 0);
    }

    public HashMap<String, Integer> getAllEventStatistic() {
        clearOldEvents();
        return counter;
    }

    public void printStatistic() {
        clearOldEvents();
        for (Map.Entry<String, Integer> entry : counter.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() / 60.0 + " [rpm]");
        }
    }

    private void clearOldEvents() {
        long SECONDS_IN_HOUR = 60 * 60;
        while (!deque.isEmpty() && deque.peekFirst().time.compareTo(clock.now().minusSeconds(SECONDS_IN_HOUR)) <= 0) {
            TimeWithName info = deque.pollFirst();
            assert info != null;
            counter.put(info.name, counter.get(info.name) - 1);
            if (counter.get(info.name) == 0) {
                counter.remove(info.name);
            }
        }
    }

    record TimeWithName(Instant time, String name) {}
}
