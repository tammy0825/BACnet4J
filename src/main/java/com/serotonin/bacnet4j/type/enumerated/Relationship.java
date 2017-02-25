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

public class Relationship extends Enumerated {
    public static final Relationship unknown = new Relationship(0);
    public static final Relationship _default = new Relationship(1);
    public static final Relationship contains = new Relationship(2);
    public static final Relationship containedBy = new Relationship(3);
    public static final Relationship uses = new Relationship(4);
    public static final Relationship usedBy = new Relationship(5);
    public static final Relationship commands = new Relationship(6);
    public static final Relationship commandedBy = new Relationship(7);
    public static final Relationship adjusts = new Relationship(8);
    public static final Relationship adjustedBy = new Relationship(9);
    public static final Relationship ingress = new Relationship(10);
    public static final Relationship egress = new Relationship(11);
    public static final Relationship suppliesAir = new Relationship(12);
    public static final Relationship receivesAir = new Relationship(13);
    public static final Relationship suppliesHotAir = new Relationship(14);
    public static final Relationship receivesHotAir = new Relationship(15);
    public static final Relationship suppliesCoolAir = new Relationship(16);
    public static final Relationship receivesCoolAir = new Relationship(17);
    public static final Relationship suppliesPower = new Relationship(18);
    public static final Relationship receivesPower = new Relationship(19);
    public static final Relationship suppliesGas = new Relationship(20);
    public static final Relationship receivesGas = new Relationship(21);
    public static final Relationship suppliesWater = new Relationship(22);
    public static final Relationship receivesWater = new Relationship(23);
    public static final Relationship suppliesHotWater = new Relationship(24);
    public static final Relationship receivesHotWater = new Relationship(25);
    public static final Relationship suppliesCoolWater = new Relationship(26);
    public static final Relationship receivesCoolWater = new Relationship(27);
    public static final Relationship suppliesSteam = new Relationship(28);
    public static final Relationship receivesSteam = new Relationship(29);

    public static final Relationship[] ALL = { unknown, _default, contains, containedBy, uses, usedBy, commands,
            commandedBy, adjusts, adjustedBy, ingress, egress, suppliesAir, receivesAir, suppliesHotAir, receivesHotAir,
            suppliesCoolAir, receivesCoolAir, suppliesPower, receivesPower, suppliesGas, receivesGas, suppliesWater,
            receivesWater, suppliesHotWater, receivesHotWater, suppliesCoolWater, receivesCoolWater, suppliesSteam,
            receivesSteam, };

    public Relationship(final int value) {
        super(value);
    }

    public Relationship(final ByteQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        final int type = intValue();
        if (type == unknown.intValue())
            return "unknown";
        if (type == _default.intValue())
            return "_default";
        if (type == contains.intValue())
            return "contains";
        if (type == containedBy.intValue())
            return "containedBy";
        if (type == uses.intValue())
            return "uses";
        if (type == usedBy.intValue())
            return "usedBy";
        if (type == commands.intValue())
            return "commands";
        if (type == commandedBy.intValue())
            return "commandedBy";
        if (type == adjusts.intValue())
            return "adjusts";
        if (type == adjustedBy.intValue())
            return "adjustedBy";
        if (type == ingress.intValue())
            return "ingress";
        if (type == egress.intValue())
            return "egress";
        if (type == suppliesAir.intValue())
            return "suppliesAir";
        if (type == receivesAir.intValue())
            return "receivesAir";
        if (type == suppliesHotAir.intValue())
            return "suppliesHotAir";
        if (type == receivesHotAir.intValue())
            return "receivesHotAir";
        if (type == suppliesCoolAir.intValue())
            return "suppliesCoolAir";
        if (type == receivesCoolAir.intValue())
            return "receivesCoolAir";
        if (type == suppliesPower.intValue())
            return "suppliesPower";
        if (type == receivesPower.intValue())
            return "receivesPower";
        if (type == suppliesGas.intValue())
            return "suppliesGas";
        if (type == receivesGas.intValue())
            return "receivesGas";
        if (type == suppliesWater.intValue())
            return "suppliesWater";
        if (type == receivesWater.intValue())
            return "receivesWater";
        if (type == suppliesHotWater.intValue())
            return "suppliesHotWater";
        if (type == receivesHotWater.intValue())
            return "receivesHotWater";
        if (type == suppliesCoolWater.intValue())
            return "suppliesCoolWater";
        if (type == receivesCoolWater.intValue())
            return "receivesCoolWater";
        if (type == suppliesSteam.intValue())
            return "suppliesSteam";
        if (type == receivesSteam.intValue())
            return "receivesSteam";
        return "Unknown (" + type + ")";
    }
}
