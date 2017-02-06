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
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

public class OptionalCharacterString extends BaseType {
    private static final long serialVersionUID = -397796812328595696L;

    private Null nullValue;
    private CharacterString characterString;

    public OptionalCharacterString(final Null nullValue) {
        this.nullValue = nullValue;
    }

    public OptionalCharacterString(final CharacterString characterString) {
        this.characterString = characterString;
    }

    public OptionalCharacterString(final String s) {
        if (s == null)
            nullValue = new Null();
        else
            characterString = new CharacterString(s);
    }

    public OptionalCharacterString(final Encodable value) {
        if (value instanceof Null)
            nullValue = (Null) value;
        else if (value instanceof CharacterString)
            characterString = (CharacterString) value;
        else
            throw new IllegalArgumentException("Unhandled type: " + value.getClass());
    }

    public Null getNullValue() {
        return nullValue;
    }

    public CharacterString getCharacterStringValue() {
        return characterString;
    }

    public String getString() {
        if (nullValue != null)
            return null;
        return characterString.getValue();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OptionalCharacterString(").append(getString()).append(")");
        return sb.toString();
    }

    @Override
    public void write(final ByteQueue queue) {
        if (nullValue != null)
            nullValue.write(queue);
        else if (characterString != null)
            characterString.write(queue);
    }

    public OptionalCharacterString(final ByteQueue queue) throws BACnetException {
        // Sweet Jesus...
        final int tag = (queue.peek(0) & 0xff) >> 4;
        if (tag == Null.TYPE_ID)
            nullValue = new Null(queue);
        else if (tag == CharacterString.TYPE_ID)
            characterString = new CharacterString(queue);
        else
            throw new BACnetErrorException(ErrorClass.property, ErrorCode.invalidDataType,
                    "Unsupported primitive id: " + tag);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (characterString == null ? 0 : characterString.hashCode());
        result = prime * result + (nullValue == null ? 0 : nullValue.hashCode());
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
        final OptionalCharacterString other = (OptionalCharacterString) obj;
        if (characterString == null) {
            if (other.characterString != null)
                return false;
        } else if (!characterString.equals(other.characterString))
            return false;
        if (nullValue == null) {
            if (other.nullValue != null)
                return false;
        } else if (!nullValue.equals(other.nullValue))
            return false;
        return true;
    }
}
