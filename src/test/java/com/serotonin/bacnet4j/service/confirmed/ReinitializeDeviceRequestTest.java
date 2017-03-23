package com.serotonin.bacnet4j.service.confirmed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.TestUtils;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.ErrorAPDUException;
import com.serotonin.bacnet4j.npdu.test.TestNetwork;
import com.serotonin.bacnet4j.service.confirmed.ReinitializeDeviceRequest.ReinitializedStateOfDevice;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.primitive.CharacterString;

public class ReinitializeDeviceRequestTest {
    private LocalDevice ld1;
    private LocalDevice ld2;
    private RemoteDevice rd2;

    @Before
    public void before() throws Exception {
        ld1 = new LocalDevice(1, new DefaultTransport(new TestNetwork(1, 0))).initialize();
        ld2 = new LocalDevice(2, new DefaultTransport(new TestNetwork(2, 0))).initialize();

        rd2 = ld1.getRemoteDevice(2).get();
    }

    @After
    public void after() {
        ld1.terminate();
        ld2.terminate();
    }

    @Test
    public void noPassword() throws Exception {
        // Create the listener in device 2
        final AtomicReference<Address> receivedAddress = new AtomicReference<>(null);
        final AtomicReference<ReinitializedStateOfDevice> receivedState = new AtomicReference<>(null);
        ld2.getEventHandler().addListener(new DeviceEventAdapter() {
            @Override
            public void reinitializeDevice(final Address from,
                    final ReinitializedStateOfDevice reinitializedStateOfDevice) {
                receivedAddress.set(from);
                receivedState.set(reinitializedStateOfDevice);
            }
        });

        ld1.send(rd2, new ReinitializeDeviceRequest(ReinitializedStateOfDevice.abortRestore, null)).get();

        assertEquals(ld1.getAllLocalAddresses()[0], receivedAddress.get());
        assertEquals(ReinitializedStateOfDevice.abortRestore, receivedState.get());
    }

    @Test
    public void badPassword() throws Exception {
        ld2.setPassword("testPassword");

        try {
            ld1.send(rd2, new ReinitializeDeviceRequest(ReinitializedStateOfDevice.abortRestore,
                    new CharacterString("wrongPassword"))).get();
            fail("Should have gotten an error response");
        } catch (final ErrorAPDUException e) {
            TestUtils.assertErrorClassAndCode(e.getError(), ErrorClass.security, ErrorCode.passwordFailure);
        }
    }

    @Test
    public void password() throws Exception {
        ld2.setPassword("testPassword");

        // Create the listener in device 2
        final AtomicReference<Address> receivedAddress = new AtomicReference<>(null);
        final AtomicReference<ReinitializedStateOfDevice> receivedState = new AtomicReference<>(null);
        ld2.getEventHandler().addListener(new DeviceEventAdapter() {
            @Override
            public void reinitializeDevice(final Address from,
                    final ReinitializedStateOfDevice reinitializedStateOfDevice) {
                receivedAddress.set(from);
                receivedState.set(reinitializedStateOfDevice);
            }
        });

        ld1.send(rd2, new ReinitializeDeviceRequest(ReinitializedStateOfDevice.abortRestore,
                new CharacterString("testPassword"))).get();

        assertEquals(ld1.getAllLocalAddresses()[0], receivedAddress.get());
        assertEquals(ReinitializedStateOfDevice.abortRestore, receivedState.get());
    }
}
