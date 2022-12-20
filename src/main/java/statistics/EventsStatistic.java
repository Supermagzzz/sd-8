package statistics;

import java.util.HashMap;

public interface EventsStatistic {
    void incEvent(String name);
    int getEventStatisticByName(String name);
    HashMap<String, Integer> getAllEventStatistic();
    void printStatistic();
}
