package com.serotonin.bacnet4j.obj;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.BACnetArray;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class BACnetObjectListenerTest extends AbstractTest {
    private MultistateValueObject mv;

    @Override
    public void before() throws Exception {
        final BACnetArray<CharacterString> stateText = new BACnetArray<>( //
                new CharacterString("Off"), //
                new CharacterString("On"), //
                new CharacterString("Auto"));
        mv = new MultistateValueObject(0, "mv0", 3, stateText, 1, false);
        mv.supportCommandable(new UnsignedInteger(0));
        d2.addObject(mv);
    }

    @Test
    public void listener() throws BACnetException {
        final List<PropChange> changes = new ArrayList<>();
        mv.addListener((pid, oldValue, newValue) -> {
            final PropChange change = new PropChange();
            change.pid = pid;
            change.oldValue = oldValue;
            change.newValue = newValue;
            changes.add(change);
        });

        d1.send(rd2, new WritePropertyRequest(new ObjectIdentifier(ObjectType.multiStateValue, 0),
                PropertyIdentifier.presentValue, null, new UnsignedInteger(2), new UnsignedInteger(8))).get();
        assertEquals(1, changes.size());
        assertEquals(PropertyIdentifier.presentValue, changes.get(0).pid);
        assertEquals(new UnsignedInteger(1), changes.get(0).oldValue);
        assertEquals(new UnsignedInteger(2), changes.get(0).newValue);

        d1.send(rd2, new WritePropertyRequest(new ObjectIdentifier(ObjectType.multiStateValue, 0),
                PropertyIdentifier.description, null, new CharacterString("a new description"), null)).get();

        assertEquals(3, changes.size());
        assertEquals(PropertyIdentifier.description, changes.get(1).pid);
        assertEquals(null, changes.get(1).oldValue);
        assertEquals(new CharacterString("a new description"), changes.get(1).newValue);
        assertEquals(PropertyIdentifier.propertyList, changes.get(2).pid);
    }

    class PropChange {
        PropertyIdentifier pid;
        Encodable oldValue;
        Encodable newValue;
    }
}
