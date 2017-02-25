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

public class TimerTransition extends Enumerated {
    public static final TimerTransition none = new TimerTransition(0);
    public static final TimerTransition idleToRunning = new TimerTransition(1);
    public static final TimerTransition runningToIdle = new TimerTransition(2);
    public static final TimerTransition runningToRunning = new TimerTransition(3);
    public static final TimerTransition runningToExpired = new TimerTransition(4);
    public static final TimerTransition forcedToExpire = new TimerTransition(5);
    public static final TimerTransition expiredToIdle = new TimerTransition(6);
    public static final TimerTransition expiredToRunning = new TimerTransition(7);

    public static final TimerTransition[] ALL = { none, idleToRunning, runningToIdle, runningToRunning,
            runningToExpired, forcedToExpire, expiredToIdle, expiredToRunning, };

    public TimerTransition(final int value) {
        super(value);
    }

    public TimerTransition(final ByteQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        final int type = intValue();
        if (type == none.intValue())
            return "none";
        if (type == idleToRunning.intValue())
            return "idleToRunning";
        if (type == runningToIdle.intValue())
            return "runningToIdle";
        if (type == runningToRunning.intValue())
            return "runningToRunning";
        if (type == runningToExpired.intValue())
            return "runningToExpired";
        if (type == forcedToExpire.intValue())
            return "forcedToExpire";
        if (type == expiredToIdle.intValue())
            return "expiredToIdle";
        if (type == expiredToRunning.intValue())
            return "expiredToRunning";
        return "Unknown(" + type + ")";
    }
}
