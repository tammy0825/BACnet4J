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

public class NetworkPortCommand extends Enumerated {
    public static final NetworkPortCommand idle = new NetworkPortCommand(0);
    public static final NetworkPortCommand discardChanges = new NetworkPortCommand(1);
    public static final NetworkPortCommand renewFdRegistration = new NetworkPortCommand(2);
    public static final NetworkPortCommand restartSlaveDiscovery = new NetworkPortCommand(3);
    public static final NetworkPortCommand renewDhcp = new NetworkPortCommand(4);
    public static final NetworkPortCommand restartAutorenegotiation = new NetworkPortCommand(5);
    public static final NetworkPortCommand disconnect = new NetworkPortCommand(6);
    public static final NetworkPortCommand restartPort = new NetworkPortCommand(7);

    public static final NetworkPortCommand[] ALL = { idle, discardChanges, renewFdRegistration, restartSlaveDiscovery,
            renewDhcp, restartAutorenegotiation, disconnect, restartPort, };

    public NetworkPortCommand(final int value) {
        super(value);
    }

    public NetworkPortCommand(final ByteQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        final int type = intValue();
        if (type == idle.intValue())
            return "idle";
        if (type == discardChanges.intValue())
            return "discardChanges";
        if (type == renewFdRegistration.intValue())
            return "renewFdRegistration";
        if (type == restartSlaveDiscovery.intValue())
            return "restartSlaveDiscovery";
        if (type == renewDhcp.intValue())
            return "renewDhcp";
        if (type == restartAutorenegotiation.intValue())
            return "restartAutorenegotiation";
        if (type == disconnect.intValue())
            return "disconnect";
        if (type == restartPort.intValue())
            return "restartPort";
        return "Unknown(" + type + ")";
    }
}
