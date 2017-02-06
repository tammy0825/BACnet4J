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
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.Unsigned32;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

public class ProcessIdSelection extends BaseType {
    private static final long serialVersionUID = -1829749552197858514L;

    private Unsigned32 processIdentifier;
    private Null nullValue;

    public ProcessIdSelection(final Unsigned32 processIdentifier) {
        this(processIdentifier, null);
    }

    public ProcessIdSelection(final Null nullValue) {
        this(null, nullValue);
    }

    private ProcessIdSelection(final Unsigned32 processIdentifier, final Null nullValue) {
        this.processIdentifier = processIdentifier;
        this.nullValue = nullValue;
    }

    @Override
    public void write(final ByteQueue queue) {
        if (processIdentifier != null)
            write(queue, processIdentifier);
        else
            write(queue, nullValue);
    }

    public ProcessIdSelection(final ByteQueue queue) throws BACnetException {
        final int tag = (queue.peek(0) & 0xff) >> 4;
        if (tag == UnsignedInteger.TYPE_ID)
            processIdentifier = new Unsigned32(queue);
        else if (tag == Null.TYPE_ID)
            nullValue = new Null(queue);
        else
            throw new BACnetErrorException(ErrorClass.property, ErrorCode.invalidParameterDataType);
    }

    public Unsigned32 getProcessIdentifier() {
        return processIdentifier;
    }

    public Null getNullValue() {
        return nullValue;
    }

    @Override
    public String toString() {
        return "ProcessIdSelection [processIdentifier=" + processIdentifier + ", nullValue=" + nullValue + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (nullValue == null ? 0 : nullValue.hashCode());
        result = prime * result + (processIdentifier == null ? 0 : processIdentifier.hashCode());
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
        final ProcessIdSelection other = (ProcessIdSelection) obj;
        if (nullValue == null) {
            if (other.nullValue != null)
                return false;
        } else if (!nullValue.equals(other.nullValue))
            return false;
        if (processIdentifier == null) {
            if (other.processIdentifier != null)
                return false;
        } else if (!processIdentifier.equals(other.processIdentifier))
            return false;
        return true;
    }
}
