package com.serotonin.bacnet4j.obj;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.TestUtils;
import com.serotonin.bacnet4j.npdu.test.TestNetwork;
import com.serotonin.bacnet4j.npdu.test.TestNetworkMap;
import com.serotonin.bacnet4j.obj.fileAccess.CrlfDelimitedFileAccess;
import com.serotonin.bacnet4j.obj.fileAccess.StreamAccess;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class FileObjectTest {
    private final TestNetworkMap map = new TestNetworkMap();
    private final String filename = "fileObjectTest.txt";
    private final String path = getClass().getClassLoader().getResource(filename).getPath();

    private LocalDevice d1;

    @Before
    public void before() throws Exception {
        d1 = new LocalDevice(1, new DefaultTransport(new TestNetwork(map, 1, 0))).initialize();
    }

    @After
    public void after() {
        d1.terminate();
    }

    @Test
    public void streamReadFileSize() throws Exception {
        final FileObject f = new FileObject(d1, 0, "test", new StreamAccess(new File(path)));
        assertEquals(new UnsignedInteger(922), f.getProperty(PropertyIdentifier.fileSize, null));
    }

    @Test
    public void recordReadFileSize() throws Exception {
        final FileObject f = new FileObject(d1, 0, "test", new CrlfDelimitedFileAccess(new File(path)));
        assertEquals(new UnsignedInteger(922), f.getProperty(PropertyIdentifier.fileSize, null));
    }

    @Test
    public void streamWriteFileSize() throws Exception {
        // Write a zero file size.
        doInCopy((file) -> {
            final FileObject f = new FileObject(d1, 0, "test", new StreamAccess(file));
            f.writeProperty(null, PropertyIdentifier.fileSize, new UnsignedInteger(0));
            assertEquals(new UnsignedInteger(0), f.getProperty(PropertyIdentifier.fileSize, null));
            d1.removeObject(f.getId());
        });

        // Write > 0 and < file size.
        doInCopy((file) -> {
            final FileObject f = new FileObject(d1, 0, "test", new StreamAccess(file));
            f.writeProperty(null, PropertyIdentifier.fileSize, new UnsignedInteger(100));
            assertEquals(new UnsignedInteger(100), f.getProperty(PropertyIdentifier.fileSize, null));
            d1.removeObject(f.getId());
        });

        // Write a file size == size
        doInCopy((file) -> {
            final FileObject f = new FileObject(d1, 0, "test", new StreamAccess(file));
            f.writeProperty(null, PropertyIdentifier.fileSize, new UnsignedInteger(922));
            assertEquals(new UnsignedInteger(922), f.getProperty(PropertyIdentifier.fileSize, null));
            d1.removeObject(f.getId());
        });

        // Write a file size > size
        doInCopy((file) -> {
            final FileObject f = new FileObject(d1, 0, "test", new StreamAccess(file));
            f.writeProperty(null, PropertyIdentifier.fileSize, new UnsignedInteger(1001));
            assertEquals(new UnsignedInteger(1001), f.getProperty(PropertyIdentifier.fileSize, null));
            d1.removeObject(f.getId());
        });
    }

    @Test
    public void recordWriteFileSize() throws Exception {
        // Write a zero record count.
        doInCopy((file) -> {
            final FileObject f = new FileObject(d1, 0, "test", new CrlfDelimitedFileAccess(file));
            f.writeProperty(null, PropertyIdentifier.fileSize, new UnsignedInteger(0));
            assertEquals(new UnsignedInteger(0), f.getProperty(PropertyIdentifier.recordCount, null));
            assertEquals(new UnsignedInteger(0), f.getProperty(PropertyIdentifier.fileSize, null));
            d1.removeObject(f.getId());
        });

        // Write > 0 and < size record count.
        doInCopy((file) -> {
            final FileObject f = new FileObject(d1, 0, "test", new CrlfDelimitedFileAccess(file));
            f.writeProperty(null, PropertyIdentifier.fileSize, new UnsignedInteger(100));
            assertEquals(new UnsignedInteger(2), f.getProperty(PropertyIdentifier.recordCount, null));
            assertEquals(new UnsignedInteger(100), f.getProperty(PropertyIdentifier.fileSize, null));
            d1.removeObject(f.getId());
        });

        // Write > 0 and < size record count.
        doInCopy((file) -> {
            final FileObject f = new FileObject(d1, 0, "test", new CrlfDelimitedFileAccess(file));
            f.writeProperty(null, PropertyIdentifier.fileSize, new UnsignedInteger(921));
            assertEquals(new UnsignedInteger(14), f.getProperty(PropertyIdentifier.recordCount, null));
            assertEquals(new UnsignedInteger(921), f.getProperty(PropertyIdentifier.fileSize, null));
            d1.removeObject(f.getId());
        });

        // Write a record count == size
        doInCopy((file) -> {
            final FileObject f = new FileObject(d1, 0, "test", new CrlfDelimitedFileAccess(file));
            f.writeProperty(null, PropertyIdentifier.fileSize, new UnsignedInteger(922));
            assertEquals(new UnsignedInteger(14), f.getProperty(PropertyIdentifier.recordCount, null));
            assertEquals(new UnsignedInteger(922), f.getProperty(PropertyIdentifier.fileSize, null));
            d1.removeObject(f.getId());
        });

        // Write a record count > size
        doInCopy((file) -> {
            final FileObject f = new FileObject(d1, 0, "test", new CrlfDelimitedFileAccess(file));
            f.writeProperty(null, PropertyIdentifier.fileSize, new UnsignedInteger(1001));
            assertEquals(new UnsignedInteger(54), f.getProperty(PropertyIdentifier.recordCount, null));
            assertEquals(new UnsignedInteger(1002), f.getProperty(PropertyIdentifier.fileSize, null));
            d1.removeObject(f.getId());
        });
    }

    @Test
    public void readOnly() throws Exception {
        final File file = new File(path);
        final FileObject f = new FileObject(d1, 0, "test", new StreamAccess(file));

        if (file.setWritable(true)) {
            assertEquals(new Boolean(false), f.getProperty(PropertyIdentifier.readOnly, null));
        }

        if (file.setReadable(false)) {
            assertEquals(new Boolean(true), f.getProperty(PropertyIdentifier.readOnly, null));
        }
    }

    @Test
    public void streamReadRecordCount() throws Exception {
        final FileObject f = new FileObject(d1, 0, "test", new StreamAccess(new File(path)));
        TestUtils.assertBACnetServiceException(() -> {
            f.getProperty(PropertyIdentifier.recordCount, null);
        }, ErrorClass.property, ErrorCode.readAccessDenied);
    }

    @Test
    public void recordReadRecordCount() throws Exception {
        final FileObject f = new FileObject(d1, 0, "test", new CrlfDelimitedFileAccess(new File(path)));
        assertEquals(new UnsignedInteger(14), f.getProperty(PropertyIdentifier.recordCount, null));
    }

    @Test
    public void streamWriteRecordCount() throws Exception {
        final FileObject f = new FileObject(d1, 0, "test", new StreamAccess(new File(path)));
        TestUtils.assertBACnetServiceException(() -> {
            f.writeProperty(null, new PropertyValue(PropertyIdentifier.recordCount, new UnsignedInteger(0)));
        }, ErrorClass.property, ErrorCode.writeAccessDenied);
    }

    @Test
    public void recordWriteRecordCount() throws Exception {
        // Write a zero record count.
        doInCopy((file) -> {
            final FileObject f = new FileObject(d1, 0, "test", new CrlfDelimitedFileAccess(file));
            f.writeProperty(null, PropertyIdentifier.recordCount, new UnsignedInteger(0));
            assertEquals(new UnsignedInteger(0), f.getProperty(PropertyIdentifier.recordCount, null));
            assertEquals(new UnsignedInteger(0), f.getProperty(PropertyIdentifier.fileSize, null));
            d1.removeObject(f.getId());
        });

        // Write > 0 and < size record count.
        doInCopy((file) -> {
            final FileObject f = new FileObject(d1, 0, "test", new CrlfDelimitedFileAccess(file));
            f.writeProperty(null, PropertyIdentifier.recordCount, new UnsignedInteger(10));
            assertEquals(new UnsignedInteger(10), f.getProperty(PropertyIdentifier.recordCount, null));
            assertEquals(new UnsignedInteger(665), f.getProperty(PropertyIdentifier.fileSize, null));
            d1.removeObject(f.getId());
        });

        // Write a record count == size
        doInCopy((file) -> {
            final FileObject f = new FileObject(d1, 0, "test", new CrlfDelimitedFileAccess(file));
            f.writeProperty(null, PropertyIdentifier.recordCount, new UnsignedInteger(14));
            assertEquals(new UnsignedInteger(14), f.getProperty(PropertyIdentifier.recordCount, null));
            assertEquals(new UnsignedInteger(922), f.getProperty(PropertyIdentifier.fileSize, null));
            d1.removeObject(f.getId());
        });

        // Write a record count > size
        doInCopy((file) -> {
            final FileObject f = new FileObject(d1, 0, "test", new CrlfDelimitedFileAccess(file));
            f.writeProperty(null, PropertyIdentifier.recordCount, new UnsignedInteger(25));
            assertEquals(new UnsignedInteger(25), f.getProperty(PropertyIdentifier.recordCount, null));
            assertEquals(new UnsignedInteger(944), f.getProperty(PropertyIdentifier.fileSize, null));
            d1.removeObject(f.getId());
        });
    }

    @FunctionalInterface
    static interface InCopyCommand {
        void doInCopy(File file) throws Exception;
    }

    private void doInCopy(final InCopyCommand command) throws Exception {
        final File source = new File(getClass().getClassLoader().getResource(filename).toURI());
        final File target = new File(source.getParentFile(), source.getName() + ".tmp");
        if (target.exists())
            target.delete();
        Files.copy(source.toPath(), target.toPath());
        command.doInCopy(target);
        target.delete();
    }
}
