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
package com.serotonin.bacnet4j.type.notificationParameters;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.BaseType;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.constructed.ChoiceOptions;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

public class NotificationParameters extends BaseType {
    private static ChoiceOptions choiceOptions = new ChoiceOptions();
    static {
        choiceOptions.addContextual(ChangeOfBitString.TYPE_ID & 0xff, ChangeOfBitString.class); // 0
        choiceOptions.addContextual(ChangeOfState.TYPE_ID & 0xff, ChangeOfState.class); // 1
        choiceOptions.addContextual(ChangeOfValue.TYPE_ID & 0xff, ChangeOfValue.class); // 2
        choiceOptions.addContextual(CommandFailure.TYPE_ID & 0xff, CommandFailure.class); // 3
        choiceOptions.addContextual(FloatingLimit.TYPE_ID & 0xff, FloatingLimit.class); // 4
        choiceOptions.addContextual(OutOfRange.TYPE_ID & 0xff, OutOfRange.class); // 5
        choiceOptions.addContextual(ComplexEventType.TYPE_ID & 0xff, ComplexEventType.class); // 6
        choiceOptions.addContextual(ChangeOfLifeSafety.TYPE_ID & 0xff, ChangeOfLifeSafety.class); // 8
        choiceOptions.addContextual(Extended.TYPE_ID & 0xff, Extended.class); // 9
        choiceOptions.addContextual(BufferReady.TYPE_ID & 0xff, BufferReady.class); // 10
        choiceOptions.addContextual(UnsignedRange.TYPE_ID & 0xff, UnsignedRange.class); // 11
        choiceOptions.addContextual(AccessEvent.TYPE_ID & 0xff, AccessEvent.class); // 13
        choiceOptions.addContextual(DoubleOutOfRange.TYPE_ID & 0xff, DoubleOutOfRange.class); // 14
        choiceOptions.addContextual(SignedOutOfRange.TYPE_ID & 0xff, SignedOutOfRange.class); // 15
        choiceOptions.addContextual(UnsignedOutOfRange.TYPE_ID & 0xff, UnsignedOutOfRange.class); // 16
        choiceOptions.addContextual(ChangeOfCharacterString.TYPE_ID & 0xff, ChangeOfCharacterString.class); // 17
        choiceOptions.addContextual(ChangeOfStatusFlags.TYPE_ID & 0xff, ChangeOfStatusFlags.class); // 18
        choiceOptions.addContextual(ChangeOfReliability.TYPE_ID & 0xff, ChangeOfReliability.class); // 19
        choiceOptions.addContextual(ChangeOfDiscreteValue.TYPE_ID & 0xff, ChangeOfDiscreteValue.class); // 21
        choiceOptions.addContextual(ChangeOfTimer.TYPE_ID & 0xff, ChangeOfTimer.class); // 22
    }

    private final Choice choice;

    public NotificationParameters(final NotificationParameter parameters) {
        choice = new Choice(choiceOptions.getContextId(parameters.getClass()), parameters, choiceOptions);
    }

    @Override
    public void write(final ByteQueue queue) {
        write(queue, choice);
    }

    public NotificationParameters(final ByteQueue queue) throws BACnetException {
        choice = readChoice(queue, choiceOptions);
    }

    public NotificationParameter getParameter() {
        return choice.getDatum();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (choice == null ? 0 : choice.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final NotificationParameters other = (NotificationParameters) obj;
        if (choice == null) {
            if (other.choice != null)
                return false;
        } else if (!choice.equals(other.choice))
            return false;
        return true;
    }
}
