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

public class LiftCarDriveStatus extends Enumerated {
    public static final LiftCarDriveStatus unknown = new LiftCarDriveStatus(0);
    public static final LiftCarDriveStatus stationary = new LiftCarDriveStatus(1);
    public static final LiftCarDriveStatus braking = new LiftCarDriveStatus(2);
    public static final LiftCarDriveStatus accelerate = new LiftCarDriveStatus(3);
    public static final LiftCarDriveStatus decelerate = new LiftCarDriveStatus(4);
    public static final LiftCarDriveStatus ratedSpeed = new LiftCarDriveStatus(5);
    public static final LiftCarDriveStatus singleFloorJump = new LiftCarDriveStatus(6);
    public static final LiftCarDriveStatus twoFloorJump = new LiftCarDriveStatus(7);
    public static final LiftCarDriveStatus threeFloorJump = new LiftCarDriveStatus(8);
    public static final LiftCarDriveStatus multiFloorJump = new LiftCarDriveStatus(9);

    public static final LiftCarDriveStatus[] ALL = { unknown, stationary, braking, accelerate, decelerate, ratedSpeed,
            singleFloorJump, twoFloorJump, threeFloorJump, multiFloorJump, };

    public LiftCarDriveStatus(final int value) {
        super(value);
    }

    public LiftCarDriveStatus(final ByteQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        final int type = intValue();
        if (type == unknown.intValue())
            return "unknown";
        if (type == stationary.intValue())
            return "stationary";
        if (type == braking.intValue())
            return "braking";
        if (type == accelerate.intValue())
            return "accelerate";
        if (type == decelerate.intValue())
            return "decelerate";
        if (type == ratedSpeed.intValue())
            return "ratedSpeed";
        if (type == singleFloorJump.intValue())
            return "singleFloorJump";
        if (type == twoFloorJump.intValue())
            return "twoFloorJump";
        if (type == threeFloorJump.intValue())
            return "threeFloorJump";
        if (type == multiFloorJump.intValue())
            return "multiFloorJump";
        return "Unknown(" + type + ")";
    }
}
