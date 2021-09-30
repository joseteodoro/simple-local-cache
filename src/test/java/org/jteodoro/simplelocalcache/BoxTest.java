package org.jteodoro.simplelocalcache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BoxTest {

    @Test
    public void useShouldIncrementCounter() throws InterruptedException {
        Box<String> box = Box.of("key", "value");
        assertEquals(0, box.getUsageCount());
        Thread.sleep(1000);
        String result = box.use();
        assertEquals(1, box.getUsageCount());
        assertEquals("value", result);
        assertTrue(box.getCreatedAt() != box.getLastUsage());
    }
    
}
