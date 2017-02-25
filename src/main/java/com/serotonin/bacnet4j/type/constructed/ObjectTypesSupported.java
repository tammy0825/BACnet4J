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

import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

public class ObjectTypesSupported extends BitString {
    public ObjectTypesSupported() {
        super(new boolean[60]);
    }

    public ObjectTypesSupported(final ByteQueue queue) {
        super(queue);
    }

    public boolean isAnalogInput() {
        return getArrayValue(0);
    }

    public void setAnalogInput(final boolean analogInput) {
        getValue()[0] = analogInput;
    }

    public boolean isAnalogOutput() {
        return getArrayValue(1);
    }

    public void setAnalogOutput(final boolean analogOutput) {
        getValue()[1] = analogOutput;
    }

    public boolean isAnalogValue() {
        return getArrayValue(2);
    }

    public void setAnalogValue(final boolean analogValue) {
        getValue()[2] = analogValue;
    }

    public boolean isBinaryInput() {
        return getArrayValue(3);
    }

    public void setBinaryInput(final boolean binaryInput) {
        getValue()[3] = binaryInput;
    }

    public boolean isBinaryOutput() {
        return getArrayValue(4);
    }

    public void setBinaryOutput(final boolean binaryOutput) {
        getValue()[4] = binaryOutput;
    }

    public boolean isBinaryValue() {
        return getArrayValue(5);
    }

    public void setBinaryValue(final boolean binaryValue) {
        getValue()[5] = binaryValue;
    }

    public boolean isCalendar() {
        return getArrayValue(6);
    }

    public void setCalendar(final boolean calendar) {
        getValue()[6] = calendar;
    }

    public boolean isCommand() {
        return getArrayValue(7);
    }

    public void setCommand(final boolean command) {
        getValue()[7] = command;
    }

    public boolean isDevice() {
        return getArrayValue(8);
    }

    public void setDevice(final boolean device) {
        getValue()[8] = device;
    }

    public boolean isEventEnrollment() {
        return getArrayValue(9);
    }

    public void setEventEnrollment(final boolean eventEnrollment) {
        getValue()[9] = eventEnrollment;
    }

    public boolean isFile() {
        return getArrayValue(10);
    }

    public void setFile(final boolean file) {
        getValue()[10] = file;
    }

    public boolean isGroup() {
        return getArrayValue(11);
    }

    public void setGroup(final boolean group) {
        getValue()[11] = group;
    }

    public boolean isLoop() {
        return getArrayValue(12);
    }

    public void setLoop(final boolean loop) {
        getValue()[12] = loop;
    }

    public boolean isMultiStateInput() {
        return getValue(13);
    }

    public void setMultiStateInput(final boolean multiStateInput) {
        getValue()[13] = multiStateInput;
    }

    public boolean isMultiStateOutput() {
        return getArrayValue(14);
    }

    public void setMultiStateOutput(final boolean multiStateOutput) {
        getValue()[14] = multiStateOutput;
    }

    public boolean isNotificationClass() {
        return getArrayValue(15);
    }

    public void setNotificationClass(final boolean notificationClass) {
        getValue()[15] = notificationClass;
    }

    public boolean isProgram() {
        return getArrayValue(16);
    }

    public void setProgram(final boolean program) {
        getValue()[16] = program;
    }

    public boolean isSchedule() {
        return getArrayValue(17);
    }

    public void setSchedule(final boolean schedule) {
        getValue()[17] = schedule;
    }

    public boolean isAveraging() {
        return getArrayValue(18);
    }

    public void setAveraging(final boolean averaging) {
        getValue()[18] = averaging;
    }

    public boolean isMultiStateValue() {
        return getArrayValue(19);
    }

    public void setMultiStateValue(final boolean multiStateValue) {
        getValue()[19] = multiStateValue;
    }

    public boolean isTrendLog() {
        return getArrayValue(20);
    }

    public void setTrendLog(final boolean trendLog) {
        getValue()[20] = trendLog;
    }

    public boolean isLifeSafetyPoint() {
        return getArrayValue(21);
    }

    public void setLifeSafetyPoint(final boolean lifeSafetyPoint) {
        getValue()[21] = lifeSafetyPoint;
    }

    public boolean isLifeSafetyZone() {
        return getArrayValue(22);
    }

    public void setLifeSafetyZone(final boolean lifeSafetyZone) {
        getValue()[22] = lifeSafetyZone;
    }

    public boolean isAccumulator() {
        return getArrayValue(23);
    }

    public void setAccumulator(final boolean accumulator) {
        getValue()[23] = accumulator;
    }

    public boolean isPulseConverter() {
        return getArrayValue(24);
    }

    public void setPulseConverter(final boolean pulseConverter) {
        getValue()[24] = pulseConverter;
    }

    public boolean isEventLog() {
        return getArrayValue(25);
    }

    public void setEventLog(final boolean eventLog) {
        getValue()[25] = eventLog;
    }

    public boolean isGlobalGroup() {
        return getArrayValue(26);
    }

    public void setGlobalGroup(final boolean globalGroup) {
        getValue()[26] = globalGroup;
    }

    public boolean isTrendLogMultiple() {
        return getArrayValue(27);
    }

    public void setTrendLogMultiple(final boolean trendLogMultiple) {
        getValue()[27] = trendLogMultiple;
    }

    public boolean isLoadControl() {
        return getArrayValue(28);
    }

    public void setLoadControl(final boolean loadControl) {
        getValue()[28] = loadControl;
    }

    public boolean isStructuredView() {
        return getArrayValue(29);
    }

