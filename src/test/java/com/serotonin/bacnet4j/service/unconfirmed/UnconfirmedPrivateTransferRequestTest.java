package com.serotonin.bacnet4j.service.unconfirmed;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.PrivateTransferHandler;
import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.npdu.test.TestNetwork;
import com.serotonin.bacnet4j.npdu.test.TestNetworkMap;
import com.serotonin.bacnet4j.npdu.test.TestNetworkUtils;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.EncodedValue;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class UnconfirmedPrivateTransferRequestTest {
    private final TestNetworkMap map = new TestNetworkMap();
    private final LocalDevice d1 = new LocalDevice(1, new DefaultTransport(new TestNetwork(map, 1, 0)));
    private final LocalDevice d2 = new LocalDevice(2, new DefaultTransport(new TestNetwork(map, 2, 0)));
    private RemoteDevice rd1;

    @Before
    public void before() throws Exception {
        d1.initialize();
        d2.initialize();
        rd1 = d2.getRemoteDeviceBlocking(1);
    }

    @After
    public void after() {
        d1.terminate();
        d2.terminate();
    }

    @Test
    public void noHandler() {
        new UnconfirmedPrivateTransferRequest(236, 11, new Real(12.3F)).handle(d1, null);
    }

    @Test
    public void noParameters() throws Exception {
        final AtomicBoolean handled = new AtomicBoolean();
        d1.addPrivateTransferHandler(236, 12, new PrivateTransferHandler() {
            @Override
            public Encodable handle(final LocalDevice localDevice, final Address from, final UnsignedInteger vendorId,
                    final UnsignedInteger serviceNumber, final EncodedValue serviceParameters, final boolean confirmed)
                    throws BACnetErrorException {
                assertEquals(d1, localDevice);
                assertEquals(TestNetworkUtils.toAddress(2), from);
                assertEquals(new UnsignedInteger(236), vendorId);
                assertEquals(new UnsignedInteger(12), serviceNumber);
                assertEquals(null, serviceParameters);
                assertEquals(false, confirmed);
                synchronized (handled) {
                    handled.set(true);
                    handled.notify();
                }
                return null;
            }
        });

        synchronized (handled) {
            d2.send(rd1, new UnconfirmedPrivateTransferRequest(236, 12, null));
            handled.wait();
        }
        assertEquals(true, handled.get());
    }

    @Test
    public void withParameters() throws Exception {
        final EncodedValue parameters = new EncodedValue(new Boolean(true), new Boolean(false),
                new CharacterString("xyz"));

        final AtomicBoolean handled = new AtomicBoolean();
        d1.addPrivateTransferHandler(236, 13, new PrivateTransferHandler() {
            @Override
            public Encodable handle(final LocalDevice localDevice, final Address from, final UnsignedInteger vendorId,
                    final UnsignedInteger serviceNumber, final EncodedValue serviceParameters, final boolean confirmed)
                    throws BACnetErrorException {
                assertEquals(d1, localDevice);
                assertEquals(TestNetworkUtils.toAddress(2), from);
                assertEquals(new UnsignedInteger(236), vendorId);
                assertEquals(new UnsignedInteger(13), serviceNumber);
                assertEquals(parameters, serviceParameters);
                assertEquals(false, confirmed);
                synchronized (handled) {
                    handled.set(true);
                    handled.notify();
                }
                return null;
            }
        });

        synchronized (handled) {
            d2.send(rd1, new UnconfirmedPrivateTransferRequest(236, 13, parameters));
            handled.wait();
        }
        assertEquals(true, handled.get());
    }
}
