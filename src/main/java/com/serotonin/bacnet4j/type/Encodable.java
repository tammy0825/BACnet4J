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
package com.serotonin.bacnet4j.type;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetRejectException;
import com.serotonin.bacnet4j.exception.ReflectionException;
import com.serotonin.bacnet4j.obj.ObjectProperties;
import com.serotonin.bacnet4j.obj.PropertyTypeDefinition;
import com.serotonin.bacnet4j.service.VendorServiceKey;
import com.serotonin.bacnet4j.type.constructed.BACnetArray;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.RejectReason;
import com.serotonin.bacnet4j.type.eventParameter.EventParameter;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.Primitive;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.BACnetUtils;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

abstract public class Encodable implements Serializable {
    private static final long serialVersionUID = -4378016931626697698L;

    abstract public void write(ByteQueue queue);

    abstract public void write(ByteQueue queue, int contextId);

    @Override
    public String toString() {
        return "Encodable(" + getClass().getName() + ")";
    }

    protected static void popTagData(final ByteQueue queue, final TagData tagData) {
        peekTagData(queue, tagData);
        queue.pop(tagData.tagLength);
    }

    protected static void peekTagData(final ByteQueue queue, final TagData tagData) {
        int peekIndex = 0;
        final byte b = queue.peek(peekIndex++);
        tagData.tagNumber = (b & 0xff) >> 4;
        tagData.contextSpecific = (b & 8) != 0;
        tagData.length = b & 7;

        if (tagData.tagNumber == 0xf)
            // Extended tag.
            tagData.tagNumber = BACnetUtils.toInt(queue.peek(peekIndex++));

        if (tagData.length == 5) {
            tagData.length = BACnetUtils.toInt(queue.peek(peekIndex++));
            if (tagData.length == 254)
                tagData.length = BACnetUtils.toInt(queue.peek(peekIndex++)) << 8
                        | BACnetUtils.toInt(queue.peek(peekIndex++));
            else if (tagData.length == 255)
                tagData.length = BACnetUtils.toLong(queue.peek(peekIndex++)) << 24
                        | BACnetUtils.toLong(queue.peek(peekIndex++)) << 16
                        | BACnetUtils.toLong(queue.peek(peekIndex++)) << 8
                        | BACnetUtils.toLong(queue.peek(peekIndex++));
        }

        tagData.tagLength = peekIndex;
    }

    protected static int peekTagNumber(final ByteQueue queue) {
        if (queue.size() == 0)
            return -1;

        // Take a peek at the tag number.
        int tagNumber = (queue.peek(0) & 0xff) >> 4;
        if (tagNumber == 15)
            tagNumber = queue.peek(1) & 0xff;
        return tagNumber;
    }

    //
    // Write context tags for base types.
    protected void writeContextTag(final ByteQueue queue, final int contextId, final boolean start) {
        if (contextId <= 14)
            queue.push(contextId << 4 | (start ? 0xe : 0xf));
        else {
            queue.push(start ? 0xfe : 0xff);
            queue.push(contextId);
        }
    }

    //
    // Read start tags.
    protected static int readStart(final ByteQueue queue) {
        if (queue.size() == 0)
            return -1;

        final int b = queue.peek(0) & 0xff;
        if ((b & 0xf) != 0xe)
            return -1;
        if ((b & 0xf0) == 0xf0)
            return queue.peek(1);
        return b >> 4;
    }

    protected static int popStart(final ByteQueue queue) {
        final int contextId = readStart(queue);
        if (contextId != -1) {
            queue.pop();
            if (contextId > 14)
                queue.pop();
        }
        return contextId;
    }

    protected static void popStart(final ByteQueue queue, final int contextId) throws BACnetErrorException {
        if (popStart(queue) != contextId)
            throw new BACnetErrorException(ErrorClass.property, ErrorCode.missingRequiredParameter);
    }

    //
    // Read end tags.
    protected static int readEnd(final ByteQueue queue) {
        if (queue.size() == 0)
            return -1;
        final int b = queue.peek(0) & 0xff;
        if ((b & 0xf) != 0xf)
            return -1;
        if ((b & 0xf0) == 0xf0)
            return queue.peek(1);
        return b >> 4;
    }

    protected static void popEnd(final ByteQueue queue, final int contextId) throws BACnetErrorException {
        if (readEnd(queue) != contextId)
            throw new BACnetErrorException(ErrorClass.property, ErrorCode.missingRequiredParameter);
        queue.pop();
        if (contextId > 14)
            queue.pop();
    }

