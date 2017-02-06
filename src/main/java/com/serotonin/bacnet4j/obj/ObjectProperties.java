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
package com.serotonin.bacnet4j.obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.AccessRule;
import com.serotonin.bacnet4j.type.constructed.AccessThreatLevel;
import com.serotonin.bacnet4j.type.constructed.AccumulatorRecord;
import com.serotonin.bacnet4j.type.constructed.ActionList;
import com.serotonin.bacnet4j.type.constructed.AddressBinding;
import com.serotonin.bacnet4j.type.constructed.AssignedAccessRights;
import com.serotonin.bacnet4j.type.constructed.AuthenticationFactor;
import com.serotonin.bacnet4j.type.constructed.AuthenticationFactorFormat;
import com.serotonin.bacnet4j.type.constructed.AuthenticationPolicy;
import com.serotonin.bacnet4j.type.constructed.CalendarEntry;
import com.serotonin.bacnet4j.type.constructed.ChannelValue;
import com.serotonin.bacnet4j.type.constructed.ClientCov;
import com.serotonin.bacnet4j.type.constructed.CovSubscription;
import com.serotonin.bacnet4j.type.constructed.CredentialAuthenticationFactor;
import com.serotonin.bacnet4j.type.constructed.DailySchedule;
import com.serotonin.bacnet4j.type.constructed.DateRange;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.constructed.Destination;
import com.serotonin.bacnet4j.type.constructed.DeviceObjectPropertyReference;
import com.serotonin.bacnet4j.type.constructed.DeviceObjectReference;
import com.serotonin.bacnet4j.type.constructed.EventLogRecord;
import com.serotonin.bacnet4j.type.constructed.EventNotificationSubscription;
import com.serotonin.bacnet4j.type.constructed.EventTransitionBits;
import com.serotonin.bacnet4j.type.constructed.FaultParameter;
import com.serotonin.bacnet4j.type.constructed.LightingCommand;
import com.serotonin.bacnet4j.type.constructed.LimitEnable;
import com.serotonin.bacnet4j.type.constructed.LogMultipleRecord;
import com.serotonin.bacnet4j.type.constructed.LogRecord;
import com.serotonin.bacnet4j.type.constructed.NetworkSecurityPolicy;
import com.serotonin.bacnet4j.type.constructed.ObjectPropertyReference;
import com.serotonin.bacnet4j.type.constructed.ObjectTypesSupported;
import com.serotonin.bacnet4j.type.constructed.OptionalCharacterString;
import com.serotonin.bacnet4j.type.constructed.PortPermission;
import com.serotonin.bacnet4j.type.constructed.Prescale;
import com.serotonin.bacnet4j.type.constructed.PriorityArray;
import com.serotonin.bacnet4j.type.constructed.ProcessIdSelection;
import com.serotonin.bacnet4j.type.constructed.PropertyAccessResult;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult;
import com.serotonin.bacnet4j.type.constructed.ReadAccessSpecification;
import com.serotonin.bacnet4j.type.constructed.Recipient;
import com.serotonin.bacnet4j.type.constructed.Scale;
import com.serotonin.bacnet4j.type.constructed.SecurityKeySet;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import com.serotonin.bacnet4j.type.constructed.SetpointReference;
import com.serotonin.bacnet4j.type.constructed.ShedLevel;
import com.serotonin.bacnet4j.type.constructed.SpecialEvent;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.constructed.VtSession;
import com.serotonin.bacnet4j.type.enumerated.AccessCredentialDisable;
import com.serotonin.bacnet4j.type.enumerated.AccessCredentialDisableReason;
import com.serotonin.bacnet4j.type.enumerated.AccessEvent;
import com.serotonin.bacnet4j.type.enumerated.AccessPassbackMode;
import com.serotonin.bacnet4j.type.enumerated.AccessUserType;
import com.serotonin.bacnet4j.type.enumerated.AccessZoneOccupancyState;
import com.serotonin.bacnet4j.type.enumerated.Action;
import com.serotonin.bacnet4j.type.enumerated.AuthenticationStatus;
import com.serotonin.bacnet4j.type.enumerated.AuthorizationExemption;
import com.serotonin.bacnet4j.type.enumerated.AuthorizationMode;
import com.serotonin.bacnet4j.type.enumerated.BackupState;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.DeviceStatus;
import com.serotonin.bacnet4j.type.enumerated.DoorAlarmState;
import com.serotonin.bacnet4j.type.enumerated.DoorSecuredStatus;
import com.serotonin.bacnet4j.type.enumerated.DoorStatus;
import com.serotonin.bacnet4j.type.enumerated.DoorValue;
import com.serotonin.bacnet4j.type.enumerated.EngineeringUnits;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.EventType;
import com.serotonin.bacnet4j.type.enumerated.FaultType;
import com.serotonin.bacnet4j.type.enumerated.FileAccessMethod;
import com.serotonin.bacnet4j.type.enumerated.LifeSafetyMode;
import com.serotonin.bacnet4j.type.enumerated.LifeSafetyOperation;
import com.serotonin.bacnet4j.type.enumerated.LifeSafetyState;
import com.serotonin.bacnet4j.type.enumerated.LightingInProgress;
import com.serotonin.bacnet4j.type.enumerated.LightingTransition;
import com.serotonin.bacnet4j.type.enumerated.LockStatus;
import com.serotonin.bacnet4j.type.enumerated.LoggingType;
import com.serotonin.bacnet4j.type.enumerated.Maintenance;
import com.serotonin.bacnet4j.type.enumerated.NodeType;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.Polarity;
import com.serotonin.bacnet4j.type.enumerated.ProgramError;
import com.serotonin.bacnet4j.type.enumerated.ProgramRequest;
import com.serotonin.bacnet4j.type.enumerated.ProgramState;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Reliability;
import com.serotonin.bacnet4j.type.enumerated.RestartReason;
import com.serotonin.bacnet4j.type.enumerated.SecurityLevel;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.enumerated.ShedState;
import com.serotonin.bacnet4j.type.enumerated.SilencedState;
import com.serotonin.bacnet4j.type.enumerated.VtClass;
import com.serotonin.bacnet4j.type.enumerated.WriteStatus;
import com.serotonin.bacnet4j.type.eventParameter.EventParameter;
import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Date;
import com.serotonin.bacnet4j.type.primitive.Double;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.Primitive;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.bacnet4j.type.primitive.Time;
import com.serotonin.bacnet4j.type.primitive.Unsigned16;
import com.serotonin.bacnet4j.type.primitive.Unsigned32;
import com.serotonin.bacnet4j.type.primitive.Unsigned8;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class ObjectProperties {
    private static final Map<ObjectType, List<PropertyTypeDefinition>> propertyTypes = new HashMap<>();

    public static PropertyTypeDefinition getPropertyTypeDefinition(final ObjectType objectType,
            final PropertyIdentifier propertyIdentifier) {
        final List<PropertyTypeDefinition> list = propertyTypes.get(objectType);
        if (list == null)
            return null;
        for (final PropertyTypeDefinition def : list) {
            if (def.getPropertyIdentifier().equals(propertyIdentifier))
                return def;
        }
        return null;
    }

    public static PropertyTypeDefinition getPropertyTypeDefinitionRequired(final ObjectType objectType,
            final PropertyIdentifier propertyIdentifier) throws BACnetServiceException {
        final PropertyTypeDefinition def = getPropertyTypeDefinition(objectType, propertyIdentifier);
        if (def == null)
            throw new BACnetServiceException(ErrorClass.property, ErrorCode.unknownProperty,
                    objectType + "/" + propertyIdentifier);
        return def;
    }

    public static List<PropertyTypeDefinition> getPropertyTypeDefinitions(final ObjectType objectType) {
        return getPropertyTypeDefinitions(objectType, 0);
    }

    public static List<PropertyTypeDefinition> getRequiredPropertyTypeDefinitions(final ObjectType objectType) {
        return getPropertyTypeDefinitions(objectType, 1);
    }

    public static List<PropertyTypeDefinition> getOptionalPropertyTypeDefinitions(final ObjectType objectType) {
        return getPropertyTypeDefinitions(objectType, 2);
    }

    public static boolean isCommandable(final ObjectType type, final PropertyIdentifier pid) {
        if (!pid.equals(PropertyIdentifier.presentValue))
            return false;
        return type.equals(ObjectType.analogOutput) || type.equals(ObjectType.analogValue)
                || type.equals(ObjectType.binaryOutput) || type.equals(ObjectType.binaryValue)
                || type.equals(ObjectType.multiStateOutput) || type.equals(ObjectType.multiStateValue)
                || type.equals(ObjectType.accessDoor);
    }

    /**
     * @param objectType
     * @param include
     *            0 = all, 1 = required, 2 = optional
     * @return
     */
    private static List<PropertyTypeDefinition> getPropertyTypeDefinitions(final ObjectType objectType,
            final int include) {
        final List<PropertyTypeDefinition> result = new ArrayList<>();
        final List<PropertyTypeDefinition> list = propertyTypes.get(objectType);
        if (list != null) {
            for (final PropertyTypeDefinition def : list) {
                if (include == 0 || include == 1 && def.isRequired() || include == 2 && def.isOptional())
                    result.add(def);
            }
        }
        return result;
    }

    private static void add(final ObjectType type, final PropertyIdentifier pid, final Class<? extends Encodable> clazz,
            final boolean sequenceOf, final boolean required) {
        List<PropertyTypeDefinition> list = propertyTypes.get(type);
        if (list == null) {
            list = new ArrayList<>();
            propertyTypes.put(type, list);
        }

        // Check for existing entries.
        for (final PropertyTypeDefinition def : list) {
            if (def.getPropertyIdentifier().equals(pid)) {
                list.remove(def);
                break;
            }
        }

        list.add(new PropertyTypeDefinition(type, pid, clazz, sequenceOf, required));
    }

    public static void addPropertyTypeDefinition(final ObjectType type, final PropertyIdentifier pid,
            final Class<? extends Encodable> clazz, final boolean sequenceOf, final boolean required) {
        final List<PropertyTypeDefinition> list = propertyTypes.get(type);
        if (list == null)
            throw new RuntimeException("ObjectType not found: " + type);

        // Check for existing entries.
        for (final PropertyTypeDefinition def : list) {
            if (def.getPropertyIdentifier().equals(pid))
                throw new RuntimeException("ObjectType already contains the given PropertyIdentifier");
        }

        list.add(new PropertyTypeDefinition(type, pid, clazz, sequenceOf, required));
    }

    static {
        // Access credential - 12.35
        add(ObjectType.accessCredential, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.accessCredential, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.accessCredential, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.accessCredential, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.accessCredential, PropertyIdentifier.globalIdentifier, Unsigned32.class, false, false);
        add(ObjectType.accessCredential, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.accessCredential, PropertyIdentifier.reliability, Reliability.class, false, true);
        add(ObjectType.accessCredential, PropertyIdentifier.credentialStatus, BinaryPV.class, false, true);
        add(ObjectType.accessCredential, PropertyIdentifier.reasonForDisable, AccessCredentialDisableReason.class,
                false, true);
        add(ObjectType.accessCredential, PropertyIdentifier.authenticationFactors, CredentialAuthenticationFactor.class,
                true, true);
        add(ObjectType.accessCredential, PropertyIdentifier.activationTime, DateTime.class, false, true);
        add(ObjectType.accessCredential, PropertyIdentifier.expiryTime, DateTime.class, false, true);
        add(ObjectType.accessCredential, PropertyIdentifier.credentialDisable, AccessCredentialDisable.class, false,
                true);
        add(ObjectType.accessCredential, PropertyIdentifier.daysRemaining, SignedInteger.class, false, false);
        add(ObjectType.accessCredential, PropertyIdentifier.usesRemaining, SignedInteger.class, false, false);
        add(ObjectType.accessCredential, PropertyIdentifier.absenteeLimit, UnsignedInteger.class, false, false);
        add(ObjectType.accessCredential, PropertyIdentifier.belongsTo, DeviceObjectReference.class, false, false);
        add(ObjectType.accessCredential, PropertyIdentifier.assignedAccessRights, AssignedAccessRights.class, true,
                true);
        add(ObjectType.accessCredential, PropertyIdentifier.lastAccessPoint, DeviceObjectReference.class, false, false);
        add(ObjectType.accessCredential, PropertyIdentifier.lastAccessEvent, AccessEvent.class, false, false);
        add(ObjectType.accessCredential, PropertyIdentifier.lastUseTime, DateTime.class, false, false);
        add(ObjectType.accessCredential, PropertyIdentifier.traceFlag, Boolean.class, false, false);
        add(ObjectType.accessCredential, PropertyIdentifier.threatAuthority, AccessThreatLevel.class, false, false);
        add(ObjectType.accessCredential, PropertyIdentifier.extendedTimeEnable, Boolean.class, false, false);
        add(ObjectType.accessCredential, PropertyIdentifier.authorizationExemptions, AuthorizationExemption.class, true,
                false);
        add(ObjectType.accessCredential, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.accessCredential, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.accessCredential, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Access door - 12.26
        add(ObjectType.accessDoor, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.accessDoor, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.accessDoor, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.accessDoor, PropertyIdentifier.presentValue, DoorValue.class, false, true);
        add(ObjectType.accessDoor, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.accessDoor, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.accessDoor, PropertyIdentifier.eventState, EventState.class, false, true); // EventState.normal
        add(ObjectType.accessDoor, PropertyIdentifier.reliability, Reliability.class, false, true);
        add(ObjectType.accessDoor, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.accessDoor, PropertyIdentifier.priorityArray, PriorityArray.class, false, true);
        add(ObjectType.accessDoor, PropertyIdentifier.relinquishDefault, DoorValue.class, false, true);
        add(ObjectType.accessDoor, PropertyIdentifier.doorStatus, DoorStatus.class, false, false);
        add(ObjectType.accessDoor, PropertyIdentifier.lockStatus, LockStatus.class, false, false);
        add(ObjectType.accessDoor, PropertyIdentifier.securedStatus, DoorSecuredStatus.class, false, false);
        add(ObjectType.accessDoor, PropertyIdentifier.doorMembers, DeviceObjectReference.class, true, false);
        add(ObjectType.accessDoor, PropertyIdentifier.doorPulseTime, UnsignedInteger.class, false, true);
        add(ObjectType.accessDoor, PropertyIdentifier.doorExtendedPulseTime, UnsignedInteger.class, false, true);
        add(ObjectType.accessDoor, PropertyIdentifier.doorUnlockDelayTime, UnsignedInteger.class, false, false);
        add(ObjectType.accessDoor, PropertyIdentifier.doorOpenTooLongTime, UnsignedInteger.class, false, true);
        add(ObjectType.accessDoor, PropertyIdentifier.doorAlarmState, DoorAlarmState.class, false, false);
        add(ObjectType.accessDoor, PropertyIdentifier.maskedAlarmValues, DoorAlarmState.class, true, false);
        add(ObjectType.accessDoor, PropertyIdentifier.maintenanceRequired, Maintenance.class, false, false);
        add(ObjectType.accessDoor, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.accessDoor, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.accessDoor, PropertyIdentifier.alarmValues, DoorAlarmState.class, true, false);
        add(ObjectType.accessDoor, PropertyIdentifier.faultValues, DoorAlarmState.class, true, false);
        add(ObjectType.accessDoor, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.accessDoor, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.accessDoor, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.accessDoor, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.accessDoor, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.accessDoor, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.accessDoor, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.accessDoor, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class, false,
                false);
        add(ObjectType.accessDoor, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.accessDoor, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.accessDoor, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.accessDoor, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.accessDoor, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Access point - 12.31
        add(ObjectType.accessPoint, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.accessPoint, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.accessPoint, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.accessPoint, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.accessPoint, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.accessPoint, PropertyIdentifier.reliability, Reliability.class, false, true);
        add(ObjectType.accessPoint, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.accessPoint, PropertyIdentifier.authenticationStatus, AuthenticationStatus.class, false, true);
        add(ObjectType.accessPoint, PropertyIdentifier.activeAuthenticationPolicy, UnsignedInteger.class, false, true);
        add(ObjectType.accessPoint, PropertyIdentifier.numberOfAuthenticationPolicies, UnsignedInteger.class, false,
                true);
        add(ObjectType.accessPoint, PropertyIdentifier.authenticationPolicyList, AuthenticationPolicy.class, true,
                false);
        add(ObjectType.accessPoint, PropertyIdentifier.authenticationPolicyNames, CharacterString.class, true, false);
        add(ObjectType.accessPoint, PropertyIdentifier.authorizationMode, AuthorizationMode.class, false, true);
        add(ObjectType.accessPoint, PropertyIdentifier.verificationTime, UnsignedInteger.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.lockout, Boolean.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.lockoutRelinquishTime, UnsignedInteger.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.failedAttempts, UnsignedInteger.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.failedAttemptEvents, AccessEvent.class, true, false);
        add(ObjectType.accessPoint, PropertyIdentifier.maxFailedAttempts, UnsignedInteger.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.failedAttemptsTime, UnsignedInteger.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.threatLevel, AccessThreatLevel.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.occupancyUpperLimitEnforced, Boolean.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.occupancyLowerLimitEnforced, Boolean.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.occupancyCountAdjust, Boolean.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.accompanimentTime, UnsignedInteger.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.accessEvent, AccessEvent.class, false, true);
        add(ObjectType.accessPoint, PropertyIdentifier.accessEventTag, UnsignedInteger.class, false, true);
        add(ObjectType.accessPoint, PropertyIdentifier.accessEventTime, TimeStamp.class, false, true);
        add(ObjectType.accessPoint, PropertyIdentifier.accessEventCredential, DeviceObjectReference.class, false, true);
        add(ObjectType.accessPoint, PropertyIdentifier.accessEventAuthenticationFactor, AuthenticationFactor.class,
                false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.accessDoors, DeviceObjectReference.class, true, true);
        add(ObjectType.accessPoint, PropertyIdentifier.priorityForWriting, UnsignedInteger.class, false, true);
        add(ObjectType.accessPoint, PropertyIdentifier.musterPoint, Boolean.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.zoneTo, DeviceObjectReference.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.zoneFrom, DeviceObjectReference.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.transactionNotificationClass, UnsignedInteger.class, false,
                false);
        add(ObjectType.accessPoint, PropertyIdentifier.accessAlarmEvents, AccessEvent.class, true, false);
        add(ObjectType.accessPoint, PropertyIdentifier.accessTransactionEvents, AccessEvent.class, true, false);
        add(ObjectType.accessPoint, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.accessPoint, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.accessPoint, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.accessPoint, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class, false,
                false);
        add(ObjectType.accessPoint, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.accessPoint, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.accessPoint, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Access rights - 12.34
        add(ObjectType.accessRights, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.accessRights, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.accessRights, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.accessRights, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.accessRights, PropertyIdentifier.globalIdentifier, Unsigned32.class, false, false);
        add(ObjectType.accessRights, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.accessRights, PropertyIdentifier.reliability, Reliability.class, false, true);
        add(ObjectType.accessRights, PropertyIdentifier.enable, Boolean.class, false, true);
        add(ObjectType.accessRights, PropertyIdentifier.negativeAccessRules, AccessRule.class, true, true);
        add(ObjectType.accessRights, PropertyIdentifier.positiveAccessRules, AccessRule.class, true, true);
        add(ObjectType.accessRights, PropertyIdentifier.accompaniment, DeviceObjectReference.class, false, false);
        add(ObjectType.accessRights, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.accessRights, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.accessRights, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Access user - 12.33
        add(ObjectType.accessUser, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.accessUser, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.accessUser, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.accessUser, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.accessUser, PropertyIdentifier.globalIdentifier, Unsigned32.class, false, false);
        add(ObjectType.accessUser, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.accessUser, PropertyIdentifier.reliability, Reliability.class, false, true);
        add(ObjectType.accessUser, PropertyIdentifier.userType, AccessUserType.class, false, true);
        add(ObjectType.accessUser, PropertyIdentifier.userName, CharacterString.class, false, false);
        add(ObjectType.accessUser, PropertyIdentifier.userExternalIdentifier, CharacterString.class, false, false);
        add(ObjectType.accessUser, PropertyIdentifier.userInformationReference, CharacterString.class, false, false);
        add(ObjectType.accessUser, PropertyIdentifier.members, DeviceObjectReference.class, true, false);
        add(ObjectType.accessUser, PropertyIdentifier.memberOf, DeviceObjectReference.class, true, false);
        add(ObjectType.accessUser, PropertyIdentifier.credentials, DeviceObjectReference.class, true, true);
        add(ObjectType.accessUser, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.accessUser, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.accessUser, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Access zone - 12.32
        add(ObjectType.accessZone, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.accessZone, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.accessZone, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.accessZone, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.globalIdentifier, Unsigned32.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.occupancyState, AccessZoneOccupancyState.class, false, true);
        add(ObjectType.accessZone, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.accessZone, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.accessZone, PropertyIdentifier.reliability, Reliability.class, false, true);
        add(ObjectType.accessZone, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.accessZone, PropertyIdentifier.occupancyCount, UnsignedInteger.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.occupancyCountEnable, Boolean.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.adjustValue, SignedInteger.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.occupancyUpperLimit, UnsignedInteger.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.occupancyLowerLimit, UnsignedInteger.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.credentialsInZone, DeviceObjectReference.class, true, false);
        add(ObjectType.accessZone, PropertyIdentifier.lastCredentialAdded, DeviceObjectReference.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.lastCredentialAddedTime, DateTime.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.lastCredentialRemoved, DeviceObjectReference.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.lastCredentialRemovedTime, DateTime.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.passbackMode, AccessPassbackMode.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.passbackTimeout, UnsignedInteger.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.entryPoints, DeviceObjectReference.class, true, true);
        add(ObjectType.accessZone, PropertyIdentifier.exitPoints, DeviceObjectReference.class, true, true);
        add(ObjectType.accessZone, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.alarmValues, AccessZoneOccupancyState.class, true, false);
        add(ObjectType.accessZone, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.accessZone, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.accessZone, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.accessZone, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class, false,
                false);
        add(ObjectType.accessZone, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.accessZone, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.accessZone, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Accumulator - 12.1
        add(ObjectType.accumulator, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.accumulator, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.accumulator, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.accumulator, PropertyIdentifier.presentValue, UnsignedInteger.class, false, true);
        add(ObjectType.accumulator, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.deviceType, CharacterString.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.accumulator, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.accumulator, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.accumulator, PropertyIdentifier.scale, Scale.class, false, true);
        add(ObjectType.accumulator, PropertyIdentifier.units, EngineeringUnits.class, false, true);
        add(ObjectType.accumulator, PropertyIdentifier.prescale, Prescale.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.maxPresValue, UnsignedInteger.class, false, true);
        add(ObjectType.accumulator, PropertyIdentifier.valueChangeTime, DateTime.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.valueBeforeChange, UnsignedInteger.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.valueSet, UnsignedInteger.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.loggingRecord, AccumulatorRecord.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.loggingObject, ObjectIdentifier.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.pulseRate, UnsignedInteger.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.highLimit, UnsignedInteger.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.lowLimit, UnsignedInteger.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.limitMonitoringInterval, UnsignedInteger.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.limitEnable, LimitEnable.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.accumulator, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.accumulator, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.accumulator, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class, false,
                false);
        add(ObjectType.accumulator, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.accumulator, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.accumulator, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Alert enrollment - 12.52
        add(ObjectType.alertEnrollment, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.alertEnrollment, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.alertEnrollment, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.alertEnrollment, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.alertEnrollment, PropertyIdentifier.presentValue, ObjectIdentifier.class, false, true);
        add(ObjectType.alertEnrollment, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.alertEnrollment, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, true);
        add(ObjectType.alertEnrollment, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.alertEnrollment, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, true);
        add(ObjectType.alertEnrollment, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, true);
        add(ObjectType.alertEnrollment, PropertyIdentifier.notifyType, NotifyType.class, false, true);
        add(ObjectType.alertEnrollment, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.alertEnrollment, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.alertEnrollment, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.alertEnrollment, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class,
                false, false);
        add(ObjectType.alertEnrollment, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.alertEnrollment, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.alertEnrollment, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Analog input - 12.2
        add(ObjectType.analogInput, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.analogInput, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.analogInput, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.analogInput, PropertyIdentifier.presentValue, Real.class, false, true);
        add(ObjectType.analogInput, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.deviceType, CharacterString.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.analogInput, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.analogInput, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.analogInput, PropertyIdentifier.updateInterval, UnsignedInteger.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.units, EngineeringUnits.class, false, true);
        add(ObjectType.analogInput, PropertyIdentifier.minPresValue, Real.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.maxPresValue, Real.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.resolution, Real.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.covIncrement, Real.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.highLimit, Real.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.lowLimit, Real.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.deadband, Real.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.limitEnable, LimitEnable.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.analogInput, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.analogInput, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.analogInput, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class, false,
                false);
        add(ObjectType.analogInput, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.analogInput, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.analogInput, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Analog output - 12.3
        add(ObjectType.analogOutput, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.analogOutput, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.analogOutput, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.analogOutput, PropertyIdentifier.presentValue, Real.class, false, true);
        add(ObjectType.analogOutput, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.deviceType, CharacterString.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.analogOutput, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.analogOutput, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.analogOutput, PropertyIdentifier.units, EngineeringUnits.class, false, true);
        add(ObjectType.analogOutput, PropertyIdentifier.minPresValue, Real.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.maxPresValue, Real.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.resolution, Real.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.priorityArray, PriorityArray.class, false, true);
        add(ObjectType.analogOutput, PropertyIdentifier.relinquishDefault, Real.class, false, true);
        add(ObjectType.analogOutput, PropertyIdentifier.covIncrement, Real.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.highLimit, Real.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.lowLimit, Real.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.deadband, Real.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.limitEnable, LimitEnable.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.analogOutput, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.analogOutput, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.analogOutput, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class, false,
                false);
        add(ObjectType.analogOutput, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.analogOutput, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.analogOutput, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Analog value - 12.4
        add(ObjectType.analogValue, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.analogValue, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.analogValue, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.analogValue, PropertyIdentifier.presentValue, Real.class, false, true);
        add(ObjectType.analogValue, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.analogValue, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.analogValue, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.analogValue, PropertyIdentifier.units, EngineeringUnits.class, false, true);
        add(ObjectType.analogValue, PropertyIdentifier.priorityArray, PriorityArray.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.relinquishDefault, Real.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.covIncrement, Real.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.highLimit, Real.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.lowLimit, Real.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.deadband, Real.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.limitEnable, LimitEnable.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.analogValue, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.analogValue, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.analogValue, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class, false,
                false);
        add(ObjectType.analogValue, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.minPresValue, Real.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.maxPresValue, Real.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.resolution, Real.class, false, false);
        add(ObjectType.analogValue, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.analogValue, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Averaging - 12.5
        add(ObjectType.averaging, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.averaging, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.averaging, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.averaging, PropertyIdentifier.minimumValue, Real.class, false, true);
        add(ObjectType.averaging, PropertyIdentifier.minimumValueTimestamp, DateTime.class, false, false);
        add(ObjectType.averaging, PropertyIdentifier.averageValue, Real.class, false, true);
        add(ObjectType.averaging, PropertyIdentifier.varianceValue, Real.class, false, false);
        add(ObjectType.averaging, PropertyIdentifier.maximumValue, Real.class, false, true);
        add(ObjectType.averaging, PropertyIdentifier.maximumValueTimestamp, DateTime.class, false, false);
        add(ObjectType.averaging, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.averaging, PropertyIdentifier.attemptedSamples, UnsignedInteger.class, false, true);
        add(ObjectType.averaging, PropertyIdentifier.validSamples, UnsignedInteger.class, false, true);
        add(ObjectType.averaging, PropertyIdentifier.objectPropertyReference, DeviceObjectPropertyReference.class,
                false, true);
        add(ObjectType.averaging, PropertyIdentifier.windowInterval, UnsignedInteger.class, false, true);
        add(ObjectType.averaging, PropertyIdentifier.windowSamples, UnsignedInteger.class, false, true);
        add(ObjectType.averaging, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.averaging, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Binary input - 12.6
        add(ObjectType.binaryInput, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.binaryInput, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.binaryInput, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.binaryInput, PropertyIdentifier.presentValue, BinaryPV.class, false, true);
        add(ObjectType.binaryInput, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.deviceType, CharacterString.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.binaryInput, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.binaryInput, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.binaryInput, PropertyIdentifier.polarity, Polarity.class, false, true);
        add(ObjectType.binaryInput, PropertyIdentifier.inactiveText, CharacterString.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.activeText, CharacterString.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.changeOfStateTime, DateTime.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.changeOfStateCount, UnsignedInteger.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.timeOfStateCountReset, DateTime.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.elapsedActiveTime, UnsignedInteger.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.timeOfActiveTimeReset, DateTime.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.alarmValue, BinaryPV.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.binaryInput, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.binaryInput, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.binaryInput, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class, false,
                false);
        add(ObjectType.binaryInput, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.binaryInput, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.binaryInput, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Binary output - 12.7
        add(ObjectType.binaryOutput, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.binaryOutput, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.binaryOutput, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.binaryOutput, PropertyIdentifier.presentValue, BinaryPV.class, false, true);
        add(ObjectType.binaryOutput, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.deviceType, CharacterString.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.binaryOutput, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.binaryOutput, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.binaryOutput, PropertyIdentifier.polarity, Polarity.class, false, true);
        add(ObjectType.binaryOutput, PropertyIdentifier.inactiveText, CharacterString.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.activeText, CharacterString.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.changeOfStateTime, DateTime.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.changeOfStateCount, UnsignedInteger.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.timeOfStateCountReset, DateTime.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.elapsedActiveTime, UnsignedInteger.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.timeOfActiveTimeReset, DateTime.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.minimumOffTime, UnsignedInteger.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.minimumOnTime, UnsignedInteger.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.priorityArray, PriorityArray.class, false, true);
        add(ObjectType.binaryOutput, PropertyIdentifier.relinquishDefault, BinaryPV.class, false, true);
        add(ObjectType.binaryOutput, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.feedbackValue, BinaryPV.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class, false,
                false);
        add(ObjectType.binaryOutput, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.binaryOutput, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.binaryOutput, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Binary value - 12.8
        add(ObjectType.binaryValue, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.binaryValue, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.binaryValue, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.binaryValue, PropertyIdentifier.presentValue, BinaryPV.class, false, true);
        add(ObjectType.binaryValue, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.binaryValue, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.binaryValue, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.outOfService, Boolean.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.inactiveText, CharacterString.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.activeText, CharacterString.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.changeOfStateTime, DateTime.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.changeOfStateCount, UnsignedInteger.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.timeOfStateCountReset, DateTime.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.elapsedActiveTime, UnsignedInteger.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.timeOfActiveTimeReset, DateTime.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.minimumOffTime, UnsignedInteger.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.minimumOnTime, UnsignedInteger.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.priorityArray, PriorityArray.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.relinquishDefault, BinaryPV.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.alarmValue, BinaryPV.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.binaryValue, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.binaryValue, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.binaryValue, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class, false,
                false);
        add(ObjectType.binaryValue, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.binaryValue, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.binaryValue, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // BitString value - 12.39
        add(ObjectType.bitstringValue, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.bitstringValue, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.bitstringValue, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.bitstringValue, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.presentValue, BitString.class, false, true);
        add(ObjectType.bitstringValue, PropertyIdentifier.bitText, CharacterString.class, true, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.bitstringValue, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.bitstringValue, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.bitstringValue, PropertyIdentifier.priorityArray, PriorityArray.class, false, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.relinquishDefault, BitString.class, false, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.alarmValues, BitString.class, true, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.bitMask, BitString.class, false, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class,
                false, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.bitstringValue, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.bitstringValue, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Calendar - 12.9
        add(ObjectType.calendar, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.calendar, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.calendar, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.calendar, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.calendar, PropertyIdentifier.presentValue, Boolean.class, false, true);
        add(ObjectType.calendar, PropertyIdentifier.dateList, CalendarEntry.class, true, true);
        add(ObjectType.calendar, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.calendar, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Channel - 12.53
        add(ObjectType.channel, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.channel, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.channel, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.channel, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.channel, PropertyIdentifier.presentValue, ChannelValue.class, false, true);
        add(ObjectType.channel, PropertyIdentifier.lastPriority, UnsignedInteger.class, false, true);
        add(ObjectType.channel, PropertyIdentifier.writeStatus, WriteStatus.class, false, true);
        add(ObjectType.channel, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.channel, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.channel, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.channel, PropertyIdentifier.listOfObjectPropertyReferences, DeviceObjectPropertyReference.class,
                true, true);
        add(ObjectType.channel, PropertyIdentifier.executionDelay, UnsignedInteger.class, true, false);
        add(ObjectType.channel, PropertyIdentifier.allowGroupDelayInhibit, Boolean.class, false, false);
        add(ObjectType.channel, PropertyIdentifier.channelNumber, Unsigned16.class, false, true);
        add(ObjectType.channel, PropertyIdentifier.controlGroups, Unsigned32.class, true, true);
        add(ObjectType.channel, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.channel, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.channel, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.channel, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.channel, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.channel, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.channel, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.channel, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.channel, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.channel, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.channel, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.channel, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // CharacterString value - 12.37
        add(ObjectType.characterstringValue, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.characterstringValue, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.characterstringValue, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.characterstringValue, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.characterstringValue, PropertyIdentifier.presentValue, CharacterString.class, false, true);
        add(ObjectType.characterstringValue, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.characterstringValue, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.characterstringValue, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.characterstringValue, PropertyIdentifier.outOfService, Boolean.class, false, false);
        add(ObjectType.characterstringValue, PropertyIdentifier.priorityArray, PriorityArray.class, false, false);
        add(ObjectType.characterstringValue, PropertyIdentifier.relinquishDefault, CharacterString.class, false, false);
        add(ObjectType.characterstringValue, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.characterstringValue, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.characterstringValue, PropertyIdentifier.alarmValues, OptionalCharacterString.class, true,
                false);
        add(ObjectType.characterstringValue, PropertyIdentifier.faultValues, OptionalCharacterString.class, true,
                false);
        add(ObjectType.characterstringValue, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.characterstringValue, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false,
                false);
        add(ObjectType.characterstringValue, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.characterstringValue, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.characterstringValue, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.characterstringValue, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true,
                false);
        add(ObjectType.characterstringValue, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.characterstringValue, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class,
                false, false);
        add(ObjectType.characterstringValue, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.characterstringValue, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.characterstringValue, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false,
                false);
        add(ObjectType.characterstringValue, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.characterstringValue, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Command - 12.10
        add(ObjectType.command, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.command, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.command, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.command, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.command, PropertyIdentifier.presentValue, UnsignedInteger.class, false, true);
        add(ObjectType.command, PropertyIdentifier.inProcess, Boolean.class, false, true);
        add(ObjectType.command, PropertyIdentifier.allWritesSuccessful, Boolean.class, false, true);
        add(ObjectType.command, PropertyIdentifier.action, ActionList.class, true, true);
        add(ObjectType.command, PropertyIdentifier.actionText, CharacterString.class, true, false);
        add(ObjectType.command, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.command, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Credential Data Input - 12.36
        add(ObjectType.credentialDataInput, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.credentialDataInput, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.credentialDataInput, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.credentialDataInput, PropertyIdentifier.presentValue, AuthenticationFactor.class, false, true);
        add(ObjectType.credentialDataInput, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.credentialDataInput, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.credentialDataInput, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.credentialDataInput, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.credentialDataInput, PropertyIdentifier.supportedFormats, AuthenticationFactorFormat.class, true,
                true);
        add(ObjectType.credentialDataInput, PropertyIdentifier.supportedFormatClasses, UnsignedInteger.class, true,
                false);
        add(ObjectType.credentialDataInput, PropertyIdentifier.updateTime, TimeStamp.class, false, true);
        add(ObjectType.credentialDataInput, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.credentialDataInput, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.credentialDataInput, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.credentialDataInput, PropertyIdentifier.eventState, EventState.class, false, false);
        add(ObjectType.credentialDataInput, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false,
                false);
        add(ObjectType.credentialDataInput, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.credentialDataInput, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.credentialDataInput, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.credentialDataInput, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true,
                false);
        add(ObjectType.credentialDataInput, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false,
                false);
        add(ObjectType.credentialDataInput, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.credentialDataInput, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Date value - 12.45
        add(ObjectType.dateValue, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.dateValue, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.dateValue, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.dateValue, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.dateValue, PropertyIdentifier.presentValue, Date.class, false, true);
        add(ObjectType.dateValue, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.dateValue, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.dateValue, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.dateValue, PropertyIdentifier.outOfService, Boolean.class, false, false);
        add(ObjectType.dateValue, PropertyIdentifier.priorityArray, PriorityArray.class, false, false);
        add(ObjectType.dateValue, PropertyIdentifier.relinquishDefault, Date.class, false, false);
        add(ObjectType.dateValue, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.dateValue, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.dateValue, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Date pattern value - 12.48
        add(ObjectType.datePatternValue, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.datePatternValue, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.datePatternValue, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.datePatternValue, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.datePatternValue, PropertyIdentifier.presentValue, Date.class, false, true);
        add(ObjectType.datePatternValue, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.datePatternValue, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.datePatternValue, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.datePatternValue, PropertyIdentifier.outOfService, Boolean.class, false, false);
        add(ObjectType.datePatternValue, PropertyIdentifier.priorityArray, PriorityArray.class, false, false);
        add(ObjectType.datePatternValue, PropertyIdentifier.relinquishDefault, Date.class, false, false);
        add(ObjectType.datePatternValue, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.datePatternValue, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.datePatternValue, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // DateTime value - 12.38
        add(ObjectType.datetimeValue, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.datetimeValue, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.datetimeValue, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.datetimeValue, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.datetimeValue, PropertyIdentifier.presentValue, DateTime.class, false, true);
        add(ObjectType.datetimeValue, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.datetimeValue, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.datetimeValue, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.datetimeValue, PropertyIdentifier.outOfService, Boolean.class, false, false);
        add(ObjectType.datetimeValue, PropertyIdentifier.priorityArray, PriorityArray.class, false, false);
        add(ObjectType.datetimeValue, PropertyIdentifier.relinquishDefault, DateTime.class, false, false);
        add(ObjectType.datetimeValue, PropertyIdentifier.isUtc, Boolean.class, false, false);
        add(ObjectType.datetimeValue, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.datetimeValue, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.datetimeValue, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // DateTime pattern - 12.46
        add(ObjectType.datetimePatternValue, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.datetimePatternValue, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.datetimePatternValue, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.datetimePatternValue, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.datetimePatternValue, PropertyIdentifier.presentValue, DateTime.class, false, true);
        add(ObjectType.datetimePatternValue, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.datetimePatternValue, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.datetimePatternValue, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.datetimePatternValue, PropertyIdentifier.outOfService, Boolean.class, false, false);
        add(ObjectType.datetimePatternValue, PropertyIdentifier.isUtc, Boolean.class, false, false);
        add(ObjectType.datetimePatternValue, PropertyIdentifier.priorityArray, PriorityArray.class, false, false);
        add(ObjectType.datetimePatternValue, PropertyIdentifier.relinquishDefault, DateTime.class, false, false);
        add(ObjectType.datetimePatternValue, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false,
                false);
        add(ObjectType.datetimePatternValue, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.datetimePatternValue, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Device - 12.11
        add(ObjectType.device, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.device, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.device, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.device, PropertyIdentifier.systemStatus, DeviceStatus.class, false, true);
        add(ObjectType.device, PropertyIdentifier.vendorName, CharacterString.class, false, true);
        add(ObjectType.device, PropertyIdentifier.vendorIdentifier, Unsigned16.class, false, true);
        add(ObjectType.device, PropertyIdentifier.modelName, CharacterString.class, false, true);
        add(ObjectType.device, PropertyIdentifier.firmwareRevision, CharacterString.class, false, true);
        add(ObjectType.device, PropertyIdentifier.applicationSoftwareVersion, CharacterString.class, false, true);
        add(ObjectType.device, PropertyIdentifier.location, CharacterString.class, false, false);
        add(ObjectType.device, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.device, PropertyIdentifier.protocolVersion, UnsignedInteger.class, false, true);
        add(ObjectType.device, PropertyIdentifier.protocolRevision, UnsignedInteger.class, false, true);
        add(ObjectType.device, PropertyIdentifier.protocolServicesSupported, ServicesSupported.class, false, true);
        add(ObjectType.device, PropertyIdentifier.protocolObjectTypesSupported, ObjectTypesSupported.class, false,
                true);
        add(ObjectType.device, PropertyIdentifier.objectList, ObjectIdentifier.class, true, true);
        add(ObjectType.device, PropertyIdentifier.structuredObjectList, ObjectIdentifier.class, true, false);
        add(ObjectType.device, PropertyIdentifier.maxApduLengthAccepted, UnsignedInteger.class, false, true);
        add(ObjectType.device, PropertyIdentifier.segmentationSupported, Segmentation.class, false, true);
        add(ObjectType.device, PropertyIdentifier.maxSegmentsAccepted, UnsignedInteger.class, false, true);
        add(ObjectType.device, PropertyIdentifier.vtClassesSupported, VtClass.class, true, false);
        add(ObjectType.device, PropertyIdentifier.activeVtSessions, VtSession.class, true, false);
        add(ObjectType.device, PropertyIdentifier.localTime, Time.class, false, false);
        add(ObjectType.device, PropertyIdentifier.localDate, Date.class, false, false);
        add(ObjectType.device, PropertyIdentifier.utcOffset, SignedInteger.class, false, false);
        add(ObjectType.device, PropertyIdentifier.daylightSavingsStatus, Boolean.class, false, false);
        add(ObjectType.device, PropertyIdentifier.apduSegmentTimeout, UnsignedInteger.class, false, true);
        add(ObjectType.device, PropertyIdentifier.apduTimeout, UnsignedInteger.class, false, true);
        add(ObjectType.device, PropertyIdentifier.numberOfApduRetries, UnsignedInteger.class, false, true);
        add(ObjectType.device, PropertyIdentifier.timeSynchronizationRecipients, Recipient.class, true, false);
        add(ObjectType.device, PropertyIdentifier.maxMaster, UnsignedInteger.class, false, false);
        add(ObjectType.device, PropertyIdentifier.maxInfoFrames, UnsignedInteger.class, false, false);
        add(ObjectType.device, PropertyIdentifier.deviceAddressBinding, AddressBinding.class, true, true);
        add(ObjectType.device, PropertyIdentifier.databaseRevision, UnsignedInteger.class, false, true);
        add(ObjectType.device, PropertyIdentifier.configurationFiles, ObjectIdentifier.class, true, false);
        add(ObjectType.device, PropertyIdentifier.lastRestoreTime, TimeStamp.class, false, false);
        add(ObjectType.device, PropertyIdentifier.backupFailureTimeout, Unsigned16.class, false, false);
        add(ObjectType.device, PropertyIdentifier.backupPreparationTime, Unsigned16.class, false, false);
        add(ObjectType.device, PropertyIdentifier.restorePreparationTime, Unsigned16.class, false, false);
        add(ObjectType.device, PropertyIdentifier.restoreCompletionTime, Unsigned16.class, false, false);
        add(ObjectType.device, PropertyIdentifier.backupAndRestoreState, BackupState.class, false, true);
        add(ObjectType.device, PropertyIdentifier.activeCovSubscriptions, CovSubscription.class, true, true);
        add(ObjectType.device, PropertyIdentifier.slaveProxyEnable, Boolean.class, true, false);
        add(ObjectType.device, PropertyIdentifier.manualSlaveAddressBinding, AddressBinding.class, true, false);
        add(ObjectType.device, PropertyIdentifier.autoSlaveDiscovery, Boolean.class, true, false);
        add(ObjectType.device, PropertyIdentifier.slaveAddressBinding, AddressBinding.class, true, false);
        add(ObjectType.device, PropertyIdentifier.lastRestartReason, RestartReason.class, false, false);
        add(ObjectType.device, PropertyIdentifier.timeOfDeviceRestart, TimeStamp.class, false, false);
        add(ObjectType.device, PropertyIdentifier.restartNotificationRecipients, Recipient.class, true, false);
        add(ObjectType.device, PropertyIdentifier.utcTimeSynchronizationRecipients, Recipient.class, true, false);
        add(ObjectType.device, PropertyIdentifier.timeSynchronizationInterval, UnsignedInteger.class, false, false);
        add(ObjectType.device, PropertyIdentifier.alignIntervals, Boolean.class, false, false);
        add(ObjectType.device, PropertyIdentifier.intervalOffset, UnsignedInteger.class, false, false);
        add(ObjectType.device, PropertyIdentifier.serialNumber, CharacterString.class, false, false);
        add(ObjectType.device, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.device, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Event enrollment - 12.12
        add(ObjectType.eventEnrollment, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.eventEnrollment, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.eventEnrollment, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.eventEnrollment, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.eventEnrollment, PropertyIdentifier.eventType, EventType.class, false, true);
        add(ObjectType.eventEnrollment, PropertyIdentifier.notifyType, NotifyType.class, false, true);
        add(ObjectType.eventEnrollment, PropertyIdentifier.eventParameters, EventParameter.class, false, true);
        add(ObjectType.eventEnrollment, PropertyIdentifier.objectPropertyReference, DeviceObjectPropertyReference.class,
                false, true);
        add(ObjectType.eventEnrollment, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.eventEnrollment, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, true);
        add(ObjectType.eventEnrollment, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, true);
        add(ObjectType.eventEnrollment, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.eventEnrollment, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.eventEnrollment, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.eventEnrollment, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.eventEnrollment, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.eventEnrollment, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class,
                false, false);
        add(ObjectType.eventEnrollment, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.eventEnrollment, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.eventEnrollment, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.eventEnrollment, PropertyIdentifier.reliability, Reliability.class, false, true);
        add(ObjectType.eventEnrollment, PropertyIdentifier.faultType, FaultType.class, false, false);
        add(ObjectType.eventEnrollment, PropertyIdentifier.faultParameters, FaultParameter.class, false, false);
        add(ObjectType.eventEnrollment, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.eventEnrollment, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.eventEnrollment, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Event log - 12.27
        add(ObjectType.eventLog, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.eventLog, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.eventLog, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.eventLog, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.eventLog, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.eventLog, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.eventLog, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.eventLog, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.eventLog, PropertyIdentifier.enable, Boolean.class, false, true);
        add(ObjectType.eventLog, PropertyIdentifier.startTime, DateTime.class, false, false);
        add(ObjectType.eventLog, PropertyIdentifier.stopTime, DateTime.class, false, false);
        add(ObjectType.eventLog, PropertyIdentifier.stopWhenFull, Boolean.class, false, true);
        add(ObjectType.eventLog, PropertyIdentifier.bufferSize, UnsignedInteger.class, false, true);
        add(ObjectType.eventLog, PropertyIdentifier.logBuffer, EventLogRecord.class, true, true);
        add(ObjectType.eventLog, PropertyIdentifier.recordCount, UnsignedInteger.class, false, true);
        add(ObjectType.eventLog, PropertyIdentifier.totalRecordCount, UnsignedInteger.class, false, true);
        add(ObjectType.eventLog, PropertyIdentifier.notificationThreshold, UnsignedInteger.class, false, false);
        add(ObjectType.eventLog, PropertyIdentifier.recordsSinceNotification, UnsignedInteger.class, false, false);
        add(ObjectType.eventLog, PropertyIdentifier.lastNotifyRecord, UnsignedInteger.class, false, false);
        add(ObjectType.eventLog, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.eventLog, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.eventLog, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.eventLog, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.eventLog, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.eventLog, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.eventLog, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.eventLog, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.eventLog, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class, false,
                false);
        add(ObjectType.eventLog, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.eventLog, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.eventLog, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // File - 12.13
        add(ObjectType.file, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.file, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.file, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.file, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.file, PropertyIdentifier.fileType, CharacterString.class, false, true);
        add(ObjectType.file, PropertyIdentifier.fileSize, UnsignedInteger.class, false, true);
        add(ObjectType.file, PropertyIdentifier.modificationDate, DateTime.class, false, true);
        add(ObjectType.file, PropertyIdentifier.archive, Boolean.class, false, true);
        add(ObjectType.file, PropertyIdentifier.readOnly, Boolean.class, false, true);
        add(ObjectType.file, PropertyIdentifier.fileAccessMethod, FileAccessMethod.class, false, true);
        add(ObjectType.file, PropertyIdentifier.recordCount, UnsignedInteger.class, false, false);
        add(ObjectType.file, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.file, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Group - 12.14
        add(ObjectType.group, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.group, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.group, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.group, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.group, PropertyIdentifier.listOfGroupMembers, ReadAccessSpecification.class, true, true);
        add(ObjectType.group, PropertyIdentifier.presentValue, ReadAccessResult.class, true, true);
        add(ObjectType.group, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.group, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Global group - 12.50
        add(ObjectType.globalGroup, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.globalGroup, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.globalGroup, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.globalGroup, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.globalGroup, PropertyIdentifier.groupMembers, DeviceObjectPropertyReference.class, true, true);
        add(ObjectType.globalGroup, PropertyIdentifier.groupMemberNames, CharacterString.class, true, false);
        add(ObjectType.globalGroup, PropertyIdentifier.presentValue, PropertyAccessResult.class, true, true);
        add(ObjectType.globalGroup, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.globalGroup, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.globalGroup, PropertyIdentifier.memberStatusFlags, StatusFlags.class, false, true);
        add(ObjectType.globalGroup, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.globalGroup, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.globalGroup, PropertyIdentifier.updateInterval, UnsignedInteger.class, false, false);
        add(ObjectType.globalGroup, PropertyIdentifier.requestedUpdateInterval, UnsignedInteger.class, false, false);
        add(ObjectType.globalGroup, PropertyIdentifier.covResubscriptionInterval, UnsignedInteger.class, false, false);
        add(ObjectType.globalGroup, PropertyIdentifier.clientCovIncrement, ClientCov.class, false, false);
        add(ObjectType.globalGroup, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.globalGroup, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.globalGroup, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.globalGroup, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.globalGroup, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.globalGroup, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.globalGroup, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.globalGroup, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.globalGroup, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.globalGroup, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class, false,
                false);
        add(ObjectType.globalGroup, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.globalGroup, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.globalGroup, PropertyIdentifier.covuPeriod, UnsignedInteger.class, false, false);
        add(ObjectType.globalGroup, PropertyIdentifier.covuRecipients, Recipient.class, true, false);
        add(ObjectType.globalGroup, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.globalGroup, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.globalGroup, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Integer value - 12.43
        add(ObjectType.integerValue, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.integerValue, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.integerValue, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.integerValue, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.presentValue, SignedInteger.class, false, true);
        add(ObjectType.integerValue, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.integerValue, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.integerValue, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.outOfService, Boolean.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.units, EngineeringUnits.class, false, true);
        add(ObjectType.integerValue, PropertyIdentifier.priorityArray, PriorityArray.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.relinquishDefault, SignedInteger.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.covIncrement, UnsignedInteger.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.highLimit, SignedInteger.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.lowLimit, SignedInteger.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.deadband, UnsignedInteger.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.limitEnable, LimitEnable.class, true, false);
        add(ObjectType.integerValue, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.integerValue, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.integerValue, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.integerValue, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class, false,
                false);
        add(ObjectType.integerValue, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.minPresValue, SignedInteger.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.maxPresValue, SignedInteger.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.resolution, SignedInteger.class, false, false);
        add(ObjectType.integerValue, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.integerValue, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Large analog value - 12.38
        add(ObjectType.largeAnalogValue, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.presentValue, Double.class, false, true);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.outOfService, Boolean.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.units, EngineeringUnits.class, false, true);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.priorityArray, PriorityArray.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.relinquishDefault, Double.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.covIncrement, Double.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.highLimit, Double.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.lowLimit, Double.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.deadband, Double.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.limitEnable, LimitEnable.class, true, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true,
                false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class,
                false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.minPresValue, Double.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.maxPresValue, Double.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.resolution, Double.class, false, false);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.largeAnalogValue, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Life safety point - 12.15
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.presentValue, LifeSafetyState.class, false, true);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.trackingValue, LifeSafetyState.class, false, true);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.deviceType, CharacterString.class, false, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.reliability, Reliability.class, false, true);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.mode, LifeSafetyMode.class, false, true);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.acceptedModes, LifeSafetyMode.class, true, true);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.lifeSafetyAlarmValues, LifeSafetyState.class, true, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.alarmValues, LifeSafetyState.class, true, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.faultValues, LifeSafetyState.class, true, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class,
                false, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.silenced, SilencedState.class, false, true);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.operationExpected, LifeSafetyOperation.class, false, true);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.maintenanceRequired, Maintenance.class, false, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.setting, UnsignedInteger.class, false, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.directReading, Real.class, false, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.units, EngineeringUnits.class, false, true);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.memberOf, DeviceObjectReference.class, true, false);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.lifeSafetyPoint, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Life safety zone - 12.16
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.presentValue, LifeSafetyState.class, false, true);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.trackingValue, LifeSafetyState.class, false, true);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.deviceType, CharacterString.class, false, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.reliability, Reliability.class, false, true);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.mode, LifeSafetyMode.class, false, true);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.acceptedModes, LifeSafetyMode.class, true, true);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.lifeSafetyAlarmValues, LifeSafetyState.class, true, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.alarmValues, LifeSafetyState.class, true, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.faultValues, LifeSafetyState.class, true, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class,
                false, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.silenced, SilencedState.class, false, true);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.operationExpected, LifeSafetyOperation.class, false, true);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.maintenanceRequired, Maintenance.class, false, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.zoneMembers, DeviceObjectReference.class, true, true);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.memberOf, DeviceObjectReference.class, true, false);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.lifeSafetyZone, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Lighting output - 12.54
        add(ObjectType.lightingOutput, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.lightingOutput, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.lightingOutput, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.lightingOutput, PropertyIdentifier.presentValue, Real.class, false, true);
        add(ObjectType.lightingOutput, PropertyIdentifier.trackingValue, Real.class, false, true);
        add(ObjectType.lightingOutput, PropertyIdentifier.lightingCommand, LightingCommand.class, false, true);
        add(ObjectType.lightingOutput, PropertyIdentifier.inProgress, LightingInProgress.class, false, true);
        add(ObjectType.lightingOutput, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.lightingOutput, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.lightingOutput, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.lightingOutput, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.lightingOutput, PropertyIdentifier.blinkWarnEnable, Boolean.class, false, true);
        add(ObjectType.lightingOutput, PropertyIdentifier.egressTime, UnsignedInteger.class, false, true);
        add(ObjectType.lightingOutput, PropertyIdentifier.egressActive, Boolean.class, false, true);
        add(ObjectType.lightingOutput, PropertyIdentifier.defaultFadeTime, UnsignedInteger.class, false, true);
        add(ObjectType.lightingOutput, PropertyIdentifier.defaultRampRate, Real.class, false, true);
        add(ObjectType.lightingOutput, PropertyIdentifier.defaultStepIncrement, Real.class, false, true);
        add(ObjectType.lightingOutput, PropertyIdentifier.transition, LightingTransition.class, false, false);
        add(ObjectType.lightingOutput, PropertyIdentifier.feedbackValue, Real.class, false, false);
        add(ObjectType.lightingOutput, PropertyIdentifier.priorityArray, PriorityArray.class, false, true);
        add(ObjectType.lightingOutput, PropertyIdentifier.relinquishDefault, Real.class, false, true);
        add(ObjectType.lightingOutput, PropertyIdentifier.power, Real.class, false, false);
        add(ObjectType.lightingOutput, PropertyIdentifier.instantaneousPower, Real.class, false, false);
        add(ObjectType.lightingOutput, PropertyIdentifier.minActualValue, Real.class, false, false);
        add(ObjectType.lightingOutput, PropertyIdentifier.maxActualValue, Real.class, false, false);
        add(ObjectType.lightingOutput, PropertyIdentifier.lightingCommandDefaultPriority, UnsignedInteger.class, false,
                true);
        add(ObjectType.lightingOutput, PropertyIdentifier.covIncrement, Real.class, false, false);
        add(ObjectType.lightingOutput, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.lightingOutput, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.lightingOutput, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Load control - 12.28
        add(ObjectType.loadControl, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.loadControl, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.loadControl, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.loadControl, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.loadControl, PropertyIdentifier.presentValue, ShedState.class, false, true);
        add(ObjectType.loadControl, PropertyIdentifier.stateDescription, CharacterString.class, false, false);
        add(ObjectType.loadControl, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.loadControl, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.loadControl, PropertyIdentifier.reliability, Reliability.class, false, true);
        add(ObjectType.loadControl, PropertyIdentifier.requestedShedLevel, ShedLevel.class, false, true);
        add(ObjectType.loadControl, PropertyIdentifier.startTime, DateTime.class, false, true);
        add(ObjectType.loadControl, PropertyIdentifier.shedDuration, UnsignedInteger.class, false, true);
        add(ObjectType.loadControl, PropertyIdentifier.dutyWindow, UnsignedInteger.class, false, true);
        add(ObjectType.loadControl, PropertyIdentifier.enable, Boolean.class, false, true);
        add(ObjectType.loadControl, PropertyIdentifier.fullDutyBaseline, Real.class, false, false);
        add(ObjectType.loadControl, PropertyIdentifier.expectedShedLevel, ShedLevel.class, false, true);
        add(ObjectType.loadControl, PropertyIdentifier.actualShedLevel, ShedLevel.class, false, true);
        add(ObjectType.loadControl, PropertyIdentifier.shedLevels, UnsignedInteger.class, true, true);
        add(ObjectType.loadControl, PropertyIdentifier.shedLevelDescriptions, CharacterString.class, true, true);
        add(ObjectType.loadControl, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.loadControl, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.loadControl, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.loadControl, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.loadControl, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.loadControl, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.loadControl, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.loadControl, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.loadControl, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.loadControl, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class, false,
                false);
        add(ObjectType.loadControl, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.loadControl, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.loadControl, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.loadControl, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.loadControl, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Loop - 12.17
        add(ObjectType.loop, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.loop, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.loop, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.loop, PropertyIdentifier.presentValue, Real.class, false, true);
        add(ObjectType.loop, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.loop, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.loop, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.loop, PropertyIdentifier.updateInterval, UnsignedInteger.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.outputUnits, EngineeringUnits.class, false, true);
        add(ObjectType.loop, PropertyIdentifier.manipulatedVariableReference, ObjectPropertyReference.class, false,
                true);
        add(ObjectType.loop, PropertyIdentifier.controlledVariableReference, ObjectPropertyReference.class, false,
                true);
        add(ObjectType.loop, PropertyIdentifier.controlledVariableValue, Real.class, false, true);
        add(ObjectType.loop, PropertyIdentifier.controlledVariableUnits, EngineeringUnits.class, false, true);
        add(ObjectType.loop, PropertyIdentifier.setpointReference, SetpointReference.class, false, true);
        add(ObjectType.loop, PropertyIdentifier.setpoint, Real.class, false, true);
        add(ObjectType.loop, PropertyIdentifier.action, Action.class, false, true);
        add(ObjectType.loop, PropertyIdentifier.proportionalConstant, Real.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.proportionalConstantUnits, EngineeringUnits.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.integralConstant, Real.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.integralConstantUnits, EngineeringUnits.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.derivativeConstant, Real.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.derivativeConstantUnits, EngineeringUnits.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.bias, Real.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.maximumOutput, Real.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.minimumOutput, Real.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.priorityForWriting, UnsignedInteger.class, false, true);
        add(ObjectType.loop, PropertyIdentifier.covIncrement, Real.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.errorLimit, Real.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.deadband, Real.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.loop, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.loop, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.loop, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.loop, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.loop, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Multi state input - 12.18
        add(ObjectType.multiStateInput, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.multiStateInput, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.multiStateInput, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.multiStateInput, PropertyIdentifier.presentValue, UnsignedInteger.class, false, true);
        add(ObjectType.multiStateInput, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.deviceType, CharacterString.class, false, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.multiStateInput, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.multiStateInput, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.multiStateInput, PropertyIdentifier.numberOfStates, UnsignedInteger.class, false, true);
        add(ObjectType.multiStateInput, PropertyIdentifier.stateText, CharacterString.class, true, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.alarmValues, UnsignedInteger.class, true, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.faultValues, UnsignedInteger.class, true, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class,
                false, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.multiStateInput, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.multiStateInput, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Multi state output - 12.19
        add(ObjectType.multiStateOutput, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.multiStateOutput, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.multiStateOutput, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.multiStateOutput, PropertyIdentifier.presentValue, UnsignedInteger.class, false, true);
        add(ObjectType.multiStateOutput, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.multiStateOutput, PropertyIdentifier.deviceType, CharacterString.class, false, false);
        add(ObjectType.multiStateOutput, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.multiStateOutput, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.multiStateOutput, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.multiStateOutput, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.multiStateOutput, PropertyIdentifier.numberOfStates, UnsignedInteger.class, false, true);
        add(ObjectType.multiStateOutput, PropertyIdentifier.stateText, CharacterString.class, true, false);
        add(ObjectType.multiStateOutput, PropertyIdentifier.priorityArray, PriorityArray.class, false, true);
        add(ObjectType.multiStateOutput, PropertyIdentifier.relinquishDefault, UnsignedInteger.class, false, true);
        add(ObjectType.multiStateOutput, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.multiStateOutput, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.multiStateOutput, PropertyIdentifier.feedbackValue, UnsignedInteger.class, false, false);
        add(ObjectType.multiStateOutput, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.multiStateOutput, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.multiStateOutput, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.multiStateOutput, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.multiStateOutput, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.multiStateOutput, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true,
                false);
        add(ObjectType.multiStateOutput, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.multiStateOutput, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class,
                false, false);
        add(ObjectType.multiStateOutput, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.multiStateOutput, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.multiStateOutput, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.multiStateOutput, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.multiStateOutput, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Multi state value - 12.20
        add(ObjectType.multiStateValue, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.multiStateValue, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.multiStateValue, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.multiStateValue, PropertyIdentifier.presentValue, UnsignedInteger.class, false, true);
        add(ObjectType.multiStateValue, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.multiStateValue, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.multiStateValue, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.multiStateValue, PropertyIdentifier.numberOfStates, UnsignedInteger.class, false, true);
        add(ObjectType.multiStateValue, PropertyIdentifier.stateText, CharacterString.class, true, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.priorityArray, PriorityArray.class, false, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.relinquishDefault, UnsignedInteger.class, false, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.alarmValues, UnsignedInteger.class, true, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.faultValues, UnsignedInteger.class, true, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class,
                false, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.multiStateValue, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.multiStateValue, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Network security - 12.49
        add(ObjectType.networkSecurity, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.networkSecurity, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.networkSecurity, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.networkSecurity, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.networkSecurity, PropertyIdentifier.baseDeviceSecurityPolicy, SecurityLevel.class, false, true);
        add(ObjectType.networkSecurity, PropertyIdentifier.networkAccessSecurityPolicies, NetworkSecurityPolicy.class,
                true, true);
        add(ObjectType.networkSecurity, PropertyIdentifier.securityTimeWindow, UnsignedInteger.class, false, true);
        add(ObjectType.networkSecurity, PropertyIdentifier.packetReorderTime, UnsignedInteger.class, false, true);
        add(ObjectType.networkSecurity, PropertyIdentifier.distributionKeyRevision, Unsigned8.class, false, true);
        add(ObjectType.networkSecurity, PropertyIdentifier.keySets, SecurityKeySet.class, true, true);
        add(ObjectType.networkSecurity, PropertyIdentifier.lastKeyServer, AddressBinding.class, false, true);
        add(ObjectType.networkSecurity, PropertyIdentifier.securityPduTimeout, Unsigned16.class, false, true);
        add(ObjectType.networkSecurity, PropertyIdentifier.updateKeySetTimeout, Unsigned16.class, false, true);
        add(ObjectType.networkSecurity, PropertyIdentifier.supportedSecurityAlgorithms, Unsigned8.class, true, true);
        add(ObjectType.networkSecurity, PropertyIdentifier.doNotHide, Boolean.class, false, true);
        add(ObjectType.networkSecurity, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.networkSecurity, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Notification class - 12.21
        add(ObjectType.notificationClass, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.notificationClass, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.notificationClass, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.notificationClass, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.notificationClass, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, true);
        add(ObjectType.notificationClass, PropertyIdentifier.priority, UnsignedInteger.class, true, true);
        add(ObjectType.notificationClass, PropertyIdentifier.ackRequired, EventTransitionBits.class, false, true);
        add(ObjectType.notificationClass, PropertyIdentifier.recipientList, Destination.class, true, true);
        add(ObjectType.notificationClass, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.notificationClass, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Notification forwarder - 12.51
        add(ObjectType.notificationForwarder, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.notificationForwarder, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.notificationForwarder, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.notificationForwarder, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.notificationForwarder, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.notificationForwarder, PropertyIdentifier.reliability, Reliability.class, false, true);
        add(ObjectType.notificationForwarder, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.notificationForwarder, PropertyIdentifier.recipientList, Destination.class, true, true);
        add(ObjectType.notificationForwarder, PropertyIdentifier.subscribedRecipients,
                EventNotificationSubscription.class, true, true);
        add(ObjectType.notificationForwarder, PropertyIdentifier.processIdentifierFilter, ProcessIdSelection.class,
                false, true);
        add(ObjectType.notificationForwarder, PropertyIdentifier.portFilter, PortPermission.class, true, false);
        add(ObjectType.notificationForwarder, PropertyIdentifier.localForwardingOnly, Boolean.class, false, true);
        add(ObjectType.notificationForwarder, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false,
                false);
        add(ObjectType.notificationForwarder, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.notificationForwarder, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // OctetString value - 12.40
        add(ObjectType.octetstringValue, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.octetstringValue, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.octetstringValue, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.octetstringValue, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.octetstringValue, PropertyIdentifier.presentValue, OctetString.class, false, true);
        add(ObjectType.octetstringValue, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.octetstringValue, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.octetstringValue, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.octetstringValue, PropertyIdentifier.outOfService, Boolean.class, false, false);
        add(ObjectType.octetstringValue, PropertyIdentifier.priorityArray, PriorityArray.class, false, false);
        add(ObjectType.octetstringValue, PropertyIdentifier.relinquishDefault, OctetString.class, false, false);
        add(ObjectType.octetstringValue, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.octetstringValue, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.octetstringValue, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Positive integer value - 12.44
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.presentValue, UnsignedInteger.class, false, true);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.outOfService, Boolean.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.units, EngineeringUnits.class, false, true);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.priorityArray, PriorityArray.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.relinquishDefault, UnsignedInteger.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.covIncrement, UnsignedInteger.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.highLimit, UnsignedInteger.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.lowLimit, UnsignedInteger.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.deadband, UnsignedInteger.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.limitEnable, LimitEnable.class, true, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false,
                false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true,
                false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class,
                false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false,
                false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.minPresValue, UnsignedInteger.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.maxPresValue, UnsignedInteger.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.resolution, UnsignedInteger.class, false, false);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.positiveIntegerValue, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Program - 12.22
        add(ObjectType.program, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.program, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.program, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.program, PropertyIdentifier.programState, ProgramState.class, false, true);
        add(ObjectType.program, PropertyIdentifier.programChange, ProgramRequest.class, false, true);
        add(ObjectType.program, PropertyIdentifier.reasonForHalt, ProgramError.class, false, false);
        add(ObjectType.program, PropertyIdentifier.descriptionOfHalt, CharacterString.class, false, true);
        add(ObjectType.program, PropertyIdentifier.programLocation, CharacterString.class, false, false);
        add(ObjectType.program, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.program, PropertyIdentifier.instanceOf, CharacterString.class, false, false);
        add(ObjectType.program, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.program, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.program, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.program, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, true);
        add(ObjectType.program, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, true);
        add(ObjectType.program, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, true);
        add(ObjectType.program, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.program, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, true);
        add(ObjectType.program, PropertyIdentifier.notifyType, NotifyType.class, false, true);
        add(ObjectType.program, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.program, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.program, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.program, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.program, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.program, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Pulse converter - 12.23
        add(ObjectType.pulseConverter, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.pulseConverter, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.pulseConverter, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.pulseConverter, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.presentValue, Real.class, false, true);
        add(ObjectType.pulseConverter, PropertyIdentifier.inputReference, ObjectPropertyReference.class, false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.pulseConverter, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.pulseConverter, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.pulseConverter, PropertyIdentifier.units, EngineeringUnits.class, false, true);
        add(ObjectType.pulseConverter, PropertyIdentifier.scaleFactor, Real.class, false, true);
        add(ObjectType.pulseConverter, PropertyIdentifier.adjustValue, Real.class, false, true);
        add(ObjectType.pulseConverter, PropertyIdentifier.count, UnsignedInteger.class, false, true);
        add(ObjectType.pulseConverter, PropertyIdentifier.updateTime, DateTime.class, false, true);
        add(ObjectType.pulseConverter, PropertyIdentifier.countChangeTime, DateTime.class, false, true);
        add(ObjectType.pulseConverter, PropertyIdentifier.countBeforeChange, UnsignedInteger.class, false, true);
        add(ObjectType.pulseConverter, PropertyIdentifier.covIncrement, Real.class, false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.covPeriod, UnsignedInteger.class, false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.timeDelay, UnsignedInteger.class, false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.highLimit, Real.class, false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.lowLimit, Real.class, false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.deadband, Real.class, false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.limitEnable, LimitEnable.class, false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class,
                false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.timeDelayNormal, UnsignedInteger.class, false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.pulseConverter, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.pulseConverter, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Schedule - 12.24
        add(ObjectType.schedule, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.schedule, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.schedule, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.schedule, PropertyIdentifier.presentValue, Primitive.class, false, true);
        add(ObjectType.schedule, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.schedule, PropertyIdentifier.effectivePeriod, DateRange.class, false, true);
        add(ObjectType.schedule, PropertyIdentifier.weeklySchedule, DailySchedule.class, true, false);
        add(ObjectType.schedule, PropertyIdentifier.exceptionSchedule, SpecialEvent.class, true, false);
        add(ObjectType.schedule, PropertyIdentifier.scheduleDefault, Primitive.class, false, true);
        add(ObjectType.schedule, PropertyIdentifier.listOfObjectPropertyReferences, DeviceObjectPropertyReference.class,
                true, true);
        add(ObjectType.schedule, PropertyIdentifier.priorityForWriting, UnsignedInteger.class, false, true);
        add(ObjectType.schedule, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.schedule, PropertyIdentifier.reliability, Reliability.class, false, true);
        add(ObjectType.schedule, PropertyIdentifier.outOfService, Boolean.class, false, true);
        add(ObjectType.schedule, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.schedule, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.schedule, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.schedule, PropertyIdentifier.eventState, EventState.class, false, false);
        add(ObjectType.schedule, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.schedule, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.schedule, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.schedule, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.schedule, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.schedule, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.schedule, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.schedule, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Structured View - 12.29
        add(ObjectType.structuredView, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.structuredView, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.structuredView, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.structuredView, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.structuredView, PropertyIdentifier.nodeType, NodeType.class, false, false);
        add(ObjectType.structuredView, PropertyIdentifier.nodeSubtype, CharacterString.class, false, false);
        add(ObjectType.structuredView, PropertyIdentifier.subordinateList, DeviceObjectReference.class, true, true);
        add(ObjectType.structuredView, PropertyIdentifier.subordinateAnnotations, CharacterString.class, true, false);
        add(ObjectType.structuredView, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.structuredView, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Time value - 12.42
        add(ObjectType.timeValue, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.timeValue, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.timeValue, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.timeValue, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.timeValue, PropertyIdentifier.presentValue, Time.class, false, true);
        add(ObjectType.timeValue, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.timeValue, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.timeValue, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.timeValue, PropertyIdentifier.outOfService, Boolean.class, false, false);
        add(ObjectType.timeValue, PropertyIdentifier.priorityArray, PriorityArray.class, false, false);
        add(ObjectType.timeValue, PropertyIdentifier.relinquishDefault, Time.class, false, false);
        add(ObjectType.timeValue, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.timeValue, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.timeValue, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Time pattern value - 12.47
        add(ObjectType.timePatternValue, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.timePatternValue, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.timePatternValue, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.timePatternValue, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.timePatternValue, PropertyIdentifier.presentValue, Time.class, false, true);
        add(ObjectType.timePatternValue, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.timePatternValue, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.timePatternValue, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.timePatternValue, PropertyIdentifier.outOfService, Boolean.class, false, false);
        add(ObjectType.timePatternValue, PropertyIdentifier.priorityArray, PriorityArray.class, false, false);
        add(ObjectType.timePatternValue, PropertyIdentifier.relinquishDefault, Time.class, false, false);
        add(ObjectType.timePatternValue, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.timePatternValue, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.timePatternValue, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Trend log - 12.25
        add(ObjectType.trendLog, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.trendLog, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.trendLog, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.trendLog, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.trendLog, PropertyIdentifier.enable, Boolean.class, false, true);
        add(ObjectType.trendLog, PropertyIdentifier.startTime, DateTime.class, false, false);
        add(ObjectType.trendLog, PropertyIdentifier.stopTime, DateTime.class, false, false);
        add(ObjectType.trendLog, PropertyIdentifier.logDeviceObjectProperty, DeviceObjectPropertyReference.class, false,
                false);
        add(ObjectType.trendLog, PropertyIdentifier.logInterval, UnsignedInteger.class, false, false);
        add(ObjectType.trendLog, PropertyIdentifier.covResubscriptionInterval, UnsignedInteger.class, false, false);
        add(ObjectType.trendLog, PropertyIdentifier.clientCovIncrement, ClientCov.class, false, false);
        add(ObjectType.trendLog, PropertyIdentifier.stopWhenFull, Boolean.class, false, true);
        add(ObjectType.trendLog, PropertyIdentifier.bufferSize, UnsignedInteger.class, false, true);
        add(ObjectType.trendLog, PropertyIdentifier.logBuffer, LogRecord.class, true, true);
        add(ObjectType.trendLog, PropertyIdentifier.recordCount, UnsignedInteger.class, false, true);
        add(ObjectType.trendLog, PropertyIdentifier.totalRecordCount, UnsignedInteger.class, false, true);
        add(ObjectType.trendLog, PropertyIdentifier.loggingType, LoggingType.class, false, true);
        add(ObjectType.trendLog, PropertyIdentifier.alignIntervals, Boolean.class, false, true);
        add(ObjectType.trendLog, PropertyIdentifier.intervalOffset, UnsignedInteger.class, false, true);
        add(ObjectType.trendLog, PropertyIdentifier.trigger, Boolean.class, false, true);
        add(ObjectType.trendLog, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.trendLog, PropertyIdentifier.reliability, Reliability.class, false, true);
        add(ObjectType.trendLog, PropertyIdentifier.notificationThreshold, UnsignedInteger.class, false, false);
        add(ObjectType.trendLog, PropertyIdentifier.recordsSinceNotification, UnsignedInteger.class, false, false);
        add(ObjectType.trendLog, PropertyIdentifier.lastNotifyRecord, UnsignedInteger.class, false, false);
        add(ObjectType.trendLog, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.trendLog, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.trendLog, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.trendLog, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.trendLog, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.trendLog, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.trendLog, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.trendLog, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true, false);
        add(ObjectType.trendLog, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.trendLog, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class, false,
                false);
        add(ObjectType.trendLog, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.trendLog, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.trendLog, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);
        add(ObjectType.trendLog, PropertyIdentifier.profileName, CharacterString.class, false, false);

        // Trend log multiple - 12.30
        add(ObjectType.trendLogMultiple, PropertyIdentifier.objectIdentifier, ObjectIdentifier.class, false, true);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.objectName, CharacterString.class, false, true);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.objectType, ObjectType.class, false, true);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.description, CharacterString.class, false, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.statusFlags, StatusFlags.class, false, true);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.eventState, EventState.class, false, true);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.reliability, Reliability.class, false, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.enable, Boolean.class, false, true);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.startTime, DateTime.class, false, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.stopTime, DateTime.class, false, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.logDeviceObjectProperty,
                DeviceObjectPropertyReference.class, true, true);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.loggingType, LoggingType.class, false, true);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.logInterval, UnsignedInteger.class, false, true);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.alignIntervals, Boolean.class, false, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.intervalOffset, UnsignedInteger.class, false, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.trigger, Boolean.class, false, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.stopWhenFull, Boolean.class, false, true);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.bufferSize, UnsignedInteger.class, false, true);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.logBuffer, LogMultipleRecord.class, true, true);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.recordCount, UnsignedInteger.class, false, true);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.totalRecordCount, UnsignedInteger.class, false, true);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.notificationThreshold, UnsignedInteger.class, false, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.recordsSinceNotification, UnsignedInteger.class, false,
                false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.lastNotifyRecord, UnsignedInteger.class, false, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.notificationClass, UnsignedInteger.class, false, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.eventEnable, EventTransitionBits.class, false, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.ackedTransitions, EventTransitionBits.class, false, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.notifyType, NotifyType.class, false, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.eventTimeStamps, TimeStamp.class, true, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.eventMessageTexts, CharacterString.class, true, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.eventMessageTextsConfig, CharacterString.class, true,
                false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.eventDetectionEnable, Boolean.class, false, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.eventAlgorithmInhibitRef, ObjectPropertyReference.class,
                false, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.eventAlgorithmInhibit, Boolean.class, false, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.reliabilityEvaluationInhibit, Boolean.class, false, false);
        add(ObjectType.trendLogMultiple, PropertyIdentifier.propertyList, PropertyIdentifier.class, true, true);

        add(ObjectType.trendLogMultiple, PropertyIdentifier.profileName, CharacterString.class, false, false);
    }
}
