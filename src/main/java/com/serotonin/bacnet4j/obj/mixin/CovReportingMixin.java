/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2015 Infinite Automation Software. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * When signing a commercial license with Infinite Automation Software,
 * the following extension to GPL is made. A special exception to the GPL is
 * included to allow you to distribute a combined work that includes BAcnet4J
 * without being obliged to provide the source code for any proprietary components.
 *
 * See www.infiniteautomation.com for commercial license options.
 *
 * @author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.obj.mixin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.serotonin.bacnet4j.exception.BACnetRuntimeException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.obj.AbstractMixin;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedCovNotificationRequest;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedCovNotificationRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

/**
 * Could add support for COV_Period...
 *
 * @author Matthew
 */
public class CovReportingMixin extends AbstractMixin {
    private final CovReportingCriteria criteria;

    public CovReportingMixin(final BACnetObject bo, final Real covIncrement) {
        super(bo);
        criteria = objectTypeCriteria.get(bo.getId().getObjectType());
        if (criteria == null)
            throw new RuntimeException(
                    "COV reporting not supported for this object type: " + bo.getId().getObjectType());
        if (covIncrement != null)
            writePropertyInternal(PropertyIdentifier.covIncrement, covIncrement);
    }

    @Override
    protected void setLocalDeviceNotify() {
        super.setLocalDeviceNotify();
        getLocalDevice().getCovContexts().put(getId(), new ArrayList<>());
    }

    @Override
    protected boolean validateProperty(final PropertyValue value) throws BACnetServiceException {
        if (PropertyIdentifier.covIncrement.equals(value.getPropertyIdentifier())) {
            final Real covIncrement = (Real) value.getValue();
            if (covIncrement.floatValue() < 0)
                throw new BACnetServiceException(ErrorClass.property, ErrorCode.writeAccessDenied);
        }

        return false;
    }

    @Override
    protected void afterWriteProperty(final PropertyIdentifier pid, final Encodable oldValue,
            final Encodable newValue) {
        final List<CovContext> ctxs = getLocalDevice().getCovContexts().get(getId());
        final long now = getLocalDevice().getClock().millis();
        synchronized (ctxs) {
            List<CovContext> expired = null;
            for (final CovContext ctx : ctxs) {
                // Check for expired contexts.
                if (ctx.hasExpired(now)) {
                    if (expired == null)
                        expired = new ArrayList<>();
                    expired.add(ctx);
                } else {
                    // At this point we know that the subscription is still valid, and applies to this object.
                    // Try to find a reason to send the notification.

                    boolean sent = false;

                    // Table 13-1
                    if (pid.isOneOf(criteria.monitoredProperties)) {
                        boolean send = true;
                        if (pid.equals(criteria.incrementProperty))
                            // Check if the increment property has changed enough for a notification to be sent.
                            send = incrementChange(ctx, newValue);
                        if (send) {
                            // We send an object notification in any case because the property that changed is one
                            // of the monitored properties in 13-1. So, send to all object subscriptions, and property
                            // subscriptions where the monitored property is one of the criteria's monitored properties.
                            if (ctx.isObjectSubscription()
                                    || ctx.getMonitoredProperty().isOneOf(criteria.monitoredProperties)) {
                                sendObjectNotification(ctx, now);
                                sent = true;
                            }
                        }
                    }

                    if (!sent) {
                        // Rows in Table 13-1a are not distinguished because currently no alternative increment
                        // value is supported.
                        if (pid.isOneOf(PropertyIdentifier.statusFlags) //
                                || ctx.getMonitoredProperty() != null && pid.equals(ctx.getMonitoredProperty())) {
                            final SequenceOf<PropertyValue> values = new SequenceOf<>();
                            addPropertyValues(ctx, values, ctx.getMonitoredProperty(), PropertyIdentifier.statusFlags);
                            sendNotification(ctx, now, values);
                            sent = true;
                        }
                    }

                    // Too difficult to figure out what Table 13-1a-2 is trying to say.
                    //                    if (!sent && pid.equals(PropertyIdentifier.valueSource)) {
                    //                        // Table 13-1a-2
                    //                        if (get(PropertyIdentifier.priorityArray) != null) {
                    //
                    //                        } else {
                    //
                    //                        }
                    //                    }
                }
            }

            if (expired != null) {
                ctxs.removeAll(expired);
            }
        }
    }

