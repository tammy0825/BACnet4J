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

public class EscalatorOperationDirection extends Enumerated {
    public static final EscalatorOperationDirection unknown = new EscalatorOperationDirection(0);
    public static final EscalatorOperationDirection stopped = new EscalatorOperationDirection(1);
    public static final EscalatorOperationDirection upRatedSpeed = new EscalatorOperationDirection(2);
    public static final EscalatorOperationDirection upReducedSpeed = new EscalatorOperationDirection(3);
    public static final EscalatorOperationDirection downRatedSpeed = new EscalatorOperationDirection(4);
    public static final EscalatorOperationDirection downReducedSpeed = new EscalatorOperationDirection(5);

    public static final EscalatorOperationDirection[] ALL = { unknown, stopped, upRatedSpeed, upReducedSpeed,
            downRatedSpeed, downReducedSpeed, };

    public EscalatorOperationDirection(final int value) {
        super(value);
    }

    public EscalatorOperationDirection(final ByteQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        final int type = intValue();
        if (type == unknown.intValue())
            return "unknown";
        if (type == stopped.intValue())
            return "stopped";
        if (type == upRatedSpeed.intValue())
            return "upRatedSpeed";
        if (type == upReducedSpeed.intValue())
            return "upReducedSpeed";
        if (type == downRatedSpeed.intValue())
            return "downRatedSpeed";
        if (type == downReducedSpeed.intValue())
            return "downReducedSpeed";
        return "Unknown(" + type + ")";
    }
}
