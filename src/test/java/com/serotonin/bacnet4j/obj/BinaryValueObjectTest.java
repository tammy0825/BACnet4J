package com.serotonin.bacnet4j.obj;

import static com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier.eventState;
import static com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier.inactiveText;
import static com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier.objectName;
import static com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier.outOfService;
import static com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier.presentValue;
import static com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier.priorityArray;
import static com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier.propertyList;
import static com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier.reliability;
import static com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier.relinquishDefault;
import static com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier.statusFlags;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.TestUtils;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.npdu.test.TestNetwork;
import com.serotonin.bacnet4j.npdu.test.TestNetworkMap;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.constructed.EventTransitionBits;
import com.serotonin.bacnet4j.type.constructed.PriorityArray;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Reliability;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.RequestUtils;

public class BinaryValueObjectTest {
    private final TestNetworkMap map = new TestNetworkMap();
    private final LocalDevice d1 = new LocalDevice(1, new DefaultTransport(new TestNetwork(map, 1, 0)));
    private final LocalDevice d2 = new LocalDevice(2, new DefaultTransport(new TestNetwork(map, 2, 0)));
    private RemoteDevice rd1;
    private BinaryValueObject bv;

    @Before
    public void before() throws Exception {
        d1.initialize();
        d2.initialize();

        rd1 = d2.getRemoteDeviceBlocking(1);

        bv = new BinaryValueObject(d1, 0, "bv", BinaryPV.inactive, true);
        new NotificationClassObject(d1, 17, "nc17", 100, 5, 200, new EventTransitionBits(false, false, false));
    }

    @After
    public void after() throws Exception {
        d1.terminate();
        d2.terminate();
    }

    @Test
    public void initialization() throws Exception {
        new BinaryValueObject(d1, 1, "bvName2", BinaryPV.inactive, false);
    }

    @Test
    public void name() throws Exception {
        // Ensure that the object has the given name.
        CharacterString name = RequestUtils.getProperty(d2, rd1, bv.getId(), objectName);
        assertEquals("bv", name.getValue());

        // Ensure that the name cannot be changed remotely.
        RequestUtils.writeProperty(d2, rd1, bv.getId(), objectName, new CharacterString("goAheadSetThis"));
        name = RequestUtils.getProperty(d2, rd1, bv.getId(), objectName);
        assertEquals("goAheadSetThis", name.getValue());

        // Ensure that the name can be changed locally.
        bv.writePropertyInternal(objectName, new CharacterString("nowThis"));
        name = RequestUtils.getProperty(d2, rd1, bv.getId(), objectName);
        assertEquals("nowThis", name.getValue());
    }

    @Test
    public void statusFlags() throws Exception {
        StatusFlags statusFlags = RequestUtils.getProperty(d2, rd1, bv.getId(), PropertyIdentifier.statusFlags);
        assertEquals(false, statusFlags.isInAlarm());
        assertEquals(false, statusFlags.isFault());
        assertEquals(false, statusFlags.isOverridden());
        assertEquals(true, statusFlags.isOutOfService());

        // Change the overridden value.
        bv.setOverridden(true);
        statusFlags = RequestUtils.getProperty(d2, rd1, bv.getId(), PropertyIdentifier.statusFlags);
        assertEquals(false, statusFlags.isInAlarm());
        assertEquals(false, statusFlags.isFault());
        assertEquals(true, statusFlags.isOverridden());
        assertEquals(true, statusFlags.isOutOfService());

        // Change the reliability value.
        bv.writeProperty(null, reliability, Reliability.communicationFailure);
        statusFlags = RequestUtils.getProperty(d2, rd1, bv.getId(), PropertyIdentifier.statusFlags);
        assertEquals(false, statusFlags.isInAlarm());
        assertEquals(true, statusFlags.isFault());
        assertEquals(true, statusFlags.isOverridden());
        assertEquals(true, statusFlags.isOutOfService());
    }

