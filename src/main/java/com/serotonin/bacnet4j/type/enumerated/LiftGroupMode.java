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

public class LiftGroupMode extends Enumerated {
    public static final LiftGroupMode unknown = new LiftGroupMode(0);
    public static final LiftGroupMode normal = new LiftGroupMode(1);
    public static final LiftGroupMode downPeak = new LiftGroupMode(2);
    public static final LiftGroupMode twoWay = new LiftGroupMode(3);
    public static final LiftGroupMode fourWay = new LiftGroupMode(4);
    public static final LiftGroupMode emergencyPower = new LiftGroupMode(5);
    public static final LiftGroupMode upPeak = new LiftGroupMode(6);

    public static final LiftGroupMode[] ALL = { unknown, normal, downPeak, twoWay, fourWay, emergencyPower, upPeak, };

    public LiftGroupMode(final int value) {
        super(value);
    }

    public LiftGroupMode(final ByteQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        final int type = intValue();
        if (type == unknown.intValue())
            return "unknown";
        if (type == normal.intValue())
            return "normal";
        if (type == downPeak.intValue())
            return "downPeak";
        if (type == twoWay.intValue())
            return "twoWay";
        if (type == fourWay.intValue())
            return "fourWay";
        if (type == emergencyPower.intValue())
            return "emergencyPower";
        if (type == upPeak.intValue())
            return "upPeak";
        return "Unknown(" + type + ")";
    }
}
