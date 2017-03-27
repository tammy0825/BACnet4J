package com.serotonin.bacnet4j.obj;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.obj.mixin.HasStatusFlagsMixin;
import com.serotonin.bacnet4j.obj.mixin.ReadOnlyPropertyMixin;
import com.serotonin.bacnet4j.obj.mixin.event.AlgoReportingMixin;
import com.serotonin.bacnet4j.obj.mixin.event.eventAlgo.EventAlgorithm;
import com.serotonin.bacnet4j.obj.mixin.event.faultAlgo.FaultAlgorithm;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.DeviceObjectPropertyReference;
import com.serotonin.bacnet4j.type.constructed.EventTransitionBits;
import com.serotonin.bacnet4j.type.constructed.FaultParameter;
import com.serotonin.bacnet4j.type.constructed.FaultParameter.AbstractFaultParameter;
import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.FaultType;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Reliability;
import com.serotonin.bacnet4j.type.error.ErrorClassAndCode;
import com.serotonin.bacnet4j.type.eventParameter.AbstractEventParameter;
import com.serotonin.bacnet4j.type.eventParameter.EventParameter;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.PropertyReferences;
import com.serotonin.bacnet4j.util.PropertyValues;
import com.serotonin.bacnet4j.util.RequestUtils;

/**
 * TODO
 * - consider using a polling delegate
 */
public class EventEnrollmentObject extends BACnetObject {
    static final Logger LOG = LoggerFactory.getLogger(EventEnrollmentObject.class);

    private final AlgoReportingMixin algoReporting;
    private final ScheduledFuture<?> pollingFuture;
    private final PropertyIdentifier[] monitoredProperties;
    private final PropertyReferences monitoredPropertyReferences;
    private boolean configurationError;

    public EventEnrollmentObject(final LocalDevice localDevice, final int instanceNumber, final String name,
            final DeviceObjectPropertyReference objectPropertyReference, final NotifyType notifyType,
            final EventParameter eventParameter, final EventTransitionBits eventEnable, final int notificationClass,
            final int pollDelayMillis, final UnsignedInteger timeDelayNormal, final FaultParameter faultParameter)
            throws BACnetServiceException {
        super(localDevice, ObjectType.eventEnrollment, instanceNumber, name);

        // Validation
        if (objectPropertyReference.getPropertyIdentifier().isOneOf(PropertyIdentifier.all, PropertyIdentifier.required,
                PropertyIdentifier.optional)) {
            throw new IllegalArgumentException("PropertyIdentifier cannot be special identifier: "
                    + objectPropertyReference.getPropertyIdentifier());
        }

        writePropertyInternal(PropertyIdentifier.eventType, eventParameter.getEventType());
        writePropertyInternal(PropertyIdentifier.notifyType, notifyType);
        writePropertyInternal(PropertyIdentifier.eventParameters, eventParameter);
        writePropertyInternal(PropertyIdentifier.objectPropertyReference, objectPropertyReference);
        writePropertyInternal(PropertyIdentifier.eventState, EventState.normal);
        writePropertyInternal(PropertyIdentifier.statusFlags, new StatusFlags(false, false, false, false));
        writePropertyInternal(PropertyIdentifier.eventEnable, eventEnable);
        writePropertyInternal(PropertyIdentifier.notificationClass, new UnsignedInteger(notificationClass));
        writePropertyInternal(PropertyIdentifier.reliability, Reliability.noFaultDetected);
        writePropertyInternal(PropertyIdentifier.eventDetectionEnable, new Boolean(true));
        if (timeDelayNormal != null)
            writePropertyInternal(PropertyIdentifier.timeDelayNormal, timeDelayNormal);
        if (faultParameter != null) {
            writePropertyInternal(PropertyIdentifier.faultType, faultParameter.getFaultType());
            writePropertyInternal(PropertyIdentifier.faultParameters, faultParameter);
        } else {
            writePropertyInternal(PropertyIdentifier.faultType, FaultType.none);
            writePropertyInternal(PropertyIdentifier.faultParameters, new FaultParameter(Null.instance));
        }

        // Mixins
        addMixin(new HasStatusFlagsMixin(this));
        addMixin(new ReadOnlyPropertyMixin(this, PropertyIdentifier.eventType));

        // Event parameters and algo
        final AbstractEventParameter aep = (AbstractEventParameter) eventParameter.getChoice().getDatum();
        final EventAlgorithm eventAlgo = aep.createEventAlgorithm();
        Objects.requireNonNull(eventAlgo, "No algorithm defined for event parameter type " + eventParameter.getClass());

        // Fault parameters and algo
        AbstractFaultParameter afp = null;
        FaultAlgorithm faultAlgo = null;
        if (faultParameter != null) {
            afp = (AbstractFaultParameter) faultParameter.getEntry().getDatum();
            faultAlgo = afp.createFaultAlgorithm();
            Objects.requireNonNull(faultAlgo,
                    "No algorithm defined for fault parameter type " + faultParameter.getClass());
        }

        // Algo reporting mixin
        algoReporting = new AlgoReportingMixin(this, eventAlgo, aep, faultAlgo, afp, objectPropertyReference);
        addMixin(algoReporting);

        // Create the list of monitored values.
        monitoredProperties = eventAlgo.getAdditionalMonitoredProperties();
        monitoredPropertyReferences = new PropertyReferences();
        // Add the referenced value.
        monitoredPropertyReferences.addIndex(objectPropertyReference.getObjectIdentifier(),
                objectPropertyReference.getPropertyIdentifier(), objectPropertyReference.getPropertyArrayIndex());
        // Add the additional monitored properties (Table 12-15.1)
        for (final PropertyIdentifier pid : monitoredProperties)
            monitoredPropertyReferences.add(objectPropertyReference.getObjectIdentifier(), pid);

        // Start polling
        pollingFuture = localDevice.scheduleWithFixedDelay(() -> doPoll(), pollDelayMillis, pollDelayMillis,
                TimeUnit.MILLISECONDS);
    }