    @Test
    public void propertyList() throws Exception {
        SequenceOf<PropertyIdentifier> pids = RequestUtils.getProperty(d2, rd1, bv.getId(), propertyList);
        assertEquals(8, pids.getCount());
        assertTrue(pids.contains(presentValue));
        assertTrue(pids.contains(statusFlags));
        assertTrue(pids.contains(eventState));
        assertTrue(pids.contains(outOfService));
        assertTrue(pids.contains(PropertyIdentifier.changeOfStateCount));
        assertTrue(pids.contains(PropertyIdentifier.changeOfStateTime));
        assertTrue(pids.contains(PropertyIdentifier.timeOfStateCountReset));
        assertTrue(pids.contains(PropertyIdentifier.reliability));

        bv.writeProperty(null, inactiveText, new CharacterString("someText"));
        pids = RequestUtils.getProperty(d2, rd1, bv.getId(), propertyList);
        assertEquals(9, pids.getCount());
        assertTrue(pids.contains(presentValue));
        assertTrue(pids.contains(statusFlags));
        assertTrue(pids.contains(eventState));
        assertTrue(pids.contains(outOfService));
        assertTrue(pids.contains(inactiveText));
        assertTrue(pids.contains(PropertyIdentifier.changeOfStateCount));
        assertTrue(pids.contains(PropertyIdentifier.changeOfStateTime));
        assertTrue(pids.contains(PropertyIdentifier.timeOfStateCountReset));
        assertTrue(pids.contains(PropertyIdentifier.reliability));
    }

    @Test
    public void propertyConformanceRequired() throws Exception {
        assertNotNull(bv.getProperty(PropertyIdentifier.objectIdentifier));
        assertNotNull(bv.getProperty(PropertyIdentifier.objectName));
        assertNotNull(bv.getProperty(PropertyIdentifier.objectType));
        assertNotNull(bv.getProperty(PropertyIdentifier.presentValue));
        assertNotNull(bv.getProperty(PropertyIdentifier.statusFlags));
        assertNotNull(bv.getProperty(PropertyIdentifier.eventState));
        assertNotNull(bv.getProperty(PropertyIdentifier.outOfService));
        assertNotNull(bv.getProperty(PropertyIdentifier.propertyList));
    }

    @Test
    public void propertyConformanceEditableWhenOutOfService() throws BACnetServiceException {
        // Should not be writable while in service
        bv.writeProperty(null, PropertyIdentifier.outOfService, Boolean.FALSE);
        TestUtils.assertBACnetServiceException(
                () -> bv.writeProperty(null,
                        new PropertyValue(PropertyIdentifier.presentValue, null, BinaryPV.active, null)),
                ErrorClass.property, ErrorCode.writeAccessDenied);
        TestUtils.assertBACnetServiceException(
                () -> bv.writeProperty(null,
                        new PropertyValue(PropertyIdentifier.reliability, null, Reliability.overRange, null)),
                ErrorClass.property, ErrorCode.writeAccessDenied);

        // Should be writable while out of service.
        bv.writeProperty(null, PropertyIdentifier.outOfService, Boolean.TRUE);
        bv.writeProperty(null, new PropertyValue(PropertyIdentifier.presentValue, null, BinaryPV.active, null));
        bv.writeProperty(null, new PropertyValue(PropertyIdentifier.reliability, null, Reliability.overRange, null));
    }

    @Test
    public void propertyConformanceReadOnly() {
        TestUtils.assertBACnetServiceException(
                () -> bv.writeProperty(null,
                        new PropertyValue(PropertyIdentifier.ackedTransitions, new UnsignedInteger(2),
                                new CharacterString("should fail"), null)),
                ErrorClass.property, ErrorCode.writeAccessDenied);
        TestUtils.assertBACnetServiceException(
                () -> bv.writeProperty(null,
                        new PropertyValue(PropertyIdentifier.eventTimeStamps, new UnsignedInteger(2),
                                new CharacterString("should fail"), null)),
                ErrorClass.property, ErrorCode.writeAccessDenied);
        TestUtils.assertBACnetServiceException(
                () -> bv.writeProperty(null,
                        new PropertyValue(PropertyIdentifier.eventMessageTexts, new UnsignedInteger(2),
                                new CharacterString("should fail"), null)),
                ErrorClass.property, ErrorCode.writeAccessDenied);
    }

