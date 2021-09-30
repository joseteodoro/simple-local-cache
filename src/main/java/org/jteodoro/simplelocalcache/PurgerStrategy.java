package org.jteodoro.simplelocalcache;

import java.util.Comparator;
import java.util.Objects;

public class PurgerStrategy {

    private PurgerStrategy () {}

    private static final int FACTOR = 10;

    private static <T> int compareLastUsage(Box<T> l, Box<T> r) {
        return (int) (r.getLastUsage() - l.getLastUsage());
    }

    private static <T> int compareCommonUsage(Box<T> l, Box<T> r) {
        return (int) (l.getUsageCount() - r.getUsageCount());
    }

    public static <T> Comparator<Box<T>> of(PurgeType type) {
        Comparator<Box<T>> lru = (l, r) -> {
            if (Objects.isNull(l.getValue())) return -1;
            if (Objects.isNull(r.getValue())) return 1;
            return compareLastUsage(l, r);
        };
        Comparator<Box<T>> lcu = (l, r) -> {
            if (Objects.isNull(l.getValue())) return -1;
            if (Objects.isNull(r.getValue())) return 1;
            return compareCommonUsage(l, r);
        };
        Comparator<Box<T>> lruLcu = (l, r) -> {
            if (Objects.isNull(l.getValue())) return -1;
            if (Objects.isNull(r.getValue())) return 1;
            return (compareCommonUsage(l, r) * FACTOR) + compareLastUsage(l, r);
        };

        if (PurgeType.LCU.equals(type)) return lcu;
        if (PurgeType.LRU_LCU.equals(type)) return lruLcu;
        return lru;
    }
    
}