    private static boolean matchContextId(final ByteQueue queue, final int contextId) {
        return peekTagNumber(queue) == contextId;
    }

    protected static boolean matchStartTag(final ByteQueue queue, final int contextId) {
        return matchContextId(queue, contextId) && (queue.peek(0) & 0xf) == 0xe;
    }

    protected static boolean matchEndTag(final ByteQueue queue, final int contextId) {
        return matchContextId(queue, contextId) && (queue.peek(0) & 0xf) == 0xf;
    }

    protected static boolean matchNonEndTag(final ByteQueue queue, final int contextId) {
        return matchContextId(queue, contextId) && (queue.peek(0) & 0xf) != 0xf;
    }

    //
    // Basic read and write. Pretty trivial.
    protected static void write(final ByteQueue queue, final Encodable type) {
        type.write(queue);
    }

    @SuppressWarnings("unchecked")
    protected static <T extends Encodable> T read(final ByteQueue queue, final Class<T> clazz) throws BACnetException {
        if (clazz == Primitive.class)
            return (T) Primitive.createPrimitive(queue);

        try {
            return clazz.getConstructor(new Class[] { ByteQueue.class }).newInstance(new Object[] { queue });
        } catch (final NoSuchMethodException e) {
            // Check if this is an EventParameter
            if (clazz == EventParameter.class)
                return (T) EventParameter.createEventParameter(queue);
            throw new BACnetException(e);
        } catch (final InvocationTargetException e) {
            // Check if there is a wrapped BACnet exception
            if (e.getCause() instanceof BACnetException)
                throw (BACnetException) e.getCause();
            throw new ReflectionException(e);
        } catch (final Exception e) {
            throw new BACnetException(e);
        }
    }

    //
    // Read and write with context id.
    protected static <T extends Encodable> T read(final ByteQueue queue, final Class<T> clazz, final int contextId)
            throws BACnetException {
        if (!matchNonEndTag(queue, contextId))
            throw new BACnetErrorException(ErrorClass.property, ErrorCode.missingRequiredParameter);

        if (Primitive.class.isAssignableFrom(clazz))
            return read(queue, clazz);
        return readWrapped(queue, clazz, contextId);
    }

    protected static void write(final ByteQueue queue, final Encodable type, final int contextId) {
        type.write(queue, contextId);
    }

    //
    // Optional read and write.
    protected static void writeOptional(final ByteQueue queue, final Encodable type) {
        if (type == null)
            return;
        write(queue, type);
    }

    protected static void writeOptional(final ByteQueue queue, final Encodable type, final int contextId) {
        if (type == null)
            return;
        write(queue, type, contextId);
    }

    protected static <T extends Encodable> T readOptional(final ByteQueue queue, final Class<T> clazz,
            final int contextId) throws BACnetException {
        if (!matchNonEndTag(queue, contextId))
            return null;
        return read(queue, clazz, contextId);
    }

    //
    // Read lists
    protected static <T extends Encodable> SequenceOf<T> readSequenceOf(final ByteQueue queue, final Class<T> clazz)
            throws BACnetException {
        return new SequenceOf<>(queue, clazz);
    }

    protected static <T extends Encodable> SequenceOf<T> readSequenceOf(final ByteQueue queue, final int count,
            final Class<T> clazz) throws BACnetException {
        return new SequenceOf<>(queue, count, clazz);
    }

    protected static <T extends Encodable> SequenceOf<T> readSequenceOf(final ByteQueue queue, final Class<T> clazz,
            final int contextId) throws BACnetException {
        popStart(queue, contextId);
        final SequenceOf<T> result = new SequenceOf<>(queue, clazz, contextId);
        popEnd(queue, contextId);
        return result;
    }

    protected static <T extends Encodable> T readSequenceType(final ByteQueue queue, final Class<T> clazz,
            final int contextId) throws BACnetException {
        popStart(queue, contextId);
        T result;
        try {
            result = clazz.getConstructor(new Class[] { ByteQueue.class, Integer.TYPE })
                    .newInstance(new Object[] { queue, contextId });
        } catch (final Exception e) {
            throw new BACnetException(e);
        }
        popEnd(queue, contextId);
        return result;
    }

    protected static SequenceOf<Choice> readSequenceOfChoice(final ByteQueue queue,
            final List<Class<? extends Encodable>> classes, final int contextId) throws BACnetException {
        popStart(queue, contextId);
        final SequenceOf<Choice> result = new SequenceOf<>();
        while (readEnd(queue) != contextId)
            result.add(new Choice(queue, classes));
        popEnd(queue, contextId);
        return result;
    }

