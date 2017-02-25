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

public class LiftCarMode extends Enumerated {
    public static final LiftCarMode unknown = new LiftCarMode(0);
    public static final LiftCarMode normal = new LiftCarMode(1);
    public static final LiftCarMode vip = new LiftCarMode(2);
    public static final LiftCarMode homing = new LiftCarMode(3);
    public static final LiftCarMode parking = new LiftCarMode(4);
    public static final LiftCarMode attendantControl = new LiftCarMode(5);
    public static final LiftCarMode firefighterControl = new LiftCarMode(6);
    public static final LiftCarMode emergencyPower = new LiftCarMode(7);
    public static final LiftCarMode inspection = new LiftCarMode(8);
    public static final LiftCarMode cabinetRecall = new LiftCarMode(9);
    public static final LiftCarMode earthquakeOperation = new LiftCarMode(10);
    public static final LiftCarMode fireOperation = new LiftCarMode(11);
    public static final LiftCarMode outOfService = new LiftCarMode(12);
    public static final LiftCarMode occupantEvacuation = new LiftCarMode(13);

    public static final LiftCarMode[] ALL = { unknown, normal, vip, homing, parking, attendantControl,
            firefighterControl, emergencyPower, inspection, cabinetRecall, earthquakeOperation, fireOperation,
            outOfService, occupantEvacuation };

    public LiftCarMode(final int value) {
        super(value);
    }

    public LiftCarMode(final ByteQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        final int type = intValue();
        if (type == unknown.intValue())
            return "unknown";
        if (type == normal.intValue())
            return "normal";
        if (type == vip.intValue())
            return "vip";
        if (type == homing.intValue())
            return "homing";
        if (type == parking.intValue())
            return "parking";
        if (type == attendantControl.intValue())
            return "attendantControl";
        if (type == firefighterControl.intValue())
            return "firefighterControl";
        if (type == emergencyPower.intValue())
            return "emergencyPower";
        if (type == inspection.intValue())
            return "inspection";
        if (type == cabinetRecall.intValue())
            return "cabinetRecall";
        if (type == earthquakeOperation.intValue())
            return "earthquakeOperation";
        if (type == fireOperation.intValue())
            return "fireOperation";
        if (type == outOfService.intValue())
            return "outOfService";
        if (type == occupantEvacuation.intValue())
            return "occupantEvacuation ";
        return "Unknown(" + type + ")";
    }
}
