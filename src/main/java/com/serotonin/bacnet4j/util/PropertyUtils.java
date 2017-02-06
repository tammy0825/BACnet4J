package com.serotonin.bacnet4j.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetRuntimeException;
import com.serotonin.bacnet4j.exception.BACnetTimeoutException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class PropertyUtils {
    static final Logger LOG = LoggerFactory.getLogger(PropertyUtils.class);

    public static DevicesObjectPropertyValues readProperties(final LocalDevice localDevice,
            final DeviceObjectPropertyReferences refs, final RequestListener callback) {
        return readProperties(localDevice, refs, callback, 0);
    }

    /**
     * A blocking call to retrieve properties from potentially multiple devices, using the property cache where
     * possible to improve performance. Note that this call can modify the given DeviceObjectPropertyReferences
     * object.
     *
     * @param localDevice
     *            the local device
     * @param refs
     *            the references to retrieve. This object may be modified during this call.
     * @param callback
     *            the progress monitor. Optional.
     * @param deviceTimeout
     *            the timeout for the lookup of devices that are not currently known. A value <= 0 means to use
     *            the default timeout.
     * @return
     */
    public static DevicesObjectPropertyValues readProperties(final LocalDevice localDevice,
            final DeviceObjectPropertyReferences refs, final RequestListener callback, final long deviceTimeout) {
        final DevicesObjectPropertyValues result = new DevicesObjectPropertyValues();

        final Map<Integer, PropertyReferences> properties = refs.getProperties();

        final AtomicInteger completedProperties = new AtomicInteger();
        final double totalProperties = refs.size();

        // Find the properties that we already have. Use iterators so that we can remove as we go the properties that
        // are found in the cache.
        final Iterator<Map.Entry<Integer, PropertyReferences>> deviceIter = properties.entrySet().iterator();
        while (deviceIter.hasNext()) {
            final Map.Entry<Integer, PropertyReferences> device = deviceIter.next();
            final Integer did = device.getKey();

            final Iterator<Map.Entry<ObjectIdentifier, List<PropertyReference>>> objectIter = device.getValue()
                    .getProperties().entrySet().iterator();
            while (objectIter.hasNext()) {
                final Map.Entry<ObjectIdentifier, List<PropertyReference>> object = objectIter.next();
                final ObjectIdentifier oid = object.getKey();

                final Iterator<PropertyReference> propertyIter = object.getValue().iterator();
                while (propertyIter.hasNext()) {
                    final PropertyReference property = propertyIter.next();
                    final PropertyIdentifier pid = property.getPropertyIdentifier();
                    final UnsignedInteger pin = property.getPropertyArrayIndex();

                    try {
                        final Encodable value = localDevice.getCachedRemoteProperty(did, oid, pid, pin);
                        if (value != null) {
                            // Found the property. Remove it from the request list.
                            propertyIter.remove();
                            // Update the result and callback.
                            updateResultAndCallback(result, callback, did, oid, pid, pin, value, completedProperties,
                                    totalProperties);
                        }
                    } catch (@SuppressWarnings("unused") final BACnetRuntimeException e) {
                        // Ignore this. It means an array index was specified for a property that is not an
                        // array/sequence. The device itself should say as much when it responds.
                    }
                }

                if (object.getValue().isEmpty()) {
                    // All of the properties for this object were found in the cache, so remove the object from
                    // the property references list.
                    objectIter.remove();
                }
            }

            if (device.getValue().size() == 0) {
                // All of the properties for this device were found in the cache, so remove the device from
                // the property references list.
                deviceIter.remove();
            }
        }

        // Any property references that remain need to be requested from the devices.
        final List<Future<?>> futures = new ArrayList<>();

        for (final Map.Entry<Integer, PropertyReferences> dev : properties.entrySet()) {
            final Integer deviceId = dev.getKey();
            final PropertyReferences propRefs = dev.getValue();

            // Check if the remote device is already cached.
            final RemoteDevice rd = localDevice.getCachedRemoteDevice(deviceId);
            Runnable runnable;
            if (rd == null) {
                // Initiate a device lookup
                runnable = () -> {
                    requestPropertiesFromDevice(localDevice, deviceId, deviceTimeout, propRefs, callback, result,
                            completedProperties, totalProperties);
                };
            } else {
                runnable = () -> {
                    // Try to get the properties from the cached device.
                    try {
                        requestRemoteDeviceProperties(localDevice, rd, propRefs, callback, completedProperties,
                                totalProperties, result);
                    } catch (@SuppressWarnings("unused") final BACnetTimeoutException e) {
                        // The cached device appears to be offline. Remove it from the cache, and try discovering it
                        // again in case its address changed.
                        localDevice.removeCachedRemoteDevice(deviceId);
                        requestPropertiesFromDevice(localDevice, deviceId, deviceTimeout, propRefs, callback, result,
                                completedProperties, totalProperties);
                    }
                };
            }

            futures.add(localDevice.submit(runnable));
        }

        // Wait on the futures
        for (final Future<?> future : futures) {
            try {
                future.get();
            } catch (final Exception e) {
                LOG.error("Error in future", e);
            }
        }

        return result;
    }

    private static void requestPropertiesFromDevice(final LocalDevice localDevice, final int deviceId,
            final long deviceTimeout, final PropertyReferences propRefs, final RequestListener callback,
            final DevicesObjectPropertyValues result, final AtomicInteger completedProperties,
            final double totalProperties) {
        try {
            final RemoteDevice r = localDevice.getRemoteDevice(deviceId).get(deviceTimeout);
            requestRemoteDeviceProperties(localDevice, r, propRefs, callback, completedProperties, totalProperties,
                    result);
        } catch (final BACnetTimeoutException e) {
            LOG.error("Timeout while finding device {}", deviceId, e);

            // Set all of the properties for the request to an error.
            final BACnetError error = new BACnetError(ErrorClass.device, ErrorCode.timeout);
            for (final Map.Entry<ObjectIdentifier, List<PropertyReference>> obj : propRefs.getProperties().entrySet()) {
                for (final PropertyReference ref : obj.getValue()) {
                    updateResultAndCallback(result, callback, deviceId, obj.getKey(), ref.getPropertyIdentifier(),
                            ref.getPropertyArrayIndex(), error, completedProperties, totalProperties);
                }
            }
        } catch (final BACnetException e) {
            completedProperties.addAndGet(propRefs.size());
            LOG.error("Exception while finding device {}", deviceId, e);
        }
    }

    private static void updateResultAndCallback(final DevicesObjectPropertyValues result,
            final RequestListener callback, final int did, final ObjectIdentifier oid, final PropertyIdentifier pid,
            final UnsignedInteger pin, final Encodable value, final AtomicInteger completedProperties,
            final double totalProperties) {
        // Add it to the result list.
        result.add(did, oid, pid, pin, value);

        if (callback != null) {
            // Notify the callback
            final double progress = completedProperties.incrementAndGet() / totalProperties;
            callback.requestProgress(progress, did, oid, pid, pin, value);
        }
    }

    private static void requestRemoteDeviceProperties(final LocalDevice localDevice, final RemoteDevice rd,
            final PropertyReferences refs, final RequestListener callback, final AtomicInteger completedProperties,
            final double totalProperties, final DevicesObjectPropertyValues result) throws BACnetTimeoutException {
        LOG.info("Properties to read from {}: {}", rd.getInstanceNumber(), refs.size());

        final AtomicInteger remaining = new AtomicInteger(refs.size());
        try {
            RequestListener deviceCallback = null;
            deviceCallback = new RequestListener() {
                @Override
                public boolean requestProgress(final double deviceProgress, final int did, final ObjectIdentifier oid,
                        final PropertyIdentifier pid, final UnsignedInteger pin, final Encodable value) {
                    // Notify the callback
                    remaining.decrementAndGet();

                    // Add to the result list.
                    result.add(did, oid, pid, pin, value);

                    // Cache the retrieve objects and properties.
                    rd.setObjectProperty(oid, pid, pin, value);

                    final double progress = completedProperties.incrementAndGet() / totalProperties;
                    if (callback == null)
                        return false;
                    return callback.requestProgress(progress, did, oid, pid, pin, value);
                }
            };

            // Request the rest of the properties.
            RequestUtils.readProperties(localDevice, rd, refs, deviceCallback);
        } catch (final BACnetTimeoutException ex) {
            throw ex;
        } catch (final BACnetException ex) {
            completedProperties.addAndGet(remaining.get());
            LOG.error("Exception while getting properties for device {}", rd.getInstanceNumber(), ex);
        }
    }
}
