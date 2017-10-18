package com.serotonin.bacnet4j.service.confirmed;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.test.TestNetwork;
import com.serotonin.bacnet4j.npdu.test.TestNetworkMap;
import com.serotonin.bacnet4j.npdu.test.TestNetworkUtils;
import com.serotonin.bacnet4j.obj.GroupObject;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyMultipleAck;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult.Result;
import com.serotonin.bacnet4j.type.constructed.ReadAccessSpecification;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.CharacterString;

public class ReadPropertyMultipleRequestTest {
    private final TestNetworkMap map = new TestNetworkMap();
    private final Address addr = TestNetworkUtils.toAddress(2);
    private LocalDevice localDevice;
    private GroupObject g0;

    @Before
    public void before() throws Exception {
        localDevice = new LocalDevice(1, new DefaultTransport(new TestNetwork(map, 1, 0)));
        localDevice.initialize();

        g0 = new GroupObject(localDevice, 0, "g0", new SequenceOf<>());
        g0.writePropertyInternal(PropertyIdentifier.description, new CharacterString("my description"));
    }

    @After
    public void after() {
        localDevice.terminate();
    }

    @Test
    public void allProperties() throws BACnetException {
        final SequenceOf<ReadAccessSpecification> listOfReadAccessSpecs = new SequenceOf<>(
                new ReadAccessSpecification(g0.getId(), PropertyIdentifier.all));
        final ReadPropertyMultipleAck ack = (ReadPropertyMultipleAck) new ReadPropertyMultipleRequest(
                listOfReadAccessSpecs).handle(localDevice, addr);

        final List<ReadAccessResult> readAccessResults = ack.getListOfReadAccessResults().getValues();
        assertEquals(1, readAccessResults.size());
        assertEquals(g0.getId(), readAccessResults.get(0).getObjectIdentifier());
        final List<Result> results = readAccessResults.get(0).getListOfResults().getValues();
        assertEquals(6, results.size());
        assertEquals(new Result(PropertyIdentifier.objectType, null, ObjectType.group), results.get(0));
        assertEquals(new Result(PropertyIdentifier.listOfGroupMembers, null, new SequenceOf<>()), results.get(1));
        assertEquals(new Result(PropertyIdentifier.presentValue, null, new SequenceOf<>()), results.get(2));
        assertEquals(new Result(PropertyIdentifier.objectIdentifier, null, g0.getId()), results.get(3));
        assertEquals(new Result(PropertyIdentifier.description, null, new CharacterString("my description")),
                results.get(4));
        assertEquals(new Result(PropertyIdentifier.objectName, null, new CharacterString("g0")), results.get(5));
    }

    @Test
    public void requiredProperties() throws BACnetException {
        final SequenceOf<ReadAccessSpecification> listOfReadAccessSpecs = new SequenceOf<>(
                new ReadAccessSpecification(g0.getId(), PropertyIdentifier.required));
        final ReadPropertyMultipleAck ack = (ReadPropertyMultipleAck) new ReadPropertyMultipleRequest(
                listOfReadAccessSpecs).handle(localDevice, addr);

        final List<ReadAccessResult> readAccessResults = ack.getListOfReadAccessResults().getValues();
        assertEquals(1, readAccessResults.size());
        assertEquals(g0.getId(), readAccessResults.get(0).getObjectIdentifier());
        final List<Result> results = readAccessResults.get(0).getListOfResults().getValues();
        assertEquals(5, results.size());
        assertEquals(new Result(PropertyIdentifier.objectType, null, ObjectType.group), results.get(0));
        assertEquals(new Result(PropertyIdentifier.listOfGroupMembers, null, new SequenceOf<>()), results.get(1));
        assertEquals(new Result(PropertyIdentifier.presentValue, null, new SequenceOf<>()), results.get(2));
        assertEquals(new Result(PropertyIdentifier.objectIdentifier, null, g0.getId()), results.get(3));
        assertEquals(new Result(PropertyIdentifier.objectName, null, new CharacterString("g0")), results.get(4));
    }

    @Test
    public void optionalProperties() throws BACnetException {
        final SequenceOf<ReadAccessSpecification> listOfReadAccessSpecs = new SequenceOf<>(
                new ReadAccessSpecification(g0.getId(), PropertyIdentifier.optional));
        final ReadPropertyMultipleAck ack = (ReadPropertyMultipleAck) new ReadPropertyMultipleRequest(
                listOfReadAccessSpecs).handle(localDevice, addr);

        final List<ReadAccessResult> readAccessResults = ack.getListOfReadAccessResults().getValues();
        assertEquals(1, readAccessResults.size());
        assertEquals(g0.getId(), readAccessResults.get(0).getObjectIdentifier());
        final List<Result> results = readAccessResults.get(0).getListOfResults().getValues();
        assertEquals(1, results.size());
        assertEquals(new Result(PropertyIdentifier.description, null, new CharacterString("my description")),
                results.get(0));
    }
}