    public void addCovSubscription(final Address from, final UnsignedInteger subscriberProcessIdentifier,
            final Boolean issueConfirmedNotifications, final UnsignedInteger lifetime,
            final PropertyReference monitoredPropertyIdentifier, final Real covIncrement)
            throws BACnetServiceException {
        final List<CovContext> ctxs = getLocalDevice().getCovContexts().get(getId());
        synchronized (ctxs) {
            final PropertyIdentifier monitored = monitoredPropertyIdentifier == null ? null
                    : monitoredPropertyIdentifier.getPropertyIdentifier();

            CovContext ctx = findCovSubscription(ctxs, from, subscriberProcessIdentifier, monitored);
            if (ctx == null) {
                // Ensure that this object is valid for COV notifications.
                if (monitoredPropertyIdentifier != null) {
                    // Don't allow a subscription on a sequence index
                    if (monitoredPropertyIdentifier.getPropertyArrayIndex() != null)
                        throw new BACnetServiceException(ErrorClass.object,
                                ErrorCode.optionalFunctionalityNotSupported);

                    // Make sure that the requested property is one of the supported properties.
                    if (!monitoredPropertyIdentifier.getPropertyIdentifier().isOneOf(criteria.monitoredProperties))
                        throw new BACnetServiceException(ErrorClass.object,
                                ErrorCode.optionalFunctionalityNotSupported);
                }

                final PropertyIdentifier exposed = monitored == null ? criteria.exposedMonitoredProperty : monitored;

                ctx = new CovContext(getLocalDevice().getClock(), from, subscriberProcessIdentifier, monitored,
                        exposed);

                ctxs.add(ctx);
            }

            ctx.setIssueConfirmedNotifications(issueConfirmedNotifications.booleanValue());
            if (lifetime == null)
                ctx.setExpiryTime(0);
            else
                ctx.setExpiryTime(lifetime.intValue());
            ctx.setCovIncrement(covIncrement);

            // "Immediately" send a notification
            final CovContext subscription = ctx;
            getLocalDevice().schedule(() -> {
                final long now = getLocalDevice().getClock().millis();
                if (subscription.getMonitoredProperty() != null)
                    sendPropertyNotification(subscription, now, subscription.getMonitoredProperty());
                else
                    sendObjectNotification(subscription, now);
            }, 20, TimeUnit.MILLISECONDS);
        }
    }

    public void removeCovSubscription(final Address from, final UnsignedInteger subscriberProcessIdentifier,
            final PropertyReference monitoredPropertyIdentifier) {
        final List<CovContext> ctxs = getLocalDevice().getCovContexts().get(getId());
        if (ctxs != null) {
            synchronized (ctxs) {
                final PropertyIdentifier monitored = monitoredPropertyIdentifier == null ? null
                        : monitoredPropertyIdentifier.getPropertyIdentifier();

                final CovContext sub = findCovSubscription(ctxs, from, subscriberProcessIdentifier, monitored);
                if (sub != null)
                    ctxs.remove(sub);
            }
        }
    }

    private static CovContext findCovSubscription(final List<CovContext> ctxs, final Address from,
            final UnsignedInteger subscriberProcessIdentifier, final PropertyIdentifier pid) {
        for (final CovContext ctx : ctxs) {
            if (ctx.getAddress().equals(from)
                    && ctx.getSubscriberProcessIdentifier().equals(subscriberProcessIdentifier)
                    && Objects.equals(ctx.getMonitoredProperty(), pid))
                return ctx;
        }
        return null;
    }

    private void sendObjectNotification(final CovContext ctx, final long now) {
        final SequenceOf<PropertyValue> values = new SequenceOf<>();
        addPropertyValues(ctx, values, criteria.propertiesReported);
        sendNotification(ctx, now, values);
    }

    private void sendPropertyNotification(final CovContext ctx, final long now, final PropertyIdentifier pid) {
        final SequenceOf<PropertyValue> values = new SequenceOf<>();

        if (pid.equals(PropertyIdentifier.valueSource)) {
            // Table 13-1a-2

        } else {
            // Table 13-1a
            addPropertyValues(ctx, values, pid, PropertyIdentifier.statusFlags);
        }

        sendNotification(ctx, now, values);
    }

    private void addPropertyValues(final CovContext ctx, final SequenceOf<PropertyValue> values,
            final PropertyIdentifier... pids) {
        for (final PropertyIdentifier pid : pids) {
            final Encodable value = get(pid);
            if (value != null) {
                if (pid.equals(criteria.incrementProperty))
                    ctx.setLastCovIncrementValue(value);
                values.add(new PropertyValue(pid, value));
            }
        }
    }

