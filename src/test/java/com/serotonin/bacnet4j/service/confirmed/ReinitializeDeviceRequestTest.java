package com.serotonin.bacnet4j.service.confirmed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.serotonin.bacnet4j.AbstractTest;
import com.serotonin.bacnet4j.TestUtils;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.ErrorAPDUException;
import com.serotonin.bacnet4j.service.confirmed.ReinitializeDeviceRequest.ReinitializedStateOfDevice;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.primitive.CharacterString;

public class ReinitializeDeviceRequestTest extends AbstractTest {
    @Test
    public void noPassword() throws Exception {
        // Create the listener in device 2
        final AtomicReference<Address> receivedAddress = new AtomicReference<>(null);
        final AtomicReference<ReinitializedStateOfDevice> receivedState = new AtomicReference<>(null);
        d2.getEventHandler().addListener(new DeviceEventAdapter() {
            @Override
            public void reinitializeDevice(final Address from,
                    final ReinitializedStateOfDevice reinitializedStateOfDevice) {
                receivedAddress.set(from);
                receivedState.set(reinitializedStateOfDevice);
            }
        });

        d1.send(rd2, new ReinitializeDeviceRequest(ReinitializedStateOfDevice.abortRestore, null)).get();

        assertEquals(d1.getAllLocalAddresses()[0], receivedAddress.get());
        assertEquals(ReinitializedStateOfDevice.abortRestore, receivedState.get());
    }

    @Test
    public void badPassword() throws Exception {
        d2.withPassword("testPassword");

        try {
            d1.send(rd2, new ReinitializeDeviceRequest(ReinitializedStateOfDevice.abortRestore,
                    new CharacterString("wrongPassword"))).get();
            fail("Should have gotten an error response");
        } catch (final ErrorAPDUException e) {
            TestUtils.assertErrorClassAndCode(e.getError(), ErrorClass.security, ErrorCode.passwordFailure);
        }
    }

    @Test
    public void password() throws Exception {
        d2.withPassword("testPassword");

        // Create the listener in device 2
        final AtomicReference<Address> receivedAddress = new AtomicReference<>(null);
        final AtomicReference<ReinitializedStateOfDevice> receivedState = new AtomicReference<>(null);
        d2.getEventHandler().addListener(new DeviceEventAdapter() {
            @Override
            public void reinitializeDevice(final Address from,
                    final ReinitializedStateOfDevice reinitializedStateOfDevice) {
                receivedAddress.set(from);
                receivedState.set(reinitializedStateOfDevice);
            }
        });

        d1.send(rd2, new ReinitializeDeviceRequest(ReinitializedStateOfDevice.abortRestore,
                new CharacterString("testPassword"))).get();

        assertEquals(d1.getAllLocalAddresses()[0], receivedAddress.get());
        assertEquals(ReinitializedStateOfDevice.abortRestore, receivedState.get());
    }
}
