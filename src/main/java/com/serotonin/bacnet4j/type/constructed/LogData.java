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
package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.AmbiguousValue;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.error.ErrorClassAndCode;
import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

public class LogData extends BaseType {
    private final LogStatus logStatus;
    private final SequenceOf<LogDataElement> logData;
    private final Real timeChange;

    public LogData(final LogStatus logStatus, final SequenceOf<LogDataElement> logData, final Real timeChange) {
        this.logStatus = logStatus;
        this.logData = logData;
        this.timeChange = timeChange;
    }

    @Override
    public void write(final ByteQueue queue) {
        write(queue, logStatus, 0);
        write(queue, logData, 1);
        write(queue, timeChange, 2);
    }

    public LogStatus getLogStatus() {
        return logStatus;
    }

    public SequenceOf<LogDataElement> getLogData() {
        return logData;
    }

    public Real getTimeChange() {
        return timeChange;
    }

    public LogData(final ByteQueue queue) throws BACnetException {
        logStatus = read(queue, LogStatus.class, 0);
        logData = readSequenceOf(queue, LogDataElement.class, 1);
        timeChange = read(queue, Real.class, 2);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (logData == null ? 0 : logData.hashCode());
        result = prime * result + (logStatus == null ? 0 : logStatus.hashCode());
        result = prime * result + (timeChange == null ? 0 : timeChange.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final LogData other = (LogData) obj;
        if (logData == null) {
            if (other.logData != null)
                return false;
        } else if (!logData.equals(other.logData))
            return false;
        if (logStatus == null) {
            if (other.logStatus != null)
                return false;
        } else if (!logStatus.equals(other.logStatus))
            return false;
        if (timeChange == null) {
            if (other.timeChange != null)
                return false;
        } else if (!timeChange.equals(other.timeChange))
            return false;
        return true;
    }

    public static class LogDataElement extends BaseType {
        private static ChoiceOptions choiceOptions = new ChoiceOptions();
        static {
            choiceOptions.addContextual(0, Boolean.class);
            choiceOptions.addContextual(1, Real.class);
            choiceOptions.addContextual(2, Enumerated.class);
            choiceOptions.addContextual(3, UnsignedInteger.class);
            choiceOptions.addContextual(4, SignedInteger.class);
            choiceOptions.addContextual(5, BitString.class);
            choiceOptions.addContextual(6, Null.class);
            choiceOptions.addContextual(7, ErrorClassAndCode.class);
            choiceOptions.addContextual(8, AmbiguousValue.class);
        }

        private final Choice choice;

        public LogDataElement(final Boolean datum) {
            choice = new Choice(0, datum, choiceOptions);
        }

        public LogDataElement(final Real datum) {
            choice = new Choice(1, datum, choiceOptions);
        }

        public LogDataElement(final Enumerated datum) {
            choice = new Choice(2, datum, choiceOptions);
        }

        public LogDataElement(final UnsignedInteger datum) {
            choice = new Choice(3, datum, choiceOptions);
        }

        public LogDataElement(final SignedInteger datum) {
            choice = new Choice(4, datum, choiceOptions);
        }

        public LogDataElement(final BitString datum) {
            choice = new Choice(5, datum, choiceOptions);
        }

        public LogDataElement(final Null datum) {
            choice = new Choice(6, datum, choiceOptions);
        }

        public LogDataElement(final ErrorClassAndCode datum) {
            choice = new Choice(7, datum, choiceOptions);
        }

        public LogDataElement(final Encodable datum) {
            choice = new Choice(8, datum, choiceOptions);
        }

        @Override
        public void write(final ByteQueue queue) {
            write(queue, choice);
        }

        public LogDataElement(final ByteQueue queue) throws BACnetException {
            choice = readChoice(queue, choiceOptions);
        }

        public Boolean getBoolean() {
            return choice.getDatum();
        }

        public Real getReal() {
            return choice.getDatum();
        }

        public Enumerated getEnumerated() {
            return choice.getDatum();
        }

        public UnsignedInteger getUnsignedInteger() {
            return choice.getDatum();
        }

        public SignedInteger getSignedInteger() {
            return choice.getDatum();
        }

        public BitString getBitString() {
            return choice.getDatum();
        }

        public Null getNull() {
            return choice.getDatum();
        }

        public ErrorClassAndCode getBACnetError() {
            return choice.getDatum();
        }

        public BaseType getAny() {
            return choice.getDatum();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (choice == null ? 0 : choice.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final LogDataElement other = (LogDataElement) obj;
            if (choice == null) {
                if (other.choice != null)
                    return false;
            } else if (!choice.equals(other.choice))
                return false;
            return true;
        }
    }
}
