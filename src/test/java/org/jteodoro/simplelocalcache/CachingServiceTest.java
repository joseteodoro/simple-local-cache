package org.jteodoro.simplelocalcache;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CachingServiceTest {

    @Test
    public void whenEvictShouldRemoveItemAfterCompact() {
        CachingService<Integer> service = CachingService.of(10, 1000, PurgeType.LRU);
        Integer item = 100;
        service.add(String.valueOf(item), item);
        assertTrue(service.hasKey(String.valueOf(item)));
        service.evict(String.valueOf(item));
        service.compact();
        assertFalse(service.hasKey(String.valueOf(item)));
    }

    @Test
    public void afterTimeoutShouldCompactAndRemovedExpiredOnes() throws InterruptedException {
        CachingService<Integer> service = CachingService.of(1, 10, PurgeType.LRU);
        Integer x = 100;
        service.add(String.valueOf(x), x);
        assertNotNull(service.get(String.valueOf(x)));

        Integer y = 1000;
        service.add(String.valueOf(y), y);
        assertNotNull(service.get(String.valueOf(y)));

        Thread.sleep(2000);

        service.compact();

        Integer z = 10;
        service.add(String.valueOf(z), z);
        assertNotNull(service.get(String.valueOf(z)));
        assertNull(service.get(String.valueOf(x)));
        assertNull(service.get(String.valueOf(y)));
    }
    
    @Test
    public void afterLimitShouldCallCompactOnInsert() throws InterruptedException {
        CachingService<Integer> service = CachingService.of(1, 1, PurgeType.LRU);
        Integer x = 100;
        service.add(String.valueOf(x), x);
        assertNotNull(service.get(String.valueOf(x)));

        Integer y = 1000;
        service.add(String.valueOf(y), y);
        assertNotNull(service.get(String.valueOf(y)));
        assertNull(service.get(String.valueOf(x)));

        Thread.sleep(1500);

        Integer z = 10;
        service.add(String.valueOf(z), z);
        assertNotNull(service.get(String.valueOf(z)));
        assertNull(service.get(String.valueOf(x)));
        assertNull(service.get(String.valueOf(y)));
    }
}
