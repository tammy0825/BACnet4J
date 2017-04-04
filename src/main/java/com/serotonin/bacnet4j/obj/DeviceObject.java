package com.serotonin.bacnet4j.obj;

import java.util.Objects;
import java.util.TimeZone;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.enums.MaxApduLength;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.obj.mixin.ActiveCovSubscriptionMixin;
import com.serotonin.bacnet4j.obj.mixin.HasStatusFlagsMixin;
import com.serotonin.bacnet4j.obj.mixin.ObjectListMixin;
import com.serotonin.bacnet4j.obj.mixin.ReadOnlyPropertyMixin;
import com.serotonin.bacnet4j.obj.mixin.TimeSynchronizationMixin;
import com.serotonin.bacnet4j.obj.mixin.event.IntrinsicReportingMixin;
import com.serotonin.bacnet4j.obj.mixin.event.eventAlgo.NoneAlgo;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.AddressBinding;
import com.serotonin.bacnet4j.type.constructed.BACnetArray;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.constructed.EventTransitionBits;
import com.serotonin.bacnet4j.type.constructed.ObjectTypesSupported;
import com.serotonin.bacnet4j.type.constructed.Recipient;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.enumerated.BackupState;
import com.serotonin.bacnet4j.type.enumerated.DeviceStatus;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Reliability;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Date;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.bacnet4j.type.primitive.Time;
import com.serotonin.bacnet4j.type.primitive.Unsigned16;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class DeviceObject extends BACnetObject {
    private static final int VENDOR_ID = 236; // Serotonin Software

    public DeviceObject(final LocalDevice localDevice, final int instanceNumber) throws BACnetServiceException {
        super(localDevice, ObjectType.device, instanceNumber, "BACnet4J device");

        writePropertyInternal(PropertyIdentifier.maxApduLengthAccepted,
                new UnsignedInteger(MaxApduLength.UP_TO_1476.getMaxLengthInt()));
        writePropertyInternal(PropertyIdentifier.vendorIdentifier, new UnsignedInteger(VENDOR_ID));
        writePropertyInternal(PropertyIdentifier.vendorName,
                new CharacterString("Serotonin Software Technologies, Inc."));
        writePropertyInternal(PropertyIdentifier.segmentationSupported, Segmentation.segmentedBoth);
        writePropertyInternal(PropertyIdentifier.maxSegmentsAccepted, new UnsignedInteger(Integer.MAX_VALUE));
        writePropertyInternal(PropertyIdentifier.apduSegmentTimeout,
                new UnsignedInteger(Transport.DEFAULT_SEG_TIMEOUT));
        writePropertyInternal(PropertyIdentifier.apduTimeout, new UnsignedInteger(Transport.DEFAULT_TIMEOUT));
        writePropertyInternal(PropertyIdentifier.numberOfApduRetries, new UnsignedInteger(Transport.DEFAULT_RETRIES));
        writePropertyInternal(PropertyIdentifier.objectList, new BACnetArray<ObjectIdentifier>());

        // Set up the supported services indicators. Remove lines as services get implemented.
        final ServicesSupported servicesSupported = new ServicesSupported();
        servicesSupported.setAcknowledgeAlarm(true);
        servicesSupported.setConfirmedCovNotification(true);
        servicesSupported.setConfirmedEventNotification(true);
        servicesSupported.setGetAlarmSummary(true);
        servicesSupported.setGetEnrollmentSummary(true);
        servicesSupported.setSubscribeCov(true);
        servicesSupported.setAtomicReadFile(true);
        servicesSupported.setAtomicWriteFile(true);
        servicesSupported.setAddListElement(true);
        servicesSupported.setRemoveListElement(true);
        servicesSupported.setCreateObject(true);
        servicesSupported.setDeleteObject(true);
        servicesSupported.setReadProperty(true);
        servicesSupported.setReadPropertyMultiple(true);
        servicesSupported.setWriteProperty(true);
        servicesSupported.setWritePropertyMultiple(true);
        servicesSupported.setDeviceCommunicationControl(true);
        servicesSupported.setConfirmedPrivateTransfer(true);
        servicesSupported.setConfirmedTextMessage(true);
        servicesSupported.setReinitializeDevice(true);
        //        servicesSupported.setVtOpen(true);
        //        servicesSupported.setVtClose(true);
        //        servicesSupported.setVtData(true);
        servicesSupported.setIAm(true);
        servicesSupported.setIHave(true);
        servicesSupported.setUnconfirmedCovNotification(true);
        servicesSupported.setUnconfirmedEventNotification(true);
        servicesSupported.setUnconfirmedPrivateTransfer(true);
        servicesSupported.setUnconfirmedTextMessage(true);
        servicesSupported.setTimeSynchronization(true);
        servicesSupported.setWhoHas(true);
        servicesSupported.setWhoIs(true);
        servicesSupported.setReadRange(true);
        servicesSupported.setUtcTimeSynchronization(true);
        servicesSupported.setLifeSafetyOperation(true);
        servicesSupported.setSubscribeCovProperty(true);
        servicesSupported.setGetEventInformation(true);
        //        servicesSupported.setWriteGroup(true);
        servicesSupported.setSubscribeCovPropertyMultiple(true);
        servicesSupported.setConfirmedCovNotificationMultiple(true);
        servicesSupported.setUnconfirmedCovNotificationMultiple(true);

        writePropertyInternal(PropertyIdentifier.protocolServicesSupported, servicesSupported);

        // Set up the object types supported.
        final ObjectTypesSupported objectTypesSupported = new ObjectTypesSupported();
        objectTypesSupported.setAnalogInput(true);
        objectTypesSupported.setAnalogOutput(true);
        objectTypesSupported.setAnalogValue(true);
        objectTypesSupported.setBinaryInput(true);
        objectTypesSupported.setBinaryOutput(true);
        objectTypesSupported.setBinaryValue(true);
        objectTypesSupported.setCalendar(true);
        //        objectTypesSupported.setCommand(true);
        objectTypesSupported.setDevice(true);
        objectTypesSupported.setEventEnrollment(true);
        objectTypesSupported.setFile(true);
        objectTypesSupported.setGroup(true);
        //        objectTypesSupported.setLoop(true);
        objectTypesSupported.setMultiStateInput(true);
        objectTypesSupported.setMultiStateOutput(true);
        objectTypesSupported.setNotificationClass(true);
        //        objectTypesSupported.setProgram(true);
        objectTypesSupported.setSchedule(true);
        //        objectTypesSupported.setAveraging(true);
        objectTypesSupported.setMultiStateValue(true);
        objectTypesSupported.setTrendLog(true);
        objectTypesSupported.setLifeSafetyPoint(true);
        objectTypesSupported.setLifeSafetyZone(true);
        objectTypesSupported.setAccumulator(true);
        objectTypesSupported.setPulseConverter(true);
        objectTypesSupported.setEventLog(true);
        //        objectTypesSupported.setGlobalGroup(true);
        objectTypesSupported.setTrendLogMultiple(true);
        //        objectTypesSupported.setLoadControl(true);
        //        objectTypesSupported.setStructuredView(true);
        //        objectTypesSupported.setAccessDoor(true);
        //        objectTypesSupported.setTimer(true);
        //        objectTypesSupported.setAccessCredential(true);
        //        objectTypesSupported.setAccessPoint(true);
        //        objectTypesSupported.setAccessRights(true);
        //        objectTypesSupported.setAccessUser(true);
        //        objectTypesSupported.setAccessZone(true);
        //        objectTypesSupported.setCredentialDataInput(true);
        //        objectTypesSupported.setNetworkSecurity(true);
        //        objectTypesSupported.setBitstringValue(true);
        //        objectTypesSupported.setCharacterstringValue(true);
        //        objectTypesSupported.setDatePatternValue(true);
        //        objectTypesSupported.setDateValue(true);
        //        objectTypesSupported.setDatetimePatternValue(true);
        //        objectTypesSupported.setDatetimeValue(true);
        //        objectTypesSupported.setIntegerValue(true);
        //        objectTypesSupported.setLargeAnalogValue(true);
        //        objectTypesSupported.setOctetstringValue(true);
        //        objectTypesSupported.setPositiveIntegerValue(true);
        //        objectTypesSupported.setTimePatternValue(true);
        //        objectTypesSupported.setTimeValue(true);
        objectTypesSupported.setNotificationForwarder(true);
        objectTypesSupported.setAlertEnrollment(true);
        //        objectTypesSupported.setChannel(true);
        //        objectTypesSupported.setLightingOutput(true);
        //        objectTypesSupported.setBinaryLightingOutput(true);
        //        objectTypesSupported.setNetworkPort(true);
        //        objectTypesSupported.setElevatorGroup(true);
        //        objectTypesSupported.setEscalator(true);
        //        objectTypesSupported.setLift(true);

        writePropertyInternal(PropertyIdentifier.protocolObjectTypesSupported, objectTypesSupported);

        // Set some other required values to defaults
        writePropertyInternal(PropertyIdentifier.systemStatus, DeviceStatus.operational);
        writePropertyInternal(PropertyIdentifier.modelName, new CharacterString("BACnet4J"));
        writePropertyInternal(PropertyIdentifier.firmwareRevision, new CharacterString("not set"));
        writePropertyInternal(PropertyIdentifier.applicationSoftwareVersion, new CharacterString(LocalDevice.VERSION));
        writePropertyInternal(PropertyIdentifier.protocolVersion, new UnsignedInteger(1));
        writePropertyInternal(PropertyIdentifier.protocolRevision, new UnsignedInteger(19));

        UnsignedInteger databaseRevision = getLocalDevice().getPersistence()
                .loadEncodable(getPersistenceKey(PropertyIdentifier.databaseRevision), UnsignedInteger.class);
        if (databaseRevision == null)
            databaseRevision = UnsignedInteger.ZERO;
        writePropertyInternal(PropertyIdentifier.databaseRevision, databaseRevision);

        writePropertyInternal(PropertyIdentifier.timeOfDeviceRestart, new TimeStamp(new DateTime(getLocalDevice())));
        writePropertyInternal(PropertyIdentifier.eventState, EventState.normal);
        writePropertyInternal(PropertyIdentifier.statusFlags, new StatusFlags(false, false, false, false));
        writePropertyInternal(PropertyIdentifier.reliability, Reliability.noFaultDetected);
        writePropertyInternal(PropertyIdentifier.configurationFiles, new BACnetArray<>(0, null));
        writePropertyInternal(PropertyIdentifier.lastRestoreTime, new TimeStamp(DateTime.UNSPECIFIED));
        writePropertyInternal(PropertyIdentifier.backupFailureTimeout, new Unsigned16(60));
        writePropertyInternal(PropertyIdentifier.backupPreparationTime, new Unsigned16(0));
        writePropertyInternal(PropertyIdentifier.restorePreparationTime, new Unsigned16(0));
        writePropertyInternal(PropertyIdentifier.restoreCompletionTime, new Unsigned16(0));
        writePropertyInternal(PropertyIdentifier.backupAndRestoreState, BackupState.idle);

        // Mixins
        addMixin(new ActiveCovSubscriptionMixin(this));
        addMixin(new HasStatusFlagsMixin(this));
        addMixin(new ReadOnlyPropertyMixin(this, PropertyIdentifier.activeCovSubscriptions,
                PropertyIdentifier.localTime, PropertyIdentifier.localDate, PropertyIdentifier.deviceAddressBinding));
        addMixin(new ObjectListMixin(this));
    }

    public DeviceObject supportTimeSynchronization(final SequenceOf<Recipient> timeSynchronizationRecipients,
            final SequenceOf<Recipient> utcTimeSynchronizationRecipients, final int timeSynchronizationInterval,
            final boolean alignIntervals, final int intervalOffset) {
        final TimeSynchronizationMixin m = new TimeSynchronizationMixin(this, timeSynchronizationRecipients,
                utcTimeSynchronizationRecipients, timeSynchronizationInterval, alignIntervals, intervalOffset);
        addMixin(m);
        m.update();
        return this;
    }

    public DeviceObject supportIntrinsicReporting(final int notificationClass, final EventTransitionBits eventEnable,
            final NotifyType notifyType) {
        Objects.requireNonNull(eventEnable);
        Objects.requireNonNull(notifyType);

        writePropertyInternal(PropertyIdentifier.notificationClass, new UnsignedInteger(notificationClass));
        writePropertyInternal(PropertyIdentifier.eventEnable, eventEnable);
        writePropertyInternal(PropertyIdentifier.notifyType, notifyType);
        writePropertyInternal(PropertyIdentifier.eventDetectionEnable, Boolean.TRUE);

        addMixin(new IntrinsicReportingMixin(this, new NoneAlgo(), null, null, new PropertyIdentifier[] {}));

        return this;
    }

    @Override
    protected void beforeGetProperty(final PropertyIdentifier pid) {
        if (pid.equals(PropertyIdentifier.localTime)) {
            set(PropertyIdentifier.localTime, new Time(getLocalDevice()));
        } else if (pid.equals(PropertyIdentifier.localDate)) {
            set(PropertyIdentifier.localDate, new Date(getLocalDevice()));
        } else if (pid.equals(PropertyIdentifier.utcOffset)) {
            final int offsetMillis = TimeZone.getDefault().getOffset(getLocalDevice().getClock().millis());
            writePropertyInternal(PropertyIdentifier.utcOffset, new SignedInteger(offsetMillis / 1000 / 60));
        } else if (pid.equals(PropertyIdentifier.daylightSavingsStatus)) {
            final boolean dst = TimeZone.getDefault()
                    .inDaylightTime(new java.util.Date(getLocalDevice().getClock().millis()));
            writePropertyInternal(PropertyIdentifier.daylightSavingsStatus, Boolean.valueOf(dst));
        } else if (pid.equals(PropertyIdentifier.deviceAddressBinding)) {
            final SequenceOf<AddressBinding> bindings = new SequenceOf<>();
            for (final RemoteDevice d : getLocalDevice().getRemoteDevices()) {
                bindings.add(new AddressBinding(d.getObjectIdentifier(), d.getAddress()));
            }
            writePropertyInternal(PropertyIdentifier.deviceAddressBinding, bindings);
        }
    }

    @Override
    protected void afterWriteProperty(final PropertyIdentifier pid, final Encodable oldValue,
            final Encodable newValue) {
        if (pid.equals(PropertyIdentifier.restartNotificationRecipients)) {
            // Persist the new list.
            getLocalDevice().getPersistence()
                    .saveEncodable(getPersistenceKey(PropertyIdentifier.restartNotificationRecipients), newValue);
        }
    }
}
