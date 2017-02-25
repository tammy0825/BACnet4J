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

/**
 * @author Matthew Lohbihler
 */
public class NodeType extends Enumerated {
    public static final NodeType unknown = new NodeType(0);
    public static final NodeType system = new NodeType(1);
    public static final NodeType network = new NodeType(2);
    public static final NodeType device = new NodeType(3);
    public static final NodeType organizational = new NodeType(4);
    public static final NodeType area = new NodeType(5);
    public static final NodeType equipment = new NodeType(6);
    public static final NodeType point = new NodeType(7);
    public static final NodeType collection = new NodeType(8);
    public static final NodeType property = new NodeType(9);
    public static final NodeType functional = new NodeType(10);
    public static final NodeType other = new NodeType(11);
    public static final NodeType subsystem = new NodeType(12);
    public static final NodeType building = new NodeType(13);
    public static final NodeType floor = new NodeType(14);
    public static final NodeType section = new NodeType(15);
    public static final NodeType module = new NodeType(16);
    public static final NodeType tree = new NodeType(17);
    public static final NodeType member = new NodeType(18);
    public static final NodeType protocol = new NodeType(19);
    public static final NodeType room = new NodeType(20);
    public static final NodeType zone = new NodeType(21);

    public static final NodeType[] ALL = { unknown, system, network, device, organizational, area, equipment, point,
            collection, property, functional, other, subsystem, building, floor, section, module, tree, member,
            protocol, room, zone, };

    public NodeType(final int value) {
        super(value);
    }

    public NodeType(final ByteQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        final int type = intValue();
        if (type == unknown.intValue())
            return "unknown";
        if (type == system.intValue())
            return "system";
        if (type == network.intValue())
            return "network";
        if (type == device.intValue())
            return "device";
        if (type == organizational.intValue())
            return "organizational";
        if (type == area.intValue())
            return "area";
        if (type == equipment.intValue())
            return "equipment";
        if (type == point.intValue())
            return "point";
        if (type == collection.intValue())
            return "collection";
        if (type == property.intValue())
            return "property";
        if (type == functional.intValue())
            return "functional";
        if (type == other.intValue())
            return "other";
        if (type == subsystem.intValue())
            return "subsystem";
        if (type == building.intValue())
            return "building";
        if (type == floor.intValue())
            return "floor";
        if (type == section.intValue())
            return "section";
        if (type == module.intValue())
            return "module";
        if (type == tree.intValue())
            return "tree";
        if (type == member.intValue())
            return "member";
        if (type == protocol.intValue())
            return "protocol";
        if (type == room.intValue())
            return "room";
        if (type == zone.intValue())
            return "zone";
        return "Unknown(" + type + ")";
    }
}
