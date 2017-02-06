package com.serotonin.bacnet4j.util;

import com.serotonin.bacnet4j.RemoteDevice;

@FunctionalInterface
public interface RemoteDeviceDiscovererCallback {
    void foundRemoteDevice(RemoteDevice d);
}
