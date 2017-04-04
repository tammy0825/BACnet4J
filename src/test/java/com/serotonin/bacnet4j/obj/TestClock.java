package com.serotonin.bacnet4j.obj;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.GregorianCalendar;

/**
 * @deprecated use WarpClock instead.
 * @author Matthew
 */
@Deprecated
public class TestClock extends Clock {
    GregorianCalendar gc;

    public TestClock() {
        this(new GregorianCalendar());
    }

    public TestClock(final GregorianCalendar gc) {
        this.gc = gc;
    }

    public void setTime(final int year, final int month, final int day, final int hour, final int min, final int sec) {
        gc = new GregorianCalendar(year, month, day, hour, min, sec);
    }

    public void add(final int field, final int amount) {
        gc.add(field, amount);
    }

    @Override
    public long millis() {
        return gc.getTimeInMillis();
    }

    @Override
    public ZoneId getZone() {
        return null;
    }

    @Override
    public Clock withZone(final ZoneId zone) {
        return null;
    }

    @Override
    public Instant instant() {
        return null;
    }
}
