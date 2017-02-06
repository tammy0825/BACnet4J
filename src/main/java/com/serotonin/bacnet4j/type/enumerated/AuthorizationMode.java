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

public class AuthorizationMode extends Enumerated {
    private static final long serialVersionUID = 5279178376141976116L;

    public static final AuthorizationMode authorize = new AuthorizationMode(0);
    public static final AuthorizationMode grantActive = new AuthorizationMode(1);
    public static final AuthorizationMode denyAll = new AuthorizationMode(2);
    public static final AuthorizationMode verificationRequired = new AuthorizationMode(3);
    public static final AuthorizationMode authorizationDelayed = new AuthorizationMode(4);
    public static final AuthorizationMode none = new AuthorizationMode(5);

    public static final AuthorizationMode[] ALL = { authorize, grantActive, denyAll, verificationRequired,
            authorizationDelayed, none, };

    public AuthorizationMode(final int value) {
        super(value);
    }

    public AuthorizationMode(final ByteQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        final int type = intValue();
        if (type == authorize.intValue())
            return "authorize";
        if (type == grantActive.intValue())
            return "grantActive";
        if (type == denyAll.intValue())
            return "denyAll";
        if (type == verificationRequired.intValue())
            return "verificationRequired";
        if (type == authorizationDelayed.intValue())
            return "authorizationDelayed";
        if (type == none.intValue())
            return "none";
        return "Unknown(" + type + ")";
    }
}