    private void sendNotification(final CovContext ctx, final long now, final SequenceOf<PropertyValue> values) {
        final ObjectIdentifier deviceId = getLocalDevice().getId();
        final ObjectIdentifier id = get(PropertyIdentifier.objectIdentifier);
        final UnsignedInteger timeLeft = new UnsignedInteger(ctx.getSecondsRemaining(now));

        if (ctx.isIssueConfirmedNotifications()) {
            final ConfirmedCovNotificationRequest req = new ConfirmedCovNotificationRequest( //
                    ctx.getSubscriberProcessIdentifier(), deviceId, id, timeLeft, values);
            getLocalDevice().send(ctx.getAddress(), req, null);
        } else {
            final UnconfirmedCovNotificationRequest req = new UnconfirmedCovNotificationRequest(
                    ctx.getSubscriberProcessIdentifier(), deviceId, id, timeLeft, values);
            getLocalDevice().send(ctx.getAddress(), req);
        }
    }

    private boolean incrementChange(final CovContext subscription, final Encodable value) {
        final Encodable lastValue = subscription.getLastCovIncrementValue();
        if (lastValue == null)
            return true;

        Encodable covIncrement = subscription.getCovIncrement();
        if (covIncrement == null && subscription.isObjectSubscription())
            covIncrement = get(PropertyIdentifier.covIncrement);
        if (covIncrement == null)
            covIncrement = new Real(0);

        double increment, last, newValue;
        if (value instanceof Real) {
            increment = ((Real) covIncrement).floatValue();
            last = ((Real) lastValue).floatValue();
            newValue = ((Real) value).floatValue();
        } else
            throw new BACnetRuntimeException("Unhandled type: " + value.getClass());

        double diff = newValue - last;
        if (diff < 0)
            diff = -diff;
        if (diff >= increment)
            return true;

        return false;
    }

    //
    //
    // COV reporting criteria. These are defined in Table 13-1 in the spec.
    //
    public static class CovReportingCriteria {
        final PropertyIdentifier[] monitoredProperties;
        final PropertyIdentifier[] propertiesReported;
        final PropertyIdentifier incrementProperty;
        final PropertyIdentifier exposedMonitoredProperty;

        public CovReportingCriteria(final PropertyIdentifier[] monitoredProperties,
                final PropertyIdentifier[] propertiesReported, final PropertyIdentifier incrementProperty,
                final PropertyIdentifier exposedMonitoredProperty) {
            this.monitoredProperties = monitoredProperties;
            this.propertiesReported = propertiesReported;
            this.incrementProperty = incrementProperty;
            this.exposedMonitoredProperty = exposedMonitoredProperty;
        }
    }

    public static final CovReportingCriteria criteria13_1_1 = new CovReportingCriteria( //
            new PropertyIdentifier[] { PropertyIdentifier.presentValue, PropertyIdentifier.statusFlags,
                    PropertyIdentifier.doorAlarmState }, //
            new PropertyIdentifier[] { PropertyIdentifier.presentValue, PropertyIdentifier.statusFlags,
                    PropertyIdentifier.doorAlarmState }, //
            null, PropertyIdentifier.presentValue);

    public static final CovReportingCriteria criteria13_1_2 = new CovReportingCriteria( //
            new PropertyIdentifier[] { PropertyIdentifier.accessEventTime, PropertyIdentifier.statusFlags }, //
            new PropertyIdentifier[] { PropertyIdentifier.accessEvent, PropertyIdentifier.statusFlags,
                    PropertyIdentifier.accessEventTag, PropertyIdentifier.accessEventTime,
                    PropertyIdentifier.accessEventCredential, PropertyIdentifier.accessEventAuthenticationFactor }, //
            null, PropertyIdentifier.accessEvent);

    public static final CovReportingCriteria criteria13_1_3 = new CovReportingCriteria( //
            new PropertyIdentifier[] { PropertyIdentifier.presentValue, PropertyIdentifier.statusFlags }, //
            new PropertyIdentifier[] { PropertyIdentifier.presentValue, PropertyIdentifier.statusFlags }, //
            PropertyIdentifier.presentValue, PropertyIdentifier.presentValue);

    public static final CovReportingCriteria criteria13_1_4 = new CovReportingCriteria( //
            new PropertyIdentifier[] { PropertyIdentifier.presentValue, PropertyIdentifier.statusFlags }, //
            new PropertyIdentifier[] { PropertyIdentifier.presentValue, PropertyIdentifier.statusFlags }, //
            null, PropertyIdentifier.presentValue);

    public static final CovReportingCriteria criteria13_1_5 = new CovReportingCriteria( //
            new PropertyIdentifier[] { PropertyIdentifier.updateTime, PropertyIdentifier.statusFlags }, //
            new PropertyIdentifier[] { PropertyIdentifier.presentValue, PropertyIdentifier.statusFlags,
                    PropertyIdentifier.updateTime }, //
            null, PropertyIdentifier.presentValue);