    public void setStructuredView(final boolean structuredView) {
        getValue()[29] = structuredView;
    }

    public boolean isAccessDoor() {
        return getArrayValue(30);
    }

    public void setAccessDoor(final boolean accessDoor) {
        getValue()[30] = accessDoor;
    }

    public boolean isTimer() {
        return getArrayValue(31);
    }

    public void setTimer(final boolean timer) {
        getValue()[31] = timer;
    }

    public boolean isAccessCredential() {
        return getArrayValue(32);
    }

    public void setAccessCredential(final boolean accessCredential) {
        getValue()[32] = accessCredential;
    }

    public boolean isAccessPoint() {
        return getArrayValue(33);
    }

    public void setAccessPoint(final boolean accessPoint) {
        getValue()[33] = accessPoint;
    }

    public boolean isAccessRights() {
        return getArrayValue(34);
    }

    public void setAccessRights(final boolean accessRights) {
        getValue()[34] = accessRights;
    }

    public boolean isAccessUser() {
        return getArrayValue(35);
    }

    public void setAccessUser(final boolean accessUser) {
        getValue()[35] = accessUser;
    }

    public boolean isAccessZone() {
        return getArrayValue(36);
    }

    public void setAccessZone(final boolean accessZone) {
        getValue()[36] = accessZone;
    }

    public boolean isCredentialDataInput() {
        return getArrayValue(37);
    }

    public void setCredentialDataInput(final boolean credentialDataInput) {
        getValue()[37] = credentialDataInput;
    }

    public boolean isNetworkSecurity() {
        return getArrayValue(38);
    }

    public void setNetworkSecurity(final boolean networkSecurity) {
        getValue()[38] = networkSecurity;
    }

    public boolean isBitstringValue() {
        return getArrayValue(39);
    }

    public void setBitstringValue(final boolean bitstringValue) {
        getValue()[39] = bitstringValue;
    }

    public boolean isCharacterstringValue() {
        return getArrayValue(40);
    }

    public void setCharacterstringValue(final boolean characterstringValue) {
        getValue()[40] = characterstringValue;
    }

    public boolean isDatePatternValue() {
        return getArrayValue(41);
    }

    public void setDatePatternValue(final boolean datePatternValue) {
        getValue()[41] = datePatternValue;
    }

    public boolean isDateValue() {
        return getArrayValue(42);
    }

    public void setDateValue(final boolean dateValue) {
        getValue()[42] = dateValue;
    }

    public boolean isDatetimePatternValue() {
        return getArrayValue(43);
    }

    public void setDatetimePatternValue(final boolean datetimePatternValue) {
        getValue()[43] = datetimePatternValue;
    }

    public boolean isDatetimeValue() {
        return getArrayValue(44);
    }

    public void setDatetimeValue(final boolean datetimeValue) {
        getValue()[44] = datetimeValue;
    }

    public boolean isIntegerValue() {
        return getArrayValue(45);
    }

    public void setIntegerValue(final boolean integerValue) {
        getValue()[45] = integerValue;
    }

    public boolean isLargeAnalogValue() {
        return getArrayValue(46);
    }

    public void setLargeAnalogValue(final boolean largeAnalogValue) {
        getValue()[46] = largeAnalogValue;
    }

    public boolean isOctetstringValue() {
        return getArrayValue(47);
    }

    public void setOctetstringValue(final boolean octetstringValue) {
        getValue()[47] = octetstringValue;
    }

    public boolean isPositiveIntegerValue() {
        return getArrayValue(48);
    }

    public void setPositiveIntegerValue(final boolean positiveIntegerValue) {
        getValue()[48] = positiveIntegerValue;
    }

    public boolean isTimePatternValue() {
        return getArrayValue(49);
    }

    public void setTimePatternValue(final boolean timePatternValue) {
        getValue()[49] = timePatternValue;
    }

    public boolean isTimeValue() {
        return getArrayValue(50);
    }

    public void setTimeValue(final boolean timeValue) {
        getValue()[50] = timeValue;
    }

    public boolean isNotificationForwarder() {
        return getArrayValue(51);
    }

    public void setNotificationForwarder(final boolean notificationForwarder) {
        getValue()[51] = notificationForwarder;
    }

    public boolean isAlertEnrollment() {
        return getArrayValue(52);
    }

    public void setAlertEnrollment(final boolean alertEnrollment) {
        getValue()[52] = alertEnrollment;
    }

    public boolean isChannel() {
        return getArrayValue(53);
    }

    public void setChannel(final boolean channel) {
        getValue()[53] = channel;
    }

    public boolean isLightingOutput() {
        return getArrayValue(54);
    }

    public void setLightingOutput(final boolean lightingOutput) {
        getValue()[54] = lightingOutput;
    }

    public boolean isBinaryLightingOutput() {
        return getArrayValue(55);
    }

    public void setBinaryLightingOutput(final boolean binaryLightingOutput) {
        getValue()[55] = binaryLightingOutput;
    }

    public boolean isNetworkPort() {
        return getArrayValue(56);
    }

    public void setNetworkPort(final boolean networkPort) {
        getValue()[56] = networkPort;
    }

    public boolean isElevatorGroup() {
        return getArrayValue(57);
    }

    public void setElevatorGroup(final boolean elevatorGroup) {
        getValue()[57] = elevatorGroup;
    }

    public boolean isEscalator() {
        return getArrayValue(58);
    }

    public void setEscalator(final boolean escalator) {
        getValue()[58] = escalator;
    }

    public boolean isLift() {
        return getArrayValue(59);
    }

    public void setLift(final boolean lift) {
        getValue()[59] = lift;
    }
}
