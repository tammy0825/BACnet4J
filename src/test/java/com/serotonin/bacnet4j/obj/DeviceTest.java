package com.serotonin.bacnet4j.obj;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.EngineeringUnits;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.error.ErrorClassAndCode;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.RequestUtils;

public class DeviceTest extends AbstractTest {
    @SuppressWarnings("unused")
    @Override
    public void before() throws Exception {
        new AnalogValueObject(d1, 0, "av0", 50, EngineeringUnits.amperes, false);
        new AnalogValueObject(d1, 1, "av1", 50, EngineeringUnits.amperes, false);
        new AnalogValueObject(d1, 2, "av2", 50, EngineeringUnits.amperes, false);
        new BinaryValueObject(d1, 0, "bv0", BinaryPV.inactive, false);
        new BinaryValueObject(d1, 1, "bv1", BinaryPV.inactive, false);
        new BinaryValueObject(d1, 2, "bv2", BinaryPV.inactive, false);
        new BinaryValueObject(d1, 3, "bv3", BinaryPV.inactive, false);
    }

    @Test
    public void objectList() throws BACnetException {
        // Get the whole list.
        final SequenceOf<ObjectIdentifier> oids = RequestUtils.getProperty(d2, rd1, PropertyIdentifier.objectList);
        assertEquals(new ObjectIdentifier(ObjectType.device, 1), oids.get(1));
        assertEquals(new ObjectIdentifier(ObjectType.analogValue, 0), oids.get(2));
        assertEquals(new ObjectIdentifier(ObjectType.analogValue, 1), oids.get(3));
        assertEquals(new ObjectIdentifier(ObjectType.analogValue, 2), oids.get(4));
        assertEquals(new ObjectIdentifier(ObjectType.binaryValue, 0), oids.get(5));
        assertEquals(new ObjectIdentifier(ObjectType.binaryValue, 1), oids.get(6));
        assertEquals(new ObjectIdentifier(ObjectType.binaryValue, 2), oids.get(7));
        assertEquals(new ObjectIdentifier(ObjectType.binaryValue, 3), oids.get(8));

        // Get one element of the list.
        final UnsignedInteger length = RequestUtils.getProperty(d2, rd1, new ObjectIdentifier(ObjectType.device, 1),
                PropertyIdentifier.objectList, 0);
        assertEquals(8, length.intValue());

        ObjectIdentifier oid = RequestUtils.getProperty(d2, rd1, new ObjectIdentifier(ObjectType.device, 1),
                PropertyIdentifier.objectList, 1);
        assertEquals(new ObjectIdentifier(ObjectType.device, 1), oid);

        oid = RequestUtils.getProperty(d2, rd1, new ObjectIdentifier(ObjectType.device, 1),
                PropertyIdentifier.objectList, 4);
        assertEquals(new ObjectIdentifier(ObjectType.analogValue, 2), oid);

        oid = RequestUtils.getProperty(d2, rd1, new ObjectIdentifier(ObjectType.device, 1),
                PropertyIdentifier.objectList, 8);
        assertEquals(new ObjectIdentifier(ObjectType.binaryValue, 3), oid);

        final ErrorClassAndCode e = RequestUtils.getProperty(d2, rd1, new ObjectIdentifier(ObjectType.device, 1),
                PropertyIdentifier.objectList, 9);
        assertEquals(ErrorClass.property, e.getErrorClass());
        assertEquals(ErrorCode.invalidArrayIndex, e.getErrorCode());
    }
}