    public static final CovReportingCriteria criteria13_1_6 = new CovReportingCriteria( //
            new PropertyIdentifier[] { PropertyIdentifier.presentValue, PropertyIdentifier.statusFlags,
                    PropertyIdentifier.requestedShedLevel, PropertyIdentifier.startTime,
                    PropertyIdentifier.shedDuration, PropertyIdentifier.dutyWindow }, //
            new PropertyIdentifier[] { PropertyIdentifier.presentValue, PropertyIdentifier.statusFlags,
                    PropertyIdentifier.requestedShedLevel, PropertyIdentifier.startTime,
                    PropertyIdentifier.shedDuration, PropertyIdentifier.dutyWindow }, //
            null, PropertyIdentifier.presentValue);

    public static final CovReportingCriteria criteria13_1_7 = new CovReportingCriteria( //
            new PropertyIdentifier[] { PropertyIdentifier.presentValue, PropertyIdentifier.statusFlags }, //
            new PropertyIdentifier[] { PropertyIdentifier.presentValue, PropertyIdentifier.statusFlags,
                    PropertyIdentifier.setpoint, PropertyIdentifier.controlledVariableValue }, //
            PropertyIdentifier.presentValue, PropertyIdentifier.presentValue);

    public static final CovReportingCriteria criteria13_1_8 = new CovReportingCriteria( //
            new PropertyIdentifier[] { PropertyIdentifier.presentValue, PropertyIdentifier.statusFlags }, //
            new PropertyIdentifier[] { PropertyIdentifier.presentValue, PropertyIdentifier.statusFlags,
                    PropertyIdentifier.updateTime }, //
            PropertyIdentifier.presentValue, PropertyIdentifier.presentValue);

    private static final Map<ObjectType, CovReportingCriteria> objectTypeCriteria = new HashMap<>();
    static {
        objectTypeCriteria.put(ObjectType.accessDoor, criteria13_1_1);

        objectTypeCriteria.put(ObjectType.accessPoint, criteria13_1_2);

        objectTypeCriteria.put(ObjectType.analogInput, criteria13_1_3);
        objectTypeCriteria.put(ObjectType.analogOutput, criteria13_1_3);
        objectTypeCriteria.put(ObjectType.analogValue, criteria13_1_3);
        objectTypeCriteria.put(ObjectType.integerValue, criteria13_1_3);
        objectTypeCriteria.put(ObjectType.largeAnalogValue, criteria13_1_3);
        objectTypeCriteria.put(ObjectType.lightingOutput, criteria13_1_3);
        objectTypeCriteria.put(ObjectType.positiveIntegerValue, criteria13_1_3);

        objectTypeCriteria.put(ObjectType.binaryInput, criteria13_1_4);
        objectTypeCriteria.put(ObjectType.binaryLightingOutput, criteria13_1_4);
        objectTypeCriteria.put(ObjectType.binaryOutput, criteria13_1_4);
        objectTypeCriteria.put(ObjectType.binaryValue, criteria13_1_4);
        objectTypeCriteria.put(ObjectType.characterstringValue, criteria13_1_4);
        objectTypeCriteria.put(ObjectType.dateValue, criteria13_1_4);
        objectTypeCriteria.put(ObjectType.datePatternValue, criteria13_1_4);
        objectTypeCriteria.put(ObjectType.datetimeValue, criteria13_1_4);
        objectTypeCriteria.put(ObjectType.datetimePatternValue, criteria13_1_4);
        objectTypeCriteria.put(ObjectType.lifeSafetyPoint, criteria13_1_4);
        objectTypeCriteria.put(ObjectType.lifeSafetyZone, criteria13_1_4);
        objectTypeCriteria.put(ObjectType.multiStateInput, criteria13_1_4);
        objectTypeCriteria.put(ObjectType.multiStateOutput, criteria13_1_4);
        objectTypeCriteria.put(ObjectType.multiStateValue, criteria13_1_4);
        objectTypeCriteria.put(ObjectType.octetstringValue, criteria13_1_4);
        objectTypeCriteria.put(ObjectType.timeValue, criteria13_1_4);
        objectTypeCriteria.put(ObjectType.timePatternValue, criteria13_1_4);

        objectTypeCriteria.put(ObjectType.credentialDataInput, criteria13_1_5);

        objectTypeCriteria.put(ObjectType.loadControl, criteria13_1_6);

        objectTypeCriteria.put(ObjectType.loop, criteria13_1_7);

        objectTypeCriteria.put(ObjectType.pulseConverter, criteria13_1_8);
    }
}