    @Test
    public void propertyConformanceRequiredWhenIntrinsicReporting() throws Exception {
        bv.supportIntrinsicReporting(30, 17, BinaryPV.active, new EventTransitionBits(true, true, true),
                NotifyType.alarm, 60);
        assertNotNull(bv.getProperty(PropertyIdentifier.timeDelay));
        assertNotNull(bv.getProperty(PropertyIdentifier.notificationClass));
        assertNotNull(bv.getProperty(PropertyIdentifier.alarmValue));
        assertNotNull(bv.getProperty(PropertyIdentifier.eventEnable));
        assertNotNull(bv.getProperty(PropertyIdentifier.ackedTransitions));
        assertNotNull(bv.getProperty(PropertyIdentifier.notifyType));
        assertNotNull(bv.getProperty(PropertyIdentifier.eventTimeStamps));
        assertNotNull(bv.getProperty(PropertyIdentifier.eventDetectionEnable));
    }

    @Test
    public void propertyConformanceForbiddenWhenNotIntrinsicReporting() throws Exception {
        assertNull(bv.getProperty(PropertyIdentifier.timeDelay));
        assertNull(bv.getProperty(PropertyIdentifier.notificationClass));
        assertNull(bv.getProperty(PropertyIdentifier.alarmValue));
        assertNull(bv.getProperty(PropertyIdentifier.eventEnable));
        assertNull(bv.getProperty(PropertyIdentifier.ackedTransitions));
        assertNull(bv.getProperty(PropertyIdentifier.notifyType));
        assertNull(bv.getProperty(PropertyIdentifier.eventTimeStamps));
        assertNull(bv.getProperty(PropertyIdentifier.eventMessageTexts));
        assertNull(bv.getProperty(PropertyIdentifier.eventMessageTextsConfig));
        assertNull(bv.getProperty(PropertyIdentifier.eventDetectionEnable));
        assertNull(bv.getProperty(PropertyIdentifier.eventAlgorithmInhibitRef));
        assertNull(bv.getProperty(PropertyIdentifier.eventAlgorithmInhibit));
        assertNull(bv.getProperty(PropertyIdentifier.timeDelayNormal));
    }

    @Test
    public void presentValue() throws Exception {
        // The non-commandable tests

        // Default value is inactive
        BinaryPV pv = RequestUtils.getProperty(d2, rd1, bv.getId(), presentValue);
        assertEquals(BinaryPV.inactive, pv);

        // When overridden, the present value is not settable
        bv.setOverridden(true);
        bv.writePropertyInternal(outOfService, Boolean.FALSE);
        TestUtils.assertErrorAPDUException(
                () -> RequestUtils.writeProperty(d2, rd1, bv.getId(), presentValue, BinaryPV.active),
                ErrorClass.property, ErrorCode.writeAccessDenied);

        // ... even when it is out of service.
        bv.setOverridden(true);
        bv.writePropertyInternal(outOfService, Boolean.TRUE);
        TestUtils.assertErrorAPDUException(
                () -> RequestUtils.writeProperty(d2, rd1, bv.getId(), presentValue, BinaryPV.active),
                ErrorClass.property, ErrorCode.writeAccessDenied);

        // When not overridden, the present value is not settable while not out of service.
        bv.setOverridden(false);
        bv.writePropertyInternal(outOfService, Boolean.FALSE);
        TestUtils.assertErrorAPDUException(
                () -> RequestUtils.writeProperty(d2, rd1, bv.getId(), presentValue, BinaryPV.active),
                ErrorClass.property, ErrorCode.writeAccessDenied);

        // ... but it is when the object is out of service.
        bv.setOverridden(false);
        bv.writePropertyInternal(outOfService, Boolean.TRUE);
        RequestUtils.writeProperty(d2, rd1, bv.getId(), presentValue, BinaryPV.active);
        pv = RequestUtils.getProperty(d2, rd1, bv.getId(), presentValue);
        assertEquals(BinaryPV.active, pv);
    }

