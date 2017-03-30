package com.serotonin.bacnet4j.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.serotonin.bacnet4j.exception.BACnetRuntimeException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.type.constructed.BACnetArray;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class MultistateValueTest extends AbstractTest {
    @Override
    public void before() throws Exception {
        // no op
    }

    @Test
    public void initialization() throws Exception {
        final MultistateValueObject mv = new MultistateValueObject(d1, 1, "mv1", 7, null, 1, false);
        assertEquals(new StatusFlags(false, false, false, false), mv.get(PropertyIdentifier.statusFlags));
    }

    @Test
    public void inconsistentStateText() throws Exception {
        try {
            new MultistateValueObject(d1, 0, "mv0", 7, new BACnetArray<>(new CharacterString("a")), 1, true);
            fail("Should have thrown an exception");
        } catch (final BACnetRuntimeException e) {
            final BACnetServiceException e1 = (BACnetServiceException) e.getCause();
            assertEquals(ErrorClass.property, e1.getErrorClass());
            assertEquals(ErrorCode.inconsistentConfiguration, e1.getErrorCode());
        }
    }

    @Test
    public void missingStateText() throws Exception {
        final MultistateValueObject mv = new MultistateValueObject(d1, 0, "mv0", 7, null, 1, true);

        try {
            mv.writeProperty(null,
                    new PropertyValue(PropertyIdentifier.stateText, new BACnetArray<>(new CharacterString("a"))));
            fail("Should have thrown an exception");
        } catch (final BACnetServiceException e) {
            assertEquals(ErrorClass.property, e.getErrorClass());
            assertEquals(ErrorCode.inconsistentConfiguration, e.getErrorCode());
        }
    }

    @Test
    public void stateText() throws Exception {
        final MultistateValueObject mv = new MultistateValueObject(d1, 0, "mv0", 7, null, 1, true);

        mv.writeProperty(null,
                new PropertyValue(PropertyIdentifier.stateText,
                        new BACnetArray<>(new CharacterString("a"), new CharacterString("b"), new CharacterString("c"),
                                new CharacterString("d"), new CharacterString("e"), new CharacterString("f"),
                                new CharacterString("g"))));

        mv.writeProperty(null, new PropertyValue(PropertyIdentifier.numberOfStates, new UnsignedInteger(6)));
        assertEquals(
                new BACnetArray<>(new CharacterString("a"), new CharacterString("b"), new CharacterString("c"),
                        new CharacterString("d"), new CharacterString("e"), new CharacterString("f")),
                mv.get(PropertyIdentifier.stateText));

        mv.writeProperty(null, new PropertyValue(PropertyIdentifier.numberOfStates, new UnsignedInteger(8)));
        assertEquals(new BACnetArray<>(new CharacterString("a"), new CharacterString("b"), new CharacterString("c"),
                new CharacterString("d"), new CharacterString("e"), new CharacterString("f"), new CharacterString(""),
                new CharacterString("")), mv.get(PropertyIdentifier.stateText));
    }
}
