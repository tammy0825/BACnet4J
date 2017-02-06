package com.serotonin.bacnet4j.cache;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.npdu.test.TestNetwork;
import com.serotonin.bacnet4j.obj.TestClock;
import com.serotonin.bacnet4j.transport.DefaultTransport;

public class RemoteEntityCachePolicyTest {
    @Test
    public void test() {
        final TestClock clock = new TestClock();

        final LocalDevice d = new LocalDevice(0, new DefaultTransport(new TestNetwork(1, 10)));
        d.setClock(clock);

        final Object neverCache = RemoteEntityCachePolicy.NEVER_CACHE.prepareState(d);
        final Object state5Seconds = RemoteEntityCachePolicy.EXPIRE_5_SECONDS.prepareState(d);
        final Object state1Minute = RemoteEntityCachePolicy.EXPIRE_1_MINUTE.prepareState(d);
        final Object state15Minutes = RemoteEntityCachePolicy.EXPIRE_15_MINUTES.prepareState(d);
        final Object state1Hour = RemoteEntityCachePolicy.EXPIRE_1_HOUR.prepareState(d);
        final Object state4Hours = RemoteEntityCachePolicy.EXPIRE_4_HOURS.prepareState(d);
        final Object state1Day = RemoteEntityCachePolicy.EXPIRE_1_DAY.prepareState(d);
        final Object neverExpire = RemoteEntityCachePolicy.NEVER_EXPIRE.prepareState(d);

        assertTrue(RemoteEntityCachePolicy.NEVER_CACHE.hasExpired(d, neverCache));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_5_SECONDS.hasExpired(d, state5Seconds));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_1_MINUTE.hasExpired(d, state1Minute));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_15_MINUTES.hasExpired(d, state15Minutes));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_1_HOUR.hasExpired(d, state1Hour));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_4_HOURS.hasExpired(d, state4Hours));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_1_DAY.hasExpired(d, state1Day));
        assertFalse(RemoteEntityCachePolicy.NEVER_EXPIRE.hasExpired(d, neverExpire));

        // Advance the clock 10 seconds
        clock.add(Calendar.SECOND, 10);

        assertTrue(RemoteEntityCachePolicy.NEVER_CACHE.hasExpired(d, neverCache));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_5_SECONDS.hasExpired(d, state5Seconds));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_1_MINUTE.hasExpired(d, state1Minute));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_15_MINUTES.hasExpired(d, state15Minutes));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_1_HOUR.hasExpired(d, state1Hour));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_4_HOURS.hasExpired(d, state4Hours));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_1_DAY.hasExpired(d, state1Day));
        assertFalse(RemoteEntityCachePolicy.NEVER_EXPIRE.hasExpired(d, neverExpire));

        // Advance the clock 1 minute
        clock.add(Calendar.MINUTE, 1);

        assertTrue(RemoteEntityCachePolicy.NEVER_CACHE.hasExpired(d, neverCache));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_5_SECONDS.hasExpired(d, state5Seconds));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_1_MINUTE.hasExpired(d, state1Minute));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_15_MINUTES.hasExpired(d, state15Minutes));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_1_HOUR.hasExpired(d, state1Hour));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_4_HOURS.hasExpired(d, state4Hours));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_1_DAY.hasExpired(d, state1Day));
        assertFalse(RemoteEntityCachePolicy.NEVER_EXPIRE.hasExpired(d, neverExpire));

        // Advance the clock 15 minutes
        clock.add(Calendar.MINUTE, 15);

        assertTrue(RemoteEntityCachePolicy.NEVER_CACHE.hasExpired(d, neverCache));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_5_SECONDS.hasExpired(d, state5Seconds));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_1_MINUTE.hasExpired(d, state1Minute));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_15_MINUTES.hasExpired(d, state15Minutes));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_1_HOUR.hasExpired(d, state1Hour));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_4_HOURS.hasExpired(d, state4Hours));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_1_DAY.hasExpired(d, state1Day));
        assertFalse(RemoteEntityCachePolicy.NEVER_EXPIRE.hasExpired(d, neverExpire));

        // Advance the clock 45 minutes
        clock.add(Calendar.MINUTE, 45);

        assertTrue(RemoteEntityCachePolicy.NEVER_CACHE.hasExpired(d, neverCache));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_5_SECONDS.hasExpired(d, state5Seconds));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_1_MINUTE.hasExpired(d, state1Minute));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_15_MINUTES.hasExpired(d, state15Minutes));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_1_HOUR.hasExpired(d, state1Hour));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_4_HOURS.hasExpired(d, state4Hours));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_1_DAY.hasExpired(d, state1Day));
        assertFalse(RemoteEntityCachePolicy.NEVER_EXPIRE.hasExpired(d, neverExpire));

        // Advance the clock 4 hours
        clock.add(Calendar.HOUR, 4);

        assertTrue(RemoteEntityCachePolicy.NEVER_CACHE.hasExpired(d, neverCache));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_5_SECONDS.hasExpired(d, state5Seconds));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_1_MINUTE.hasExpired(d, state1Minute));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_15_MINUTES.hasExpired(d, state15Minutes));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_1_HOUR.hasExpired(d, state1Hour));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_4_HOURS.hasExpired(d, state4Hours));
        assertFalse(RemoteEntityCachePolicy.EXPIRE_1_DAY.hasExpired(d, state1Day));
        assertFalse(RemoteEntityCachePolicy.NEVER_EXPIRE.hasExpired(d, neverExpire));

        // Advance the clock 20 hours
        clock.add(Calendar.HOUR, 20);

        assertTrue(RemoteEntityCachePolicy.NEVER_CACHE.hasExpired(d, neverCache));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_5_SECONDS.hasExpired(d, state5Seconds));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_1_MINUTE.hasExpired(d, state1Minute));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_15_MINUTES.hasExpired(d, state15Minutes));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_1_HOUR.hasExpired(d, state1Hour));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_4_HOURS.hasExpired(d, state4Hours));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_1_DAY.hasExpired(d, state1Day));
        assertFalse(RemoteEntityCachePolicy.NEVER_EXPIRE.hasExpired(d, neverExpire));

        // Advance the clock 1 year
        clock.add(Calendar.YEAR, 1);

        assertTrue(RemoteEntityCachePolicy.NEVER_CACHE.hasExpired(d, neverCache));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_5_SECONDS.hasExpired(d, state5Seconds));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_1_MINUTE.hasExpired(d, state1Minute));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_15_MINUTES.hasExpired(d, state15Minutes));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_1_HOUR.hasExpired(d, state1Hour));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_4_HOURS.hasExpired(d, state4Hours));
        assertTrue(RemoteEntityCachePolicy.EXPIRE_1_DAY.hasExpired(d, state1Day));
        assertFalse(RemoteEntityCachePolicy.NEVER_EXPIRE.hasExpired(d, neverExpire));
    }
}