    @Test
    public void commandablePresentValue() throws Exception {
        // The commandable tests
        bv.supportCommandable(BinaryPV.inactive);

        // Default value is inactive
        BinaryPV pv = RequestUtils.getProperty(d2, rd1, bv.getId(), presentValue);
        assertEquals(BinaryPV.inactive, pv);

        // When overridden, the present value is not settable
        bv.setOverridden(true);
        bv.writePropertyInternal(outOfService, Boolean.FALSE);
        TestUtils.assertErrorAPDUException(
                () -> RequestUtils.writeProperty(d2, rd1, bv.getId(), presentValue, BinaryPV.active),
                ErrorClass.property, ErrorCode.writeAccessDenied);

        // ... even when it is out of service.
        bv.setOverridden(true);
        bv.writePropertyInternal(outOfService, Boolean.TRUE);
        TestUtils.assertErrorAPDUException(
                () -> RequestUtils.writeProperty(d2, rd1, bv.getId(), presentValue, BinaryPV.active),
                ErrorClass.property, ErrorCode.writeAccessDenied);

        // When not overridden, the present value is writable while not out of service.
        bv.setOverridden(false);
        bv.writePropertyInternal(outOfService, Boolean.TRUE);
        RequestUtils.writeProperty(d2, rd1, bv.getId(), presentValue, BinaryPV.active);
        pv = RequestUtils.getProperty(d2, rd1, bv.getId(), presentValue);
        assertEquals(BinaryPV.active, pv);
        assertEquals(BinaryPV.active, bv.properties.get(presentValue));

        // When not overridden and in service, the present value is commandable
        bv.setOverridden(false);
        bv.writePropertyInternal(outOfService, Boolean.FALSE);

        // Set a value at priority 16.
        RequestUtils.writeProperty(d2, rd1, bv.getId(), presentValue, BinaryPV.inactive);
        // Ensure the priority array looks right.
        assertEquals(new PriorityArray().put(16, BinaryPV.inactive), bv.getProperty(priorityArray));
        // Ensure the present value looks right.
        assertEquals(BinaryPV.inactive, bv.getProperty(presentValue));
        // Ensure the present value looks right when read via service.
        assertEquals(BinaryPV.inactive, RequestUtils.getProperty(d2, rd1, bv.getId(), presentValue));

        // Set a value at priority 15.
        RequestUtils.writeProperty(d2, rd1, bv.getId(), presentValue, BinaryPV.active, 15);
        // Ensure the priority array looks right.
        assertEquals(new PriorityArray().put(15, BinaryPV.active).put(16, BinaryPV.inactive),
                bv.getProperty(priorityArray));
        // Ensure the present value looks right.
        assertEquals(BinaryPV.active, bv.getProperty(presentValue));
        // Ensure the present value looks right when read via service.
        assertEquals(BinaryPV.active, RequestUtils.getProperty(d2, rd1, bv.getId(), presentValue));

        // Relinquish at 16
        RequestUtils.writeProperty(d2, rd1, bv.getId(), presentValue, Null.instance);
        // Ensure the priority array looks right.
        assertEquals(new PriorityArray().put(15, BinaryPV.active), bv.getProperty(priorityArray));
        // Ensure the present value looks right.
        assertEquals(BinaryPV.active, bv.getProperty(presentValue));
        // Ensure the present value looks right when read via service.
        assertEquals(BinaryPV.active, RequestUtils.getProperty(d2, rd1, bv.getId(), presentValue));

        // Relinquish at priority 15.
        RequestUtils.writeProperty(d2, rd1, bv.getId(), presentValue, Null.instance, 15);
        // Ensure the priority array looks right.
        assertEquals(new PriorityArray(), bv.getProperty(priorityArray));
        // Ensure the present value looks right.
        assertEquals(BinaryPV.inactive, bv.getProperty(presentValue));
        // Ensure the present value looks right when read via service.
        assertEquals(BinaryPV.inactive, RequestUtils.getProperty(d2, rd1, bv.getId(), presentValue));

        // Change the relinquish default
        RequestUtils.writeProperty(d2, rd1, bv.getId(), relinquishDefault, BinaryPV.active);
        // Ensure the relinquish default looks right.
        assertEquals(BinaryPV.active, bv.getProperty(relinquishDefault));
        // Ensure the present value looks right.
        assertEquals(BinaryPV.active, bv.getProperty(presentValue));
        // Ensure the present value looks right when read via service.
        assertEquals(BinaryPV.active, RequestUtils.getProperty(d2, rd1, bv.getId(), presentValue));
    }
}
