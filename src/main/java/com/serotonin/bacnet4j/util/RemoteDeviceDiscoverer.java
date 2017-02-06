package com.serotonin.bacnet4j.util;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;

public class RemoteDeviceDiscoverer {
    private final LocalDevice localDevice;
    private final RemoteDeviceDiscovererCallback callback;

    private DeviceEventAdapter adapter;
    private final List<RemoteDevice> allDevices = new ArrayList<>();
    private final List<RemoteDevice> latestDevices = new ArrayList<>();

    public RemoteDeviceDiscoverer(final LocalDevice localDevice) {
        this(localDevice, null);
    }

    public RemoteDeviceDiscoverer(final LocalDevice localDevice, final RemoteDeviceDiscovererCallback callback) {
        this.localDevice = localDevice;
        this.callback = callback;
    }

    public void start() {
        adapter = new DeviceEventAdapter() {
            @Override
            public void iAmReceived(final RemoteDevice d) {
                synchronized (allDevices) {
                    // Check if we already know about this device.
                    boolean found = false;
                    for (final RemoteDevice known : allDevices) {
                        if (d.getInstanceNumber() == known.getInstanceNumber()) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        // Add to all devices
                        allDevices.add(d);

                        // Add to latest devices
                        latestDevices.add(d);

                        // Notify the callback
                        if (callback != null) {
                            callback.foundRemoteDevice(d);
                        }
                    }
                }
            }
        };

        // Register self as an event listener
        localDevice.getEventHandler().addListener(adapter);

        // Send a WhoIs
        localDevice.sendGlobalBroadcast(new WhoIsRequest());
    }

    public void stop() {
        // Unregister as a listener
        localDevice.getEventHandler().removeListener(adapter);
    }

    /**
     * Returns all devices discovered by this discoverer so far.
     */
    public List<RemoteDevice> getRemoteDevices() {
        synchronized (allDevices) {
            return new ArrayList<>(allDevices);
        }
    }

    /**
     * Returns all devices discovered by this discoverer since the last time this method was called.
     */
    public List<RemoteDevice> getLatestRemoteDevices() {
        synchronized (allDevices) {
            final List<RemoteDevice> result = new ArrayList<>(latestDevices);
            latestDevices.clear();
            return result;
        }
    }
}
