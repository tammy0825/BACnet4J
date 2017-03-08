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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetRuntimeException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.enumerated.FileAccessMethod;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.sero.StreamUtils;

/**
 * @author Matthew Lohbihler
 */
public class FileObject extends BACnetObject {
    /**
     * The actual file that this object represents.
     */
    private final File file;

    public FileObject(final LocalDevice localDevice, final int instanceNumber, final File file,
            final FileAccessMethod fileAccessMethod) throws BACnetServiceException {
        super(localDevice, ObjectType.file, instanceNumber, file.getName());
        this.file = file;

        if (file.isDirectory())
            throw new BACnetRuntimeException("File is a directory");

        updateProperties();

        writePropertyInternal(PropertyIdentifier.fileAccessMethod, fileAccessMethod);
    }

    public void updateProperties() {
        // NOTE: This is only a snapshot. Property read methods need to be overridden to report real time values.
        writePropertyInternal(PropertyIdentifier.fileSize,
                new UnsignedInteger(new BigInteger(Long.toString(length()))));
        writePropertyInternal(PropertyIdentifier.modificationDate, new DateTime(file.lastModified()));
        writePropertyInternal(PropertyIdentifier.readOnly, new Boolean(!file.canWrite()));
    }

    public long length() {
        return file.length();
    }

    public OctetString readData(final long start, final long length) throws IOException {
        long skip = start;
        try (FileInputStream in = new FileInputStream(file)) {
            while (skip > 0) {
                final long result = in.skip(skip);
                if (result == -1)
                    // EOF
                    break;
                skip -= result;
            }
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            StreamUtils.transfer(in, out, length);
            return new OctetString(out.toByteArray());
        }
    }

    public void writeData(final long start, final OctetString fileData) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            final byte data[] = fileData.getBytes();
            final long newLength = start + data.length;
            raf.seek(start);
            raf.write(data);
            raf.setLength(newLength);
        }

        updateProperties();
    }

    //
    // public SequenceOf<OctetString> readRecords(int start, int length) throws IOException {
    //
    // }
}
