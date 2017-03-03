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
package com.serotonin.bacnet4j.service.confirmed;

import java.io.IOException;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.exception.NotImplementedException;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.obj.FileObject;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.acknowledgement.AtomicReadFileAck;
import com.serotonin.bacnet4j.service.acknowledgement.AtomicReadFileAck.StreamAccessAck;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.BaseType;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.constructed.ChoiceOptions;
import com.serotonin.bacnet4j.type.enumerated.BackupState;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.FileAccessMethod;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

public class AtomicReadFileRequest extends ConfirmedRequestService {
    public static final byte TYPE_ID = 6;

    private static final ChoiceOptions choiceOptions = new ChoiceOptions();
    static {
        choiceOptions.addContextual(0, StreamAccess.class);
        choiceOptions.addContextual(1, RecordAccess.class);
    }

    private final ObjectIdentifier fileIdentifier;
    private final Choice accessMethod;

    public AtomicReadFileRequest(final ObjectIdentifier fileIdentifier, final StreamAccess streamAccess) {
        this.fileIdentifier = fileIdentifier;
        this.accessMethod = new Choice(0, streamAccess, choiceOptions);
    }

    public AtomicReadFileRequest(final ObjectIdentifier fileIdentifier, final RecordAccess recordAccess) {
        this.fileIdentifier = fileIdentifier;
        this.accessMethod = new Choice(1, recordAccess, choiceOptions);
    }

    @Override
    public void write(final ByteQueue queue) {
        write(queue, fileIdentifier);
        write(queue, accessMethod);
    }

