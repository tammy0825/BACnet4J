package com.serotonin.bacnet4j.obj.mixin;

import com.serotonin.bacnet4j.obj.AbstractMixin;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;

public class ObjectIdAndNameMixin extends AbstractMixin {
    public ObjectIdAndNameMixin(final BACnetObject owner) {
        super(owner);
    }

    @Override
    protected void afterWriteProperty(final PropertyIdentifier pid, final Encodable oldValue,
            final Encodable newValue) {
        if (pid.isOneOf(PropertyIdentifier.objectIdentifier, PropertyIdentifier.objectName)) {
            getLocalDevice().incrementDatabaseRevision();
        }
    }
}
