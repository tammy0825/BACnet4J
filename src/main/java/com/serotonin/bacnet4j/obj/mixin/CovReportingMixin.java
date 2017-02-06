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
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.serotonin.bacnet4j.exception.BACnetRuntimeException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.obj.AbstractMixin;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedCovNotificationRequest;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedCovNotificationRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.CovSubscription;
import com.serotonin.bacnet4j.type.constructed.ObjectPropertyReference;
import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.Recipient;
import com.serotonin.bacnet4j.type.constructed.RecipientProcess;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
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
    private final List<ObjectCovSubscription> covSubscriptions = new ArrayList<>();

    public CovReportingMixin(final BACnetObject bo, final CovReportingCriteria criteria, final Real covIncrement) {
        super(bo);
        this.criteria = criteria;
        if (covIncrement != null)
            writePropertyImpl(PropertyIdentifier.covIncrement, covIncrement);
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
        if (pid.isOneOf(criteria.monitoredProperties)) {
            final long now = getLocalDevice().getClock().millis();
            synchronized (covSubscriptions) {
                List<ObjectCovSubscription> expired = null;
                for (final ObjectCovSubscription subscription : covSubscriptions) {
                    if (subscription.hasExpired(now)) {
                        if (expired == null)
                            expired = new ArrayList<>();
                        expired.add(subscription);
                    } else {
                        boolean send = true;
                        if (pid.equals(criteria.incrementProperty))
                            send = incrementChange(subscription, newValue);
                        if (send) {
                            if (pid.equals(subscription.getMonitoredProperty()))
                                sendPropertyNotification(subscription, now, pid);
                            else if (subscription.getMonitoredProperty() == null)
                                sendObjectNotification(subscription, now);
                        }
                    }
                }

                if (expired != null)
                    covSubscriptions.removeAll(expired);
            }
        }
    }

    public void addCovSubscription(final Address from, final UnsignedInteger subscriberProcessIdentifier,
            final com.serotonin.bacnet4j.type.primitive.Boolean issueConfirmedNotifications,
            final UnsignedInteger lifetime, final PropertyReference monitoredPropertyIdentifier,
            final Real covIncrement) throws BACnetServiceException {
        synchronized (covSubscriptions) {
            ObjectCovSubscription sub = findCovSubscription(from, subscriberProcessIdentifier);

            if (sub == null) {
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

                sub = new ObjectCovSubscription(getLocalDevice().getClock(), from, subscriberProcessIdentifier, //
                        monitoredPropertyIdentifier == null ? null
                                : monitoredPropertyIdentifier.getPropertyIdentifier());

                covSubscriptions.add(sub);
            }

            sub.setIssueConfirmedNotifications(issueConfirmedNotifications.booleanValue());
            sub.setExpiryTime(lifetime.intValue());
            sub.setCovIncrement(covIncrement);

            // Remove from device list.
            final RecipientProcess rp = new RecipientProcess(new Recipient(from), subscriberProcessIdentifier);
            removeFromDeviceList(rp);

            // Add to the device list.
            final ObjectPropertyReference opr = new ObjectPropertyReference(
                    (ObjectIdentifier) get(PropertyIdentifier.objectIdentifier),
                    monitoredPropertyIdentifier == null ? null : monitoredPropertyIdentifier.getPropertyIdentifier(),
                    monitoredPropertyIdentifier == null ? null : monitoredPropertyIdentifier.getPropertyArrayIndex());
            final CovSubscription cs = new CovSubscription(rp, opr, issueConfirmedNotifications, lifetime,
                    covIncrement);
            final SequenceOf<CovSubscription> deviceList = getLocalDevice().getConfiguration()
                    .get(PropertyIdentifier.activeCovSubscriptions);
            deviceList.add(cs);

            // "Immediately" send a notification
            final ObjectCovSubscription subscription = sub;
            getLocalDevice().schedule(() -> {
                final long now = getLocalDevice().getClock().millis();
                if (subscription.getMonitoredProperty() != null)
                    sendPropertyNotification(subscription, now, subscription.getMonitoredProperty());
                else
                    sendObjectNotification(subscription, now);
            }, 20, TimeUnit.MILLISECONDS);
        }
    }

    public void removeCovSubscription(final Address from, final UnsignedInteger subscriberProcessIdentifier) {
        synchronized (covSubscriptions) {
            final ObjectCovSubscription sub = findCovSubscription(from, subscriberProcessIdentifier);
            if (sub != null)
                covSubscriptions.remove(sub);

            removeFromDeviceList(new RecipientProcess(new Recipient(from), subscriberProcessIdentifier));
        }
    }

    private ObjectCovSubscription findCovSubscription(final Address from,
            final UnsignedInteger subscriberProcessIdentifier) {
        for (final ObjectCovSubscription sub : covSubscriptions) {
            if (sub.getAddress().equals(from)
                    && sub.getSubscriberProcessIdentifier().equals(subscriberProcessIdentifier))
                return sub;
        }
        return null;
    }

    private void removeFromDeviceList(final RecipientProcess rp) {
        final SequenceOf<CovSubscription> deviceList = getLocalDevice().getConfiguration()
                .get(PropertyIdentifier.activeCovSubscriptions);
        for (final CovSubscription cs : deviceList) {
            if (cs.getRecipient().equals(rp)) {
                deviceList.remove(cs);
                break;
            }
        }
    }

    void sendObjectNotification(final ObjectCovSubscription subscription, final long now) {
        final SequenceOf<PropertyValue> values = new SequenceOf<>();
        for (final PropertyIdentifier pid : criteria.propertiesReported) {
            final Encodable value = get(pid);
            if (pid.equals(criteria.incrementProperty))
                subscription.setLastCovIncrementValue(value);
            values.add(new PropertyValue(pid, value));
        }
        sendNotification(subscription, now, values);
    }

    void sendPropertyNotification(final ObjectCovSubscription subscription, final long now,
            final PropertyIdentifier pid) {
        final Encodable value = get(pid);
        if (pid.equals(criteria.incrementProperty))
            subscription.setLastCovIncrementValue(value);
        sendNotification(subscription, now, new SequenceOf<>(new PropertyValue(pid, value)));
    }

    private void sendNotification(final ObjectCovSubscription subscription, final long now,
            final SequenceOf<PropertyValue> values) {
        final ObjectIdentifier deviceId = getLocalDevice().getConfiguration().getId();
        final ObjectIdentifier id = get(PropertyIdentifier.objectIdentifier);
        final UnsignedInteger timeLeft = new UnsignedInteger(subscription.getTimeRemaining(now));

        if (subscription.isIssueConfirmedNotifications()) {
            final ConfirmedCovNotificationRequest req = new ConfirmedCovNotificationRequest( //
                    subscription.getSubscriberProcessIdentifier(), deviceId, id, timeLeft, values);
            getLocalDevice().send(subscription.getAddress(), req, null);
        } else {
            final UnconfirmedCovNotificationRequest req = new UnconfirmedCovNotificationRequest(
                    subscription.getSubscriberProcessIdentifier(), deviceId, id, timeLeft, values);
            getLocalDevice().send(subscription.getAddress(), req);
        }
    }

    //
    //
    // COV reporting criteria
    //
    public static class CovReportingCriteria {
        final PropertyIdentifier[] monitoredProperties;
        final PropertyIdentifier[] propertiesReported;
        final PropertyIdentifier incrementProperty;

        public CovReportingCriteria(final PropertyIdentifier[] monitoredProperties,
                final PropertyIdentifier[] propertiesReported, final PropertyIdentifier incrementProperty) {
            this.monitoredProperties = monitoredProperties;
            this.propertiesReported = propertiesReported;
            this.incrementProperty = incrementProperty;
        }
    }

    // For: Analog Input, Analog Output, Analog Value, Large Analog Value, Integer Value, Positive Integer Value,
    // Lighting Output
    public static final CovReportingCriteria criteria13_1_3 = new CovReportingCriteria( //
            new PropertyIdentifier[] { PropertyIdentifier.presentValue, PropertyIdentifier.statusFlags }, //
            new PropertyIdentifier[] { PropertyIdentifier.presentValue, PropertyIdentifier.statusFlags }, //
            PropertyIdentifier.presentValue);

    // For: Binary Input, Binary Output, Binary Value, Life Safety Point, Life Safety Zone, Multi-state Input,
    // Multi-state Output, Multi-state Value, OctetString Value, CharacterString Value, Time Value, DateTime Value,
    // Date Value, Time Pattern Value, Date Pattern Value, DateTime Pattern Value
    public static final CovReportingCriteria criteria13_1_4 = new CovReportingCriteria( //
            new PropertyIdentifier[] { PropertyIdentifier.presentValue, PropertyIdentifier.statusFlags }, //
            new PropertyIdentifier[] { PropertyIdentifier.presentValue, PropertyIdentifier.statusFlags }, //
            null);

    boolean incrementChange(final ObjectCovSubscription subscription, final Encodable value) {
        final Encodable lastValue = subscription.getLastCovIncrementValue();
        if (lastValue == null)
            return true;

        Encodable covIncrement = subscription.getCovIncrement();
        if (covIncrement == null)
            covIncrement = get(PropertyIdentifier.covIncrement);

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
}
