package com.serotonin.bacnet4j.obj;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.enums.MaxApduLength;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.obj.mixin.ActiveCovSubscriptionMixin;
import com.serotonin.bacnet4j.obj.mixin.ObjectListMixin;
import com.serotonin.bacnet4j.obj.mixin.ReadOnlyPropertyMixin;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.AddressBinding;
import com.serotonin.bacnet4j.type.constructed.BACnetArray;
import com.serotonin.bacnet4j.type.constructed.CovSubscription;
import com.serotonin.bacnet4j.type.constructed.ObjectTypesSupported;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import com.serotonin.bacnet4j.type.enumerated.DeviceStatus;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class DeviceObject extends BACnetObject {
    private static final int VENDOR_ID = 236; // Serotonin Software

    public DeviceObject(final LocalDevice localDevice, final int instanceNumber) throws BACnetServiceException {
        super(localDevice, ObjectType.device, instanceNumber, "Device " + instanceNumber);

        writePropertyInternal(PropertyIdentifier.maxApduLengthAccepted,
                new UnsignedInteger(MaxApduLength.UP_TO_1476.getMaxLengthInt()));
        writePropertyInternal(PropertyIdentifier.vendorIdentifier, new UnsignedInteger(VENDOR_ID));
        writePropertyInternal(PropertyIdentifier.vendorName,
                new CharacterString("Serotonin Software Technologies, Inc."));
        writePropertyInternal(PropertyIdentifier.segmentationSupported, Segmentation.segmentedBoth);
        writePropertyInternal(PropertyIdentifier.maxSegmentsAccepted, new UnsignedInteger(1000));
        writePropertyInternal(PropertyIdentifier.apduSegmentTimeout,
                new UnsignedInteger(Transport.DEFAULT_SEG_TIMEOUT));
        writePropertyInternal(PropertyIdentifier.apduTimeout, new UnsignedInteger(Transport.DEFAULT_TIMEOUT));
        writePropertyInternal(PropertyIdentifier.numberOfApduRetries, new UnsignedInteger(Transport.DEFAULT_RETRIES));
        writePropertyInternal(PropertyIdentifier.deviceAddressBinding, new SequenceOf<AddressBinding>());
        writePropertyInternal(PropertyIdentifier.activeCovSubscriptions, new SequenceOf<CovSubscription>());
        writePropertyInternal(PropertyIdentifier.objectList, new BACnetArray<ObjectIdentifier>());

        // Set up the supported services indicators. Remove lines as services get implemented.
        final ServicesSupported servicesSupported = new ServicesSupported();
        servicesSupported.setAcknowledgeAlarm(true);
        servicesSupported.setConfirmedCovNotification(true);
        servicesSupported.setConfirmedEventNotification(true);
        servicesSupported.setGetAlarmSummary(true);
        servicesSupported.setGetEnrollmentSummary(true);
        servicesSupported.setSubscribeCov(true);
        //        servicesSupported.setAtomicReadFile(true);
        //        servicesSupported.setAtomicWriteFile(true);
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
        //        servicesSupported.setLifeSafetyOperation(true);
        servicesSupported.setSubscribeCovProperty(true);
        servicesSupported.setGetEventInformation(true);
        //        servicesSupported.setWriteGroup(true);
        servicesSupported.setSubscribeCovPropertyMultiple(true);
        servicesSupported.setConfirmedCovNotificationMultiple(true);
        servicesSupported.setUnconfirmedCovNotificationMultiple(true);

        writePropertyInternal(PropertyIdentifier.protocolServicesSupported, servicesSupported);

        // Set up the object types supported.
        final ObjectTypesSupported objectTypesSupported = new ObjectTypesSupported();
        objectTypesSupported.setAll(true);
        writePropertyInternal(PropertyIdentifier.protocolObjectTypesSupported, objectTypesSupported);

        // Set some other required values to defaults
        writePropertyInternal(PropertyIdentifier.objectName, new CharacterString("BACnet4J device"));
        writePropertyInternal(PropertyIdentifier.systemStatus, DeviceStatus.operational);
        writePropertyInternal(PropertyIdentifier.modelName, new CharacterString("BACnet4J"));
        writePropertyInternal(PropertyIdentifier.firmwareRevision, new CharacterString("not set"));
        writePropertyInternal(PropertyIdentifier.applicationSoftwareVersion, new CharacterString("4.0.0"));
        writePropertyInternal(PropertyIdentifier.protocolVersion, new UnsignedInteger(1));
        writePropertyInternal(PropertyIdentifier.protocolRevision, new UnsignedInteger(19));
        writePropertyInternal(PropertyIdentifier.databaseRevision, new UnsignedInteger(0));

        // Mixins
        addMixin(new ActiveCovSubscriptionMixin(this));
        addMixin(new ReadOnlyPropertyMixin(this, PropertyIdentifier.activeCovSubscriptions));
        addMixin(new ObjectListMixin(this));
    }
}
