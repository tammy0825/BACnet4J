package com.serotonin.bacnet4j.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.TestUtils;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.npdu.test.TestNetwork;
import com.serotonin.bacnet4j.npdu.test.TestNetworkMap;
import com.serotonin.bacnet4j.npdu.test.TestNetworkUtils;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.constructed.BACnetArray;
import com.serotonin.bacnet4j.type.constructed.Destination;
import com.serotonin.bacnet4j.type.constructed.DeviceObjectPropertyReference;
import com.serotonin.bacnet4j.type.constructed.EventTransitionBits;
import com.serotonin.bacnet4j.type.constructed.LimitEnable;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.Recipient;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.constructed.ValueSource;
import com.serotonin.bacnet4j.type.enumerated.EngineeringUnits;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.EventType;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Reliability;
import com.serotonin.bacnet4j.type.eventParameter.EventParameter;
import com.serotonin.bacnet4j.type.eventParameter.OutOfRange;
import com.serotonin.bacnet4j.type.notificationParameters.NotificationParameters;
import com.serotonin.bacnet4j.type.notificationParameters.OutOfRangeNotif;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

import lohbihler.warp.WarpClock;

public class AnalogOutputObjectTest {
    static final Logger LOG = LoggerFactory.getLogger(AnalogOutputObjectTest.class);

    private final WarpClock clock = new WarpClock();
    private final TestNetworkMap map = new TestNetworkMap();
    private LocalDevice d1;
    private LocalDevice d2;
    private RemoteDevice rd1;
    private RemoteDevice rd2;
    private AnalogOutputObject ao;
    private NotificationClassObject nc;

    @Before
    public void before() throws Exception {
        d1 = new LocalDevice(1, new DefaultTransport(new TestNetwork(map, 1, 0))).withClock(clock).initialize();
        d2 = new LocalDevice(2, new DefaultTransport(new TestNetwork(map, 2, 0))).withClock(clock).initialize();

        rd1 = d2.getRemoteDevice(1).get();
        rd2 = d1.getRemoteDevice(2).get();

        ao = new AnalogOutputObject(d1, 0, "ao", 50, EngineeringUnits.amperes, false, 45);

        nc = new NotificationClassObject(d1, 17, "nc17", 100, 5, 200, new EventTransitionBits(false, false, false));
    }

