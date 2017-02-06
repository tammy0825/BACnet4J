/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2009 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 */
package com.serotonin.bacnet4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.serotonin.bacnet4j.apdu.APDU;
import com.serotonin.bacnet4j.apdu.ComplexACK;
import com.serotonin.bacnet4j.apdu.ConfirmedRequest;
import com.serotonin.bacnet4j.apdu.Segmentable;
import com.serotonin.bacnet4j.apdu.SimpleACK;
import com.serotonin.bacnet4j.apdu.UnconfirmedRequest;
import com.serotonin.bacnet4j.enums.DayOfWeek;
import com.serotonin.bacnet4j.enums.MaxApduLength;
import com.serotonin.bacnet4j.enums.MaxSegments;
import com.serotonin.bacnet4j.enums.Month;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.VendorServiceKey;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.acknowledgement.AtomicReadFileAck;
import com.serotonin.bacnet4j.service.acknowledgement.AtomicWriteFileAck;
import com.serotonin.bacnet4j.service.acknowledgement.ConfirmedPrivateTransferAck;
import com.serotonin.bacnet4j.service.acknowledgement.CreateObjectAck;
import com.serotonin.bacnet4j.service.acknowledgement.GetAlarmSummaryAck;
import com.serotonin.bacnet4j.service.acknowledgement.GetEnrollmentSummaryAck;
import com.serotonin.bacnet4j.service.acknowledgement.GetEventInformationAck;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyMultipleAck;
import com.serotonin.bacnet4j.service.acknowledgement.ReadRangeAck;
import com.serotonin.bacnet4j.service.acknowledgement.VtDataAck;
import com.serotonin.bacnet4j.service.acknowledgement.VtOpenAck;
import com.serotonin.bacnet4j.service.confirmed.AcknowledgeAlarmRequest;
import com.serotonin.bacnet4j.service.confirmed.AddListElementRequest;
import com.serotonin.bacnet4j.service.confirmed.AtomicReadFileRequest;
import com.serotonin.bacnet4j.service.confirmed.AtomicWriteFileRequest;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedCovNotificationRequest;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedEventNotificationRequest;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedPrivateTransferRequest;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedTextMessageRequest;
import com.serotonin.bacnet4j.service.confirmed.CreateObjectRequest;
import com.serotonin.bacnet4j.service.confirmed.DeleteObjectRequest;
import com.serotonin.bacnet4j.service.confirmed.DeviceCommunicationControlRequest;
import com.serotonin.bacnet4j.service.confirmed.DeviceCommunicationControlRequest.EnableDisable;
import com.serotonin.bacnet4j.service.confirmed.GetAlarmSummaryRequest;
import com.serotonin.bacnet4j.service.confirmed.GetEnrollmentSummaryRequest;
import com.serotonin.bacnet4j.service.confirmed.GetEnrollmentSummaryRequest.PriorityFilter;
import com.serotonin.bacnet4j.service.confirmed.GetEventInformationRequest;
import com.serotonin.bacnet4j.service.confirmed.LifeSafetyOperationRequest;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyMultipleRequest;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.confirmed.ReinitializeDeviceRequest;
import com.serotonin.bacnet4j.service.confirmed.ReinitializeDeviceRequest.ReinitializedStateOfDevice;
import com.serotonin.bacnet4j.service.confirmed.RemoveListElementRequest;
import com.serotonin.bacnet4j.service.confirmed.SubscribeCOVPropertyRequest;
import com.serotonin.bacnet4j.service.confirmed.SubscribeCOVRequest;
import com.serotonin.bacnet4j.service.confirmed.VtCloseRequest;
import com.serotonin.bacnet4j.service.confirmed.VtDataRequest;
import com.serotonin.bacnet4j.service.confirmed.VtOpenRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyMultipleRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.IAmRequest;
import com.serotonin.bacnet4j.service.unconfirmed.IHaveRequest;
import com.serotonin.bacnet4j.service.unconfirmed.TimeSynchronizationRequest;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedCovNotificationRequest;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedEventNotificationRequest;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedPrivateTransferRequest;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedRequestService;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedTextMessageRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoHasRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.SequenceDefinition;
import com.serotonin.bacnet4j.type.SequenceDefinition.ElementSpecification;
import com.serotonin.bacnet4j.type.constructed.BACnetArray;
import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.constructed.BaseType;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.constructed.EventTransitionBits;
import com.serotonin.bacnet4j.type.constructed.LogRecord;
import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult.Result;
import com.serotonin.bacnet4j.type.constructed.ReadAccessSpecification;
import com.serotonin.bacnet4j.type.constructed.Recipient;
import com.serotonin.bacnet4j.type.constructed.RecipientProcess;
import com.serotonin.bacnet4j.type.constructed.ResultFlags;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.constructed.WriteAccessSpecification;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.EventType;
import com.serotonin.bacnet4j.type.enumerated.FileAccessMethod;
import com.serotonin.bacnet4j.type.enumerated.LifeSafetyOperation;
import com.serotonin.bacnet4j.type.enumerated.MessagePriority;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Reliability;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.enumerated.VtClass;
import com.serotonin.bacnet4j.type.error.BaseError;
import com.serotonin.bacnet4j.type.notificationParameters.OutOfRange;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Date;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.bacnet4j.type.primitive.Time;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

public class AnnexFEncodingTest {
    //    public static void main(String[] args) {
    //        new AnnexFEncodingTest().executeAll();
    //    }

    private ServicesSupported servicesSupported;

    @Before
    public void before() {
        servicesSupported = new ServicesSupported();
        servicesSupported.setAll(true);
    }

    @Test
    public void e1_1aTest() {
        final AcknowledgeAlarmRequest acknowledgeAlarmRequest = new AcknowledgeAlarmRequest(new UnsignedInteger(1),
                new ObjectIdentifier(ObjectType.analogInput, 2), EventState.highLimit,
                new TimeStamp(new UnsignedInteger(16)), new CharacterString(CharacterString.Encodings.ANSI_X3_4, "MDL"),
                new TimeStamp(
                        new DateTime(new Date(1992, Month.JUNE, 21, DayOfWeek.UNSPECIFIED), new Time(13, 3, 41, 9))));

        final ConfirmedRequest pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 7, (byte) 0, 0, acknowledgeAlarmRequest);

