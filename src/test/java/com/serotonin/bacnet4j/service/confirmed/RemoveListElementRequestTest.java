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

public class RemoveListElementRequestTest {
    private final Address addr = TestNetworkUtils.toAddress(2);
    private LocalDevice localDevice;

    @Before
    public void before() throws Exception {
        localDevice = new LocalDevice(1, new DefaultTransport(new TestNetwork(1, 0)));
        localDevice.writePropertyInternal(PropertyIdentifier.deviceAddressBinding,
                new SequenceOf<>( //
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 2), TestNetworkUtils.toAddress(2)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 3), TestNetworkUtils.toAddress(3)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 4), TestNetworkUtils.toAddress(4)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 5), TestNetworkUtils.toAddress(5)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 6), TestNetworkUtils.toAddress(6)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 7), TestNetworkUtils.toAddress(7)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 8), TestNetworkUtils.toAddress(8))));
        localDevice.writePropertyInternal(PropertyIdentifier.tags,
                new BACnetArray<>( //
                        new NameValue("tag1", new Real(3.14F)), //
                        new NameValue("tag2", new Real(3.15F)), //
                        new NameValue("tag3", new Real(3.16F)), //
                        new NameValue("tag4", new Real(3.17F)), //
                        new NameValue("tag5", new Real(3.18F)), //
                        new NameValue("tag6", new Real(3.19F))));
        localDevice.initialize();
    }

    @After
    public void after() {
        localDevice.terminate();
    }

    @Test // 15.2.1.3.1
    public void errorTypes() {
        TestUtils.assertRequestHandleException( //
                () -> new RemoveListElementRequest( //
                        new ObjectIdentifier(ObjectType.accessDoor, 0), //
                        PropertyIdentifier.absenteeLimit, //
                        null, //
                        new SequenceOf<>() //
                ).handle(localDevice, addr), ErrorClass.object, ErrorCode.unknownObject);

        TestUtils.assertRequestHandleException( //
                () -> new RemoveListElementRequest( //
                        new ObjectIdentifier(ObjectType.device, 1), //
                        PropertyIdentifier.forId(5555), //
                        null, //
                        new SequenceOf<>() //
                ).handle(localDevice, addr), ErrorClass.property, ErrorCode.unknownProperty);

        TestUtils.assertRequestHandleException( //
                () -> new RemoveListElementRequest( //
                        new ObjectIdentifier(ObjectType.device, 1), //
                        PropertyIdentifier.absenteeLimit, //
                        null, //
                        new SequenceOf<>() //
                ).handle(localDevice, addr), ErrorClass.property, ErrorCode.unknownProperty);

        TestUtils.assertRequestHandleException( //
                () -> new RemoveListElementRequest( //
                        new ObjectIdentifier(ObjectType.device, 1), //
                        PropertyIdentifier.deviceAddressBinding, //
                        null, //
                        new SequenceOf<>(new ObjectIdentifier(ObjectType.device, 2)) //
                ).handle(localDevice, addr), ErrorClass.property, ErrorCode.datatypeNotSupported);

        TestUtils.assertRequestHandleException( //
                () -> new RemoveListElementRequest( //
                        new ObjectIdentifier(ObjectType.device, 1), //
                        PropertyIdentifier.tags, //
                        new UnsignedInteger(1), //
                        new SequenceOf<>(new ObjectIdentifier(ObjectType.device, 2)) //
                ).handle(localDevice, addr), ErrorClass.property, ErrorCode.writeAccessDenied);

        TestUtils.assertRequestHandleException( //
                () -> new RemoveListElementRequest( //
                        new ObjectIdentifier(ObjectType.device, 1), //
                        PropertyIdentifier.tags, //
                        null, //
                        new SequenceOf<>(new ObjectIdentifier(ObjectType.device, 2)) //
                ).handle(localDevice, addr), ErrorClass.services, ErrorCode.propertyIsNotAList);

        TestUtils.assertRequestHandleException( //
                () -> new RemoveListElementRequest( //
                        new ObjectIdentifier(ObjectType.device, 1), //
                        PropertyIdentifier.deviceAddressBinding, //
                        null, //
                        new SequenceOf<>(new AddressBinding(new ObjectIdentifier(ObjectType.device, 20),
                                TestNetworkUtils.toAddress(20))) //
                ).handle(localDevice, addr), ErrorClass.services, ErrorCode.listElementNotFound);
    }

    @Test
    public void list() throws Exception {
        // Remove a few elements.
        new RemoveListElementRequest( //
                new ObjectIdentifier(ObjectType.device, 1), //
                PropertyIdentifier.deviceAddressBinding, //
                null, //
                new SequenceOf<>(//
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 3), TestNetworkUtils.toAddress(3)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 5), TestNetworkUtils.toAddress(5)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 7), TestNetworkUtils.toAddress(7))) //
        ).handle(localDevice, addr);

        SequenceOf<AddressBinding> dabs = localDevice.getProperty(PropertyIdentifier.deviceAddressBinding);
        assertEquals(
                new SequenceOf<>( //
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 2), TestNetworkUtils.toAddress(2)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 4), TestNetworkUtils.toAddress(4)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 6), TestNetworkUtils.toAddress(6)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 8), TestNetworkUtils.toAddress(8))),
                dabs);

        // Remove a few more.
        new RemoveListElementRequest( //
                new ObjectIdentifier(ObjectType.device, 1), //
                PropertyIdentifier.deviceAddressBinding, //
                null, //
                new SequenceOf<>(//
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 2), TestNetworkUtils.toAddress(2)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 6), TestNetworkUtils.toAddress(6)),
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 8), TestNetworkUtils.toAddress(8))) //
        ).handle(localDevice, addr);

        dabs = localDevice.getProperty(PropertyIdentifier.deviceAddressBinding);
        assertEquals(
                new SequenceOf<>( //
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 4), TestNetworkUtils.toAddress(4))),
                dabs);

        // Remove the last one.
        new RemoveListElementRequest( //
                new ObjectIdentifier(ObjectType.device, 1), //
                PropertyIdentifier.deviceAddressBinding, //
                null, //
                new SequenceOf<>(//
                        new AddressBinding(new ObjectIdentifier(ObjectType.device, 4), TestNetworkUtils.toAddress(4))) //
        ).handle(localDevice, addr);

        dabs = localDevice.getProperty(PropertyIdentifier.deviceAddressBinding);
        assertEquals(new SequenceOf<>(), dabs);
    }
}
