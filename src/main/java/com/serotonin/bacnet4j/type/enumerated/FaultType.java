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
package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

public class FaultType extends Enumerated {
    private static final long serialVersionUID = -2447330212564862936L;
    public static final FaultType none = new FaultType(0);
    public static final FaultType faultCharacterString = new FaultType(1);
    public static final FaultType faultExtended = new FaultType(2);
    public static final FaultType faultLifeSafety = new FaultType(3);
    public static final FaultType faultState = new FaultType(4);
    public static final FaultType faultStatusFlags = new FaultType(5);

    public static final FaultType[] ALL = { none, faultCharacterString, faultExtended, faultLifeSafety, faultState,
            faultStatusFlags, };

    public FaultType(final int value) {
        super(value);
    }

    public FaultType(final ByteQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        final int type = intValue();
        if (type == none.intValue())
            return "none";
        if (type == faultCharacterString.intValue())
            return "faultCharacterString";
        if (type == faultExtended.intValue())
            return "faultExtended";
        if (type == faultLifeSafety.intValue())
            return "faultLifeSafety";
        if (type == faultState.intValue())
            return "faultState";
        if (type == faultStatusFlags.intValue())
            return "faultStatusFlags";
        return "Unknown(" + type + ")";
    }
}
