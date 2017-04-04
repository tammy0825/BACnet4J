package com.serotonin.bacnet4j.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Map;

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
import com.serotonin.bacnet4j.service.confirmed.SubscribeCOVRequest;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.constructed.BACnetArray;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.constructed.Destination;
import com.serotonin.bacnet4j.type.constructed.EventTransitionBits;
import com.serotonin.bacnet4j.type.constructed.LimitEnable;
import com.serotonin.bacnet4j.type.constructed.ObjectPropertyReference;
import com.serotonin.bacnet4j.type.constructed.Prescale;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.Recipient;
import com.serotonin.bacnet4j.type.constructed.Scale;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.enumerated.EngineeringUnits;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.EventType;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Reliability;
import com.serotonin.bacnet4j.type.notificationParameters.NotificationParameters;
import com.serotonin.bacnet4j.type.notificationParameters.OutOfRangeNotif;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

import lohbihler.warp.WarpClock;

public class PulseConverterObjectTest {
    static final Logger LOG = LoggerFactory.getLogger(AccumulatorObjectTest.class);

    private final WarpClock clock = new WarpClock();
    private final TestNetworkMap map = new TestNetworkMap();

    private LocalDevice d1;
    private LocalDevice d2;
    private RemoteDevice rd1;
    private RemoteDevice rd2;
    private PulseConverterObject pc;
    private NotificationClassObject nc;

    @Before
    public void before() throws Exception {
        d1 = new LocalDevice(1, new DefaultTransport(new TestNetwork(map, 1, 0))).withClock(clock).initialize();
        d2 = new LocalDevice(2, new DefaultTransport(new TestNetwork(map, 2, 20))).withClock(clock).initialize();

        rd1 = d2.getRemoteDevice(1).get();
        rd2 = d1.getRemoteDevice(2).get();

        pc = new PulseConverterObject(d1, 0, "pc", 0, 7.5F, EngineeringUnits.amperes, false);

        nc = new NotificationClassObject(d1, 54, "nc54", 100, 5, 200, new EventTransitionBits(true, true, true));
    }

