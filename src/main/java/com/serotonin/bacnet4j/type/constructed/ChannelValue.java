package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

public class ChannelValue extends BaseType {
    private static final long serialVersionUID = -2620538935921657482L;

    private Null nullValue;
    private Real realValue;
    private BinaryPV binaryValue;
    private UnsignedInteger integerValue;
    private LightingCommand lightingCommand;

    public ChannelValue(final Null nullValue) {
        this.nullValue = nullValue;
    }

    public ChannelValue(final Real realValue) {
        this.realValue = realValue;
    }

    public ChannelValue(final BinaryPV binaryValue) {
        this.binaryValue = binaryValue;
    }

    public ChannelValue(final UnsignedInteger integerValue) {
        this.integerValue = integerValue;
    }

    public ChannelValue(final LightingCommand lightingCommand) {
        this.lightingCommand = lightingCommand;
    }

    public Null getNullValue() {
        return nullValue;
    }

    public Real getRealValue() {
        return realValue;
    }

    public BinaryPV getBinaryValue() {
        return binaryValue;
    }

    public UnsignedInteger getIntegerValue() {
        return integerValue;
    }

    public LightingCommand getLightingCommand() {
        return lightingCommand;
    }

    public boolean isNull() {
        return nullValue != null;
    }

    public Encodable getValue() {
        if (nullValue != null)
            return nullValue;
        if (realValue != null)
            return realValue;
        if (binaryValue != null)
            return binaryValue;
        if (integerValue != null)
            return integerValue;
        return lightingCommand;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PriorityValue(");
        if (nullValue != null)
            sb.append("nullValue=").append(nullValue);
        else if (realValue != null)
            sb.append("realValue=").append(realValue);
        else if (binaryValue != null)
            sb.append("binaryValue=").append(binaryValue);
        else if (integerValue != null)
            sb.append("integerValue=").append(integerValue);
        else if (lightingCommand != null)
            sb.append("constructedValue=").append(lightingCommand);
        sb.append(")");
        return sb.toString();
    }

    @Override
    public void write(final ByteQueue queue) {
        if (nullValue != null)
            nullValue.write(queue);
        else if (realValue != null)
            realValue.write(queue);
        else if (binaryValue != null)
            binaryValue.write(queue);
        else if (integerValue != null)
            integerValue.write(queue);
        else
            lightingCommand.write(queue, 0);
    }

    public ChannelValue(final ByteQueue queue) throws BACnetException {
        // Sweet Jesus...
        int tag = queue.peek(0) & 0xff;
        if ((tag & 8) == 8) {
            // A class tag, so this is a constructed value.
            lightingCommand = read(queue, LightingCommand.class, 0);
        } else {
            // A primitive value
            tag = tag >> 4;
            if (tag == Null.TYPE_ID)
                nullValue = new Null(queue);
            else if (tag == Real.TYPE_ID)
                realValue = new Real(queue);
            else if (tag == Enumerated.TYPE_ID)
                binaryValue = new BinaryPV(queue);
            else if (tag == UnsignedInteger.TYPE_ID)
                integerValue = new UnsignedInteger(queue);
            else
                throw new BACnetErrorException(ErrorClass.property, ErrorCode.invalidDataType,
                        "Unsupported primitive id: " + tag);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (binaryValue == null ? 0 : binaryValue.hashCode());
        result = prime * result + (integerValue == null ? 0 : integerValue.hashCode());
        result = prime * result + (lightingCommand == null ? 0 : lightingCommand.hashCode());
        result = prime * result + (nullValue == null ? 0 : nullValue.hashCode());
        result = prime * result + (realValue == null ? 0 : realValue.hashCode());
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
        final ChannelValue other = (ChannelValue) obj;
        if (binaryValue == null) {
            if (other.binaryValue != null)
                return false;
        } else if (!binaryValue.equals(other.binaryValue))
            return false;
        if (integerValue == null) {
            if (other.integerValue != null)
                return false;
        } else if (!integerValue.equals(other.integerValue))
            return false;
        if (lightingCommand == null) {
            if (other.lightingCommand != null)
                return false;
        } else if (!lightingCommand.equals(other.lightingCommand))
            return false;
        if (nullValue == null) {
            if (other.nullValue != null)
                return false;
        } else if (!nullValue.equals(other.nullValue))
            return false;
        if (realValue == null) {
            if (other.realValue != null)
                return false;
        } else if (!realValue.equals(other.realValue))
            return false;
        return true;
    }
}
