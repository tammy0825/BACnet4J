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
package com.serotonin.bacnet4j.obj;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.serotonin.bacnet4j.NotificationClassListener;
import com.serotonin.bacnet4j.type.constructed.BACnetArray;
import com.serotonin.bacnet4j.type.constructed.Destination;
import com.serotonin.bacnet4j.type.constructed.EventTransitionBits;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.EventType;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.notificationParameters.NotificationParameters;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class NotificationClassObject extends BACnetObject {
    private static final long serialVersionUID = -3881883402727466774L;

    private final List<NotificationClassListener> eventListeners = new CopyOnWriteArrayList<>();

    public NotificationClassObject(final int instanceNumber, final String name, final int toOffnormalPriority,
            final int toFaultPriority, final int toNormalPriority, final EventTransitionBits ackRequired) {
        this(instanceNumber, name, new BACnetArray<>(new UnsignedInteger(toOffnormalPriority),
                new UnsignedInteger(toFaultPriority), new UnsignedInteger(toNormalPriority)), ackRequired);

        writePropertyInternal(PropertyIdentifier.recipientList, new SequenceOf<Destination>());
    }

    public NotificationClassObject(final int instanceNumber, final String name,
            final BACnetArray<UnsignedInteger> priority, final EventTransitionBits ackRequired) {
        super(ObjectType.notificationClass, instanceNumber, name);

        writePropertyInternal(PropertyIdentifier.notificationClass, new UnsignedInteger(instanceNumber));
        writePropertyInternal(PropertyIdentifier.priority, priority);
        writePropertyInternal(PropertyIdentifier.ackRequired, ackRequired);
    }

    public void addEventListener(final NotificationClassListener l) {
        eventListeners.add(l);
    }

    public void removeEventListener(final NotificationClassListener l) {
        eventListeners.remove(l);
    }

    public void fireEventNotification(final ObjectIdentifier eventObjectIdentifier, final TimeStamp timeStamp,
            final UnsignedInteger notificationClass, final UnsignedInteger priority, final EventType eventType,
            final CharacterString messageText, final NotifyType notifyType, final Boolean ackRequired,
            final EventState fromState, final EventState toState, final NotificationParameters eventValues) {
        for (final NotificationClassListener l : eventListeners)
            l.event(eventObjectIdentifier, timeStamp, notificationClass, priority, eventType, messageText, notifyType,
                    ackRequired, fromState, toState, eventValues);
    }
}
