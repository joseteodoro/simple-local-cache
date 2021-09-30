package org.jteodoro.simplelocalcache;

import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.HashMap;

public class CachingService<T> {

    private static final long DEFAULT_MAX_ITEMS = 10000;

    private static final long DEFAULT_TTL = 300;
    
    private final Map<String, Box<T>> entries;
    
    private final PriorityQueue<Box<T>> purger;

    private final long ttlInSeconds;

    private final long maxItems;

    private CachingService(PurgeType type, long ttlInSeconds, long maxItems) {
        this.entries = new HashMap<>();
        this.purger = new PriorityQueue<>(PurgerStrategy.<T>of(type));
        this.ttlInSeconds = ttlInSeconds;
        this.maxItems = maxItems;
    }

    public static <T> CachingService<T> of(long ttlInSeconds, long maxItems, PurgeType type) {
        return new CachingService<>(type, ttlInSeconds, maxItems);
    }
    
    public static <T> CachingService<T> of(long ttlInSeconds, PurgeType type) {
        return of(ttlInSeconds, DEFAULT_MAX_ITEMS, type);
    }

    public static <T> CachingService<T> of(PurgeType type) {
        return of(DEFAULT_TTL, DEFAULT_MAX_ITEMS, type);
    }

    public synchronized void add(String key, T value) {
        if (shouldReleaseSpace()) {
            this.compact();
        }
        Box<T> container = Box.of(key, value);
        this.entries.put(key, container);
        this.purger.add(container);
    }

    public boolean hasKey(String key) {
        return Objects.nonNull(this.entries.get(key)) &&
            Objects.nonNull(this.entries.get(key).getValue());
    }

    public T get(String key) {
        return Objects.nonNull(this.entries.get(key))
            ? this.entries.get(key).use()
            : null;
    }

    private void remove(Box<T> box) {
        this.purger.remove(box);
        this.entries.remove(box.getKey());
    }

    private void removeExpired() {
        long now = CacheUtils.toEpoch();
        boolean keepRemoving = false;
        do {
            Box<T> item = this.purger.peek();
            keepRemoving = Objects.nonNull(item) && 
                (Objects.isNull(item.getValue()) || item.getCreatedAt() < now - this.ttlInSeconds);

            if (keepRemoving) {
                this.remove(item);
            }
        }
        while (keepRemoving);
    }

    private boolean shouldReleaseSpace() {
        return this.entries.size() >= this.maxItems;
    }

    private void releaseSpace() {
        if (shouldReleaseSpace()) {
            Box<T> item = this.purger.peek();
            this.remove(item);
        }
    }

    public synchronized void compact() {
        removeExpired();
        releaseSpace();
    }

    public int size() {
        this.compact();
        return this.entries.size();
    }

    public long getMaxItems() {
        return maxItems;
    }

    public long getTtlInSeconds() {
        return ttlInSeconds;
    }

    public void evict(String key) {
        if (this.hasKey(key)) {
            this.entries.get(key).evict();
        }
    }

}
