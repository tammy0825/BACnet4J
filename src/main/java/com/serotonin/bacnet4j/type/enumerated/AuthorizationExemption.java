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

public class AuthorizationExemption extends Enumerated {
    public static final AuthorizationExemption passback = new AuthorizationExemption(0);
    public static final AuthorizationExemption occupancyCheck = new AuthorizationExemption(1);
    public static final AuthorizationExemption accessRights = new AuthorizationExemption(2);
    public static final AuthorizationExemption lockout = new AuthorizationExemption(3);
    public static final AuthorizationExemption deny = new AuthorizationExemption(4);
    public static final AuthorizationExemption verification = new AuthorizationExemption(5);
    public static final AuthorizationExemption authorizationDelay = new AuthorizationExemption(6);

    public static final AuthorizationExemption[] ALL = { passback, occupancyCheck, accessRights, lockout, deny,
            verification, authorizationDelay, };

    public AuthorizationExemption(final int value) {
        super(value);
    }

    public AuthorizationExemption(final ByteQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        final int type = intValue();
        if (type == passback.intValue())
            return "passback";
        if (type == occupancyCheck.intValue())
            return "occupancyCheck";
        if (type == accessRights.intValue())
            return "accessRights";
        if (type == lockout.intValue())
            return "lockout";
        if (type == deny.intValue())
            return "deny";
        if (type == verification.intValue())
            return "verification";
        if (type == authorizationDelay.intValue())
            return "authorizationDelay";
        return "Unknown(" + type + ")";
    }
}
