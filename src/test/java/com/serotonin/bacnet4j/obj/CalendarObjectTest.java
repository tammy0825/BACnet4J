package com.serotonin.bacnet4j.obj;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.enums.DayOfWeek;
import com.serotonin.bacnet4j.enums.Month;
import com.serotonin.bacnet4j.npdu.test.TestNetwork;
import com.serotonin.bacnet4j.npdu.test.TestNetworkMap;
import com.serotonin.bacnet4j.service.confirmed.AddListElementRequest;
import com.serotonin.bacnet4j.service.confirmed.RemoveListElementRequest;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.constructed.CalendarEntry;
import com.serotonin.bacnet4j.type.constructed.DateRange;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.WeekNDay;
import com.serotonin.bacnet4j.type.constructed.WeekNDay.WeekOfMonth;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.Date;

public class CalendarObjectTest {
    private final TestNetworkMap map = new TestNetworkMap();
    private final LocalDevice d1 = new LocalDevice(1, new DefaultTransport(new TestNetwork(map, 1, 10)));
    private final LocalDevice d2 = new LocalDevice(2, new DefaultTransport(new TestNetwork(map, 2, 20)));
    private RemoteDevice rd1;

    @Before
    public void before() throws Exception {
        d1.initialize();
        d2.initialize();

        // Get d1 as a remote object.
        rd1 = d2.getRemoteDevice(1).get();
    }

    @After
    public void after() {
        // Shut down
        d1.terminate();
        d2.terminate();
    }

    @Test
    public void test() throws Exception {
        final TestClock clock = new TestClock();
        clock.setTime(2115, Calendar.JANUARY, 1, 12, 0, 0);

        final CalendarEntry ce = new CalendarEntry(
                new WeekNDay(Month.UNSPECIFIED, WeekOfMonth.days22to28, DayOfWeek.WEDNESDAY)); // The Wednesday during the 4th week of each month.
        final SequenceOf<CalendarEntry> dateList = new SequenceOf<>( //
                new CalendarEntry(new Date(-1, null, -1, DayOfWeek.FRIDAY)), // Every Friday.
                new CalendarEntry(
                        new DateRange(new Date(-1, Month.NOVEMBER, -1, null), new Date(-1, Month.FEBRUARY, -1, null))), // November to February
                ce);

        final CalendarObject co = new CalendarObject(d1, 0, "cal0", dateList, clock);

        co.updatePresentValue(); // November to February
        assertEquals(Boolean.TRUE, co.get(PropertyIdentifier.presentValue));

        clock.setTime(2115, Calendar.MARCH, 2, 12, 0, 0);
        co.updatePresentValue();
        assertEquals(Boolean.FALSE, co.get(PropertyIdentifier.presentValue));

        clock.setTime(2115, Calendar.MARCH, 8, 12, 0, 0); // A Friday
        co.updatePresentValue();
        assertEquals(Boolean.TRUE, co.get(PropertyIdentifier.presentValue));

        clock.setTime(2115, Calendar.MAY, 27, 12, 0, 0);
        co.updatePresentValue();
        assertEquals(Boolean.FALSE, co.get(PropertyIdentifier.presentValue));

        clock.setTime(2115, Calendar.MAY, 22, 12, 0, 0); // The Wednesday during the 4th week of each month.
        co.updatePresentValue();
        assertEquals(Boolean.TRUE, co.get(PropertyIdentifier.presentValue));

        // Set the time source to a time that does not match the current date list, but
        // will match a new entry.
        clock.setTime(2115, Calendar.JUNE, 17, 12, 0, 0);
        co.updatePresentValue(); // Uses the above time source.
        assertEquals(Boolean.FALSE, co.get(PropertyIdentifier.presentValue));

        final CalendarEntry newEntry = new CalendarEntry(new Date(-1, Month.JUNE, -1, null));
        final AddListElementRequest addReq = new AddListElementRequest(co.getId(), PropertyIdentifier.dateList, null,
                new SequenceOf<>(newEntry));
        d2.send(rd1, addReq).get();
        assertEquals(Boolean.TRUE, co.get(PropertyIdentifier.presentValue));

        clock.setTime(2115, Calendar.JULY, 24, 12, 0, 0);
        co.updatePresentValue(); // Uses the above time source.
        assertEquals(Boolean.TRUE, co.get(PropertyIdentifier.presentValue));

        final RemoveListElementRequest remReq = new RemoveListElementRequest(co.getId(), PropertyIdentifier.dateList,
                null, new SequenceOf<>(ce));
        d2.send(rd1, remReq).get();
        assertEquals(Boolean.FALSE, co.get(PropertyIdentifier.presentValue));

        // Check that the compensatory time works.
        co.setTimeTolerance(1000 * 60 * 3);
        clock.setTime(2115, Calendar.AUGUST, 8, 23, 58, 0);
        co.updatePresentValue(); // Uses the above time source.
        assertEquals(Boolean.TRUE, co.get(PropertyIdentifier.presentValue));
    }
}
