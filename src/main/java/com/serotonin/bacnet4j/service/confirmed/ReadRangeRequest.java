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

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.NotImplementedException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.BaseType;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.constructed.ChoiceOptions;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

public class ReadRangeRequest extends ConfirmedRequestService {
    public static final byte TYPE_ID = 26;

    private static ChoiceOptions choiceOptions = new ChoiceOptions();
    static {
        choiceOptions.addContextual(3, ByPosition.class);
        choiceOptions.addContextual(6, BySequenceNumber.class);
        choiceOptions.addContextual(7, ByTime.class);
    }
    private final ObjectIdentifier objectIdentifier;
    private final PropertyIdentifier propertyIdentifier;
    private final UnsignedInteger propertyArrayIndex;
    private final Choice range;

    public ReadRangeRequest(final ObjectIdentifier objectIdentifier, final PropertyIdentifier propertyIdentifier,
            final UnsignedInteger propertyArrayIndex, final ByPosition range) {
        this(objectIdentifier, propertyIdentifier, propertyArrayIndex, new Choice(3, range, choiceOptions));
    }

    public ReadRangeRequest(final ObjectIdentifier objectIdentifier, final PropertyIdentifier propertyIdentifier,
            final UnsignedInteger propertyArrayIndex, final BySequenceNumber range) {
        this(objectIdentifier, propertyIdentifier, propertyArrayIndex, new Choice(6, range, choiceOptions));
    }

    public ReadRangeRequest(final ObjectIdentifier objectIdentifier, final PropertyIdentifier propertyIdentifier,
            final UnsignedInteger propertyArrayIndex, final ByTime range) {
        this(objectIdentifier, propertyIdentifier, propertyArrayIndex, new Choice(7, range, choiceOptions));
    }

    private ReadRangeRequest(final ObjectIdentifier objectIdentifier, final PropertyIdentifier propertyIdentifier,
            final UnsignedInteger propertyArrayIndex, final Choice range) {
        this.objectIdentifier = objectIdentifier;
        this.propertyIdentifier = propertyIdentifier;
        this.propertyArrayIndex = propertyArrayIndex;
        this.range = range;
    }

    @Override
    public void write(final ByteQueue queue) {
        write(queue, objectIdentifier, 0);
        write(queue, propertyIdentifier, 1);
        writeOptional(queue, propertyArrayIndex, 2);
        writeOptional(queue, range);
    }

