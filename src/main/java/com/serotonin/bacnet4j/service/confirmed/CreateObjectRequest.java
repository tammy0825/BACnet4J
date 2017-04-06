/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2015 Infinite Automation Software. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * When signing a commercial license with Infinite Automation Software,
 * the following extension to GPL is made. A special exception to the GPL is
 * included to allow you to distribute a combined work that includes BAcnet4J
 * without being obliged to provide the source code for any proprietary components.
 *
 * See www.infiniteautomation.com for commercial license options.
 *
 * @author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.service.confirmed;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.obj.GroupObject;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.acknowledgement.CreateObjectAck;
import com.serotonin.bacnet4j.type.ThreadLocalObjectTypeStack;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.constructed.ChoiceOptions;
import com.serotonin.bacnet4j.type.constructed.ObjectTypesSupported;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.ReadAccessSpecification;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.error.CreateObjectError;
import com.serotonin.bacnet4j.type.error.ErrorClassAndCode;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

public class CreateObjectRequest extends ConfirmedRequestService {
    static final Logger LOG = LoggerFactory.getLogger(CreateObjectRequest.class);

    public static final byte TYPE_ID = 10;

    private static ChoiceOptions choiceOptions = new ChoiceOptions();
    static {
        choiceOptions.addContextual(0, ObjectType.class);
        choiceOptions.addContextual(1, ObjectIdentifier.class);
    }

    private static Map<ObjectType, ObjectCreator> creators = new HashMap<>();
    static {
        creators.put(ObjectType.group, new GroupCreator());
    }

    private final Choice objectSpecifier;
    private final SequenceOf<PropertyValue> listOfInitialValues;

    public CreateObjectRequest(final ObjectType objectType, final SequenceOf<PropertyValue> listOfInitialValues) {
        objectSpecifier = new Choice(0, objectType, choiceOptions);
        this.listOfInitialValues = listOfInitialValues;
    }

    public CreateObjectRequest(final ObjectIdentifier objectIdentifier,
            final SequenceOf<PropertyValue> listOfInitialValues) {
        objectSpecifier = new Choice(1, objectIdentifier, choiceOptions);
        this.listOfInitialValues = listOfInitialValues;
    }

    CreateObjectRequest(final ByteQueue queue) throws BACnetException {
        objectSpecifier = readChoice(queue, choiceOptions, 0);
        try {
            ThreadLocalObjectTypeStack.set(getObjectType());
            listOfInitialValues = readOptionalSequenceOf(queue, PropertyValue.class, 1);
        } finally {
            ThreadLocalObjectTypeStack.remove();
        }
    }

    @Override
    public void write(final ByteQueue queue) {
        write(queue, objectSpecifier, 0);
        writeOptional(queue, listOfInitialValues, 1);
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    private ObjectType getObjectType() {
        if (objectSpecifier.isa(ObjectType.class)) {
            return objectSpecifier.getDatum();
        }
        final ObjectIdentifier oid = objectSpecifier.getDatum();
        return oid.getObjectType();
    }

    @Override
    public AcknowledgementService handle(final LocalDevice localDevice, final Address from) throws BACnetException {
        final ObjectType objectType;
        int instanceNumber;

        if (objectSpecifier.isa(ObjectType.class)) {
            objectType = objectSpecifier.getDatum();
            instanceNumber = -1;
        } else {
            final ObjectIdentifier oid = objectSpecifier.getDatum();
            objectType = oid.getObjectType();
            instanceNumber = oid.getInstanceNumber();
        }

        // Get the list of object types supported by the local device.
        final ObjectTypesSupported objectTypesSupported = localDevice.getDeviceObject()
                .get(PropertyIdentifier.protocolObjectTypesSupported);
        if (!objectTypesSupported.is(objectType)) {
            throw createException(ErrorClass.object, ErrorCode.unsupportedObjectType, 0);
        }

        // Find the creator
        final ObjectCreator creator = creators.get(objectType);
        if (creator == null) {
            throw createException(ErrorClass.object, ErrorCode.dynamicCreationNotSupported, 0);
        }

        // Get a new instance number if needed.
        if (instanceNumber == -1) {
            instanceNumber = localDevice.getNextInstanceObjectNumber(objectType);
        }

        final BACnetObject bo = creator.create(localDevice, instanceNumber, listOfInitialValues);

        // Dynamically created objects can also be deleted.
        bo.setDeletable(true);

        return new CreateObjectAck(bo.getId());
    }

    private static BACnetErrorException createException(final ErrorClass errorClass, final ErrorCode errorCode,
            final int firstFailedElementNumber) {
        return new BACnetErrorException(TYPE_ID, new CreateObjectError(new ErrorClassAndCode(errorClass, errorCode),
                new UnsignedInteger(firstFailedElementNumber)));
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + (listOfInitialValues == null ? 0 : listOfInitialValues.hashCode());
        result = PRIME * result + (objectSpecifier == null ? 0 : objectSpecifier.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final CreateObjectRequest other = (CreateObjectRequest) obj;
        if (listOfInitialValues == null) {
            if (other.listOfInitialValues != null)
                return false;
        } else if (!listOfInitialValues.equals(other.listOfInitialValues))
            return false;
        if (objectSpecifier == null) {
            if (other.objectSpecifier != null)
                return false;
        } else if (!objectSpecifier.equals(other.objectSpecifier))
            return false;
        return true;
    }

    public static interface ObjectCreator {
        BACnetObject create(LocalDevice d, int instanceNumber, final SequenceOf<PropertyValue> listOfInitialValues)
                throws BACnetErrorException;
    }

    static class GroupCreator implements ObjectCreator {
        @Override
        public BACnetObject create(final LocalDevice d, final int instanceNumber,
                final SequenceOf<PropertyValue> listOfInitialValues) throws BACnetErrorException {
            CharacterString name = new CharacterString(ObjectType.group.toString() + " " + instanceNumber);
            SequenceOf<ReadAccessSpecification> listOfGroupMembers = new SequenceOf<>();

            // Pull the initialization values out of the given list.
            for (int i = 0; i < listOfInitialValues.getCount(); i++) {
                final PropertyValue pv = listOfInitialValues.get(i);
                if (pv.getPropertyIdentifier().equals(PropertyIdentifier.objectName)) {
                    name = pv.getValue();
                } else if (pv.getPropertyIdentifier().equals(PropertyIdentifier.listOfGroupMembers)) {
                    listOfGroupMembers = pv.getValue();
                } else {
                    throw createException(ErrorClass.property, ErrorCode.writeAccessDenied, i + 1);
                }
            }

            try {
                return new GroupObject(d, instanceNumber, name.getValue(), listOfGroupMembers);
            } catch (final BACnetServiceException e) {
                try {
                    d.removeObject(new ObjectIdentifier(ObjectType.group, instanceNumber));
                } catch (final BACnetServiceException e1) {
                    LOG.warn("Error removing object that failed to initialize", e1);
                }
                throw createException(e.getErrorClass(), e.getErrorCode(), 0);
            }
        }
    }
}
