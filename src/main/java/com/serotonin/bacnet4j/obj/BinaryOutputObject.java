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

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.obj.mixin.CommandableMixin;
import com.serotonin.bacnet4j.obj.mixin.HasStatusFlagsMixin;
import com.serotonin.bacnet4j.obj.mixin.PropertyListMixin;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.Polarity;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;

public class BinaryOutputObject extends BACnetObject {
    public BinaryOutputObject(final LocalDevice localDevice, final int instanceNumber, final String name,
            final BinaryPV presentValue, final boolean outOfService, final Polarity polarity,
            final BinaryPV relinquishDefault) throws BACnetServiceException {
        super(localDevice, ObjectType.binaryOutput, instanceNumber, name);

        writePropertyInternal(PropertyIdentifier.eventState, EventState.normal);
        writePropertyInternal(PropertyIdentifier.outOfService, new Boolean(outOfService));
        writePropertyInternal(PropertyIdentifier.statusFlags, new StatusFlags(false, false, false, outOfService));

        // Mixins
        addMixin(new HasStatusFlagsMixin(this));
        addMixin(new CommandableMixin(this, PropertyIdentifier.presentValue));
        addMixin(new PropertyListMixin(this));

        _supportCommandable(relinquishDefault);
        _supportValueSource();

        writePropertyInternal(PropertyIdentifier.presentValue, presentValue);
        writePropertyInternal(PropertyIdentifier.polarity, polarity);

        // TODO intrinsic reporting: COMMAND_FAILURE
        //        Event_State BACnetEventState R
        //        Time_Delay Unsigned O4,6
        //        Notification_Class Unsigned O4,6
        //        Feedback_Value BACnetBinaryPV O4
        //        Event_Enable BACnetEventTransitionBits O4,6
        //        Acked_Transitions BACnetEventTransitionBits O4,6
        //        Notify_Type BACnetNotifyType O4,6
        //        Event_Time_Stamps BACnetARRAY[3] of BACnetTimeStamp O4,6
        //        Event_Message_Texts BACnetARRAY[3] of CharacterString O6
        //        Event_Message_Texts_Config BACnetARRAY[3] of CharacterString O6
        //        Event_Detection_Enable BOOLEAN O4,6
        //        Event_Algorithm_Inhibit_Ref BACnetObjectPropertyReference O6
        //        Event_Algorithm_Inhibit BOOLEAN O6,7
        //        Time_Delay_Normal Unsigned O6
        //        Reliability_Evaluation_Inhibit BOOLEAN O8        

        // ?? changeOfStateTime
        // ?? changeOfStateCount
        // ?? timeOfStateCountReset
        // ?? elapsedActiveTime
        // ?? timeOfActiveTimeReset
    }

    public void addStateText(final String inactive, final String active) {
        writePropertyInternal(PropertyIdentifier.inactiveText, new CharacterString(inactive));
        writePropertyInternal(PropertyIdentifier.activeText, new CharacterString(active));
    }

    public void supportCovReporting() {
        _supportCovReporting(null);
    }
}