    @Override
    public void terminate() {
        super.terminate();
        pollingFuture.cancel(false);
    }

    private void doPoll() {
        try {
            doPollThrow();
        } catch (final PollException e) {
            if (!configurationError) {
                configurationError = true;
                writePropertyInternal(PropertyIdentifier.reliability, Reliability.configurationError);
                LOG.warn("Polling exception", e);
            }
        } catch (final Exception e) {
            LOG.warn("Exception while polling", e);
        }
    }

    private static class PollException extends Exception {
        private static final long serialVersionUID = 1L;

        public PollException(final String message, final Throwable cause) {
            super(message, cause);
        }

        public PollException(final String message) {
            super(message);
        }
    }

    private void doPollThrow() throws PollException {
        final DeviceObjectPropertyReference ref = get(PropertyIdentifier.objectPropertyReference);

        Encodable value;
        final Map<PropertyIdentifier, Encodable> additionalValues = new HashMap<>();

        if (ref.getDeviceIdentifier().equals(getLocalDevice().getId())) {
            // A local object
            final BACnetObject bo = getLocalDevice().getObject(ref.getObjectIdentifier());
            if (bo == null) {
                throw new PollException("EventEnrollment could not find local object at " + ref);
            }

            try {
                value = bo.getProperty(ref.getPropertyIdentifier(), ref.getPropertyArrayIndex());
                for (final PropertyIdentifier pid : monitoredProperties) {
                    additionalValues.put(pid, bo.getProperty(pid));
                }
            } catch (final BACnetServiceException e) {
                throw new PollException("Error getting property from local object at " + ref, e);
            }
        } else {
            // A remote object
            final RemoteDevice rd;
            try {
                rd = getLocalDevice().getRemoteDeviceBlocking(ref.getDeviceIdentifier().getInstanceNumber(), 30000);
            } catch (final BACnetException e) {
                throw new PollException("Error finding remote device at " + ref, e);
            }

            try {
                final PropertyValues results = RequestUtils.readProperties(getLocalDevice(), rd,
                        monitoredPropertyReferences, null);

                value = results.getNoErrorCheck(ref.getObjectIdentifier(),
                        new PropertyReference(ref.getPropertyIdentifier(), ref.getPropertyArrayIndex()));

                // Gather the additional properties
                for (final PropertyIdentifier pid : monitoredProperties) {
                    final Encodable e = results.getNoErrorCheck(ref.getObjectIdentifier(), pid);
                    if (e instanceof ErrorClassAndCode) {
                        throw new PollException("Error returned from reading " + pid + " for " + ref + ": " + e);
                    }
                    additionalValues.put(pid, e);
                }
            } catch (final BACnetException e) {
                throw new PollException("Error getting property from remote device at " + ref, e);
            }
        }

        if (value == null) {
            throw new PollException("Null property found at " + ref);
        }

        if (value instanceof ErrorClassAndCode) {
            throw new PollException("Error returned from reading property at " + ref + ": " + value);
        }

        // Check if the reliability value needs to be reset.
        if (configurationError) {
            configurationError = false;
            writePropertyInternal(PropertyIdentifier.reliability, Reliability.noFaultDetected);
        }

        algoReporting.updateValue(value, additionalValues);
    }
}