    AtomicReadFileRequest(final ByteQueue queue) throws BACnetException {
        fileIdentifier = read(queue, ObjectIdentifier.class);
        accessMethod = readChoice(queue, choiceOptions);
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public AcknowledgementService handle(final LocalDevice localDevice, final Address from) throws BACnetException {
        AtomicReadFileAck response;

        BACnetObject obj;
        FileObject file;
        try {
            // Find the file.
            obj = localDevice.getObjectRequired(fileIdentifier);
            if (!(obj instanceof FileObject)) {
                System.out.println("File access request on an object that is not a file");
                throw new BACnetServiceException(ErrorClass.object, ErrorCode.rejectInconsistentParameters);
            }

            // Check for status (backup/restore)
            final BackupState bsOld = (BackupState) localDevice.getProperty(PropertyIdentifier.backupAndRestoreState);
            if (bsOld.intValue() == BackupState.preparingForBackup.intValue()
                    || bsOld.intValue() == BackupState.preparingForRestore.intValue())
                // Send error: device configuration in progress as response
                throw new BACnetServiceException(ErrorClass.device, ErrorCode.configurationInProgress);

            file = (FileObject) obj;

            // Validation.
            final boolean recordAccess = accessMethod.isa(RecordAccess.class);
            final FileAccessMethod fileAccessMethod = (FileAccessMethod) file
                    .getProperty(PropertyIdentifier.fileAccessMethod);
            if (recordAccess && fileAccessMethod.equals(FileAccessMethod.streamAccess)
                    || !recordAccess && fileAccessMethod.equals(FileAccessMethod.recordAccess))
                throw new BACnetErrorException(getChoiceId(), ErrorClass.object, ErrorCode.invalidFileAccessMethod);
        } catch (final BACnetServiceException e) {
            throw new BACnetErrorException(getChoiceId(), e);
        }

        if (accessMethod.isa(RecordAccess.class))
            throw new NotImplementedException();

        final StreamAccess streamAccess = accessMethod.getDatum();
        final long start = streamAccess.getFileStartPosition().longValue();
        final long length = streamAccess.getRequestedOctetCount().longValue();

        // Throw an exception when the following conditions are met
        //   - start is a negative number
        //   - start exceeds the length of the file object
        if (start < 0 || start > file.length())
            throw new BACnetErrorException(getChoiceId(), ErrorClass.object, ErrorCode.invalidFileStartPosition);

        try {
            response = new AtomicReadFileAck(new Boolean(file.length() <= start + length),
                    new StreamAccessAck(streamAccess.getFileStartPosition(), file.readData(start, length)));
        } catch (@SuppressWarnings("unused") final IOException e) {
            throw new BACnetErrorException(getChoiceId(), ErrorClass.object, ErrorCode.fileAccessDenied);
        }

        return response;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (accessMethod == null ? 0 : accessMethod.hashCode());
        result = prime * result + (fileIdentifier == null ? 0 : fileIdentifier.hashCode());
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
        final AtomicReadFileRequest other = (AtomicReadFileRequest) obj;
        if (accessMethod == null) {
            if (other.accessMethod != null)
                return false;
        } else if (!accessMethod.equals(other.accessMethod))
            return false;
        if (fileIdentifier == null) {
            if (other.fileIdentifier != null)
                return false;
        } else if (!fileIdentifier.equals(other.fileIdentifier))
            return false;
        return true;
    }

    public static class StreamAccess extends BaseType {
        private final SignedInteger fileStartPosition;
        private final UnsignedInteger requestedOctetCount;

        public StreamAccess(final SignedInteger fileStartPosition, final UnsignedInteger requestedOctetCount) {
            this.fileStartPosition = fileStartPosition;
            this.requestedOctetCount = requestedOctetCount;
        }

        @Override
        public void write(final ByteQueue queue) {
            write(queue, fileStartPosition);
            write(queue, requestedOctetCount);
        }

        public StreamAccess(final ByteQueue queue) throws BACnetException {
            fileStartPosition = read(queue, SignedInteger.class);
            requestedOctetCount = read(queue, UnsignedInteger.class);
        }

        public SignedInteger getFileStartPosition() {
            return fileStartPosition;
        }

        public UnsignedInteger getRequestedOctetCount() {
            return requestedOctetCount;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (fileStartPosition == null ? 0 : fileStartPosition.hashCode());
            result = prime * result + (requestedOctetCount == null ? 0 : requestedOctetCount.hashCode());
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
            final StreamAccess other = (StreamAccess) obj;
            if (fileStartPosition == null) {
                if (other.fileStartPosition != null)
                    return false;
            } else if (!fileStartPosition.equals(other.fileStartPosition))
                return false;
            if (requestedOctetCount == null) {
                if (other.requestedOctetCount != null)
                    return false;
            } else if (!requestedOctetCount.equals(other.requestedOctetCount))
                return false;
            return true;
        }
    }

    public static class RecordAccess extends BaseType {
        private final SignedInteger fileStartRecord;
        private final UnsignedInteger requestedRecordCount;

        public RecordAccess(final SignedInteger fileStartRecord, final UnsignedInteger requestedRecordCount) {
            this.fileStartRecord = fileStartRecord;
            this.requestedRecordCount = requestedRecordCount;
        }

        @Override
        public void write(final ByteQueue queue) {
            write(queue, fileStartRecord);
            write(queue, requestedRecordCount);
        }

        public RecordAccess(final ByteQueue queue) throws BACnetException {
            fileStartRecord = read(queue, SignedInteger.class);
            requestedRecordCount = read(queue, UnsignedInteger.class);
        }

        public SignedInteger getFileStartRecord() {
            return fileStartRecord;
        }

        public UnsignedInteger getRequestedRecordCount() {
            return requestedRecordCount;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (fileStartRecord == null ? 0 : fileStartRecord.hashCode());
            result = prime * result + (requestedRecordCount == null ? 0 : requestedRecordCount.hashCode());
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
            final RecordAccess other = (RecordAccess) obj;
            if (fileStartRecord == null) {
                if (other.fileStartRecord != null)
                    return false;
            } else if (!fileStartRecord.equals(other.fileStartRecord))
                return false;
            if (requestedRecordCount == null) {
                if (other.requestedRecordCount != null)
                    return false;
            } else if (!requestedRecordCount.equals(other.requestedRecordCount))
                return false;
            return true;
        }
    }
}
