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
package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.AmbiguousValue;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Date;
import com.serotonin.bacnet4j.type.primitive.Double;
import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.bacnet4j.type.primitive.Time;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

public class PriorityValue extends BaseType {
    private static final long serialVersionUID = 213834169635261132L;

    private Null nullValue;
    private Real realValue;
    private Enumerated enumeratedValue;
    private UnsignedInteger unsignedValue;
    private Boolean booleanValue;
    private SignedInteger signedValue;
    private Double doubleValue;
    private Time timeValue;
    private CharacterString characterStringValue;
    private OctetString octetStringValue;
    private BitString bitStringValue;
    private Date dateValue;
    private ObjectIdentifier oidValue;
    private Encodable constructedValue;
    private DateTime dateTimeValue;

    public PriorityValue(final Null nullValue) {
        this.nullValue = nullValue;
    }

    public PriorityValue(final Real realValue) {
        this.realValue = realValue;
    }

    public PriorityValue(final Enumerated enumeratedValue) {
        this.enumeratedValue = enumeratedValue;
    }

    public PriorityValue(final UnsignedInteger unsignedValue) {
        this.unsignedValue = unsignedValue;
    }

    public PriorityValue(final Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public PriorityValue(final SignedInteger signedValue) {
        this.signedValue = signedValue;
    }

    public PriorityValue(final Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public PriorityValue(final Time timeValue) {
        this.timeValue = timeValue;
    }

    public PriorityValue(final CharacterString characterStringValue) {
        this.characterStringValue = characterStringValue;
    }

    public PriorityValue(final OctetString octetStringValue) {
        this.octetStringValue = octetStringValue;
    }

    public PriorityValue(final BitString bitStringValue) {
        this.bitStringValue = bitStringValue;
    }

    public PriorityValue(final Date dateValue) {
        this.dateValue = dateValue;
    }

    public PriorityValue(final ObjectIdentifier oidValue) {
        this.oidValue = oidValue;
    }

    public PriorityValue(final BaseType constructedValue) {
        this.constructedValue = constructedValue;
    }

    public PriorityValue(final DateTime dateTimeValue) {
        this.dateTimeValue = dateTimeValue;
    }

    public PriorityValue(final Encodable value) {
        if (value instanceof Null)
            nullValue = (Null) value;
        else if (value instanceof Real)
            realValue = (Real) value;
        else if (value instanceof Enumerated)
            enumeratedValue = (Enumerated) value;
        else if (value instanceof UnsignedInteger)
            unsignedValue = (UnsignedInteger) value;
        else if (value instanceof Boolean)
            booleanValue = (Boolean) value;
        else if (value instanceof SignedInteger)
            signedValue = (SignedInteger) value;
        else if (value instanceof Double)
            doubleValue = (Double) value;
        else if (value instanceof Time)
            timeValue = (Time) value;
        else if (value instanceof CharacterString)
            characterStringValue = (CharacterString) value;
        else if (value instanceof OctetString)
            octetStringValue = (OctetString) value;
        else if (value instanceof BitString)
            bitStringValue = (BitString) value;
        else if (value instanceof Date)
            dateValue = (Date) value;
        else if (value instanceof ObjectIdentifier)
            oidValue = (ObjectIdentifier) value;
        else if (value instanceof DateTime)
            dateTimeValue = (DateTime) value;
        else if (value instanceof BaseType)
            constructedValue = value;
        else
            throw new IllegalArgumentException("Unhandled priority type: " + value.getClass());
    }

    public Null getNullValue() {
        return nullValue;
    }

    public Real getRealValue() {
        return realValue;
    }

    public Enumerated getEnumeratedValue() {
        return enumeratedValue;
    }

    public UnsignedInteger getUnsignedValue() {
        return unsignedValue;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public SignedInteger getSignedValue() {
        return signedValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public Time getTimeValue() {
        return timeValue;
    }

    public CharacterString getCharacterStringValue() {
        return characterStringValue;
    }

    public OctetString getOctetStringValue() {
        return octetStringValue;
    }

    public BitString getBitStringValue() {
        return bitStringValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public ObjectIdentifier getOidValue() {
        return oidValue;
    }

    public DateTime getDateTimeValue() {
        return dateTimeValue;
    }

    public Encodable getConstructedValue() {
        return constructedValue;
    }

    public boolean isNull() {
        return nullValue != null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Encodable> T getValue() {
        if (nullValue != null)
            return (T) nullValue;
        if (realValue != null)
            return (T) realValue;
        if (enumeratedValue != null)
            return (T) enumeratedValue;
        if (unsignedValue != null)
            return (T) unsignedValue;
        if (booleanValue != null)
            return (T) booleanValue;
        if (signedValue != null)
            return (T) signedValue;
        if (doubleValue != null)
            return (T) doubleValue;
        if (timeValue != null)
            return (T) timeValue;
        if (characterStringValue != null)
            return (T) characterStringValue;
        if (octetStringValue != null)
            return (T) octetStringValue;
        if (bitStringValue != null)
            return (T) bitStringValue;
        if (dateValue != null)
            return (T) dateValue;
        if (oidValue != null)
            return (T) oidValue;
        if (dateTimeValue != null)
            return (T) dateTimeValue;
        return (T) constructedValue;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PriorityValue(");
        if (nullValue != null)
            sb.append("nullValue=").append(nullValue);
        else if (realValue != null)
            sb.append("realValue=").append(realValue);
        else if (enumeratedValue != null)
            sb.append("enumeratedValue=").append(enumeratedValue);
        else if (unsignedValue != null)
            sb.append("unsignedValue=").append(unsignedValue);
        else if (booleanValue != null)
            sb.append("booleanValue=").append(booleanValue);
        else if (signedValue != null)
            sb.append("signedValue=").append(signedValue);
        else if (doubleValue != null)
            sb.append("doubleValue=").append(doubleValue);
        else if (timeValue != null)
            sb.append("timeValue=").append(timeValue);
        else if (characterStringValue != null)
            sb.append("characterStringValue=").append(characterStringValue);
        else if (octetStringValue != null)
            sb.append("octetStringValue=").append(octetStringValue);
        else if (bitStringValue != null)
            sb.append("bitStringValue=").append(bitStringValue);
        else if (dateValue != null)
            sb.append("dateValue=").append(dateValue);
        else if (oidValue != null)
            sb.append("oidValue=").append(oidValue);
        else if (dateTimeValue != null)
            sb.append("dateTimeValue=").append(dateTimeValue);
        else if (constructedValue != null)
            sb.append("constructedValue=").append(constructedValue);
        sb.append(")");
        return sb.toString();
    }

    @Override
    public void write(final ByteQueue queue) {
        if (nullValue != null)
            nullValue.write(queue);
        else if (realValue != null)
            realValue.write(queue);
        else if (enumeratedValue != null)
            enumeratedValue.write(queue);
        else if (unsignedValue != null)
            unsignedValue.write(queue);
        else if (booleanValue != null)
            booleanValue.write(queue);
        else if (signedValue != null)
            signedValue.write(queue);
        else if (doubleValue != null)
            doubleValue.write(queue);
        else if (timeValue != null)
            timeValue.write(queue);
        else if (characterStringValue != null)
            characterStringValue.write(queue);
        else if (octetStringValue != null)
            octetStringValue.write(queue);
        else if (bitStringValue != null)
            bitStringValue.write(queue);
        else if (dateValue != null)
            dateValue.write(queue);
        else if (oidValue != null)
            oidValue.write(queue);
        else if (constructedValue != null)
            constructedValue.write(queue, 0);
        else
            dateTimeValue.write(queue, 1);
    }

    public PriorityValue(final ByteQueue queue) throws BACnetException {
        // Sweet Jesus...
        int tag = queue.peek(0) & 0xff;

        if ((tag & 8) == 8) {
            // A class tag, so this is a constructed value.
            tag = tag >> 4;
            if (tag == 0)
                constructedValue = new AmbiguousValue(queue, 0);
            else if (tag == 1)
                dateTimeValue = read(queue, DateTime.class, 1);
        } else {
            // A primitive value
            tag = tag >> 4;
            if (tag == Null.TYPE_ID)
                nullValue = new Null(queue);
            else if (tag == Real.TYPE_ID)
                realValue = new Real(queue);
            else if (tag == Enumerated.TYPE_ID)
                enumeratedValue = new Enumerated(queue);
            else if (tag == UnsignedInteger.TYPE_ID)
                unsignedValue = new UnsignedInteger(queue);
            else if (tag == Boolean.TYPE_ID)
                booleanValue = new Boolean(queue);
            else if (tag == SignedInteger.TYPE_ID)
                signedValue = new SignedInteger(queue);
            else if (tag == Double.TYPE_ID)
                doubleValue = new Double(queue);
            else if (tag == Time.TYPE_ID)
                timeValue = new Time(queue);
            else if (tag == CharacterString.TYPE_ID)
                characterStringValue = new CharacterString(queue);
            else if (tag == OctetString.TYPE_ID)
                octetStringValue = new OctetString(queue);
            else if (tag == BitString.TYPE_ID)
                bitStringValue = new BitString(queue);
            else if (tag == Date.TYPE_ID)
                dateValue = new Date(queue);
            else if (tag == ObjectIdentifier.TYPE_ID)
                oidValue = new ObjectIdentifier(queue);
            else
                throw new BACnetErrorException(ErrorClass.property, ErrorCode.invalidDataType,
                        "Unsupported primitive id: " + tag);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (bitStringValue == null ? 0 : bitStringValue.hashCode());
        result = prime * result + (booleanValue == null ? 0 : booleanValue.hashCode());
        result = prime * result + (characterStringValue == null ? 0 : characterStringValue.hashCode());
        result = prime * result + (constructedValue == null ? 0 : constructedValue.hashCode());
        result = prime * result + (dateTimeValue == null ? 0 : dateTimeValue.hashCode());
        result = prime * result + (dateValue == null ? 0 : dateValue.hashCode());
        result = prime * result + (doubleValue == null ? 0 : doubleValue.hashCode());
        result = prime * result + (enumeratedValue == null ? 0 : enumeratedValue.hashCode());
        result = prime * result + (nullValue == null ? 0 : nullValue.hashCode());
        result = prime * result + (octetStringValue == null ? 0 : octetStringValue.hashCode());
        result = prime * result + (oidValue == null ? 0 : oidValue.hashCode());
        result = prime * result + (realValue == null ? 0 : realValue.hashCode());
        result = prime * result + (signedValue == null ? 0 : signedValue.hashCode());
        result = prime * result + (timeValue == null ? 0 : timeValue.hashCode());
        result = prime * result + (unsignedValue == null ? 0 : unsignedValue.hashCode());
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
        final PriorityValue other = (PriorityValue) obj;
        if (bitStringValue == null) {
            if (other.bitStringValue != null)
                return false;
        } else if (!bitStringValue.equals(other.bitStringValue))
            return false;
        if (booleanValue == null) {
            if (other.booleanValue != null)
                return false;
        } else if (!booleanValue.equals(other.booleanValue))
            return false;
        if (characterStringValue == null) {
            if (other.characterStringValue != null)
                return false;
        } else if (!characterStringValue.equals(other.characterStringValue))
            return false;
        if (constructedValue == null) {
            if (other.constructedValue != null)
                return false;
        } else if (!constructedValue.equals(other.constructedValue))
            return false;
        if (dateTimeValue == null) {
            if (other.dateTimeValue != null)
                return false;
        } else if (!dateTimeValue.equals(other.dateTimeValue))
            return false;
        if (dateValue == null) {
            if (other.dateValue != null)
                return false;
        } else if (!dateValue.equals(other.dateValue))
            return false;
        if (doubleValue == null) {
            if (other.doubleValue != null)
                return false;
        } else if (!doubleValue.equals(other.doubleValue))
            return false;
        if (enumeratedValue == null) {
            if (other.enumeratedValue != null)
                return false;
        } else if (!enumeratedValue.equals(other.enumeratedValue))
            return false;
        if (nullValue == null) {
            if (other.nullValue != null)
                return false;
        } else if (!nullValue.equals(other.nullValue))
            return false;
        if (octetStringValue == null) {
            if (other.octetStringValue != null)
                return false;
        } else if (!octetStringValue.equals(other.octetStringValue))
            return false;
        if (oidValue == null) {
            if (other.oidValue != null)
                return false;
        } else if (!oidValue.equals(other.oidValue))
            return false;
        if (realValue == null) {
            if (other.realValue != null)
                return false;
        } else if (!realValue.equals(other.realValue))
            return false;
        if (signedValue == null) {
            if (other.signedValue != null)
                return false;
        } else if (!signedValue.equals(other.signedValue))
            return false;
        if (timeValue == null) {
            if (other.timeValue != null)
                return false;
        } else if (!timeValue.equals(other.timeValue))
            return false;
        if (unsignedValue == null) {
            if (other.unsignedValue != null)
                return false;
        } else if (!unsignedValue.equals(other.unsignedValue))
            return false;
        return true;
    }
}
