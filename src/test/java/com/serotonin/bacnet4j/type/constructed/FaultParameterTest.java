package com.serotonin.bacnet4j.type.constructed;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.serotonin.bacnet4j.enums.DayOfWeek;
import com.serotonin.bacnet4j.enums.Month;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.FaultParameter.FaultCharacterString;
import com.serotonin.bacnet4j.type.constructed.FaultParameter.FaultExtended;
import com.serotonin.bacnet4j.type.constructed.FaultParameter.FaultExtended.FaultExtendedParameter;
import com.serotonin.bacnet4j.type.constructed.FaultParameter.FaultLifeSafety;
import com.serotonin.bacnet4j.type.constructed.FaultParameter.FaultState;
import com.serotonin.bacnet4j.type.constructed.FaultParameter.FaultStatusFlags;
import com.serotonin.bacnet4j.type.enumerated.Action;
import com.serotonin.bacnet4j.type.enumerated.LifeSafetyState;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.ProgramError;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.WriteStatus;
import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Date;
import com.serotonin.bacnet4j.type.primitive.Double;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.bacnet4j.type.primitive.Time;
import com.serotonin.bacnet4j.type.primitive.Unsigned16;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

public class FaultParameterTest {
    @Test
    public void characterString() throws BACnetException {
        final FaultParameter faultParameter = new FaultParameter(new FaultCharacterString(new SequenceOf<>( //
                new CharacterString("a"), //
                new CharacterString("bb"), //
                new CharacterString("ccc"))));

        final ByteQueue queue = new ByteQueue();
        faultParameter.write(queue);

        assertEquals(faultParameter, new FaultParameter(queue));
    }

    @Test
    public void extended() throws BACnetException {
        final FaultParameter faultParameter = new FaultParameter(new FaultExtended(new Unsigned16(236),
                new UnsignedInteger(314),
                new SequenceOf<>( //
                        new FaultExtendedParameter(new Null()), //
                        new FaultExtendedParameter(new Real(3.1415F)), //
                        new FaultExtendedParameter(new UnsignedInteger(314)), //
                        new FaultExtendedParameter(new Boolean(true)), //
                        new FaultExtendedParameter(new SignedInteger(-314)), //
                        new FaultExtendedParameter(new Double(Math.PI)), //
                        new FaultExtendedParameter(new OctetString(new byte[] { 0, 1, 2, 3, 4, 5 })), //
                        new FaultExtendedParameter(new CharacterString("This is an extended fault parameter")), //
                        new FaultExtendedParameter(new BitString(new boolean[] { true, false, false, true })), //
                        new FaultExtendedParameter(PropertyIdentifier.absenteeLimit), //
                        new FaultExtendedParameter(new Date(2017, Month.FEBRUARY, 1, DayOfWeek.WEDNESDAY)), //
                        new FaultExtendedParameter(new Time(17, 04, 31, 57)), //
                        new FaultExtendedParameter(new ObjectIdentifier(ObjectType.credentialDataInput, 12)), //
                        new FaultExtendedParameter(new DeviceObjectPropertyReference(
                                new ObjectIdentifier(ObjectType.largeAnalogValue, 13), PropertyIdentifier.description,
                                null, new ObjectIdentifier(ObjectType.device, 1234))))));

        final ByteQueue queue = new ByteQueue();
        faultParameter.write(queue);

        assertEquals(faultParameter, new FaultParameter(queue));
    }

    @Test
    public void lifeSafety() throws BACnetException {
        final FaultParameter faultParameter = new FaultParameter(new FaultLifeSafety(
                new SequenceOf<>( //
                        LifeSafetyState.abnormal, //
                        LifeSafetyState.active, //
                        LifeSafetyState.blocked, //
                        LifeSafetyState.duress), //
                new DeviceObjectPropertyReference(new ObjectIdentifier(ObjectType.largeAnalogValue, 13),
                        PropertyIdentifier.description, null, new ObjectIdentifier(ObjectType.device, 1234))));

        final ByteQueue queue = new ByteQueue();
        faultParameter.write(queue);

        assertEquals(faultParameter, new FaultParameter(queue));
    }

    @Test
    public void state() throws BACnetException {
        final FaultParameter faultParameter = new FaultParameter(new FaultState(new SequenceOf<>( //
                new PropertyStates(new Boolean(true)), //
                new PropertyStates(new Boolean(false)), //
                new PropertyStates(ProgramError.loadFailed), //
                new PropertyStates(Action.reverse), //
                new PropertyStates(NotifyType.ackNotification), //
                new PropertyStates(NotifyType.event), //
                new PropertyStates(WriteStatus.failed), //
                new PropertyStates(WriteStatus.failed), //
                new PropertyStates(WriteStatus.inProgress), //
                new PropertyStates(WriteStatus.successful) //
        )));

        final ByteQueue queue = new ByteQueue();
        faultParameter.write(queue);

        assertEquals(faultParameter, new FaultParameter(queue));
    }

    @Test
    public void statusFlags() throws BACnetException {
        final FaultParameter faultParameter = new FaultParameter(new FaultStatusFlags(
                new DeviceObjectPropertyReference(new ObjectIdentifier(ObjectType.largeAnalogValue, 13),
                        PropertyIdentifier.description, null, new ObjectIdentifier(ObjectType.device, 1234))));

        final ByteQueue queue = new ByteQueue();
        faultParameter.write(queue);

        assertEquals(faultParameter, new FaultParameter(queue));
    }
}