        final byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x07, (byte) 0x00, (byte) 0x09, (byte) 0x01,
                (byte) 0x1c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x29, (byte) 0x03, (byte) 0x3e,
                (byte) 0x19, (byte) 0x10, (byte) 0x3f, (byte) 0x4c, (byte) 0x00, (byte) 0x4d, (byte) 0x44, (byte) 0x4c,
                (byte) 0x5e, (byte) 0x2e, (byte) 0xa4, (byte) 0x5c, (byte) 0x06, (byte) 0x15, (byte) 0xff, (byte) 0xb4,
                (byte) 0x0d, (byte) 0x03, (byte) 0x29, (byte) 0x09, (byte) 0x2f, (byte) 0x5f };

        compare(pdu, expectedResult);
    }

    @Test
    public void e1_1bTest() {
        final SimpleACK simpleACK = new SimpleACK((byte) 7, 0);
        final byte[] expectedResult = { (byte) 0x20, (byte) 0x07, (byte) 0x00 };
        compare(simpleACK, expectedResult);
    }

    @Test
    public void e1_2aTest() {
        final List<PropertyValue> list = new ArrayList<>();
        list.add(new PropertyValue(PropertyIdentifier.presentValue, null, new Real(65), null));
        list.add(new PropertyValue(PropertyIdentifier.statusFlags, null, new StatusFlags(false, false, false, false),
                null));

        final SequenceOf<PropertyValue> listOfValues = new SequenceOf<>(list);

        final ConfirmedCovNotificationRequest confirmedCovNotification = new ConfirmedCovNotificationRequest(
                new UnsignedInteger(18), new ObjectIdentifier(ObjectType.device, 4),
                new ObjectIdentifier(ObjectType.analogInput, 10), new UnsignedInteger(0), listOfValues);

        final ConfirmedRequest pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 15, (byte) 0, 0, confirmedCovNotification);

        final byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x0f, (byte) 0x01, (byte) 0x09, (byte) 0x12,
                (byte) 0x1c, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x2c, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x0a, (byte) 0x39, (byte) 0x00, (byte) 0x4e, (byte) 0x09, (byte) 0x55, (byte) 0x2e,
                (byte) 0x44, (byte) 0x42, (byte) 0x82, (byte) 0x00, (byte) 0x00, (byte) 0x2f, (byte) 0x09, (byte) 0x6f,
                (byte) 0x2e, (byte) 0x82, (byte) 0x04, (byte) 0x00, (byte) 0x2f, (byte) 0x4f };

        compare(pdu, expectedResult);
    }

    @Test
    public void e1_2bTest() {
        final SimpleACK pdu = new SimpleACK((byte) 15, 1);
        final byte[] expectedResult = { (byte) 0x20, (byte) 0x0f, (byte) 0x01 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e1_3Test() {
        final List<PropertyValue> list = new ArrayList<>();
        list.add(new PropertyValue(PropertyIdentifier.presentValue, null, new Real(65), null));
        list.add(new PropertyValue(PropertyIdentifier.statusFlags, null, new StatusFlags(false, false, false, false),
                null));

        final SequenceOf<PropertyValue> listOfValues = new SequenceOf<>(list);

        final UnconfirmedCovNotificationRequest unconfirmedCovNotificationRequest = new UnconfirmedCovNotificationRequest(
                new UnsignedInteger(18), new ObjectIdentifier(ObjectType.device, 4),
                new ObjectIdentifier(ObjectType.analogInput, 10), new UnsignedInteger(0), listOfValues);

        final UnconfirmedRequest pdu = new UnconfirmedRequest(unconfirmedCovNotificationRequest);

        final byte[] expectedResult = { (byte) 0x10, (byte) 0x02, (byte) 0x09, (byte) 0x12, (byte) 0x1c, (byte) 0x02,
                (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x2c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0a,
                (byte) 0x39, (byte) 0x00, (byte) 0x4e, (byte) 0x09, (byte) 0x55, (byte) 0x2e, (byte) 0x44, (byte) 0x42,
                (byte) 0x82, (byte) 0x00, (byte) 0x00, (byte) 0x2f, (byte) 0x09, (byte) 0x6f, (byte) 0x2e, (byte) 0x82,
                (byte) 0x04, (byte) 0x00, (byte) 0x2f, (byte) 0x4f };

        compare(pdu, expectedResult);
    }

    @Test
    public void e1_4aTest() {
        final ConfirmedEventNotificationRequest confirmedEventNotificationRequest = new ConfirmedEventNotificationRequest(
                new UnsignedInteger(1), new ObjectIdentifier(ObjectType.device, 4),
                new ObjectIdentifier(ObjectType.analogInput, 2), new TimeStamp(new UnsignedInteger(16)),
                new UnsignedInteger(4), new UnsignedInteger(100), EventType.outOfRange, null, NotifyType.alarm,
                new com.serotonin.bacnet4j.type.primitive.Boolean(true), EventState.normal, EventState.highLimit,
                new OutOfRange(new Real(80.1f), new StatusFlags(true, false, false, false), new Real(1), new Real(80)));

        final ConfirmedRequest pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 16, (byte) 0, 0, confirmedEventNotificationRequest);

        final byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x10, (byte) 0x02, (byte) 0x09, (byte) 0x01,
                (byte) 0x1c, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x2c, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x02, (byte) 0x3e, (byte) 0x19, (byte) 0x10, (byte) 0x3f, (byte) 0x49, (byte) 0x04,
                (byte) 0x59, (byte) 0x64, (byte) 0x69, (byte) 0x05, (byte) 0x89, (byte) 0x00, (byte) 0x99, (byte) 0x01,
                (byte) 0xa9, (byte) 0x00, (byte) 0xb9, (byte) 0x03, (byte) 0xce, (byte) 0x5e, (byte) 0x0c, (byte) 0x42,
                (byte) 0xa0, (byte) 0x33, (byte) 0x33, (byte) 0x1a, (byte) 0x04, (byte) 0x80, (byte) 0x2c, (byte) 0x3f,
                (byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x3c, (byte) 0x42, (byte) 0xa0, (byte) 0x00, (byte) 0x00,
                (byte) 0x5f, (byte) 0xcf };

        compare(pdu, expectedResult);
    }

    @Test
    public void e1_4bTest() {
        final SimpleACK pdu = new SimpleACK((byte) 16, 2);
        final byte[] expectedResult = { (byte) 0x20, (byte) 0x10, (byte) 0x02 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e1_5Test() {
        final UnconfirmedEventNotificationRequest unconfirmedEventNotificationRequest = new UnconfirmedEventNotificationRequest(
                new UnsignedInteger(1), new ObjectIdentifier(ObjectType.device, 9),
                new ObjectIdentifier(ObjectType.analogInput, 2), new TimeStamp(new UnsignedInteger(16)),
                new UnsignedInteger(4), new UnsignedInteger(100), EventType.outOfRange, null, NotifyType.alarm,
                new com.serotonin.bacnet4j.type.primitive.Boolean(true), EventState.normal, EventState.highLimit,
                new OutOfRange(new Real(80.1f), new StatusFlags(true, false, false, false), new Real(1), new Real(80)));

        final UnconfirmedRequest pdu = new UnconfirmedRequest(unconfirmedEventNotificationRequest);

        final byte[] expectedResult = { (byte) 0x10, (byte) 0x03, (byte) 0x09, (byte) 0x01, (byte) 0x1c, (byte) 0x02,
                (byte) 0x00, (byte) 0x00, (byte) 0x09, (byte) 0x2c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02,
                (byte) 0x3e, (byte) 0x19, (byte) 0x10, (byte) 0x3f, (byte) 0x49, (byte) 0x04, (byte) 0x59, (byte) 0x64,
                (byte) 0x69, (byte) 0x05, (byte) 0x89, (byte) 0x00, (byte) 0x99, (byte) 0x01, (byte) 0xa9, (byte) 0x00,
                (byte) 0xb9, (byte) 0x03, (byte) 0xce, (byte) 0x5e, (byte) 0x0c, (byte) 0x42, (byte) 0xa0, (byte) 0x33,
                (byte) 0x33, (byte) 0x1a, (byte) 0x04, (byte) 0x80, (byte) 0x2c, (byte) 0x3f, (byte) 0x80, (byte) 0x00,
                (byte) 0x00, (byte) 0x3c, (byte) 0x42, (byte) 0xa0, (byte) 0x00, (byte) 0x00, (byte) 0x5f,
                (byte) 0xcf };

        compare(pdu, expectedResult);
    }

    @Test
    public void e1_6aTest() {
        final GetAlarmSummaryRequest getAlarmSummaryRequest = new GetAlarmSummaryRequest();

        final ConfirmedRequest pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 1, (byte) 0, 0, getAlarmSummaryRequest);

        final byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0x03 };

        compare(pdu, expectedResult);
    }

    @Test
    public void e1_6bTest() {
        final List<GetAlarmSummaryAck.AlarmSummary> alarmSummaries = new ArrayList<>();
        alarmSummaries.add(new GetAlarmSummaryAck.AlarmSummary(new ObjectIdentifier(ObjectType.analogInput, 2),
                EventState.highLimit, new EventTransitionBits(false, true, true)));
        alarmSummaries.add(new GetAlarmSummaryAck.AlarmSummary(new ObjectIdentifier(ObjectType.analogInput, 3),
                EventState.lowLimit, new EventTransitionBits(true, true, true)));

        final GetAlarmSummaryAck getAlarmSummaryAck = new GetAlarmSummaryAck(
                new SequenceOf<>(alarmSummaries));

        final ComplexACK pdu = new ComplexACK(false, false, (byte) 1, 0, 0, getAlarmSummaryAck);

        final byte[] expectedResult = { (byte) 0x30, (byte) 0x01, (byte) 0x03, (byte) 0xc4, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x02, (byte) 0x91, (byte) 0x03, (byte) 0x82, (byte) 0x05, (byte) 0x60, (byte) 0xc4,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x91, (byte) 0x04, (byte) 0x82, (byte) 0x05,
                (byte) 0xe0 };

        compare(pdu, expectedResult);
    }

    @Test
    public void e1_7aTest() {
        final GetEnrollmentSummaryRequest getEnrollmentSummaryRequest = new GetEnrollmentSummaryRequest(
                GetEnrollmentSummaryRequest.AcknowledgmentFilter.notAcked, null, null, null, null, null);

        final ConfirmedRequest pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 1, (byte) 0, 0, getEnrollmentSummaryRequest);

        final byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0x04, (byte) 0x09, (byte) 0x02 };

        compare(pdu, expectedResult);
    }

    @Test
    public void e1_7bTest() {
        final List<GetEnrollmentSummaryAck.EnrollmentSummary> enrollmentSummaries = new ArrayList<>();
        enrollmentSummaries
                .add(new GetEnrollmentSummaryAck.EnrollmentSummary(new ObjectIdentifier(ObjectType.analogInput, 2),
                        EventType.outOfRange, EventState.highLimit, new UnsignedInteger(100), new UnsignedInteger(4)));
        enrollmentSummaries
                .add(new GetEnrollmentSummaryAck.EnrollmentSummary(new ObjectIdentifier(ObjectType.eventEnrollment, 6),
                        EventType.changeOfState, EventState.normal, new UnsignedInteger(50), new UnsignedInteger(2)));

        final GetEnrollmentSummaryAck getEnrollmentSummaryAck = new GetEnrollmentSummaryAck(
                new SequenceOf<>(enrollmentSummaries));

        final ComplexACK pdu = new ComplexACK(false, false, (byte) 1, 0, 0, getEnrollmentSummaryAck);

        final byte[] expectedResult = { (byte) 0x30, (byte) 0x01, (byte) 0x04, (byte) 0xc4, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x02, (byte) 0x91, (byte) 0x05, (byte) 0x91, (byte) 0x03, (byte) 0x21, (byte) 0x64,
                (byte) 0x21, (byte) 0x04, (byte) 0xc4, (byte) 0x02, (byte) 0x40, (byte) 0x00, (byte) 0x06, (byte) 0x91,
                (byte) 0x01, (byte) 0x91, (byte) 0x00, (byte) 0x21, (byte) 0x32, (byte) 0x21, (byte) 0x02 };

        compare(pdu, expectedResult);
    }

    @Test
    public void e1_7cTest() {
        final GetEnrollmentSummaryRequest getEnrollmentSummaryRequest = new GetEnrollmentSummaryRequest(
                GetEnrollmentSummaryRequest.AcknowledgmentFilter.all,
                new RecipientProcess(new Recipient(new ObjectIdentifier(ObjectType.device, 17)),
                        new UnsignedInteger(9)),
                null, null, new PriorityFilter(new UnsignedInteger(6), new UnsignedInteger(10)), null);

        final ConfirmedRequest pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 2, (byte) 0, 0, getEnrollmentSummaryRequest);

        final byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x02, (byte) 0x04, (byte) 0x09, (byte) 0x00,
                (byte) 0x1e, (byte) 0x0e, (byte) 0x0c, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x11, (byte) 0x0f,
                (byte) 0x19, (byte) 0x09, (byte) 0x1f, (byte) 0x4e, (byte) 0x09, (byte) 0x06, (byte) 0x19, (byte) 0x0a,
                (byte) 0x4f };

        compare(pdu, expectedResult);
    }

    @Test
    public void e1_7dTest() {
        final List<GetEnrollmentSummaryAck.EnrollmentSummary> enrollmentSummaries = new ArrayList<>();
        enrollmentSummaries
                .add(new GetEnrollmentSummaryAck.EnrollmentSummary(new ObjectIdentifier(ObjectType.analogInput, 2),
                        EventType.outOfRange, EventState.normal, new UnsignedInteger(8), new UnsignedInteger(4)));
        enrollmentSummaries
                .add(new GetEnrollmentSummaryAck.EnrollmentSummary(new ObjectIdentifier(ObjectType.analogInput, 3),
                        EventType.outOfRange, EventState.normal, new UnsignedInteger(8), new UnsignedInteger(4)));
        enrollmentSummaries
                .add(new GetEnrollmentSummaryAck.EnrollmentSummary(new ObjectIdentifier(ObjectType.analogInput, 4),
                        EventType.outOfRange, EventState.normal, new UnsignedInteger(8), new UnsignedInteger(4)));
        enrollmentSummaries
                .add(new GetEnrollmentSummaryAck.EnrollmentSummary(new ObjectIdentifier(ObjectType.eventEnrollment, 7),
                        EventType.floatingLimit, EventState.normal, new UnsignedInteger(3), new UnsignedInteger(8)));

        final GetEnrollmentSummaryAck getEnrollmentSummaryAck = new GetEnrollmentSummaryAck(
                new SequenceOf<>(enrollmentSummaries));

        final ComplexACK pdu = new ComplexACK(false, false, (byte) 2, 0, 0, getEnrollmentSummaryAck);

        final byte[] expectedResult = { (byte) 0x30, (byte) 0x02, (byte) 0x04, (byte) 0xc4, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x02, (byte) 0x91, (byte) 0x05, (byte) 0x91, (byte) 0x00, (byte) 0x21, (byte) 0x08,
                (byte) 0x21, (byte) 0x04, (byte) 0xc4, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x91,
                (byte) 0x05, (byte) 0x91, (byte) 0x00, (byte) 0x21, (byte) 0x08, (byte) 0x21, (byte) 0x04, (byte) 0xc4,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x91, (byte) 0x05, (byte) 0x91, (byte) 0x00,
                (byte) 0x21, (byte) 0x08, (byte) 0x21, (byte) 0x04, (byte) 0xc4, (byte) 0x02, (byte) 0x40, (byte) 0x00,
                (byte) 0x07, (byte) 0x91, (byte) 0x04, (byte) 0x91, (byte) 0x00, (byte) 0x21, (byte) 0x03, (byte) 0x21,
                (byte) 0x08 };

        compare(pdu, expectedResult);
    }

    @Test
    public void e1_8aTest() {
        final GetEventInformationRequest getEventInformation = new GetEventInformationRequest(null);

        final ConfirmedRequest pdu = new ConfirmedRequest(false, false, true, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 1, (byte) 0, 0, getEventInformation);

        final byte[] expectedResult = { (byte) 0x02, (byte) 0x02, (byte) 0x01, (byte) 0x1d };

        compare(pdu, expectedResult);
    }

    @Test
    public void e1_8bTest() {
        final List<GetEventInformationAck.EventSummary> eventSummaries = new ArrayList<>();
        eventSummaries.add(new GetEventInformationAck.EventSummary( //
                new ObjectIdentifier(ObjectType.analogInput, 2), //
                EventState.highLimit, //
                new EventTransitionBits(false, true, true), //
                new BACnetArray<>(new TimeStamp(new Time(15, 35, 0, 20)),
                        new TimeStamp(new Time(255, 255, 255, 255)), new TimeStamp(new Time(255, 255, 255, 255))), //
                NotifyType.alarm, //
                new EventTransitionBits(true, true, true), //
                new BACnetArray<>(new UnsignedInteger(15), new UnsignedInteger(15),
                        new UnsignedInteger(20))));
        eventSummaries.add(new GetEventInformationAck.EventSummary( //
                new ObjectIdentifier(ObjectType.analogInput, 3), //
                EventState.normal, //
                new EventTransitionBits(true, true, false), //
                new BACnetArray<>(new TimeStamp(new Time(15, 40, 0, 0)),
                        new TimeStamp(new Time(255, 255, 255, 255)), new TimeStamp(new Time(15, 45, 30, 30))), //
                NotifyType.alarm, //
                new EventTransitionBits(true, true, true), new BACnetArray<>(new UnsignedInteger(15),
                        new UnsignedInteger(15), new UnsignedInteger(20))));

        final GetEventInformationAck getEventInformationAck = new GetEventInformationAck(
                new SequenceOf<>(eventSummaries), new Boolean(false));

        final ComplexACK pdu = new ComplexACK(false, false, (byte) 1, 0, 0, getEventInformationAck);

        final byte[] expectedResult = { (byte) 0x30, (byte) 0x01, (byte) 0x1d, (byte) 0x0e, (byte) 0x0c, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x19, (byte) 0x03, (byte) 0x2a, (byte) 0x05, (byte) 0x60,
                (byte) 0x3e, (byte) 0x0c, (byte) 0x0f, (byte) 0x23, (byte) 0x00, (byte) 0x14, (byte) 0x0c, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x0c, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0x3f, (byte) 0x49, (byte) 0x00, (byte) 0x5a, (byte) 0x05, (byte) 0xe0, (byte) 0x6e, (byte) 0x21,
                (byte) 0x0f, (byte) 0x21, (byte) 0x0f, (byte) 0x21, (byte) 0x14, (byte) 0x6f, (byte) 0x0c, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x19, (byte) 0x00, (byte) 0x2a, (byte) 0x05, (byte) 0xc0,
                (byte) 0x3e, (byte) 0x0c, (byte) 0x0f, (byte) 0x28, (byte) 0x00, (byte) 0x00, (byte) 0x0c, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x0c, (byte) 0x0f, (byte) 0x2d, (byte) 0x1e, (byte) 0x1e,
                (byte) 0x3f, (byte) 0x49, (byte) 0x00, (byte) 0x5a, (byte) 0x05, (byte) 0xe0, (byte) 0x6e, (byte) 0x21,
                (byte) 0x0f, (byte) 0x21, (byte) 0x0f, (byte) 0x21, (byte) 0x14, (byte) 0x6f, (byte) 0x0f, (byte) 0x19,
                (byte) 0x00 };

        compare(pdu, expectedResult);
    }

    @Test
    public void e1_9aTest() {
        final LifeSafetyOperationRequest lifeSafetyOperationRequest = new LifeSafetyOperationRequest(
                new UnsignedInteger(18), new CharacterString(CharacterString.Encodings.ANSI_X3_4, "MDL"),
                LifeSafetyOperation.reset, new ObjectIdentifier(ObjectType.lifeSafetyPoint, 1));

        final ConfirmedRequest pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 15, (byte) 0, 0, lifeSafetyOperationRequest);

        final byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x0f, (byte) 0x1b, (byte) 0x09, (byte) 0x12,
                (byte) 0x1c, (byte) 0x00, (byte) 0x4d, (byte) 0x44, (byte) 0x4c, (byte) 0x29, (byte) 0x04, (byte) 0x3c,
                (byte) 0x05, (byte) 0x40, (byte) 0x00, (byte) 0x01 };

        compare(pdu, expectedResult);
    }

    @Test
    public void e1_9bTest() {
        final SimpleACK pdu = new SimpleACK((byte) 15, 27);
        final byte[] expectedResult = { (byte) 0x20, (byte) 0x0f, (byte) 0x1b };
        compare(pdu, expectedResult);
    }

    @Test
    public void e1_10aTest() {
        final SubscribeCOVRequest subscribeCOVRequest = new SubscribeCOVRequest(new UnsignedInteger(18),
                new ObjectIdentifier(ObjectType.analogInput, 10), new Boolean(true), new UnsignedInteger(0));

        final ConfirmedRequest pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 15, (byte) 0, 0, subscribeCOVRequest);

        final byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x0f, (byte) 0x05, (byte) 0x09, (byte) 0x12,
                (byte) 0x1c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0a, (byte) 0x29, (byte) 0x01, (byte) 0x39,
                (byte) 0x00 };

        compare(pdu, expectedResult);
    }

    @Test
    public void e1_10bTest() {
        final SimpleACK pdu = new SimpleACK((byte) 15, 5);
        final byte[] expectedResult = { (byte) 0x20, (byte) 0x0f, (byte) 0x05 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e1_11aTest() {
        final SubscribeCOVPropertyRequest subscribeCOVPropertyRequest = new SubscribeCOVPropertyRequest(
                new UnsignedInteger(18), new ObjectIdentifier(ObjectType.analogInput, 10), new Boolean(true),
                new UnsignedInteger(60), new PropertyReference(PropertyIdentifier.presentValue, null), new Real(1));

        final ConfirmedRequest pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED,
                MaxApduLength.UP_TO_206, (byte) 15, (byte) 0, 0, subscribeCOVPropertyRequest);

        final byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x0f, (byte) 0x1c, (byte) 0x09, (byte) 0x12,
                (byte) 0x1c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0a, (byte) 0x29, (byte) 0x01, (byte) 0x39,
                (byte) 0x3c, (byte) 0x4e, (byte) 0x09, (byte) 0x55, (byte) 0x4f, (byte) 0x5c, (byte) 0x3f, (byte) 0x80,
                (byte) 0x00, (byte) 0x00 };

        compare(pdu, expectedResult);
    }

    @Test
    public void e1_11bTest() {
        final SimpleACK pdu = new SimpleACK((byte) 15, 28);
        final byte[] expectedResult = { (byte) 0x20, (byte) 0x0f, (byte) 0x1c };
        compare(pdu, expectedResult);
    }

    @Test
    public void e2_1aTest() {
        final ConfirmedRequestService service = new AtomicReadFileRequest(new ObjectIdentifier(ObjectType.file, 1),
                false, new SignedInteger(0), new UnsignedInteger(27));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_206,
                (byte) 0, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x06, (byte) 0xC4, (byte) 0x02,
                (byte) 0x80, (byte) 0x00, (byte) 0x01, (byte) 0x0E, (byte) 0x31, (byte) 0x00, (byte) 0x21, (byte) 0x1B,
                (byte) 0x0F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e2_1bTest() {
        final AcknowledgementService service = new AtomicReadFileAck(new Boolean(false), new SignedInteger(0),
                new OctetString("Chiller01 On-Time=4.3 Hours".getBytes()));
        final APDU pdu = new ComplexACK(false, false, (byte) 0, 0, 0, service);
        final byte[] expectedResult = { (byte) 0x30, (byte) 0x00, (byte) 0x06, (byte) 0x10, (byte) 0x0E, (byte) 0x31,
                (byte) 0x00, (byte) 0x65, (byte) 0x1B, (byte) 0x43, (byte) 0x68, (byte) 0x69, (byte) 0x6C, (byte) 0x6C,
                (byte) 0x65, (byte) 0x72, (byte) 0x30, (byte) 0x31, (byte) 0x20, (byte) 0x4F, (byte) 0x6E, (byte) 0x2D,
                (byte) 0x54, (byte) 0x69, (byte) 0x6D, (byte) 0x65, (byte) 0x3D, (byte) 0x34, (byte) 0x2E, (byte) 0x33,
                (byte) 0x20, (byte) 0x48, (byte) 0x6F, (byte) 0x75, (byte) 0x72, (byte) 0x73, (byte) 0x0F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e2_1cTest() {
        final ConfirmedRequestService service = new AtomicReadFileRequest(new ObjectIdentifier(ObjectType.file, 2),
                true, new SignedInteger(14), new UnsignedInteger(3));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_206,
                (byte) 18, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x12, (byte) 0x06, (byte) 0xC4, (byte) 0x02,
                (byte) 0x80, (byte) 0x00, (byte) 0x02, (byte) 0x1E, (byte) 0x31, (byte) 0x0E, (byte) 0x21, (byte) 0x03,
                (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e2_1dTest() {
        final List<OctetString> strings = new ArrayList<>();
        strings.add(new OctetString("12:00,45.6".getBytes()));
        strings.add(new OctetString("12:15,44.8".getBytes()));
        final AcknowledgementService service = new AtomicReadFileAck(new Boolean(true), new SignedInteger(14),
                new UnsignedInteger(2), new SequenceOf<>(strings));
        final APDU pdu = new ComplexACK(false, false, (byte) 18, 0, 0, service);
        final byte[] expectedResult = { (byte) 0x30, (byte) 0x12, (byte) 0x06, (byte) 0x11, (byte) 0x1E, (byte) 0x31,
                (byte) 0x0E, (byte) 0x21, (byte) 0x02, (byte) 0x65, (byte) 0x0A, (byte) 0x31, (byte) 0x32, (byte) 0x3A,
                (byte) 0x30, (byte) 0x30, (byte) 0x2C, (byte) 0x34, (byte) 0x35, (byte) 0x2E, (byte) 0x36, (byte) 0x65,
                (byte) 0x0A, (byte) 0x31, (byte) 0x32, (byte) 0x3A, (byte) 0x31, (byte) 0x35, (byte) 0x2C, (byte) 0x34,
                (byte) 0x34, (byte) 0x2E, (byte) 0x38, (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e2_2aTest() {
        final ConfirmedRequestService service = new AtomicWriteFileRequest(new ObjectIdentifier(ObjectType.file, 1),
                new SignedInteger(30), new OctetString("Chiller01 On-Time=4.3 Hours".getBytes()));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_206,
                (byte) 85, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x55, (byte) 0x07, (byte) 0xC4, (byte) 0x02,
                (byte) 0x80, (byte) 0x00, (byte) 0x01, (byte) 0x0E, (byte) 0x31, (byte) 0x1E, (byte) 0x65, (byte) 0x1B,
                (byte) 0x43, (byte) 0x68, (byte) 0x69, (byte) 0x6C, (byte) 0x6C, (byte) 0x65, (byte) 0x72, (byte) 0x30,
                (byte) 0x31, (byte) 0x20, (byte) 0x4F, (byte) 0x6E, (byte) 0x2D, (byte) 0x54, (byte) 0x69, (byte) 0x6D,
                (byte) 0x65, (byte) 0x3D, (byte) 0x34, (byte) 0x2E, (byte) 0x33, (byte) 0x20, (byte) 0x48, (byte) 0x6F,
                (byte) 0x75, (byte) 0x72, (byte) 0x73, (byte) 0x0F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e2_2bTest() {
        final AcknowledgementService service = new AtomicWriteFileAck(false, new SignedInteger(30));
        final APDU pdu = new ComplexACK(false, false, (byte) 85, 0, 0, service);
        final byte[] expectedResult = { (byte) 0x30, (byte) 0x55, (byte) 0x07, (byte) 0x09, (byte) 0x1E };
        compare(pdu, expectedResult);
    }

    @Test
    public void e2_2cTest() {
        final List<OctetString> strings = new ArrayList<>();
        strings.add(new OctetString("12:00,45.6".getBytes()));
        strings.add(new OctetString("12:15,44.8".getBytes()));
        final ConfirmedRequestService service = new AtomicWriteFileRequest(new ObjectIdentifier(ObjectType.file, 2),
                new SignedInteger(-1), new UnsignedInteger(2), new SequenceOf<>(strings));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_206,
                (byte) 85, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x55, (byte) 0x07, (byte) 0xC4, (byte) 0x02,
                (byte) 0x80, (byte) 0x00, (byte) 0x02, (byte) 0x1E, (byte) 0x31, (byte) 0xFF, (byte) 0x21, (byte) 0x02,
                (byte) 0x65, (byte) 0x0A, (byte) 0x31, (byte) 0x32, (byte) 0x3A, (byte) 0x30, (byte) 0x30, (byte) 0x2C,
                (byte) 0x34, (byte) 0x35, (byte) 0x2E, (byte) 0x36, (byte) 0x65, (byte) 0x0A, (byte) 0x31, (byte) 0x32,
                (byte) 0x3A, (byte) 0x31, (byte) 0x35, (byte) 0x2C, (byte) 0x34, (byte) 0x34, (byte) 0x2E, (byte) 0x38,
                (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e2_2dTest() {
        final AcknowledgementService service = new AtomicWriteFileAck(true, new SignedInteger(14));
        final APDU pdu = new ComplexACK(false, false, (byte) 85, 0, 0, service);
        final byte[] expectedResult = { (byte) 0x30, (byte) 0x55, (byte) 0x07, (byte) 0x19, (byte) 0x0E };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_1aTest() {
        final List<PropertyReference> propertyReferences = new ArrayList<>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        propertyReferences.add(new PropertyReference(PropertyIdentifier.reliability, null));

        final List<ReadAccessSpecification> elements = new ArrayList<>();
        elements.add(new ReadAccessSpecification(new ObjectIdentifier(ObjectType.analogInput, 15),
                new SequenceOf<>(propertyReferences)));
        final ConfirmedRequestService service = new AddListElementRequest(new ObjectIdentifier(ObjectType.group, 3),
                PropertyIdentifier.listOfGroupMembers, null, new SequenceOf<>(elements));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_206,
                (byte) 1, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0x08, (byte) 0x0C, (byte) 0x02,
                (byte) 0xC0, (byte) 0x00, (byte) 0x03, (byte) 0x19, (byte) 0x35, (byte) 0x3E, (byte) 0x0C, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x0F, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x09, (byte) 0x67,
                (byte) 0x1F, (byte) 0x3F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_1bTest() {
        final APDU pdu = new SimpleACK((byte) 1, 8);
        final byte[] expectedResult = { (byte) 0x20, (byte) 0x01, (byte) 0x08 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_2aTest() {
        final List<ReadAccessSpecification> readAccessSpecs = new ArrayList<>();

        List<PropertyReference> propertyReferences = new ArrayList<>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        propertyReferences.add(new PropertyReference(PropertyIdentifier.reliability, null));
        propertyReferences.add(new PropertyReference(PropertyIdentifier.description, null));
        readAccessSpecs.add(new ReadAccessSpecification(new ObjectIdentifier(ObjectType.analogInput, 12),
                new SequenceOf<>(propertyReferences)));

        propertyReferences = new ArrayList<>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        propertyReferences.add(new PropertyReference(PropertyIdentifier.reliability, null));
        propertyReferences.add(new PropertyReference(PropertyIdentifier.description, null));
        readAccessSpecs.add(new ReadAccessSpecification(new ObjectIdentifier(ObjectType.analogInput, 13),
                new SequenceOf<>(propertyReferences)));

        final ConfirmedRequestService service = new RemoveListElementRequest(new ObjectIdentifier(ObjectType.group, 3),
                PropertyIdentifier.listOfGroupMembers, null, new SequenceOf<>(readAccessSpecs));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_206,
                (byte) 52, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x34, (byte) 0x09, (byte) 0x0C, (byte) 0x02,
                (byte) 0xC0, (byte) 0x00, (byte) 0x03, (byte) 0x19, (byte) 0x35, (byte) 0x3E, (byte) 0x0C, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x0C, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x09, (byte) 0x67,
                (byte) 0x09, (byte) 0x1C, (byte) 0x1F, (byte) 0x0C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0D,
                (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x09, (byte) 0x67, (byte) 0x09, (byte) 0x1C, (byte) 0x1F,
                (byte) 0x3F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_2bTest() {
        final APDU pdu = new SimpleACK((byte) 52, 9);
        final byte[] expectedResult = { (byte) 0x20, (byte) 0x34, (byte) 0x09 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_2cTest() {
        final List<ReadAccessSpecification> readAccessSpecs = new ArrayList<>();

        List<PropertyReference> propertyReferences = new ArrayList<>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        propertyReferences.add(new PropertyReference(PropertyIdentifier.reliability, null));
        readAccessSpecs.add(new ReadAccessSpecification(new ObjectIdentifier(ObjectType.analogInput, 12),
                new SequenceOf<>(propertyReferences)));

        propertyReferences = new ArrayList<>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        propertyReferences.add(new PropertyReference(PropertyIdentifier.reliability, null));
        readAccessSpecs.add(new ReadAccessSpecification(new ObjectIdentifier(ObjectType.analogInput, 13),
                new SequenceOf<>(propertyReferences)));

        final ConfirmedRequestService service = new AddListElementRequest(new ObjectIdentifier(ObjectType.group, 3),
                PropertyIdentifier.listOfGroupMembers, null, new SequenceOf<>(readAccessSpecs));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_206,
                (byte) 53, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x02, (byte) 0x35, (byte) 0x08, (byte) 0x0C, (byte) 0x02,
                (byte) 0xC0, (byte) 0x00, (byte) 0x03, (byte) 0x19, (byte) 0x35, (byte) 0x3E, (byte) 0x0C, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x0C, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x09, (byte) 0x67,
                (byte) 0x1F, (byte) 0x0C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0D, (byte) 0x1E, (byte) 0x09,
                (byte) 0x55, (byte) 0x09, (byte) 0x67, (byte) 0x1F, (byte) 0x3F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_2dTest() {
        final APDU pdu = new SimpleACK((byte) 53, 8);
        final byte[] expectedResult = { (byte) 0x20, (byte) 0x35, (byte) 0x08 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_3aTest() {
        final List<PropertyValue> propertyValues = new ArrayList<>();
        propertyValues.add(new PropertyValue(PropertyIdentifier.objectName, null,
                new CharacterString(CharacterString.Encodings.ANSI_X3_4, "Trend 1"), null));
        propertyValues
                .add(new PropertyValue(PropertyIdentifier.fileAccessMethod, null, FileAccessMethod.recordAccess, null));
        final ConfirmedRequestService service = new CreateObjectRequest(ObjectType.file,
                new SequenceOf<>(propertyValues));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 86, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x56, (byte) 0x0A, (byte) 0x0E, (byte) 0x09,
                (byte) 0x0A, (byte) 0x0F, (byte) 0x1E, (byte) 0x09, (byte) 0x4D, (byte) 0x2E, (byte) 0x75, (byte) 0x08,
                (byte) 0x00, (byte) 0x54, (byte) 0x72, (byte) 0x65, (byte) 0x6E, (byte) 0x64, (byte) 0x20, (byte) 0x31,
                (byte) 0x2F, (byte) 0x09, (byte) 0x29, (byte) 0x2E, (byte) 0x91, (byte) 0x00, (byte) 0x2F,
                (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_3bTest() {
        final AcknowledgementService service = new CreateObjectAck(new ObjectIdentifier(ObjectType.file, 13));
        final APDU pdu = new ComplexACK(false, false, (byte) 86, 0, 0, service);
        final byte[] expectedResult = { (byte) 0x30, (byte) 0x56, (byte) 0x0A, (byte) 0xC4, (byte) 0x02, (byte) 0x80,
                (byte) 0x00, (byte) 0x0D };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_4aTest() {
        final ConfirmedRequestService service = new DeleteObjectRequest(new ObjectIdentifier(ObjectType.group, 6));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 87, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x57, (byte) 0x0B, (byte) 0xC4, (byte) 0x02,
                (byte) 0xC0, (byte) 0x00, (byte) 0x06 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_4bTest() {
        final APDU pdu = new SimpleACK((byte) 87, 11);
        final byte[] expectedResult = { (byte) 0x20, (byte) 0x57, (byte) 0x0B };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_4cTest() {
        final ConfirmedRequestService service = new DeleteObjectRequest(new ObjectIdentifier(ObjectType.group, 7));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 88, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x58, (byte) 0x0B, (byte) 0xC4, (byte) 0x02,
                (byte) 0xC0, (byte) 0x00, (byte) 0x07 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_4dTest() {
        final BaseError baseError = new BaseError((byte) 11,
                new BACnetError(ErrorClass.object, ErrorCode.objectDeletionNotPermitted));
        final APDU pdu = new com.serotonin.bacnet4j.apdu.Error((byte) 88, baseError);
        final byte[] expectedResult = { (byte) 0x50, (byte) 0x58, (byte) 0x0B, (byte) 0x91, (byte) 0x01, (byte) 0x91,
                (byte) 0x17 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_5aTest() {
        final ConfirmedRequestService service = new ReadPropertyRequest(new ObjectIdentifier(ObjectType.analogInput, 5),
                PropertyIdentifier.presentValue, null);
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_50,
                (byte) 1, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x0C, (byte) 0x0C, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x19, (byte) 0x55 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_5bTest() {
        final AcknowledgementService service = new ReadPropertyAck(new ObjectIdentifier(ObjectType.analogInput, 5),
                PropertyIdentifier.presentValue, null, new Real(72.3f));
        final APDU pdu = new ComplexACK(false, false, (byte) 1, 0, 0, service);
        final byte[] expectedResult = { (byte) 0x30, (byte) 0x01, (byte) 0x0C, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x05, (byte) 0x19, (byte) 0x55, (byte) 0x3E, (byte) 0x44, (byte) 0x42, (byte) 0x90,
                (byte) 0x99, (byte) 0x9A, (byte) 0x3F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_7aTest() {
        final List<ReadAccessSpecification> readAccessSpecs = new ArrayList<>();
        final List<PropertyReference> propertyReferences = new ArrayList<>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        propertyReferences.add(new PropertyReference(PropertyIdentifier.reliability, null));
        readAccessSpecs.add(new ReadAccessSpecification(new ObjectIdentifier(ObjectType.analogInput, 16),
                new SequenceOf<>(propertyReferences)));
        final ConfirmedRequestService service = new ReadPropertyMultipleRequest(
                new SequenceOf<>(readAccessSpecs));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 241, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0xF1, (byte) 0x0E, (byte) 0x0C, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x09, (byte) 0x67,
                (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_7bTest() {
        final List<ReadAccessResult> readAccessResults = new ArrayList<>();
        final List<Result> results = new ArrayList<>();
        results.add(new Result(PropertyIdentifier.presentValue, null, new Real(72.3f)));
        results.add(new Result(PropertyIdentifier.reliability, null, Reliability.noFaultDetected));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.analogInput, 16),
                new SequenceOf<>(results)));
        final AcknowledgementService service = new ReadPropertyMultipleAck(
                new SequenceOf<>(readAccessResults));
        final APDU pdu = new ComplexACK(false, false, (byte) 241, 0, 0, service);
        final byte[] expectedResult = { (byte) 0x30, (byte) 0xF1, (byte) 0x0E, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x10, (byte) 0x1E, (byte) 0x29, (byte) 0x55, (byte) 0x4E, (byte) 0x44, (byte) 0x42,
                (byte) 0x90, (byte) 0x99, (byte) 0x9A, (byte) 0x4F, (byte) 0x29, (byte) 0x67, (byte) 0x4E, (byte) 0x91,
                (byte) 0x00, (byte) 0x4F, (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_7cTest() {
        final List<ReadAccessSpecification> readAccessSpecs = new ArrayList<>();

        List<PropertyReference> propertyReferences = new ArrayList<>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        readAccessSpecs.add(new ReadAccessSpecification(new ObjectIdentifier(ObjectType.analogInput, 33),
                new SequenceOf<>(propertyReferences)));

        propertyReferences = new ArrayList<>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        readAccessSpecs.add(new ReadAccessSpecification(new ObjectIdentifier(ObjectType.analogInput, 50),
                new SequenceOf<>(propertyReferences)));

        propertyReferences = new ArrayList<>();
        propertyReferences.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        readAccessSpecs.add(new ReadAccessSpecification(new ObjectIdentifier(ObjectType.analogInput, 35),
                new SequenceOf<>(propertyReferences)));

        final ConfirmedRequestService service = new ReadPropertyMultipleRequest(
                new SequenceOf<>(readAccessSpecs));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 2, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x02, (byte) 0x0E, (byte) 0x0C, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x21, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x1F, (byte) 0x0C,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x32, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x1F,
                (byte) 0x0C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x23, (byte) 0x1E, (byte) 0x09, (byte) 0x55,
                (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_7dTest() {
        final List<ReadAccessResult> readAccessResults = new ArrayList<>();

        List<Result> results = new ArrayList<>();
        results.add(new Result(PropertyIdentifier.presentValue, null, new Real(42.3f)));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.analogInput, 33),
                new SequenceOf<>(results)));

        results = new ArrayList<>();
        results.add(new Result(PropertyIdentifier.presentValue, null,
                new BACnetError(ErrorClass.object, ErrorCode.unknownObject)));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.analogInput, 50),
                new SequenceOf<>(results)));

        results = new ArrayList<>();
        results.add(new Result(PropertyIdentifier.presentValue, null, new Real(435.7f)));
        readAccessResults.add(new ReadAccessResult(new ObjectIdentifier(ObjectType.analogInput, 35),
                new SequenceOf<>(results)));

        final AcknowledgementService service = new ReadPropertyMultipleAck(
                new SequenceOf<>(readAccessResults));
        final APDU pdu = new ComplexACK(false, false, (byte) 2, 0, 0, service);
        final byte[] expectedResult = { (byte) 0x30, (byte) 0x02, (byte) 0x0E, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x21, (byte) 0x1E, (byte) 0x29, (byte) 0x55, (byte) 0x4E, (byte) 0x44, (byte) 0x42,
                (byte) 0x29, (byte) 0x33, (byte) 0x33, (byte) 0x4F, (byte) 0x1F, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x32, (byte) 0x1E, (byte) 0x29, (byte) 0x55, (byte) 0x5E, (byte) 0x91, (byte) 0x01,
                (byte) 0x91, (byte) 0x1F, (byte) 0x5F, (byte) 0x1F, (byte) 0x0C, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x23, (byte) 0x1E, (byte) 0x29, (byte) 0x55, (byte) 0x4E, (byte) 0x44, (byte) 0x43, (byte) 0xD9,
                (byte) 0xD9, (byte) 0x9A, (byte) 0x4F, (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_8aTest() {
        // Deprecated parameters.
    }

    @Test
    public void e3_8bTest() {
        final List<BaseType> itemData = new ArrayList<>();
        itemData.add(
                new LogRecord(new DateTime(new Date(1998, Month.MARCH, 23, DayOfWeek.MONDAY), new Time(19, 54, 27, 0)),
                        false, new Real(18), new StatusFlags(false, false, false, false)));
        itemData.add(
                new LogRecord(new DateTime(new Date(1998, Month.MARCH, 23, DayOfWeek.MONDAY), new Time(19, 56, 27, 0)),
                        false, new Real(18.1f), new StatusFlags(false, false, false, false)));
        final AcknowledgementService service = new ReadRangeAck(new ObjectIdentifier(ObjectType.trendLog, 1),
                PropertyIdentifier.logBuffer, null, new ResultFlags(true, true, false), new UnsignedInteger(2),
                new SequenceOf<>(itemData), null);
        final APDU pdu = new ComplexACK(false, false, (byte) 1, 0, 0, service);
        final byte[] expectedResult = { (byte) 0x30, (byte) 0x01, (byte) 0x1A, (byte) 0x0C, (byte) 0x05, (byte) 0x00,
                (byte) 0x00, (byte) 0x01, (byte) 0x19, (byte) 0x83, (byte) 0x3A, (byte) 0x05, (byte) 0xC0, (byte) 0x49,
                (byte) 0x02, (byte) 0x5E, (byte) 0x0E, (byte) 0xA4, (byte) 0x62, (byte) 0x03, (byte) 0x17, (byte) 0x01,
                (byte) 0xB4, (byte) 0x13, (byte) 0x36, (byte) 0x1B, (byte) 0x00, (byte) 0x0F, (byte) 0x1E, (byte) 0x2C,
                (byte) 0x41, (byte) 0x90, (byte) 0x00, (byte) 0x00, (byte) 0x1F, (byte) 0x2A, (byte) 0x04, (byte) 0x00,
                (byte) 0x0E, (byte) 0xA4, (byte) 0x62, (byte) 0x03, (byte) 0x17, (byte) 0x01, (byte) 0xB4, (byte) 0x13,
                (byte) 0x38, (byte) 0x1B, (byte) 0x00, (byte) 0x0F, (byte) 0x1E, (byte) 0x2C, (byte) 0x41, (byte) 0x90,
                (byte) 0xCC, (byte) 0xCD, (byte) 0x1F, (byte) 0x2A, (byte) 0x04, (byte) 0x00, (byte) 0x5F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_9aTest() {
        final ConfirmedRequestService service = new WritePropertyRequest(
                new ObjectIdentifier(ObjectType.analogValue, 1), PropertyIdentifier.presentValue, null, new Real(180),
                null);
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 89, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x59, (byte) 0x0F, (byte) 0x0C, (byte) 0x00,
                (byte) 0x80, (byte) 0x00, (byte) 0x01, (byte) 0x19, (byte) 0x55, (byte) 0x3E, (byte) 0x44, (byte) 0x43,
                (byte) 0x34, (byte) 0x00, (byte) 0x00, (byte) 0x3F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_9bTest() {
        final APDU pdu = new SimpleACK((byte) 89, 15);
        final byte[] expectedResult = { (byte) 0x20, (byte) 0x59, (byte) 0x0F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_10aTest() {
        final List<WriteAccessSpecification> writeAccessSpecs = new ArrayList<>();

        List<PropertyValue> propertyValues = new ArrayList<>();
        propertyValues.add(new PropertyValue(PropertyIdentifier.presentValue, null, new Real(67), null));
        writeAccessSpecs.add(new WriteAccessSpecification(new ObjectIdentifier(ObjectType.analogValue, 5),
                new SequenceOf<>(propertyValues)));

        propertyValues = new ArrayList<>();
        propertyValues.add(new PropertyValue(PropertyIdentifier.presentValue, null, new Real(67), null));
        writeAccessSpecs.add(new WriteAccessSpecification(new ObjectIdentifier(ObjectType.analogValue, 6),
                new SequenceOf<>(propertyValues)));

        propertyValues = new ArrayList<>();
        propertyValues.add(new PropertyValue(PropertyIdentifier.presentValue, null, new Real(72), null));
        writeAccessSpecs.add(new WriteAccessSpecification(new ObjectIdentifier(ObjectType.analogValue, 7),
                new SequenceOf<>(propertyValues)));
        final ConfirmedRequestService service = new WritePropertyMultipleRequest(
                new SequenceOf<>(writeAccessSpecs));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 1, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x01, (byte) 0x10, (byte) 0x0C, (byte) 0x00,
                (byte) 0x80, (byte) 0x00, (byte) 0x05, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x2E, (byte) 0x44,
                (byte) 0x42, (byte) 0x86, (byte) 0x00, (byte) 0x00, (byte) 0x2F, (byte) 0x1F, (byte) 0x0C, (byte) 0x00,
                (byte) 0x80, (byte) 0x00, (byte) 0x06, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x2E, (byte) 0x44,
                (byte) 0x42, (byte) 0x86, (byte) 0x00, (byte) 0x00, (byte) 0x2F, (byte) 0x1F, (byte) 0x0C, (byte) 0x00,
                (byte) 0x80, (byte) 0x00, (byte) 0x07, (byte) 0x1E, (byte) 0x09, (byte) 0x55, (byte) 0x2E, (byte) 0x44,
                (byte) 0x42, (byte) 0x90, (byte) 0x00, (byte) 0x00, (byte) 0x2F, (byte) 0x1F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e3_10bTest() {
        final APDU pdu = new SimpleACK((byte) 1, 16);
        final byte[] expectedResult = { (byte) 0x20, (byte) 0x01, (byte) 0x10 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_1aTest() {
        final ConfirmedRequestService service = new DeviceCommunicationControlRequest(new UnsignedInteger(5),
                EnableDisable.disable, new CharacterString(CharacterString.Encodings.ANSI_X3_4, "#egbdf!"));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 5, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x05, (byte) 0x11, (byte) 0x09, (byte) 0x05,
                (byte) 0x19, (byte) 0x01, (byte) 0x2D, (byte) 0x08, (byte) 0x00, (byte) 0x23, (byte) 0x65, (byte) 0x67,
                (byte) 0x62, (byte) 0x64, (byte) 0x66, (byte) 0x21 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_1bTest() {
        final APDU pdu = new SimpleACK((byte) 5, 17);
        final byte[] expectedResult = { (byte) 0x20, (byte) 0x05, (byte) 0x11 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_2aTest() {
        final List<ElementSpecification> elements = new ArrayList<>();
        elements.add(new ElementSpecification("value1", Real.class, false, false));
        elements.add(new ElementSpecification("value2", OctetString.class, false, false));
        final SequenceDefinition def = new SequenceDefinition(elements);

        final Map<String, Encodable> values = new HashMap<>();
        values.put("value1", new Real(72.4f));
        values.put("value2", new OctetString(new byte[] { 0x16, 0x49 }));
        final Sequence parameters = new Sequence(def, values);

        LocalDevice.vendorServiceRequestResolutions
                .put(new VendorServiceKey(new UnsignedInteger(25), new UnsignedInteger(8)), def);
        final ConfirmedRequestService service = new ConfirmedPrivateTransferRequest(new UnsignedInteger(25),
                new UnsignedInteger(8), parameters);
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_1024,
                (byte) 85, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x04, (byte) 0x55, (byte) 0x12, (byte) 0x09, (byte) 0x19,
                (byte) 0x19, (byte) 0x08, (byte) 0x2E, (byte) 0x44, (byte) 0x42, (byte) 0x90, (byte) 0xCC, (byte) 0xCD,
                (byte) 0x62, (byte) 0x16, (byte) 0x49, (byte) 0x2F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_2bTest() {
        final AcknowledgementService service = new ConfirmedPrivateTransferAck(new UnsignedInteger(25),
                new UnsignedInteger(8), null);
        final APDU pdu = new ComplexACK(false, false, (byte) 85, 0, 0, service);
        final byte[] expectedResult = { (byte) 0x30, (byte) 0x55, (byte) 0x12, (byte) 0x09, (byte) 0x19, (byte) 0x19,
                (byte) 0x08 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_3Test() {
        final List<ElementSpecification> elements = new ArrayList<>();
        elements.add(new ElementSpecification("value1", Real.class, false, false));
        elements.add(new ElementSpecification("value2", OctetString.class, false, false));
        final SequenceDefinition def = new SequenceDefinition(elements);

        final Map<String, Encodable> values = new HashMap<>();
        values.put("value1", new Real(72.4f));
        values.put("value2", new OctetString(new byte[] { 0x16, 0x49 }));
        final Sequence parameters = new Sequence(def, values);

        LocalDevice.vendorServiceRequestResolutions
                .put(new VendorServiceKey(new UnsignedInteger(25), new UnsignedInteger(8)), def);
        final UnconfirmedRequestService service = new UnconfirmedPrivateTransferRequest(new UnsignedInteger(25),
                new UnsignedInteger(8), parameters);
        final APDU pdu = new UnconfirmedRequest(service);
        final byte[] expectedResult = { (byte) 0x10, (byte) 0x04, (byte) 0x09, (byte) 0x19, (byte) 0x19, (byte) 0x08,
                (byte) 0x2E, (byte) 0x44, (byte) 0x42, (byte) 0x90, (byte) 0xCC, (byte) 0xCD, (byte) 0x62, (byte) 0x16,
                (byte) 0x49, (byte) 0x2F };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_4aTest() {
        final ConfirmedRequestService service = new ReinitializeDeviceRequest(ReinitializedStateOfDevice.warmstart,
                new CharacterString(CharacterString.Encodings.ANSI_X3_4, "AbCdEfGh"));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_128,
                (byte) 2, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x14, (byte) 0x09, (byte) 0x01,
                (byte) 0x1D, (byte) 0x09, (byte) 0x00, (byte) 0x41, (byte) 0x62, (byte) 0x43, (byte) 0x64, (byte) 0x45,
                (byte) 0x66, (byte) 0x47, (byte) 0x68 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_4bTest() {
        final APDU pdu = new SimpleACK((byte) 2, 20);
        final byte[] expectedResult = { (byte) 0x20, (byte) 0x02, (byte) 0x14 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_5aTest() {
        final ConfirmedRequestService service = new ConfirmedTextMessageRequest(
                new ObjectIdentifier(ObjectType.device, 5), MessagePriority.normal,
                new CharacterString(CharacterString.Encodings.ANSI_X3_4, "PM required for PUMP347"));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_128,
                (byte) 3, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x01, (byte) 0x03, (byte) 0x13, (byte) 0x0C, (byte) 0x02,
                (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x29, (byte) 0x00, (byte) 0x3D, (byte) 0x18, (byte) 0x00,
                (byte) 0x50, (byte) 0x4D, (byte) 0x20, (byte) 0x72, (byte) 0x65, (byte) 0x71, (byte) 0x75, (byte) 0x69,
                (byte) 0x72, (byte) 0x65, (byte) 0x64, (byte) 0x20, (byte) 0x66, (byte) 0x6F, (byte) 0x72, (byte) 0x20,
                (byte) 0x50, (byte) 0x55, (byte) 0x4D, (byte) 0x50, (byte) 0x33, (byte) 0x34, (byte) 0x37 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_5bTest() {
        final APDU pdu = new SimpleACK((byte) 3, 19);
        final byte[] expectedResult = { (byte) 0x20, (byte) 0x03, (byte) 0x13 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_6Test() {
        final UnconfirmedRequestService service = new UnconfirmedTextMessageRequest(
                new ObjectIdentifier(ObjectType.device, 5), MessagePriority.normal,
                new CharacterString(CharacterString.Encodings.ANSI_X3_4, "PM required for PUMP347"));
        final APDU pdu = new UnconfirmedRequest(service);
        final byte[] expectedResult = { (byte) 0x10, (byte) 0x05, (byte) 0x0C, (byte) 0x02, (byte) 0x00, (byte) 0x00,
                (byte) 0x05, (byte) 0x29, (byte) 0x00, (byte) 0x3D, (byte) 0x18, (byte) 0x00, (byte) 0x50, (byte) 0x4D,
                (byte) 0x20, (byte) 0x72, (byte) 0x65, (byte) 0x71, (byte) 0x75, (byte) 0x69, (byte) 0x72, (byte) 0x65,
                (byte) 0x64, (byte) 0x20, (byte) 0x66, (byte) 0x6F, (byte) 0x72, (byte) 0x20, (byte) 0x50, (byte) 0x55,
                (byte) 0x4D, (byte) 0x50, (byte) 0x33, (byte) 0x34, (byte) 0x37 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_7Test() {
        final UnconfirmedRequestService service = new TimeSynchronizationRequest(
                new DateTime(new Date(1992, Month.NOVEMBER, 17, DayOfWeek.UNSPECIFIED), new Time(22, 45, 30, 70)));
        final APDU pdu = new UnconfirmedRequest(service);
        final byte[] expectedResult = { (byte) 0x10, (byte) 0x06, (byte) 0xA4, (byte) 0x5C, (byte) 0x0B, (byte) 0x11,
                (byte) 0xFF, (byte) 0xB4, (byte) 0x16, (byte) 0x2D, (byte) 0x1E, (byte) 0x46 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_8aTest() {
        final UnconfirmedRequestService service = new WhoHasRequest(null,
                new CharacterString(CharacterString.Encodings.ANSI_X3_4, "OATemp"));
        final APDU pdu = new UnconfirmedRequest(service);
        final byte[] expectedResult = { (byte) 0x10, (byte) 0x07, (byte) 0x3D, (byte) 0x07, (byte) 0x00, (byte) 0x4F,
                (byte) 0x41, (byte) 0x54, (byte) 0x65, (byte) 0x6D, (byte) 0x70 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_8bTest() {
        final UnconfirmedRequestService service = new IHaveRequest(new ObjectIdentifier(ObjectType.device, 8),
                new ObjectIdentifier(ObjectType.analogInput, 3),
                new CharacterString(CharacterString.Encodings.ANSI_X3_4, "OATemp"));
        final APDU pdu = new UnconfirmedRequest(service);
        final byte[] expectedResult = { (byte) 0x10, (byte) 0x01, (byte) 0xC4, (byte) 0x02, (byte) 0x00, (byte) 0x00,
                (byte) 0x08, (byte) 0xC4, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x75, (byte) 0x07,
                (byte) 0x00, (byte) 0x4F, (byte) 0x41, (byte) 0x54, (byte) 0x65, (byte) 0x6D, (byte) 0x70 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_8cTest() {
        final UnconfirmedRequestService service = new WhoHasRequest(null,
                new ObjectIdentifier(ObjectType.analogInput, 3));
        final APDU pdu = new UnconfirmedRequest(service);
        final byte[] expectedResult = { (byte) 0x10, (byte) 0x07, (byte) 0x2C, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x03 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_8dTest() {
        final UnconfirmedRequestService service = new IHaveRequest(new ObjectIdentifier(ObjectType.device, 8),
                new ObjectIdentifier(ObjectType.analogInput, 3),
                new CharacterString(CharacterString.Encodings.ANSI_X3_4, "OATemp"));
        final APDU pdu = new UnconfirmedRequest(service);
        final byte[] expectedResult = { (byte) 0x10, (byte) 0x01, (byte) 0xC4, (byte) 0x02, (byte) 0x00, (byte) 0x00,
                (byte) 0x08, (byte) 0xC4, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x75, (byte) 0x07,
                (byte) 0x00, (byte) 0x4F, (byte) 0x41, (byte) 0x54, (byte) 0x65, (byte) 0x6D, (byte) 0x70 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_9aTest() {
        final UnconfirmedRequestService service = new WhoIsRequest(new UnsignedInteger(3), new UnsignedInteger(3));
        final APDU pdu = new UnconfirmedRequest(service);
        final byte[] expectedResult = { (byte) 0x10, (byte) 0x08, (byte) 0x09, (byte) 0x03, (byte) 0x19, (byte) 0x03 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_9bTest() {
        final UnconfirmedRequestService service = new IAmRequest(new ObjectIdentifier(ObjectType.device, 3),
                new UnsignedInteger(1024), Segmentation.noSegmentation, new UnsignedInteger(99));
        final APDU pdu = new UnconfirmedRequest(service);
        final byte[] expectedResult = { (byte) 0x10, (byte) 0x00, (byte) 0xC4, (byte) 0x02, (byte) 0x00, (byte) 0x00,
                (byte) 0x03, (byte) 0x22, (byte) 0x04, (byte) 0x00, (byte) 0x91, (byte) 0x03, (byte) 0x21,
                (byte) 0x63 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_9cTest() {
        final UnconfirmedRequestService service = new WhoIsRequest();
        final APDU pdu = new UnconfirmedRequest(service);
        final byte[] expectedResult = { (byte) 0x10, (byte) 0x08 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_9dTest() {
        final UnconfirmedRequestService service = new IAmRequest(new ObjectIdentifier(ObjectType.device, 1),
                new UnsignedInteger(480), Segmentation.segmentedTransmit, new UnsignedInteger(99));
        final APDU pdu = new UnconfirmedRequest(service);
        final byte[] expectedResult = { (byte) 0x10, (byte) 0x00, (byte) 0xC4, (byte) 0x02, (byte) 0x00, (byte) 0x00,
                (byte) 0x01, (byte) 0x22, (byte) 0x01, (byte) 0xE0, (byte) 0x91, (byte) 0x01, (byte) 0x21,
                (byte) 0x63 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_9eTest() {
        final UnconfirmedRequestService service = new IAmRequest(new ObjectIdentifier(ObjectType.device, 2),
                new UnsignedInteger(206), Segmentation.segmentedReceive, new UnsignedInteger(33));
        final APDU pdu = new UnconfirmedRequest(service);
        final byte[] expectedResult = { (byte) 0x10, (byte) 0x00, (byte) 0xC4, (byte) 0x02, (byte) 0x00, (byte) 0x00,
                (byte) 0x02, (byte) 0x21, (byte) 0xCE, (byte) 0x91, (byte) 0x02, (byte) 0x21, (byte) 0x21 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_9fTest() {
        final UnconfirmedRequestService service = new IAmRequest(new ObjectIdentifier(ObjectType.device, 3),
                new UnsignedInteger(1024), Segmentation.noSegmentation, new UnsignedInteger(99));
        final APDU pdu = new UnconfirmedRequest(service);
        final byte[] expectedResult = { (byte) 0x10, (byte) 0x00, (byte) 0xC4, (byte) 0x02, (byte) 0x00, (byte) 0x00,
                (byte) 0x03, (byte) 0x22, (byte) 0x04, (byte) 0x00, (byte) 0x91, (byte) 0x03, (byte) 0x21,
                (byte) 0x63 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e4_9gTest() {
        final UnconfirmedRequestService service = new IAmRequest(new ObjectIdentifier(ObjectType.device, 4),
                new UnsignedInteger(128), Segmentation.segmentedBoth, new UnsignedInteger(66));
        final APDU pdu = new UnconfirmedRequest(service);
        final byte[] expectedResult = { (byte) 0x10, (byte) 0x00, (byte) 0xC4, (byte) 0x02, (byte) 0x00, (byte) 0x00,
                (byte) 0x04, (byte) 0x21, (byte) 0x80, (byte) 0x91, (byte) 0x00, (byte) 0x21, (byte) 0x42 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e5_aTest() {
        final ConfirmedRequestService service = new VtOpenRequest(VtClass.ansi_x3_64, new UnsignedInteger(5));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_128,
                (byte) 80, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x01, (byte) 0x50, (byte) 0x15, (byte) 0x91, (byte) 0x01,
                (byte) 0x21, (byte) 0x05 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e5_bTest() {
        final AcknowledgementService service = new VtOpenAck(new UnsignedInteger(29));
        final APDU pdu = new ComplexACK(false, false, (byte) 80, 0, 0, service);
        final byte[] expectedResult = { (byte) 0x30, (byte) 0x50, (byte) 0x15, (byte) 0x21, (byte) 0x1D };
        compare(pdu, expectedResult);
    }

    @Test
    public void e5_cTest() {
        final byte[] data = "\r\nEnter User Name:".getBytes();
        final ConfirmedRequestService service = new VtDataRequest(new UnsignedInteger(5), new OctetString(data),
                new UnsignedInteger(0));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_128,
                (byte) 81, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x01, (byte) 0x51, (byte) 0x17, (byte) 0x21, (byte) 0x05,
                (byte) 0x65, (byte) 0x12, (byte) 0x0D, (byte) 0x0A, (byte) 0x45, (byte) 0x6E, (byte) 0x74, (byte) 0x65,
                (byte) 0x72, (byte) 0x20, (byte) 0x55, (byte) 0x73, (byte) 0x65, (byte) 0x72, (byte) 0x20, (byte) 0x4E,
                (byte) 0x61, (byte) 0x6D, (byte) 0x65, (byte) 0x3A, (byte) 0x21, (byte) 0x00 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e5_dTest() {
        final AcknowledgementService service = new VtDataAck(new Boolean(true), null);
        final APDU pdu = new ComplexACK(false, false, (byte) 81, 0, 0, service);
        final byte[] expectedResult = { (byte) 0x30, (byte) 0x51, (byte) 0x17, (byte) 0x09, (byte) 0x01 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e5_eTest() {
        final ConfirmedRequestService service = new VtDataRequest(new UnsignedInteger(29),
                new OctetString("FRED\r".getBytes()), new UnsignedInteger(0));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_128,
                (byte) 82, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x01, (byte) 0x52, (byte) 0x17, (byte) 0x21, (byte) 0x1D,
                (byte) 0x65, (byte) 0x05, (byte) 0x46, (byte) 0x52, (byte) 0x45, (byte) 0x44, (byte) 0x0D, (byte) 0x21,
                (byte) 0x00 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e5_fTest() {
        final AcknowledgementService service = new VtDataAck(new Boolean(true), null);
        final APDU pdu = new ComplexACK(false, false, (byte) 82, 0, 0, service);
        final byte[] expectedResult = { (byte) 0x30, (byte) 0x52, (byte) 0x17, (byte) 0x09, (byte) 0x01 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e5_gTest() {
        final ConfirmedRequestService service = new VtDataRequest(new UnsignedInteger(5),
                new OctetString("FRED\r\nEnter Password:".getBytes()), new UnsignedInteger(1));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_128,
                (byte) 83, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x01, (byte) 0x53, (byte) 0x17, (byte) 0x21, (byte) 0x05,
                (byte) 0x65, (byte) 0x15, (byte) 0x46, (byte) 0x52, (byte) 0x45, (byte) 0x44, (byte) 0x0D, (byte) 0x0A,
                (byte) 0x45, (byte) 0x6E, (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x20, (byte) 0x50, (byte) 0x61,
                (byte) 0x73, (byte) 0x73, (byte) 0x77, (byte) 0x6F, (byte) 0x72, (byte) 0x64, (byte) 0x3A, (byte) 0x21,
                (byte) 0x01 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e5_hTest() {
        final AcknowledgementService service = new VtDataAck(new Boolean(true), null);
        final APDU pdu = new ComplexACK(false, false, (byte) 83, 0, 0, service);
        final byte[] expectedResult = { (byte) 0x30, (byte) 0x53, (byte) 0x17, (byte) 0x09, (byte) 0x01 };
        compare(pdu, expectedResult);
    }

    @Test
    public void e5_iTest() {
        final List<UnsignedInteger> ids = new ArrayList<>();
        ids.add(new UnsignedInteger(29));
        final ConfirmedRequestService service = new VtCloseRequest(new SequenceOf<>(ids));
        final APDU pdu = new ConfirmedRequest(false, false, false, MaxSegments.UNSPECIFIED, MaxApduLength.UP_TO_128,
                (byte) 84, (byte) 0, 0, service);
        final byte[] expectedResult = { (byte) 0x00, (byte) 0x01, (byte) 0x54, (byte) 0x16, (byte) 0x21, (byte) 0x1D };
        compare(pdu, expectedResult);
    }

    @Test
    public void e5_jTest() {
        final APDU pdu = new SimpleACK((byte) 84, 22);
        final byte[] expectedResult = { (byte) 0x20, (byte) 0x54, (byte) 0x16 };
        compare(pdu, expectedResult);
    }

    private void compare(final APDU pdu, final byte[] expectedResult) {
        final ByteQueue queue = new ByteQueue();
        pdu.write(queue);

        if (queue.size() != expectedResult.length)
            throw new RuntimeException("Size of queue differs from expected: " + queue);

        for (int i = 0; i < expectedResult.length; i++) {
            if (queue.peek(i) != expectedResult[i])
                throw new RuntimeException("Unexpected content at index " + i + ": " + queue);
        }

        APDU parsedAPDU;
        try {
            parsedAPDU = APDU.createAPDU(servicesSupported, queue);
            if (parsedAPDU instanceof Segmentable)
                ((Segmentable) parsedAPDU).parseServiceData();
            if (parsedAPDU instanceof UnconfirmedRequest)
                ((UnconfirmedRequest) parsedAPDU).parseServiceData();
        } catch (final BACnetException e) {
            throw new RuntimeException(e);
        }

        if (queue.size() != 0)
            throw new RuntimeException("Queue not empty after parse");
        if (!parsedAPDU.equals(pdu)) {
            parsedAPDU.equals(pdu); // For debugging
            parsedAPDU.equals(pdu);
            parsedAPDU.equals(pdu);
            throw new RuntimeException("Parsed APDU does not equal given APDU");
        }
    }
}
