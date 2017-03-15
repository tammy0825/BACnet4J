package com.serotonin.bacnet4j.service.confirmed;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.TestUtils;
import com.serotonin.bacnet4j.npdu.test.TestNetwork;
import com.serotonin.bacnet4j.npdu.test.TestNetworkUtils;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.AddressBinding;
import com.serotonin.bacnet4j.type.constructed.BACnetArray;
import com.serotonin.bacnet4j.type.constructed.NameValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class AddListElementRequestTest {
    private final Address addr = TestNetworkUtils.toAddress(2);
    private LocalDevice localDevice;

    @Before
    public void before() throws Exception {
        localDevice = new LocalDevice(1, new DefaultTransport(new TestNetwork(1, 0)));
        localDevice.writePropertyInternal(PropertyIdentifier.tags,
                new BACnetArray<>( //
                        new NameValue("tag1", new Real(3.14F)), //
                        new NameValue("tag2", new Real(3.15F)), //
                        new NameValue("tag3", new Real(3.16F))));
        localDevice.initialize();
    }

    @After
    public void after() {
        localDevice.terminate();
    }

    @Test // 15.1.1.3.1
    public void errorTypes() {
        TestUtils.assertRequestHandleException( //
                () -> new AddListElementRequest( //
                        new ObjectIdentifier(ObjectType.accessDoor, 0), //
                        PropertyIdentifier.absenteeLimit, //
                        null, //
                        new SequenceOf<>() //
                ).handle(localDevice, addr), ErrorClass.object, ErrorCode.unknownObject);

        TestUtils.assertRequestHandleException( //
                () -> new AddListElementRequest( //
                        new ObjectIdentifier(ObjectType.device, 1), //
                        PropertyIdentifier.forId(5555), //
                        null, //
                        new SequenceOf<>() //
                ).handle(localDevice, addr), ErrorClass.property, ErrorCode.unknownProperty);

        TestUtils.assertRequestHandleException( //
                () -> new AddListElementRequest( //
                        new ObjectIdentifier(ObjectType.device, 1), //
                        PropertyIdentifier.absenteeLimit, //
                        null, //
                        new SequenceOf<>() //
                ).handle(localDevice, addr), ErrorClass.property, ErrorCode.unknownProperty);

        TestUtils.assertRequestHandleException( //
                () -> new AddListElementRequest( //
                        new ObjectIdentifier(ObjectType.device, 1), //
                        PropertyIdentifier.deviceAddressBinding, //
                        null, //
                        new SequenceOf<>(new ObjectIdentifier(ObjectType.device, 2)) //
                ).handle(localDevice, addr), ErrorClass.property, ErrorCode.datatypeNotSupported);

        TestUtils.assertRequestHandleException( //
                () -> new AddListElementRequest( //
                        new ObjectIdentifier(ObjectType.device, 1), //
                        PropertyIdentifier.tags, //
                        new UnsignedInteger(1), //
                        new SequenceOf<>(new ObjectIdentifier(ObjectType.device, 2)) //
                ).handle(localDevice, addr), ErrorClass.property, ErrorCode.datatypeNotSupported);

        TestUtils.assertRequestHandleException( //
                () -> new AddListElementRequest( //
                        new ObjectIdentifier(ObjectType.device, 1), //
                        PropertyIdentifier.deviceAddressBinding, //
                        new UnsignedInteger(1), //
                        new SequenceOf<>(new ObjectIdentifier(ObjectType.device, 2)) //
                ).handle(localDevice, addr), ErrorClass.property, ErrorCode.propertyIsNotAnArray);

        TestUtils.assertRequestHandleException( //
                () -> new AddListElementRequest( //
                        new ObjectIdentifier(ObjectType.device, 1), //
                        PropertyIdentifier.tags, //
                        null, //
                        new SequenceOf<>(new ObjectIdentifier(ObjectType.device, 2)) //
                ).handle(localDevice, addr), ErrorClass.services, ErrorCode.propertyIsNotAList);

        TestUtils.assertRequestHandleException( //
                () -> new AddListElementRequest( //
                        new ObjectIdentifier(ObjectType.device, 1), //
                        PropertyIdentifier.tags, //
                        new UnsignedInteger(0), //
                        new SequenceOf<>(new NameValue("tag2", new Real(3.15F))) //
                ).handle(localDevice, addr), ErrorClass.property, ErrorCode.invalidArrayIndex);

        TestUtils.assertRequestHandleException( //
                () -> new AddListElementRequest( //
                        new ObjectIdentifier(ObjectType.device, 1), //
                        PropertyIdentifier.tags, //
                        new UnsignedInteger(4), //
                        new SequenceOf<>(new NameValue("tag2", new Real(3.15F))) //
                ).handle(localDevice, addr), ErrorClass.property, ErrorCode.invalidArrayIndex);
    }

    @Test
    public void list() throws Exception {
        // Add a few elements.
        new AddListElementRequest( //
                new ObjectIdentifier(ObjectType.device, 1), //
                PropertyIdentifier.deviceAddressBinding, //
                null, //
                new SequenceOf<>(//
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 2), TestNetworkUtils.toAddress(2)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 3), TestNetworkUtils.toAddress(3)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 4), TestNetworkUtils.toAddress(4))) //
        ).handle(localDevice, addr);

        SequenceOf<AddressBinding> dabs = localDevice.getProperty(PropertyIdentifier.deviceAddressBinding);
        assertEquals(
                new SequenceOf<>( //
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 2), TestNetworkUtils.toAddress(2)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 3), TestNetworkUtils.toAddress(3)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 4), TestNetworkUtils.toAddress(4))),
                dabs);

        // Add a few more
        new AddListElementRequest( //
                new ObjectIdentifier(ObjectType.device, 1), //
                PropertyIdentifier.deviceAddressBinding, //
                null, //
                new SequenceOf<>(//
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 5), TestNetworkUtils.toAddress(5)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 6), TestNetworkUtils.toAddress(6))) //
        ).handle(localDevice, addr);

        dabs = localDevice.getProperty(PropertyIdentifier.deviceAddressBinding);
        assertEquals(
                new SequenceOf<>( //
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 2), TestNetworkUtils.toAddress(2)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 3), TestNetworkUtils.toAddress(3)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 4), TestNetworkUtils.toAddress(4)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 5), TestNetworkUtils.toAddress(5)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 6), TestNetworkUtils.toAddress(6))),
                dabs);
    }

    @Test
    public void array() throws Exception {
        // Replace all of the elements
        new AddListElementRequest( //
                new ObjectIdentifier(ObjectType.device, 1), //
                PropertyIdentifier.tags, //
                new UnsignedInteger(1), //
                new SequenceOf<>(//
                        new NameValue("tag4", new Real(3.24F)), //
                        new NameValue("tag5", new Real(3.25F)), //
                        new NameValue("tag6", new Real(3.26F))) //
        ).handle(localDevice, addr);

        SequenceOf<NameValue> tags = localDevice.getProperty(PropertyIdentifier.tags);
        assertEquals(new BACnetArray<>( //
                new NameValue("tag4", new Real(3.24F)), //
                new NameValue("tag5", new Real(3.25F)), //
                new NameValue("tag6", new Real(3.26F))), tags);

        // Only replace the second element
        new AddListElementRequest( //
                new ObjectIdentifier(ObjectType.device, 1), //
                PropertyIdentifier.tags, //
                new UnsignedInteger(2), //
                new SequenceOf<>(new NameValue("tag7", new Real(3.35F))) //
        ).handle(localDevice, addr);

        tags = localDevice.getProperty(PropertyIdentifier.tags);
        assertEquals(new BACnetArray<>( //
                new NameValue("tag4", new Real(3.24F)), //
                new NameValue("tag7", new Real(3.35F)), //
                new NameValue("tag6", new Real(3.26F))), tags);
    }
}
