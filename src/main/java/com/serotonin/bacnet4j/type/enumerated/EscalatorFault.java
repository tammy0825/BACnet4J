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

public class EscalatorFault extends Enumerated {
    public static final EscalatorFault controllerFault = new EscalatorFault(0);
    public static final EscalatorFault driveAndMotorFault = new EscalatorFault(1);
    public static final EscalatorFault mechanicalComponentFault = new EscalatorFault(2);
    public static final EscalatorFault overspeedFault = new EscalatorFault(3);
    public static final EscalatorFault powerSupplyFault = new EscalatorFault(4);
    public static final EscalatorFault safetyDeviceFault = new EscalatorFault(5);
    public static final EscalatorFault controllerSupplyFault = new EscalatorFault(6);
    public static final EscalatorFault driveTemperatureExceeded = new EscalatorFault(7);
    public static final EscalatorFault combPlateFault = new EscalatorFault(8);

    public static final EscalatorFault[] ALL = { controllerFault, driveAndMotorFault, mechanicalComponentFault,
            overspeedFault, powerSupplyFault, safetyDeviceFault, controllerSupplyFault, driveTemperatureExceeded,
            combPlateFault, };

    public EscalatorFault(final int value) {
        super(value);
    }

    public EscalatorFault(final ByteQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        final int type = intValue();
        if (type == controllerFault.intValue())
            return "controllerFault";
        if (type == driveAndMotorFault.intValue())
            return "driveAndMotorFault";
        if (type == mechanicalComponentFault.intValue())
            return "mechanicalComponentFault";
        if (type == overspeedFault.intValue())
            return "overspeedFault";
        if (type == powerSupplyFault.intValue())
            return "powerSupplyFault";
        if (type == safetyDeviceFault.intValue())
            return "safetyDeviceFault";
        if (type == controllerSupplyFault.intValue())
            return "controllerSupplyFault";
        if (type == driveTemperatureExceeded.intValue())
            return "driveTemperatureExceeded";
        if (type == combPlateFault.intValue())
            return "combPlateFault";
        return "Unknown(" + type + ")";
    }
}