    @After
    public void after() {
        d1.terminate();
        d2.terminate();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void intrinsicReporting() throws Exception {
        final SequenceOf<Destination> recipients = nc.get(PropertyIdentifier.recipientList);
        recipients.add(new Destination(new Recipient(rd2.getAddress()), new UnsignedInteger(10), Boolean.TRUE,
                new EventTransitionBits(true, true, true)));

        // Create an event listener on d2 to catch the event notifications.
        final EventNotifListener listener = new EventNotifListener();
        d2.getEventHandler().addListener(listener);

        ao.supportIntrinsicReporting(60, 17, 100, 20, 5, new LimitEnable(true, true),
                new EventTransitionBits(true, true, true), NotifyType.alarm, 180);
        // Ensure that initializing the intrinsic reporting didn't fire any notifications.
        Thread.sleep(40);
        assertEquals(0, listener.notifs.size());

        // Do a real state change. Write an out of range value. After 60s the alarm will be raised.
        ao.writePropertyInternal(PropertyIdentifier.presentValue, new Real(101));
        clock.plus(59500, TimeUnit.MILLISECONDS, 500, TimeUnit.MILLISECONDS, 0, 40);
        assertEquals(EventState.normal, ao.getProperty(PropertyIdentifier.eventState)); // Still normal at this point.
        clock.plus(600, TimeUnit.MILLISECONDS, 600, TimeUnit.MILLISECONDS, 0, 40);
        assertEquals(EventState.highLimit, ao.getProperty(PropertyIdentifier.eventState));
        assertEquals(new StatusFlags(true, false, false, false), ao.getProperty(PropertyIdentifier.statusFlags));

        // Ensure that a proper looking event notification was received.
        assertEquals(1, listener.notifs.size());
        Map<String, Object> notif = listener.notifs.remove(0);
        assertEquals(new UnsignedInteger(10), notif.get("processIdentifier"));
        assertEquals(rd1.getObjectIdentifier(), notif.get("initiatingDevice"));
        assertEquals(ao.getId(), notif.get("eventObjectIdentifier"));
        assertEquals(((BACnetArray<TimeStamp>) ao.getProperty(PropertyIdentifier.eventTimeStamps))
                .getBase1(EventState.offnormal.getTransitionIndex()), notif.get("timeStamp"));
        assertEquals(new UnsignedInteger(17), notif.get("notificationClass"));
        assertEquals(new UnsignedInteger(100), notif.get("priority"));
        assertEquals(EventType.outOfRange, notif.get("eventType"));
        assertEquals(null, notif.get("messageText"));
        assertEquals(NotifyType.alarm, notif.get("notifyType"));
        assertEquals(Boolean.FALSE, notif.get("ackRequired"));
        assertEquals(EventState.normal, notif.get("fromState"));
        assertEquals(EventState.highLimit, notif.get("toState"));
        assertEquals(
                new NotificationParameters(new OutOfRangeNotif(new Real(101),
                        new StatusFlags(true, false, false, false), new Real(5), new Real(100))),
                notif.get("eventValues"));

        // Return to normal. After 180s the notification will be sent.
        ao.writePropertyInternal(PropertyIdentifier.presentValue, new Real(94));
        assertEquals(EventState.highLimit, ao.getProperty(PropertyIdentifier.eventState));
        assertEquals(0, listener.notifs.size());
        clock.plus(179999, TimeUnit.MILLISECONDS, 179999, TimeUnit.MILLISECONDS, 0, 40);
        assertEquals(EventState.highLimit, ao.getProperty(PropertyIdentifier.eventState));
        assertEquals(0, listener.notifs.size());
        clock.plus(2, TimeUnit.MILLISECONDS, 2, TimeUnit.MILLISECONDS, 0, 40);
        assertEquals(EventState.normal, ao.getProperty(PropertyIdentifier.eventState));
        assertEquals(1, listener.notifs.size());
        notif = listener.notifs.remove(0);
        assertEquals(EventState.highLimit, notif.get("fromState"));
        assertEquals(EventState.normal, notif.get("toState"));
        assertEquals(
                new NotificationParameters(new OutOfRangeNotif(new Real(94),
                        new StatusFlags(false, false, false, false), new Real(5), new Real(100))),
                notif.get("eventValues"));
    }

    @Test
    public void propertyConformanceRequired() throws Exception {
        assertNotNull(ao.getProperty(PropertyIdentifier.objectIdentifier));
        assertNotNull(ao.getProperty(PropertyIdentifier.objectName));
        assertNotNull(ao.getProperty(PropertyIdentifier.objectType));
        assertNotNull(ao.getProperty(PropertyIdentifier.presentValue));
        assertNotNull(ao.getProperty(PropertyIdentifier.statusFlags));
        assertNotNull(ao.getProperty(PropertyIdentifier.eventState));
        assertNotNull(ao.getProperty(PropertyIdentifier.outOfService));
        assertNotNull(ao.getProperty(PropertyIdentifier.units));
        assertNotNull(ao.getProperty(PropertyIdentifier.priorityArray));
        assertNotNull(ao.getProperty(PropertyIdentifier.relinquishDefault));
        assertNotNull(ao.getProperty(PropertyIdentifier.propertyList));
        assertNotNull(ao.getProperty(PropertyIdentifier.currentCommandPriority));
    }

    @Test
    public void propertyConformanceEditableWhenOutOfService() throws BACnetServiceException {
        // Should not be writable while in service
        TestUtils.assertBACnetServiceException(
                () -> ao.writeProperty(null,
                        new PropertyValue(PropertyIdentifier.reliability, null, Reliability.unreliableOther, null)),
                ErrorClass.property, ErrorCode.writeAccessDenied);

        // Should be writable while out of service.
        ao.writeProperty(null, PropertyIdentifier.outOfService, Boolean.TRUE);
        ao.writeProperty(new ValueSource(TestNetworkUtils.toAddress(1)),
                new PropertyValue(PropertyIdentifier.presentValue, null, new Real(51), null));
        ao.writeProperty(null,
                new PropertyValue(PropertyIdentifier.reliability, null, Reliability.unreliableOther, null));
    }

    @Test
    public void propertyConformanceRequiredWhenCOVReporting() throws Exception {
        ao.supportCovReporting(1);
        assertNotNull(ao.getProperty(PropertyIdentifier.covIncrement));
    }

    @Test
    public void propertyConformanceRequiredWhenIntrinsicReporting() throws Exception {
        ao.supportIntrinsicReporting(30, 17, 60, 40, 1, new LimitEnable(true, true),
                new EventTransitionBits(true, true, true), NotifyType.alarm, 10);
        assertNotNull(ao.getProperty(PropertyIdentifier.timeDelay));
        assertNotNull(ao.getProperty(PropertyIdentifier.notificationClass));
        assertNotNull(ao.getProperty(PropertyIdentifier.highLimit));
        assertNotNull(ao.getProperty(PropertyIdentifier.lowLimit));
        assertNotNull(ao.getProperty(PropertyIdentifier.deadband));
        assertNotNull(ao.getProperty(PropertyIdentifier.limitEnable));
        assertNotNull(ao.getProperty(PropertyIdentifier.eventEnable));
        assertNotNull(ao.getProperty(PropertyIdentifier.ackedTransitions));
        assertNotNull(ao.getProperty(PropertyIdentifier.notifyType));
        assertNotNull(ao.getProperty(PropertyIdentifier.eventTimeStamps));
        assertNotNull(ao.getProperty(PropertyIdentifier.eventDetectionEnable));
    }

    @Test
    public void propertyConformanceForbiddenWhenNotIntrinsicReporting() throws Exception {
        assertNull(ao.getProperty(PropertyIdentifier.timeDelay));
        assertNull(ao.getProperty(PropertyIdentifier.notificationClass));
        assertNull(ao.getProperty(PropertyIdentifier.highLimit));
        assertNull(ao.getProperty(PropertyIdentifier.lowLimit));
        assertNull(ao.getProperty(PropertyIdentifier.deadband));
        assertNull(ao.getProperty(PropertyIdentifier.limitEnable));
        assertNull(ao.getProperty(PropertyIdentifier.eventEnable));
        assertNull(ao.getProperty(PropertyIdentifier.ackedTransitions));
        assertNull(ao.getProperty(PropertyIdentifier.notifyType));
        assertNull(ao.getProperty(PropertyIdentifier.eventTimeStamps));
        assertNull(ao.getProperty(PropertyIdentifier.eventMessageTexts));
        assertNull(ao.getProperty(PropertyIdentifier.eventMessageTextsConfig));
        assertNull(ao.getProperty(PropertyIdentifier.eventDetectionEnable));
        assertNull(ao.getProperty(PropertyIdentifier.eventAlgorithmInhibitRef));
        assertNull(ao.getProperty(PropertyIdentifier.eventAlgorithmInhibit));
        assertNull(ao.getProperty(PropertyIdentifier.timeDelayNormal));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void algorithmicReporting() throws Exception {
        final DeviceObjectPropertyReference ref = new DeviceObjectPropertyReference(1, ao.getId(),
                PropertyIdentifier.presentValue);
        final EventEnrollmentObject ee = new EventEnrollmentObject(d1, 0, "ee", ref, NotifyType.alarm,
                new EventParameter(new OutOfRange(new UnsignedInteger(30), new Real(40), new Real(60), new Real(2))),
                new EventTransitionBits(true, true, true), 17, 1000, null, null);

        // Set up the notification destination
        final SequenceOf<Destination> recipients = nc.get(PropertyIdentifier.recipientList);
        recipients.add(new Destination(new Recipient(rd2.getAddress()), new UnsignedInteger(10), Boolean.TRUE,
                new EventTransitionBits(true, true, true)));

        // Create an event listener on d2 to catch the event notifications.
        final EventNotifListener listener = new EventNotifListener();
        d2.getEventHandler().addListener(listener);

        // Ensure that initializing the event enrollment object didn't fire any notifications.
        Thread.sleep(40);
        assertEquals(EventState.normal, ee.getProperty(PropertyIdentifier.eventState));
        assertEquals(0, listener.notifs.size());

        //
        // Go to high limit.
        ao.writePropertyInternal(PropertyIdentifier.presentValue, new Real(70));
        // Allow the EE to poll
        clock.plus(1100, TimeUnit.MILLISECONDS, 1100, TimeUnit.MILLISECONDS, 0, 40);
        assertEquals(EventState.normal, ee.getProperty(PropertyIdentifier.eventState));
        // Wait until just before the time delay.
        clock.plus(29500, TimeUnit.MILLISECONDS, 29500, TimeUnit.MILLISECONDS, 0, 40);
        assertEquals(EventState.normal, ee.getProperty(PropertyIdentifier.eventState));
        // Wait until after the time delay.
        clock.plus(600, TimeUnit.MILLISECONDS, 600, TimeUnit.MILLISECONDS, 0, 40);
        assertEquals(EventState.highLimit, ee.getProperty(PropertyIdentifier.eventState));

        // Ensure that a proper looking event notification was received.
        assertEquals(1, listener.notifs.size());
        Map<String, Object> notif = listener.notifs.remove(0);
        assertEquals(new UnsignedInteger(10), notif.get("processIdentifier"));
        assertEquals(d1.getId(), notif.get("initiatingDevice"));
        assertEquals(ee.getId(), notif.get("eventObjectIdentifier"));
        assertEquals(((BACnetArray<TimeStamp>) ee.getProperty(PropertyIdentifier.eventTimeStamps))
                .getBase1(EventState.highLimit.getTransitionIndex()), notif.get("timeStamp"));
        assertEquals(new UnsignedInteger(17), notif.get("notificationClass"));
        assertEquals(new UnsignedInteger(100), notif.get("priority"));
        assertEquals(EventType.outOfRange, notif.get("eventType"));
        assertEquals(null, notif.get("messageText"));
        assertEquals(NotifyType.alarm, notif.get("notifyType"));
        assertEquals(Boolean.FALSE, notif.get("ackRequired"));
        assertEquals(EventState.normal, notif.get("fromState"));
        assertEquals(EventState.highLimit, notif.get("toState"));
        assertEquals(
                new NotificationParameters(new OutOfRangeNotif(new Real(70),
                        new StatusFlags(false, false, false, false), new Real(2), new Real(60))),
                notif.get("eventValues"));

        //
        // Return to normal
        ao.writePropertyInternal(PropertyIdentifier.presentValue, new Real(40));
        // Allow the EE to poll
        clock.plus(1100, TimeUnit.MILLISECONDS, 1100, TimeUnit.MILLISECONDS, 0, 40);
        assertEquals(EventState.highLimit, ee.getProperty(PropertyIdentifier.eventState));
        // Wait until just before the time delay.
        clock.plus(29500, TimeUnit.MILLISECONDS, 29500, TimeUnit.MILLISECONDS, 0, 40);
        assertEquals(EventState.highLimit, ee.getProperty(PropertyIdentifier.eventState));
        // Wait until after the time delay.
        clock.plus(600, TimeUnit.MILLISECONDS, 600, TimeUnit.MILLISECONDS, 0, 40);
        assertEquals(EventState.normal, ee.getProperty(PropertyIdentifier.eventState));

        // Ensure that a proper looking event notification was received.
        assertEquals(1, listener.notifs.size());
        notif = listener.notifs.remove(0);
        assertEquals(new UnsignedInteger(10), notif.get("processIdentifier"));
        assertEquals(d1.getId(), notif.get("initiatingDevice"));
        assertEquals(ee.getId(), notif.get("eventObjectIdentifier"));
        assertEquals(((BACnetArray<TimeStamp>) ee.getProperty(PropertyIdentifier.eventTimeStamps))
                .getBase1(EventState.normal.getTransitionIndex()), notif.get("timeStamp"));
        assertEquals(new UnsignedInteger(17), notif.get("notificationClass"));
        assertEquals(new UnsignedInteger(200), notif.get("priority"));
        assertEquals(EventType.outOfRange, notif.get("eventType"));
        assertEquals(null, notif.get("messageText"));
        assertEquals(NotifyType.alarm, notif.get("notifyType"));
        assertEquals(Boolean.FALSE, notif.get("ackRequired"));
        assertEquals(EventState.highLimit, notif.get("fromState"));
        assertEquals(EventState.normal, notif.get("toState"));
        assertEquals(
                new NotificationParameters(new OutOfRangeNotif(new Real(40),
                        new StatusFlags(false, false, false, false), new Real(2), new Real(60))),
                notif.get("eventValues"));
    }
}
