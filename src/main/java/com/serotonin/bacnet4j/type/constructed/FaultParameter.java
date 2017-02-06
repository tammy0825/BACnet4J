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

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.LifeSafetyState;
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
import com.serotonin.bacnet4j.type.primitive.Unsigned16;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

public class FaultParameter extends BaseType {
    private static final long serialVersionUID = -465308973114139126L;

    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<>();
        classes.add(Null.class);
        classes.add(FaultCharacterString.class);
        classes.add(FaultExtended.class);
        classes.add(FaultLifeSafety.class);
        classes.add(FaultState.class);
        classes.add(FaultStatusFlags.class);
    }

    private final Choice entry;

    public FaultParameter(final Null value) {
        entry = new Choice(0, value);
    }

    public FaultParameter(final FaultCharacterString value) {
        entry = new Choice(1, value);
    }

    public FaultParameter(final FaultExtended value) {
        entry = new Choice(2, value);
    }

    public FaultParameter(final FaultLifeSafety value) {
        entry = new Choice(3, value);
    }

    public FaultParameter(final FaultState value) {
        entry = new Choice(4, value);
    }

    public FaultParameter(final FaultStatusFlags value) {
        entry = new Choice(5, value);
    }

    @Override
    public void write(final ByteQueue queue) {
        write(queue, entry);
    }

    public FaultParameter(final ByteQueue queue) throws BACnetException {
        entry = new Choice(queue, classes);
    }

    public boolean isNull() {
        return entry.getContextId() == 0;
    }

    public boolean isFaultCharacterString() {
        return entry.getContextId() == 1;
    }

    public boolean isFaultExtended() {
        return entry.getContextId() == 2;
    }

    public boolean isFaultLifeSafety() {
        return entry.getContextId() == 3;
    }

    public boolean isFaultState() {
        return entry.getContextId() == 4;
    }

    public boolean isFaultStatusFlags() {
        return entry.getContextId() == 5;
    }

    public Null getNull() {
        return (Null) entry.getDatum();
    }

    public FaultCharacterString getFaultCharacterString() {
        return (FaultCharacterString) entry.getDatum();
    }

    public FaultExtended getFaultExtended() {
        return (FaultExtended) entry.getDatum();
    }

    public FaultLifeSafety getFaultLifeSafety() {
        return (FaultLifeSafety) entry.getDatum();
    }

    public FaultState getFaultState() {
        return (FaultState) entry.getDatum();
    }

    public FaultStatusFlags getFaultStatusFlags() {
        return (FaultStatusFlags) entry.getDatum();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (entry == null ? 0 : entry.hashCode());
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
        final FaultParameter other = (FaultParameter) obj;
        if (entry == null) {
            if (other.entry != null)
                return false;
        } else if (!entry.equals(other.entry))
            return false;
        return true;
    }

    public static class FaultCharacterString extends BaseType {
        private static final long serialVersionUID = -6244553018644048034L;

        private final SequenceOf<CharacterString> listOfFaultValues;

        public FaultCharacterString(final SequenceOf<CharacterString> listOfFaultValues) {
            this.listOfFaultValues = listOfFaultValues;
        }

        public SequenceOf<CharacterString> getListOfFaultValues() {
            return listOfFaultValues;
        }

        @Override
        public void write(final ByteQueue queue) {
            write(queue, listOfFaultValues, 0);
        }

        @Override
        public String toString() {
            return "FaultCharacterString [listOfFaultValues=" + listOfFaultValues + "]";
        }

        public FaultCharacterString(final ByteQueue queue) throws BACnetException {
            listOfFaultValues = readSequenceOf(queue, CharacterString.class, 0);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (listOfFaultValues == null ? 0 : listOfFaultValues.hashCode());
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
            final FaultCharacterString other = (FaultCharacterString) obj;
            if (listOfFaultValues == null) {
                if (other.listOfFaultValues != null)
                    return false;
            } else if (!listOfFaultValues.equals(other.listOfFaultValues))
                return false;
            return true;
        }
    }

    public static class FaultExtended extends BaseType {
        private static final long serialVersionUID = -6389905193957957932L;

        private final Unsigned16 vendorId;
        private final UnsignedInteger extendedFaultType;
        private final SequenceOf<FaultExtendedParameter> parameters;

        public FaultExtended(final Unsigned16 vendorId, final UnsignedInteger extendedFaultType,
                final SequenceOf<FaultExtendedParameter> parameters) {
            this.vendorId = vendorId;
            this.extendedFaultType = extendedFaultType;
            this.parameters = parameters;
        }

        @Override
        public void write(final ByteQueue queue) {
            write(queue, vendorId, 0);
            write(queue, extendedFaultType, 1);
            write(queue, parameters, 2);
        }

        @Override
        public String toString() {
            return "FaultExtended [vendorId=" + vendorId + ", extendedFaultType=" + extendedFaultType + ", parameters="
                    + parameters + "]";
        }

        public Unsigned16 getVendorId() {
            return vendorId;
        }

        public UnsignedInteger getExtendedFaultType() {
            return extendedFaultType;
        }

        public SequenceOf<FaultExtendedParameter> getParameters() {
            return parameters;
        }

        public FaultExtended(final ByteQueue queue) throws BACnetException {
            vendorId = read(queue, Unsigned16.class, 0);
            extendedFaultType = read(queue, UnsignedInteger.class, 1);
            parameters = readSequenceOf(queue, FaultExtendedParameter.class, 2);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (extendedFaultType == null ? 0 : extendedFaultType.hashCode());
            result = prime * result + (parameters == null ? 0 : parameters.hashCode());
            result = prime * result + (vendorId == null ? 0 : vendorId.hashCode());
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
            final FaultExtended other = (FaultExtended) obj;
            if (extendedFaultType == null) {
                if (other.extendedFaultType != null)
                    return false;
            } else if (!extendedFaultType.equals(other.extendedFaultType))
                return false;
            if (parameters == null) {
                if (other.parameters != null)
                    return false;
            } else if (!parameters.equals(other.parameters))
                return false;
            if (vendorId == null) {
                if (other.vendorId != null)
                    return false;
            } else if (!vendorId.equals(other.vendorId))
                return false;
            return true;
        }

        public static class FaultExtendedParameter extends BaseType {
            private static final long serialVersionUID = 334765649997595069L;

            private Null nullValue;
            private Real realValue;
            private UnsignedInteger unsignedValue;
            private Boolean booleanValue;
            private SignedInteger integerValue;
            private Double doubleValue;
            private OctetString octetValue;
            private CharacterString characterStringValue;
            private BitString bitStringValue;
            private Enumerated enumValue;
            private Date dateValue;
            private Time timeValue;
            private ObjectIdentifier objectIdentifierValue;
            private DeviceObjectPropertyReference referenceValue;

            public FaultExtendedParameter(final Null nullValue) {
                this.nullValue = nullValue;
            }

            public FaultExtendedParameter(final Real realValue) {
                this.realValue = realValue;
            }

            public FaultExtendedParameter(final UnsignedInteger unsignedValue) {
                this.unsignedValue = unsignedValue;
            }

            public FaultExtendedParameter(final Boolean booleanValue) {
                this.booleanValue = booleanValue;
            }

            public FaultExtendedParameter(final SignedInteger integerValue) {
                this.integerValue = integerValue;
            }

            public FaultExtendedParameter(final Double doubleValue) {
                this.doubleValue = doubleValue;
            }

            public FaultExtendedParameter(final OctetString octetValue) {
                this.octetValue = octetValue;
            }

            public FaultExtendedParameter(final CharacterString characterStringValue) {
                this.characterStringValue = characterStringValue;
            }

            public FaultExtendedParameter(final BitString bitStringValue) {
                this.bitStringValue = bitStringValue;
            }

            public FaultExtendedParameter(final Enumerated enumValue) {
                this.enumValue = enumValue;
            }

            public FaultExtendedParameter(final Date dateValue) {
                this.dateValue = dateValue;
            }

            public FaultExtendedParameter(final Time timeValue) {
                this.timeValue = timeValue;
            }

            public FaultExtendedParameter(final ObjectIdentifier objectIdentifierValue) {
                this.objectIdentifierValue = objectIdentifierValue;
            }

            public FaultExtendedParameter(final DeviceObjectPropertyReference referenceValue) {
                this.referenceValue = referenceValue;
            }

            public Null getNullValue() {
                return nullValue;
            }

            public Real getRealValue() {
                return realValue;
            }

            public UnsignedInteger getUnsignedValue() {
                return unsignedValue;
            }

            public Boolean getBooleanValue() {
                return booleanValue;
            }

            public SignedInteger getIntegerValue() {
                return integerValue;
            }

            public Double getDoubleValue() {
                return doubleValue;
            }

            public OctetString getOctetValue() {
                return octetValue;
            }

            public CharacterString getCharacterStringValue() {
                return characterStringValue;
            }

            public BitString getBitStringValue() {
                return bitStringValue;
            }

            public Enumerated getEnumValue() {
                return enumValue;
            }

            public Date getDateValue() {
                return dateValue;
            }

            public Time getTimeValue() {
                return timeValue;
            }

            public ObjectIdentifier getObjectIdentifierValue() {
                return objectIdentifierValue;
            }

            public DeviceObjectPropertyReference getReferenceValue() {
                return referenceValue;
            }

            public boolean isNull() {
                return nullValue != null;
            }

            public Encodable getValue() {
                if (nullValue != null)
                    return nullValue;
                if (realValue != null)
                    return realValue;
                if (unsignedValue != null)
                    return unsignedValue;
                if (booleanValue != null)
                    return booleanValue;
                if (integerValue != null)
                    return integerValue;
                if (doubleValue != null)
                    return doubleValue;
                if (octetValue != null)
                    return octetValue;
                if (characterStringValue != null)
                    return characterStringValue;
                if (bitStringValue != null)
                    return bitStringValue;
                if (enumValue != null)
                    return enumValue;
                if (dateValue != null)
                    return dateValue;
                if (timeValue != null)
                    return timeValue;
                if (objectIdentifierValue != null)
                    return objectIdentifierValue;
                return referenceValue;
            }

            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append("PriorityValue(");
                if (nullValue != null)
                    sb.append("nullValue=").append(nullValue);
                else if (realValue != null)
                    sb.append("realValue=").append(realValue);
                else if (unsignedValue != null)
                    sb.append("unsignedValue=").append(unsignedValue);
                else if (booleanValue != null)
                    sb.append("booleanValue=").append(booleanValue);
                else if (integerValue != null)
                    sb.append("integerValue=").append(integerValue);
                else if (doubleValue != null)
                    sb.append("doubleValue=").append(doubleValue);
                else if (octetValue != null)
                    sb.append("octetValue=").append(octetValue);
                else if (characterStringValue != null)
                    sb.append("characterStringValue=").append(characterStringValue);
                else if (bitStringValue != null)
                    sb.append("bitStringValue=").append(bitStringValue);
                else if (enumValue != null)
                    sb.append("enumValue=").append(enumValue);
                else if (dateValue != null)
                    sb.append("dateValue=").append(dateValue);
                else if (timeValue != null)
                    sb.append("timeValue=").append(timeValue);
                else if (objectIdentifierValue != null)
                    sb.append("objectIdentifierValue=").append(objectIdentifierValue);
                else if (referenceValue != null)
                    sb.append("referenceValue=").append(referenceValue);
                sb.append(")");
                return sb.toString();
            }

            @Override
            public void write(final ByteQueue queue) {
                if (nullValue != null)
                    nullValue.write(queue);
                else if (realValue != null)
                    realValue.write(queue);
                else if (unsignedValue != null)
                    unsignedValue.write(queue);
                else if (booleanValue != null)
                    booleanValue.write(queue);
                else if (integerValue != null)
                    integerValue.write(queue);
                else if (doubleValue != null)
                    doubleValue.write(queue);
                else if (octetValue != null)
                    octetValue.write(queue);
                else if (characterStringValue != null)
                    characterStringValue.write(queue);
                else if (bitStringValue != null)
                    bitStringValue.write(queue);
                else if (enumValue != null)
                    enumValue.write(queue);
                else if (dateValue != null)
                    dateValue.write(queue);
                else if (timeValue != null)
                    timeValue.write(queue);
                else if (objectIdentifierValue != null)
                    objectIdentifierValue.write(queue);
                else if (referenceValue != null)
                    referenceValue.write(queue, 0);
            }

            public FaultExtendedParameter(final ByteQueue queue) throws BACnetException {
                int tag = queue.peek(0) & 0xff;
                if ((tag & 8) == 8) {
                    // A class tag, so this is a constructed value.
                    referenceValue = read(queue, DeviceObjectPropertyReference.class, 0);
                } else {
                    // A primitive value
                    tag = tag >> 4;
                    if (tag == Null.TYPE_ID)
                        nullValue = new Null(queue);
                    else if (tag == Real.TYPE_ID)
                        realValue = new Real(queue);
                    else if (tag == UnsignedInteger.TYPE_ID)
                        unsignedValue = new UnsignedInteger(queue);
                    else if (tag == Boolean.TYPE_ID)
                        booleanValue = new Boolean(queue);
                    else if (tag == SignedInteger.TYPE_ID)
                        integerValue = new SignedInteger(queue);
                    else if (tag == Double.TYPE_ID)
                        doubleValue = new Double(queue);
                    else if (tag == OctetString.TYPE_ID)
                        octetValue = new OctetString(queue);
                    else if (tag == CharacterString.TYPE_ID)
                        characterStringValue = new CharacterString(queue);
                    else if (tag == BitString.TYPE_ID)
                        bitStringValue = new BitString(queue);
                    else if (tag == Enumerated.TYPE_ID)
                        enumValue = new Enumerated(queue);
                    else if (tag == Date.TYPE_ID)
                        dateValue = new Date(queue);
                    else if (tag == Time.TYPE_ID)
                        timeValue = new Time(queue);
                    else if (tag == ObjectIdentifier.TYPE_ID)
                        objectIdentifierValue = new ObjectIdentifier(queue);
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
                result = prime * result + (dateValue == null ? 0 : dateValue.hashCode());
                result = prime * result + (doubleValue == null ? 0 : doubleValue.hashCode());
                result = prime * result + (enumValue == null ? 0 : enumValue.hashCode());
                result = prime * result + (integerValue == null ? 0 : integerValue.hashCode());
                result = prime * result + (nullValue == null ? 0 : nullValue.hashCode());
                result = prime * result + (objectIdentifierValue == null ? 0 : objectIdentifierValue.hashCode());
                result = prime * result + (octetValue == null ? 0 : octetValue.hashCode());
                result = prime * result + (realValue == null ? 0 : realValue.hashCode());
                result = prime * result + (referenceValue == null ? 0 : referenceValue.hashCode());
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
                final FaultExtendedParameter other = (FaultExtendedParameter) obj;
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
                if (enumValue == null) {
                    if (other.enumValue != null)
                        return false;
                } else if (!enumValue.equals(other.enumValue))
                    return false;
                if (integerValue == null) {
                    if (other.integerValue != null)
                        return false;
                } else if (!integerValue.equals(other.integerValue))
                    return false;
                if (nullValue == null) {
                    if (other.nullValue != null)
                        return false;
                } else if (!nullValue.equals(other.nullValue))
                    return false;
                if (objectIdentifierValue == null) {
                    if (other.objectIdentifierValue != null)
                        return false;
                } else if (!objectIdentifierValue.equals(other.objectIdentifierValue))
                    return false;
                if (octetValue == null) {
                    if (other.octetValue != null)
                        return false;
                } else if (!octetValue.equals(other.octetValue))
                    return false;
                if (realValue == null) {
                    if (other.realValue != null)
                        return false;
                } else if (!realValue.equals(other.realValue))
                    return false;
                if (referenceValue == null) {
                    if (other.referenceValue != null)
                        return false;
                } else if (!referenceValue.equals(other.referenceValue))
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
    }

    public static class FaultLifeSafety extends BaseType {
        private static final long serialVersionUID = -5852300578634203306L;

        private final SequenceOf<LifeSafetyState> listOfFaultValues;
        private final DeviceObjectPropertyReference modePropertyReference;

        public FaultLifeSafety(final SequenceOf<LifeSafetyState> listOfFaultValues,
                final DeviceObjectPropertyReference modePropertyReference) {
            this.listOfFaultValues = listOfFaultValues;
            this.modePropertyReference = modePropertyReference;
        }

        @Override
        public void write(final ByteQueue queue) {
            write(queue, listOfFaultValues, 0);
            write(queue, modePropertyReference, 1);
        }

        @Override
        public String toString() {
            return "FaultLifeSafety [listOfFaultValues=" + listOfFaultValues + ", modePropertyReference="
                    + modePropertyReference + "]";
        }

        public SequenceOf<LifeSafetyState> getListOfFaultValues() {
            return listOfFaultValues;
        }

        public DeviceObjectPropertyReference getModePropertyReference() {
            return modePropertyReference;
        }

        public FaultLifeSafety(final ByteQueue queue) throws BACnetException {
            listOfFaultValues = readSequenceOf(queue, LifeSafetyState.class, 0);
            modePropertyReference = read(queue, DeviceObjectPropertyReference.class, 1);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (listOfFaultValues == null ? 0 : listOfFaultValues.hashCode());
            result = prime * result + (modePropertyReference == null ? 0 : modePropertyReference.hashCode());
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
            final FaultLifeSafety other = (FaultLifeSafety) obj;
            if (listOfFaultValues == null) {
                if (other.listOfFaultValues != null)
                    return false;
            } else if (!listOfFaultValues.equals(other.listOfFaultValues))
                return false;
            if (modePropertyReference == null) {
                if (other.modePropertyReference != null)
                    return false;
            } else if (!modePropertyReference.equals(other.modePropertyReference))
                return false;
            return true;
        }
    }

    public static class FaultState extends BaseType {
        private static final long serialVersionUID = 8215607054305312995L;

        private final SequenceOf<PropertyStates> listOfFaultValues;

        public FaultState(final SequenceOf<PropertyStates> listOfFaultValues) {
            this.listOfFaultValues = listOfFaultValues;
        }

        public SequenceOf<PropertyStates> getListOfFaultValues() {
            return listOfFaultValues;
        }

        @Override
        public void write(final ByteQueue queue) {
            write(queue, listOfFaultValues, 0);
        }

        @Override
        public String toString() {
            return "FaultState [listOfFaultValues=" + listOfFaultValues + "]";
        }

        public FaultState(final ByteQueue queue) throws BACnetException {
            listOfFaultValues = readSequenceOf(queue, PropertyStates.class, 0);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (listOfFaultValues == null ? 0 : listOfFaultValues.hashCode());
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
            final FaultState other = (FaultState) obj;
            if (listOfFaultValues == null) {
                if (other.listOfFaultValues != null)
                    return false;
            } else if (!listOfFaultValues.equals(other.listOfFaultValues))
                return false;
            return true;
        }
    }

    public static class FaultStatusFlags extends BaseType {
        private static final long serialVersionUID = 1832473272124794781L;

        private final DeviceObjectPropertyReference statusFlagsReference;

        public FaultStatusFlags(final DeviceObjectPropertyReference statusFlagsReference) {
            this.statusFlagsReference = statusFlagsReference;
        }

        @Override
        public void write(final ByteQueue queue) {
            write(queue, statusFlagsReference, 0);
        }

        @Override
        public String toString() {
            return "FaultStatusFlags [statusFlagsReference=" + statusFlagsReference + "]";
        }

        public DeviceObjectPropertyReference getStatusFlagsReference() {
            return statusFlagsReference;
        }

        public FaultStatusFlags(final ByteQueue queue) throws BACnetException {
            statusFlagsReference = read(queue, DeviceObjectPropertyReference.class, 0);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (statusFlagsReference == null ? 0 : statusFlagsReference.hashCode());
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
            final FaultStatusFlags other = (FaultStatusFlags) obj;
            if (statusFlagsReference == null) {
                if (other.statusFlagsReference != null)
                    return false;
            } else if (!statusFlagsReference.equals(other.statusFlagsReference))
                return false;
            return true;
        }
    }
}