    public ReadRangeRequest(final ByteQueue queue) throws BACnetException {
        objectIdentifier = read(queue, ObjectIdentifier.class, 0);
        propertyIdentifier = read(queue, PropertyIdentifier.class, 1);
        propertyArrayIndex = readOptional(queue, UnsignedInteger.class, 2);
        range = readOptionalChoice(queue, choiceOptions);
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    public ObjectIdentifier getObjectIdentifier() {
        return objectIdentifier;
    }

    public PropertyIdentifier getPropertyIdentifier() {
        return propertyIdentifier;
    }

    public UnsignedInteger getPropertyArrayIndex() {
        return propertyArrayIndex;
    }

    public boolean isByPosition() {
        return range.getContextId() == 3;
    }

    public ByPosition getByPosition() {
        return range.getDatum();
    }

    public boolean isBySequenceNumber() {
        return range.getContextId() == 6;
    }

    public ByPosition getBySequenceNumber() {
        return range.getDatum();
    }

    public boolean isByTime() {
        return range.getContextId() == 7;
    }

    public ByPosition getByTime() {
        return range.getDatum();
    }

    @Override
    public AcknowledgementService handle(final LocalDevice localDevice, final Address from) throws BACnetException {
        throw new NotImplementedException();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + (objectIdentifier == null ? 0 : objectIdentifier.hashCode());
        result = PRIME * result + (propertyArrayIndex == null ? 0 : propertyArrayIndex.hashCode());
        result = PRIME * result + (propertyIdentifier == null ? 0 : propertyIdentifier.hashCode());
        result = PRIME * result + (range == null ? 0 : range.hashCode());
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
        final ReadRangeRequest other = (ReadRangeRequest) obj;
        if (objectIdentifier == null) {
            if (other.objectIdentifier != null)
                return false;
        } else if (!objectIdentifier.equals(other.objectIdentifier))
            return false;
        if (propertyArrayIndex == null) {
            if (other.propertyArrayIndex != null)
                return false;
        } else if (!propertyArrayIndex.equals(other.propertyArrayIndex))
            return false;
        if (propertyIdentifier == null) {
            if (other.propertyIdentifier != null)
                return false;
        } else if (!propertyIdentifier.equals(other.propertyIdentifier))
            return false;
        if (range == null) {
            if (other.range != null)
                return false;
        } else if (!range.equals(other.range))
            return false;
        return true;
    }

    abstract public static class Range extends BaseType {
        protected SignedInteger count;

        public Range(final SignedInteger count) {
            this.count = count;
        }

        Range() {
            // no op
        }

        public SignedInteger getCount() {
            return count;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (count == null ? 0 : count.hashCode());
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
            final Range other = (Range) obj;
            if (count == null) {
                if (other.count != null)
                    return false;
            } else if (!count.equals(other.count))
                return false;
            return true;
        }
    }

    public static class ByPosition extends Range {
        private final UnsignedInteger referenceIndex;

        public ByPosition(final UnsignedInteger referenceIndex, final SignedInteger count) {
            super(count);
            this.referenceIndex = referenceIndex;
        }

        @Override
        public void write(final ByteQueue queue) {
            write(queue, referenceIndex);
            write(queue, count);
        }

        public ByPosition(final ByteQueue queue) throws BACnetException {
            referenceIndex = read(queue, UnsignedInteger.class);
            count = read(queue, SignedInteger.class);
        }

        public UnsignedInteger getReferenceIndex() {
            return referenceIndex;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + (referenceIndex == null ? 0 : referenceIndex.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            final ByPosition other = (ByPosition) obj;
            if (referenceIndex == null) {
                if (other.referenceIndex != null)
                    return false;
            } else if (!referenceIndex.equals(other.referenceIndex))
                return false;
            return true;
        }
    }

    public static class BySequenceNumber extends Range {
        private final UnsignedInteger referenceIndex;

        public BySequenceNumber(final UnsignedInteger referenceIndex, final SignedInteger count) {
            super(count);
            this.referenceIndex = referenceIndex;
        }

        @Override
        public void write(final ByteQueue queue) {
            write(queue, referenceIndex);
            write(queue, count);
        }

        public BySequenceNumber(final ByteQueue queue) throws BACnetException {
            referenceIndex = read(queue, UnsignedInteger.class);
            count = read(queue, SignedInteger.class);
        }

        public UnsignedInteger getReferenceIndex() {
            return referenceIndex;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + (referenceIndex == null ? 0 : referenceIndex.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            final BySequenceNumber other = (BySequenceNumber) obj;
            if (referenceIndex == null) {
                if (other.referenceIndex != null)
                    return false;
            } else if (!referenceIndex.equals(other.referenceIndex))
                return false;
            return true;
        }
    }

    public static class ByTime extends Range {
        private final DateTime referenceTime;

        public ByTime(final DateTime referenceTime, final SignedInteger count) {
            super(count);
            this.referenceTime = referenceTime;
        }

        @Override
        public void write(final ByteQueue queue) {
            write(queue, referenceTime);
            write(queue, count);
        }

        public ByTime(final ByteQueue queue) throws BACnetException {
            referenceTime = read(queue, DateTime.class);
            count = read(queue, SignedInteger.class);
        }

        public DateTime getReferenceTime() {
            return referenceTime;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + (referenceTime == null ? 0 : referenceTime.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            final ByTime other = (ByTime) obj;
            if (referenceTime == null) {
                if (other.referenceTime != null)
                    return false;
            } else if (!referenceTime.equals(other.referenceTime))
                return false;
            return true;
        }
    }
}
