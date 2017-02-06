package com.serotonin.bacnet4j.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Calendar;

import org.junit.Test;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.npdu.test.TestNetwork;
import com.serotonin.bacnet4j.obj.TestClock;
import com.serotonin.bacnet4j.transport.DefaultTransport;

public class RemoteEntityCacheTest {
    @Test
    public void test() {
        final TestClock clock = new TestClock();

        final LocalDevice d = new LocalDevice(0, new DefaultTransport(new TestNetwork(1, 10)));
        d.setClock(clock);

        final RemoteEntityCache<String, String> cache = new RemoteEntityCache<>(d);

        cache.putEntity("key1", "value1", RemoteEntityCachePolicy.NEVER_CACHE);
        cache.putEntity("key2", "value2", RemoteEntityCachePolicy.EXPIRE_5_SECONDS);
        cache.putEntity("key3", "value3", RemoteEntityCachePolicy.NEVER_EXPIRE);

        assertNull(cache.getCachedEntity("key1"));
        assertEquals("value2", cache.getCachedEntity("key2"));
        assertEquals("value3", cache.getCachedEntity("key3"));

        // Advance the clock 10 seconds
        clock.add(Calendar.SECOND, 10);

        assertNull(cache.getCachedEntity("key1"));
        assertNull(cache.getCachedEntity("key2"));
        assertEquals("value3", cache.getCachedEntity("key3"));

        // Advance the clock 1 year
        clock.add(Calendar.YEAR, 1);

        assertNull(cache.getCachedEntity("key1"));
        assertNull(cache.getCachedEntity("key2"));
        assertEquals("value3", cache.getCachedEntity("key3"));
    }
}
