package com.serotonin.bacnet4j.util;

import java.time.Duration;
import java.util.concurrent.CancellationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetTimeoutException;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

/**
 * A utility for finding a specific device by id. Generally this should not be used directly. It is better to use the
 * LocalDevice.getRemoteDevice method - which in turn uses this utility - which will also add the remote device to the
 * local cache. Use this if you specifically do *not* want the remote device to be cached.
 */
public class RemoteDeviceFinder {
    static final Logger LOG = LoggerFactory.getLogger(RemoteDeviceFinder.class);

    public static RemoteDeviceFuture findDevice(final LocalDevice localDevice, final int instanceId) {
        return new DeviceFutureImpl(localDevice, instanceId);
    }

    public interface RemoteDeviceFuture {
        static final long DEFAULT_TIMEOUT = Duration.ofSeconds(10).toMillis();

        default RemoteDevice get() throws BACnetException, CancellationException {
            return get(DEFAULT_TIMEOUT);
        }

        RemoteDevice get(long timeoutMillis) throws BACnetException, CancellationException;

        void cancel();
    }

    static class DeviceFutureImpl implements RemoteDeviceFuture {
        private final LocalDevice localDevice;
        private final int instanceId;

        private final DeviceEventAdapter listener;
        private RemoteDevice remoteDevice;
        private volatile boolean cancelled;

        public DeviceFutureImpl(final LocalDevice localDevice, final int instanceId) {
            this.localDevice = localDevice;
            this.instanceId = instanceId;

            // Register as an event listener
            listener = new DeviceEventAdapter() {
                @Override
                public void iAmReceived(final RemoteDevice remoteDevice) {
                    if (remoteDevice.getInstanceNumber() == instanceId) {
                        LOG.debug("Found device {}", instanceId);
                        setRemoteDevice(remoteDevice);
                    }
                }
            };

            localDevice.getEventHandler().addListener(listener);

            // Send a WhoIs with the device id.
            localDevice.sendGlobalBroadcast(
                    new WhoIsRequest(new UnsignedInteger(instanceId), new UnsignedInteger(instanceId)));
        }

        @Override
        public RemoteDevice get(final long timeoutMillis) throws BACnetException, CancellationException {
            synchronized (this) {
                if (cancelled)
                    throw new CancellationException();
                if (remoteDevice != null)
                    return remoteDevice;

                try {
                    wait(timeoutMillis);
                } catch (final InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (cancelled)
                    throw new CancellationException();
                if (remoteDevice != null)
                    return remoteDevice;
                throw new BACnetTimeoutException("No response from instanceId " + instanceId);
            }
        }

        @Override
        public void cancel() {
            cancelled = true;
            done();
        }

        private void setRemoteDevice(final RemoteDevice remoteDevice) {
            this.remoteDevice = remoteDevice;
            done();
        }

        private void done() {
            localDevice.getEventHandler().removeListener(listener);
            synchronized (this) {
                notifyAll();
            }
        }
    }
}