    protected static <T extends Encodable> SequenceOf<T> readOptionalSequenceOf(final ByteQueue queue,
            final Class<T> clazz, final int contextId) throws BACnetException {
        if (readStart(queue) != contextId)
            return null;
        return readSequenceOf(queue, clazz, contextId);
    }

    protected static <T extends Encodable> BACnetArray<T> readArray(final ByteQueue queue, final Class<T> clazz,
            final int contextId) throws BACnetException {
        popStart(queue, contextId);
        final BACnetArray<T> result = new BACnetArray<>(queue, clazz, contextId);
        popEnd(queue, contextId);
        return result;
    }

    // Read and write encodable
    protected static void writeEncodable(final ByteQueue queue, final Encodable type, final int contextId) {
        if (Primitive.class.isAssignableFrom(type.getClass()))
            ((Primitive) type).writeEncodable(queue, contextId);
        else
            type.write(queue, contextId);
    }

    protected static Encodable readEncodable(final ByteQueue queue, final ObjectType objectType,
            final PropertyIdentifier propertyIdentifier, final UnsignedInteger propertyArrayIndex, final int contextId)
            throws BACnetException {
        // A property array index of 0 indicates a request for the length of an array.
        if (propertyArrayIndex != null && propertyArrayIndex.intValue() == 0)
            return readWrapped(queue, UnsignedInteger.class, contextId);

        if (!matchNonEndTag(queue, contextId))
            throw new BACnetErrorException(ErrorClass.property, ErrorCode.missingRequiredParameter);

        final PropertyTypeDefinition def = ObjectProperties.getPropertyTypeDefinition(objectType, propertyIdentifier);
        if (def == null)
            return new AmbiguousValue(queue, contextId).attemptConversion();

        if (ObjectProperties.isCommandable(objectType, propertyIdentifier)) {
            // If the object is commandable, it could be set to Null, so we need to treat it as ambiguous.
            final AmbiguousValue amb = new AmbiguousValue(queue, contextId);

            if (amb.isNull())
                return new Null();

            // Try converting to the definition value.
            return amb.convertTo(def.getClazz());
        }

        if (propertyArrayIndex != null) {
            if (!def.isSequenceOf() && !SequenceOf.class.isAssignableFrom(def.getClazz()))
                throw new BACnetErrorException(ErrorClass.property, ErrorCode.propertyIsNotAList);
            if (SequenceOf.class.isAssignableFrom(def.getClazz()))
                return readWrapped(queue, def.getInnerType(), contextId);
        } else {
            if (def.isSequenceOf())
                return readSequenceOf(queue, def.getClazz(), contextId);
            if (SequenceOf.class.isAssignableFrom(def.getClazz()))
                return readSequenceType(queue, def.getClazz(), contextId);
        }

        return readWrapped(queue, def.getClazz(), contextId);
    }

    protected static Encodable readOptionalEncodable(final ByteQueue queue, final ObjectType objectType,
            final PropertyIdentifier propertyIdentifier, final int contextId) throws BACnetException {
        if (readStart(queue) != contextId)
            return null;
        return readEncodable(queue, objectType, propertyIdentifier, null, contextId);
    }

    protected static SequenceOf<? extends Encodable> readSequenceOfEncodable(final ByteQueue queue,
            final ObjectType objectType, final PropertyIdentifier propertyIdentifier, final int contextId)
            throws BACnetException {
        final PropertyTypeDefinition def = ObjectProperties.getPropertyTypeDefinition(objectType, propertyIdentifier);
        if (def == null)
            return readSequenceOf(queue, AmbiguousValue.class, contextId);
        return readSequenceOf(queue, def.getClazz(), contextId);
    }

    // Read vendor-specific
    protected static Sequence readVendorSpecific(final ByteQueue queue, final UnsignedInteger vendorId,
            final UnsignedInteger serviceNumber, final Map<VendorServiceKey, SequenceDefinition> resolutions,
            final int contextId) throws BACnetException {
        if (readStart(queue) != contextId)
            return null;

        final VendorServiceKey key = new VendorServiceKey(vendorId, serviceNumber);
        final SequenceDefinition def = resolutions.get(key);
        if (def == null)
            throw new BACnetRejectException(RejectReason.unrecognizedService);

        return new Sequence(def, queue, contextId);
    }

    private static <T extends Encodable> T readWrapped(final ByteQueue queue, final Class<T> clazz, final int contextId)
            throws BACnetException {
        popStart(queue, contextId);
        final T result = read(queue, clazz);
        popEnd(queue, contextId);
        return result;
    }
}
