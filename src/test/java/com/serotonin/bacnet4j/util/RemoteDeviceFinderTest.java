package com.serotonin.bacnet4j.util;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetTimeoutException;
import com.serotonin.bacnet4j.npdu.test.TestNetwork;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.util.RemoteDeviceFinder.RemoteDeviceFuture;

public class RemoteDeviceFinderTest {
    LocalDevice d1;
    LocalDevice d2;
    LocalDevice d3;
    RemoteDevice rd1;
    RemoteDevice rd2;
    RemoteDevice rd3;

    @Before
    public void before() throws Exception {
        d1 = new LocalDevice(1, new DefaultTransport(new TestNetwork(1, 20))).initialize();
        d2 = new LocalDevice(2, new DefaultTransport(new TestNetwork(2, 20))).initialize();
        d3 = new LocalDevice(3, new DefaultTransport(new TestNetwork(3, 20))).initialize();
    }

    @After
    public void after() {
        // Shut down
        d1.terminate();
        d2.terminate();
        d3.terminate();
    }

    @Test
    public void success() throws CancellationException, BACnetException {
        final RemoteDeviceFuture future1 = RemoteDeviceFinder.findDevice(d1, 2);
        final RemoteDeviceFuture future2 = RemoteDeviceFinder.findDevice(d1, 3);
        final RemoteDeviceFuture future3 = RemoteDeviceFinder.findDevice(d2, 1);
        final RemoteDeviceFuture future4 = RemoteDeviceFinder.findDevice(d2, 3);
        final RemoteDeviceFuture future5 = RemoteDeviceFinder.findDevice(d3, 1);
        final RemoteDeviceFuture future6 = RemoteDeviceFinder.findDevice(d3, 2);

        final RemoteDevice discovered1 = future1.get();
        final RemoteDevice discovered2 = future2.get();
        final RemoteDevice discovered3 = future3.get();
        final RemoteDevice discovered4 = future4.get();
        final RemoteDevice discovered5 = future5.get();
        final RemoteDevice discovered6 = future6.get();
        final RemoteDevice discovered61 = future6.get();

        assertEquals(2, discovered1.getInstanceNumber());
        assertEquals(3, discovered2.getInstanceNumber());
        assertEquals(1, discovered3.getInstanceNumber());
        assertEquals(3, discovered4.getInstanceNumber());
        assertEquals(1, discovered5.getInstanceNumber());
        assertEquals(2, discovered6.getInstanceNumber());
        assertEquals(2, discovered61.getInstanceNumber());

        // Ensure extended information
        assertEquals(236, discovered1.getVendorIdentifier());
    }

    @Test(expected = CancellationException.class)
    public void cancel() throws CancellationException, BACnetException {
        final RemoteDeviceFuture future = RemoteDeviceFinder.findDevice(d1, 2);
        future.cancel();
        future.get();
    }

    @Test(expected = CancellationException.class)
    public void cancelAfterWait() throws CancellationException, BACnetException {
        final RemoteDeviceFuture future = RemoteDeviceFinder.findDevice(d2, 3);
        d2.schedule(() -> future.cancel(), 1, TimeUnit.MILLISECONDS);
        future.get();
    }

    @Test(expected = BACnetTimeoutException.class)
    public void timeout() throws CancellationException, BACnetException {
        final RemoteDeviceFuture future = RemoteDeviceFinder.findDevice(d3, 4);
        future.get(100);
    }
}
