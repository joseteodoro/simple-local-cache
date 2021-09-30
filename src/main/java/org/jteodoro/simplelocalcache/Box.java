package org.jteodoro.simplelocalcache;

import java.lang.ref.SoftReference;
import java.util.UUID;

public class Box<T> {

    private final UUID uuid;

    private final long createdAt;

    private final String key;

    private final SoftReference<T> softReference;

    private long usageCount;

    private long lastUsage;
    
    private Box(String key, T value) {
        this.softReference = new SoftReference<>(value);
        this.uuid = UUID.randomUUID();
        this.createdAt = CacheUtils.toEpoch();
        this.usageCount = 0;
        this.lastUsage = this.createdAt;
        this.key = key;
    }

    public static <T> Box<T> of(String key, T value) {
        return new Box<>(key, value);
    }

    public synchronized T use() {
        this.lastUsage = CacheUtils.toEpoch();
        this.usageCount++;
        return this.softReference.get();
    }

    public T getValue() {
        return this.softReference.get();
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public String getKey() {
        return key;
    }

    public long getLastUsage() {
        return this.lastUsage;
    }

    public long getUsageCount() {
        return this.usageCount;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void evict() {
        this.softReference.clear();
    }

}
