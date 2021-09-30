package org.jteodoro.simplelocalcache;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class PurgerStrategyTest {

    @Test
    public void LNullShouldReturnR() {
        Box<String> l = Box.of("key0", null);
        Box<String> r = Box.of("key1", "value");
        List<Box<String>> list = Arrays.asList(l, r);
        Collections.sort(list, PurgerStrategy.<String>of(PurgeType.LCU));
        assertEquals("key0", list.get(0).getKey());
    }

    @Test
    public void RNullShouldReturnL() {
        Box<String> l = Box.of("key1", null);
        Box<String> r = Box.of("key0", "value");
        List<Box<String>> list = Arrays.asList(l, r);
        Collections.sort(list, PurgerStrategy.<String>of(PurgeType.LCU));
        assertEquals("key1", list.get(0).getKey());
    }

    @Test
    public void LRNullShouldReturnL() {
        Box<String> l = Box.of("key1", null);
        Box<String> r = Box.of("key0", null);
        List<Box<String>> list = Arrays.asList(l, r);
        Collections.sort(list, PurgerStrategy.<String>of(PurgeType.LCU));
        assertEquals("key0", list.get(0).getKey());
    }

    @Test
    public void whenUsingLCUAndWhenLmoreUsedShouldReturnR() {
        Box<String> l = Box.of("key0", "value0");
        l.use();
        l.use();
        l.use();
        Box<String> r = Box.of("key1", "value1");
        r.use();
        List<Box<String>> list = Arrays.asList(l, r);
        Collections.sort(list, PurgerStrategy.<String>of(PurgeType.LCU));
        assertEquals("key1", list.get(0).getKey());
    }
    
    @Test
    public void whenUsingLRUAndWhenLmoreUsedShouldReturnR() throws InterruptedException {
        Box<String> l = Box.of("key0", "value0");
        l.use();
        Box<String> r = Box.of("key1", "value1");
        r.use();
        Thread.sleep(1000);
        l.use();
        List<Box<String>> list = Arrays.asList(l, r);
        Collections.sort(list, PurgerStrategy.<String>of(PurgeType.LCU));
        assertEquals("key1", list.get(0).getKey());
    }

    @Test
    public void whenUsingLRU_LCUAndWhenLmoreUsedShouldReturnL() throws InterruptedException {
        Box<String> l = Box.of("key0", "value0");
        l.use();
        Box<String> r = Box.of("key1", "value1");
        r.use();
        r.use();
        r.use();
        Thread.sleep(1000);
        l.use();
        List<Box<String>> list = Arrays.asList(l, r);
        Collections.sort(list, PurgerStrategy.<String>of(PurgeType.LCU));
        assertEquals("key0", list.get(0).getKey());
    }

}