    //
    @After
    public void abstractAfter() {
        d1.terminate();
        d2.terminate();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void intrinsicReporting() throws Exception {
        // Default the present value by setting adjust value.
        pc.writePropertyInternal(PropertyIdentifier.adjustValue, new Real(-50));
        assertEquals(new Real(45), pc.get(PropertyIdentifier.presentValue));

        // Set up the notification destination
        final SequenceOf<Destination> recipients = nc.get(PropertyIdentifier.recipientList);
        recipients.add(new Destination(new Recipient(rd2.getAddress()), new UnsignedInteger(10), Boolean.TRUE,
                new EventTransitionBits(true, true, true)));

        // Create an event listener on d2 to catch the event notifications.
        final EventNotifListener listener = new EventNotifListener();
        d2.getEventHandler().addListener(listener);

        // Set up intrinsic reporting on the pulse converter.
        // Limits are 30/70. Time delay is 10s. Time delay normal is null, and so should default to time delay.
        pc.supportIntrinsicReporting(70, 30, 3, 10, null, 54, new LimitEnable(true, true),
                new EventTransitionBits(true, true, true), NotifyType.event);
        // Ensure that initializing the intrinsic reporting didn't fire any notifications.
        assertEquals(0, listener.notifs.size());

        // strictly required by the time delay.
        pc.pulse();
        assertEquals(new Real(52.5F), pc.getProperty(PropertyIdentifier.presentValue));
        assertEquals(new UnsignedInteger(7), pc.getProperty(PropertyIdentifier.count));
        assertEquals(new DateTime(d1), pc.getProperty(PropertyIdentifier.updateTime));
        assertEquals(EventState.normal, pc.getProperty(PropertyIdentifier.eventState)); // Still normal at this point.
        // Ensure that no notifications are sent.
        assertEquals(0, listener.notifs.size());

        //
        // Write pulses to go out of range.
        pc.pulses(3);
        clock.plusSeconds(11);
        Thread.sleep(40);
        assertEquals(EventState.highLimit, pc.getProperty(PropertyIdentifier.eventState));
        assertEquals(new StatusFlags(true, false, false, false), pc.getProperty(PropertyIdentifier.statusFlags));

        // Ensure that a proper looking event notification was received.
        Thread.sleep(40);
        assertEquals(1, listener.notifs.size());
        Map<String, Object> notif = listener.notifs.remove(0);
        assertEquals(new UnsignedInteger(10), notif.get("processIdentifier"));
        assertEquals(d1.getId(), notif.get("initiatingDevice"));
        assertEquals(pc.getId(), notif.get("eventObjectIdentifier"));
        assertEquals(((BACnetArray<TimeStamp>) pc.getProperty(PropertyIdentifier.eventTimeStamps))
                .getBase1(EventState.offnormal.getTransitionIndex()), notif.get("timeStamp"));
        assertEquals(new UnsignedInteger(54), notif.get("notificationClass"));
        assertEquals(new UnsignedInteger(100), notif.get("priority"));
        assertEquals(EventType.outOfRange, notif.get("eventType"));
        assertEquals(null, notif.get("messageText"));
        assertEquals(NotifyType.event, notif.get("notifyType"));
        assertEquals(Boolean.TRUE, notif.get("ackRequired"));
        assertEquals(EventState.normal, notif.get("fromState"));
        assertEquals(EventState.highLimit, notif.get("toState"));
        assertEquals(new NotificationParameters(new OutOfRangeNotif(new Real(75),
                new StatusFlags(true, false, false, false), new Real(3), new Real(70))), notif.get("eventValues"));

        //
        // Use adjust value to put back into normal.
        clock.plusSeconds(5);
        pc.writePropertyInternal(PropertyIdentifier.adjustValue, new Real(45));
        assertEquals(new Real(30), pc.get(PropertyIdentifier.presentValue));
        assertEquals(new UnsignedInteger(4), pc.getProperty(PropertyIdentifier.count));
        assertEquals(new DateTime(d1), pc.getProperty(PropertyIdentifier.updateTime));
        assertEquals(new UnsignedInteger(10), pc.getProperty(PropertyIdentifier.countBeforeChange));
        assertEquals(new DateTime(d1), pc.getProperty(PropertyIdentifier.countChangeTime));

        // Ensure that after 9 seconds we are still in high limit.
        clock.plusSeconds(9);
        assertEquals(EventState.highLimit, pc.getProperty(PropertyIdentifier.eventState));
        assertEquals(0, listener.notifs.size());

        clock.plusSeconds(2);
        Thread.sleep(40);
        assertEquals(EventState.normal, pc.getProperty(PropertyIdentifier.eventState));
        assertEquals(new StatusFlags(false, false, false, false), pc.getProperty(PropertyIdentifier.statusFlags));
        assertEquals(1, listener.notifs.size());
        notif = listener.notifs.remove(0);
        assertEquals(new UnsignedInteger(10), notif.get("processIdentifier"));
        assertEquals(d1.getId(), notif.get("initiatingDevice"));
        assertEquals(pc.getId(), notif.get("eventObjectIdentifier"));
        assertEquals(((BACnetArray<TimeStamp>) pc.getProperty(PropertyIdentifier.eventTimeStamps))
                .getBase1(EventState.normal.getTransitionIndex()), notif.get("timeStamp"));
        assertEquals(new UnsignedInteger(54), notif.get("notificationClass"));
        assertEquals(new UnsignedInteger(200), notif.get("priority"));
        assertEquals(EventType.outOfRange, notif.get("eventType"));
        assertEquals(null, notif.get("messageText"));
        assertEquals(NotifyType.event, notif.get("notifyType"));
        assertEquals(Boolean.TRUE, notif.get("ackRequired"));
        assertEquals(EventState.highLimit, notif.get("fromState"));
        assertEquals(EventState.normal, notif.get("toState"));
        assertEquals(
                new NotificationParameters(new OutOfRangeNotif(new Real(30),
                        new StatusFlags(false, false, false, false), new Real(3), new Real(70))),
                notif.get("eventValues"));

        // Remove the object
        d1.removeObject(pc.getId());
    }

    @Test
    public void propertyConformanceRequired() throws Exception {
        assertNotNull(pc.getProperty(PropertyIdentifier.objectIdentifier));
        assertNotNull(pc.getProperty(PropertyIdentifier.objectName));
        assertNotNull(pc.getProperty(PropertyIdentifier.objectType));
        assertNotNull(pc.getProperty(PropertyIdentifier.presentValue));
        assertNotNull(pc.getProperty(PropertyIdentifier.statusFlags));
        assertNotNull(pc.getProperty(PropertyIdentifier.eventState));
        assertNotNull(pc.getProperty(PropertyIdentifier.outOfService));
        assertNotNull(pc.getProperty(PropertyIdentifier.units));
        assertNotNull(pc.getProperty(PropertyIdentifier.scaleFactor));
        assertNotNull(pc.getProperty(PropertyIdentifier.count));
        assertNotNull(pc.getProperty(PropertyIdentifier.updateTime));
        assertNotNull(pc.getProperty(PropertyIdentifier.countChangeTime));
        assertNotNull(pc.getProperty(PropertyIdentifier.countBeforeChange));
        assertNotNull(pc.getProperty(PropertyIdentifier.propertyList));
    }

    @Test
    public void propertyConformanceEditableWhenOutOfService() throws BACnetServiceException {
        // Should not be writable while in service
        TestUtils.assertBACnetServiceException(
                () -> pc.writeProperty(null,
                        new PropertyValue(PropertyIdentifier.presentValue, null, new Real(51), null)),
                ErrorClass.property, ErrorCode.writeAccessDenied);

        // Should be writable while out of service.
        pc.writeProperty(null, PropertyIdentifier.outOfService, Boolean.TRUE);
        pc.writeProperty(null, new PropertyValue(PropertyIdentifier.presentValue, null, new Real(51), null));
    }

    @Test
    public void propertyConformanceReadOnly() {
        TestUtils.assertBACnetServiceException(
                () -> pc.writeProperty(null,
                        new PropertyValue(PropertyIdentifier.count, null, DateTime.UNSPECIFIED, null)),
                ErrorClass.property, ErrorCode.writeAccessDenied);
        TestUtils.assertBACnetServiceException(
                () -> pc.writeProperty(null,
                        new PropertyValue(PropertyIdentifier.updateTime, new UnsignedInteger(2),
                                new CharacterString("should fail"), null)),
                ErrorClass.property, ErrorCode.writeAccessDenied);
        TestUtils.assertBACnetServiceException(
                () -> pc.writeProperty(null,
                        new PropertyValue(PropertyIdentifier.ackedTransitions, new UnsignedInteger(2),
                                new CharacterString("should fail"), null)),
                ErrorClass.property, ErrorCode.writeAccessDenied);
        TestUtils.assertBACnetServiceException(
                () -> pc.writeProperty(null,
                        new PropertyValue(PropertyIdentifier.eventMessageTexts, new UnsignedInteger(2),
                                new CharacterString("should fail"), null)),
                ErrorClass.property, ErrorCode.writeAccessDenied);
    }

    @Test
    public void propertyConformanceForbiddenWhenNotCov() throws Exception {
        assertNull(pc.getProperty(PropertyIdentifier.covIncrement));
        assertNull(pc.getProperty(PropertyIdentifier.covPeriod));
    }

    @Test
    public void propertyConformanceRequiredWhenCov() throws Exception {
        pc.supportCovReporting(2);
        assertNotNull(pc.getProperty(PropertyIdentifier.covIncrement));
        assertNotNull(pc.getProperty(PropertyIdentifier.covPeriod));
    }

    @Test
    public void propertyConformanceRequiredWhenIntrinsicReporting() throws Exception {
        pc.supportIntrinsicReporting(90, 10, 10, 10, new UnsignedInteger(15), 54, new LimitEnable(true, true),
                new EventTransitionBits(true, true, true), NotifyType.alarm);
        assertNotNull(pc.getProperty(PropertyIdentifier.timeDelay));
        assertNotNull(pc.getProperty(PropertyIdentifier.notificationClass));
        assertNotNull(pc.getProperty(PropertyIdentifier.highLimit));
        assertNotNull(pc.getProperty(PropertyIdentifier.lowLimit));
        assertNotNull(pc.getProperty(PropertyIdentifier.deadband));
        assertNotNull(pc.getProperty(PropertyIdentifier.limitEnable));
        assertNotNull(pc.getProperty(PropertyIdentifier.eventEnable));
        assertNotNull(pc.getProperty(PropertyIdentifier.ackedTransitions));
        assertNotNull(pc.getProperty(PropertyIdentifier.notifyType));
        assertNotNull(pc.getProperty(PropertyIdentifier.eventTimeStamps));
        assertNotNull(pc.getProperty(PropertyIdentifier.eventDetectionEnable));
    }

    @Test
    public void propertyConformanceForbiddenWhenNotIntrinsicReporting() throws Exception {
        assertNull(pc.getProperty(PropertyIdentifier.timeDelay));
        assertNull(pc.getProperty(PropertyIdentifier.notificationClass));
        assertNull(pc.getProperty(PropertyIdentifier.highLimit));
        assertNull(pc.getProperty(PropertyIdentifier.lowLimit));
        assertNull(pc.getProperty(PropertyIdentifier.deadband));
        assertNull(pc.getProperty(PropertyIdentifier.limitEnable));
        assertNull(pc.getProperty(PropertyIdentifier.eventEnable));
        assertNull(pc.getProperty(PropertyIdentifier.ackedTransitions));
        assertNull(pc.getProperty(PropertyIdentifier.notifyType));
        assertNull(pc.getProperty(PropertyIdentifier.eventTimeStamps));
        assertNull(pc.getProperty(PropertyIdentifier.eventMessageTexts));
        assertNull(pc.getProperty(PropertyIdentifier.eventMessageTextsConfig));
        assertNull(pc.getProperty(PropertyIdentifier.eventDetectionEnable));
        assertNull(pc.getProperty(PropertyIdentifier.eventAlgorithmInhibitRef));
        assertNull(pc.getProperty(PropertyIdentifier.eventAlgorithmInhibit));
        assertNull(pc.getProperty(PropertyIdentifier.timeDelayNormal));
    }

    @Test
    public void covNotifications() throws Exception {
        pc.supportCovReporting(10);

        // Create a COV listener to catch the notifications.
        final CovNotifListener listener = new CovNotifListener();
        d2.getEventHandler().addListener(listener);

        //
        // Subscribe for notifications. Doing so should cause an initial notification to be sent.
        d2.send(rd1,
                new SubscribeCOVRequest(new UnsignedInteger(987), pc.getId(), Boolean.FALSE, new UnsignedInteger(600)))
                .get();
        Thread.sleep(40);
        assertEquals(1, listener.notifs.size());
        Map<String, Object> notif = listener.notifs.remove(0);
        assertEquals(new UnsignedInteger(987), notif.get("subscriberProcessIdentifier"));
        assertEquals(d1.getId(), notif.get("initiatingDevice"));
        assertEquals(pc.getId(), notif.get("monitoredObjectIdentifier"));
        assertEquals(new UnsignedInteger(600), notif.get("timeRemaining"));
        assertEquals(new SequenceOf<>(new PropertyValue(PropertyIdentifier.presentValue, new Real(0)),
                new PropertyValue(PropertyIdentifier.statusFlags, new StatusFlags(false, false, false, false)),
                new PropertyValue(PropertyIdentifier.updateTime, new DateTime(d1))), notif.get("listOfValues"));

        //
        // Add a pulse. No notification should be sent.
        pc.pulse();
        Thread.sleep(40);
        assertEquals(0, listener.notifs.size());

        //
        // Add another pulse. Now a notification should be sent. Advance the clock to test time remaining
        // and the update time.
        clock.plusMinutes(2);
        pc.pulse();
        Thread.sleep(40);
        assertEquals(1, listener.notifs.size());
        notif = listener.notifs.remove(0);
        assertEquals(new UnsignedInteger(987), notif.get("subscriberProcessIdentifier"));
        assertEquals(d1.getId(), notif.get("initiatingDevice"));
        assertEquals(pc.getId(), notif.get("monitoredObjectIdentifier"));
        assertEquals(new UnsignedInteger(480), notif.get("timeRemaining"));
        assertEquals(new SequenceOf<>(new PropertyValue(PropertyIdentifier.presentValue, new Real(15)),
                new PropertyValue(PropertyIdentifier.statusFlags, new StatusFlags(false, false, false, false)),
                new PropertyValue(PropertyIdentifier.updateTime, new DateTime(d1))), notif.get("listOfValues"));
    }

    @Test
    public void polling() throws Exception {
        // Ensure that before the support is indicated, the property is not writable.
        TestUtils.assertBACnetServiceException(
                () -> pc.writeProperty(null,
                        new PropertyValue(PropertyIdentifier.inputReference,
                                new ObjectPropertyReference(new ObjectIdentifier(ObjectType.accumulator, 0),
                                        PropertyIdentifier.presentValue))),
                ErrorClass.property, ErrorCode.writeAccessDenied);

        pc.supportInputReference(new ObjectPropertyReference(new ObjectIdentifier(ObjectType.accumulator, 0),
                PropertyIdentifier.presentValue), 5000);

        // Advance the clock 6 seconds, to do a poll. The reliability should be in fault.
        clock.plusSeconds(6);
        Thread.sleep(20);
        assertEquals(Reliability.configurationError, pc.get(PropertyIdentifier.reliability));
        assertEquals(new StatusFlags(false, true, false, false), pc.get(PropertyIdentifier.statusFlags));

        // Add the accumulator object. Polling should now succeed.
        final AccumulatorObject a = new AccumulatorObject(d1, 0, "a", 50, 0, EngineeringUnits.amperes, false,
                new Scale(new SignedInteger(10)), new Prescale(new UnsignedInteger(1), new UnsignedInteger(1)), 1000,
                5);
        clock.plusSeconds(5);
        Thread.sleep(20);
        assertEquals(Reliability.noFaultDetected, pc.get(PropertyIdentifier.reliability));
        assertEquals(new StatusFlags(false, false, false, false), pc.get(PropertyIdentifier.statusFlags));

        // Point to a non-existent property.
        pc.writePropertyInternal(PropertyIdentifier.inputReference,
                new ObjectPropertyReference(a.getId(), PropertyIdentifier.stateText));
        clock.plusSeconds(5);
        Thread.sleep(20);
        assertEquals(Reliability.configurationError, pc.get(PropertyIdentifier.reliability));
        assertEquals(new StatusFlags(false, true, false, false), pc.get(PropertyIdentifier.statusFlags));

        // Point to a property of the wrong type.
        pc.writePropertyInternal(PropertyIdentifier.inputReference,
                new ObjectPropertyReference(a.getId(), PropertyIdentifier.objectName));
        clock.plusSeconds(5);
        Thread.sleep(20);
        assertEquals(Reliability.configurationError, pc.get(PropertyIdentifier.reliability));
        assertEquals(new StatusFlags(false, true, false, false), pc.get(PropertyIdentifier.statusFlags));

        // Point back to the correct property.
        pc.writePropertyInternal(PropertyIdentifier.inputReference,
                new ObjectPropertyReference(a.getId(), PropertyIdentifier.presentValue));
        clock.plusSeconds(5);
        Thread.sleep(20);
        assertEquals(Reliability.noFaultDetected, pc.get(PropertyIdentifier.reliability));
        assertEquals(new StatusFlags(false, false, false, false), pc.get(PropertyIdentifier.statusFlags));

        //
        // Try a pulse
        try {
            pc.pulse();
            fail("Should have thrown IllegalStateException");
        } catch (@SuppressWarnings("unused") final IllegalStateException e) {
            // Expected
        }

        //
        // Allow another poll and check the present value.
        assertEquals(new Real(0), pc.get(PropertyIdentifier.presentValue));
        a.pulses(50);
        clock.plusSeconds(5);
        Thread.sleep(20);
        assertEquals(new Real(375), pc.get(PropertyIdentifier.presentValue));

        //
        // Change to out of service, add some pulses, advance the clock, and check that the present
        // value did not change.
        pc.writePropertyInternal(PropertyIdentifier.outOfService, Boolean.TRUE);
        a.pulses(50);
        clock.plusSeconds(5);
        Thread.sleep(20);
        assertEquals(new Real(375), pc.get(PropertyIdentifier.presentValue));

        //
        // Change back to in service, add some pulses, advance the clock, and check that all of the pulses
        // were added in.
        pc.writePropertyInternal(PropertyIdentifier.outOfService, Boolean.FALSE);
        a.pulses(50);
        clock.plusSeconds(5);
        Thread.sleep(20);
        assertEquals(new Real(1125), pc.get(PropertyIdentifier.presentValue));

        // Remove the object.
        d1.removeObject(pc.getId());
    }
}
