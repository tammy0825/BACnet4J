package com.serotonin.bacnet4j.obj.mixin;

import java.util.List;
import java.util.Map;

import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.error.ErrorClassAndCode;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.DeviceObjectPropertyReferences;
import com.serotonin.bacnet4j.util.DevicesObjectPropertyValues;
import com.serotonin.bacnet4j.util.PropertyReferences;
import com.serotonin.bacnet4j.util.PropertyUtils;

public class PollingDelegate {
    private final BACnetObject owner;
    private final PropertyReferences localReferences;
    private final DeviceObjectPropertyReferences remoteReferences;

    public PollingDelegate(final BACnetObject bo, final DeviceObjectPropertyReferences polledReferences) {
        this.owner = bo;

        // Split the given references into local and remote.
        localReferences = new PropertyReferences();
        remoteReferences = new DeviceObjectPropertyReferences();
        for (final Map.Entry<Integer, PropertyReferences> deviceRefs : polledReferences.getProperties().entrySet()) {
            if (deviceRefs.getKey() == bo.getLocalDevice().getInstanceNumber()) {
                // Local references
                localReferences.add(deviceRefs.getValue());
            } else {
                // Remote references
                remoteReferences.add(deviceRefs.getKey(), deviceRefs.getValue());
            }
        }
    }

    public DevicesObjectPropertyValues doPoll() {
        // Get the remote properties first. If there are no remote properties this will return an empty values object.
        final DevicesObjectPropertyValues result = PropertyUtils.readProperties(owner.getLocalDevice(),
                remoteReferences, null);

        for (final Map.Entry<ObjectIdentifier, List<PropertyReference>> oidRefs : localReferences.getProperties()
                .entrySet()) {
            final BACnetObject localObject = owner.getLocalDevice().getObject(oidRefs.getKey());
            if (localObject == null) {
                // Add errors for each of the references
                final ErrorClassAndCode ecac = new ErrorClassAndCode(ErrorClass.object, ErrorCode.unknownObject);
                for (final PropertyReference ref : oidRefs.getValue()) {
                    result.add(owner.getLocalDevice().getInstanceNumber(), oidRefs.getKey(),
                            ref.getPropertyIdentifier(), ref.getPropertyArrayIndex(), ecac);
                }
            } else {
                for (final PropertyReference ref : oidRefs.getValue()) {
                    Encodable value;
                    try {
                        value = localObject.getProperty(ref.getPropertyIdentifier(), ref.getPropertyArrayIndex());
                    } catch (final BACnetServiceException e) {
                        value = new ErrorClassAndCode(e);
                    }
                    result.add(owner.getLocalDevice().getInstanceNumber(), oidRefs.getKey(),
                            ref.getPropertyIdentifier(), ref.getPropertyArrayIndex(), value);
                }
            }
        }

        return result;
    }
}
