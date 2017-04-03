package com.serotonin.bacnet4j.service.confirmed;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.npdu.test.TestNetwork;
import com.serotonin.bacnet4j.npdu.test.TestNetworkMap;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.enumerated.MessagePriority;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class ConfirmedTextMessageRequestTest {
    private final TestNetworkMap map = new TestNetworkMap();
    private LocalDevice ld1;
    private LocalDevice ld2;
    private RemoteDevice rd2;

    @Before
    public void before() throws Exception {
        ld1 = new LocalDevice(1, new DefaultTransport(new TestNetwork(map, 1, 0))).initialize();
        ld2 = new LocalDevice(2, new DefaultTransport(new TestNetwork(map, 2, 0))).initialize();

        rd2 = ld1.getRemoteDevice(2).get();
    }

    @After
    public void after() {
        ld1.terminate();
        ld2.terminate();
    }

    @Test
    public void happy() throws Exception {
        // Create the listener in device 2
        final AtomicReference<ObjectIdentifier> receivedObjectIdentifier = new AtomicReference<>(null);
        final AtomicReference<Choice> receivedMessageClass = new AtomicReference<>(null);
        final AtomicReference<MessagePriority> receivedMessagePriority = new AtomicReference<>(null);
        final AtomicReference<CharacterString> receivedMessage = new AtomicReference<>(null);
        ld2.getEventHandler().addListener(new DeviceEventAdapter() {
            @Override
            public void textMessageReceived(final ObjectIdentifier textMessageSourceDevice, final Choice messageClass,
                    final MessagePriority messagePriority, final CharacterString message) {
                receivedObjectIdentifier.set(textMessageSourceDevice);
                receivedMessageClass.set(messageClass);
                receivedMessagePriority.set(messagePriority);
                receivedMessage.set(message);
            }
        });

        // First request constructor.
        ld1.send(rd2, new ConfirmedTextMessageRequest(new ObjectIdentifier(ObjectType.accessCredential, 0),
                MessagePriority.normal, new CharacterString("The message"))).get();

        assertEquals(new ObjectIdentifier(ObjectType.accessCredential, 0), receivedObjectIdentifier.get());
        assertEquals(null, receivedMessageClass.get());
        assertEquals(MessagePriority.normal, receivedMessagePriority.get());
        assertEquals(new CharacterString("The message"), receivedMessage.get());

        // Second request constructor.
        ld1.send(rd2, new ConfirmedTextMessageRequest(new ObjectIdentifier(ObjectType.accessCredential, 0),
                new UnsignedInteger(12), MessagePriority.normal, new CharacterString("The message"))).get();

        assertEquals(new ObjectIdentifier(ObjectType.accessCredential, 0), receivedObjectIdentifier.get());
        assertEquals(new UnsignedInteger(12), receivedMessageClass.get().getDatum());
        assertEquals(MessagePriority.normal, receivedMessagePriority.get());
        assertEquals(new CharacterString("The message"), receivedMessage.get());

        // Third request constructor.
        ld1.send(rd2, new ConfirmedTextMessageRequest(new ObjectIdentifier(ObjectType.accessCredential, 0),
                new CharacterString("Some message class"), MessagePriority.normal, new CharacterString("The message")))
                .get();

        assertEquals(new ObjectIdentifier(ObjectType.accessCredential, 0), receivedObjectIdentifier.get());
        assertEquals(new CharacterString("Some message class"), receivedMessageClass.get().getDatum());
        assertEquals(MessagePriority.normal, receivedMessagePriority.get());
        assertEquals(new CharacterString("The message"), receivedMessage.get());
    }
}
