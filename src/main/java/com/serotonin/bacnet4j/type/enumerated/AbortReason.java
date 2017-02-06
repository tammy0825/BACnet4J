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

public class AbortReason extends Enumerated {
    private static final long serialVersionUID = -5845112557505021907L;
    public static final AbortReason other = new AbortReason(0);
    public static final AbortReason bufferOverflow = new AbortReason(1);
    public static final AbortReason invalidApduInThisState = new AbortReason(2);
    public static final AbortReason preemptedByHigherPriorityTask = new AbortReason(3);
    public static final AbortReason segmentationNotSupported = new AbortReason(4);
    public static final AbortReason securityError = new AbortReason(5);
    public static final AbortReason insufficientSecurity = new AbortReason(6);
    public static final AbortReason windowSizeOutOfRange = new AbortReason(7);
    public static final AbortReason applicationExceededReplyTime = new AbortReason(8);
    public static final AbortReason outOfResources = new AbortReason(9);
    public static final AbortReason tsmTimeout = new AbortReason(10);
    public static final AbortReason apduTooLong = new AbortReason(11);

    public static final AbortReason[] ALL = { other, bufferOverflow, invalidApduInThisState,
            preemptedByHigherPriorityTask, segmentationNotSupported, securityError, insufficientSecurity,
            windowSizeOutOfRange, applicationExceededReplyTime, outOfResources, tsmTimeout, apduTooLong };

    public AbortReason(final int value) {
        super(value);
    }

    public AbortReason(final ByteQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        final int type = intValue();
        if (type == other.intValue())
            return "other";
        if (type == bufferOverflow.intValue())
            return "bufferOverflow";
        if (type == invalidApduInThisState.intValue())
            return "invalidApduInThisState";
        if (type == preemptedByHigherPriorityTask.intValue())
            return "preemptedByHigherPriorityTask";
        if (type == segmentationNotSupported.intValue())
            return "segmentationNotSupported";
        if (type == securityError.intValue())
            return "securityError";
        if (type == insufficientSecurity.intValue())
            return "insufficientSecurity";
        if (type == windowSizeOutOfRange.intValue())
            return "windowSizeOutOfRange";
        if (type == applicationExceededReplyTime.intValue())
            return "applicationExceededReplyTime";
        if (type == outOfResources.intValue())
            return "outOfResources";
        if (type == tsmTimeout.intValue())
            return "tsmTimeout";
        if (type == apduTooLong.intValue())
            return "apduTooLong";
        return "Unknown(" + type + ")";
    }
}
