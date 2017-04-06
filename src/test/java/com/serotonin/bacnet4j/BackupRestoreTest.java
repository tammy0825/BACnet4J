package com.serotonin.bacnet4j;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.serotonin.bacnet4j.event.DefaultReinitializeDeviceHandler;
import com.serotonin.bacnet4j.npdu.test.TestNetwork;
import com.serotonin.bacnet4j.npdu.test.TestNetworkMap;
import com.serotonin.bacnet4j.obj.FileObject;
import com.serotonin.bacnet4j.obj.fileAccess.StreamAccess;
import com.serotonin.bacnet4j.persistence.FilePersistence;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.constructed.BACnetArray;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.BackupClient;
import com.serotonin.bacnet4j.util.RestoreClient;

/**
 * Testing of backup and restore procedures as described in 19.1.
 *
 * @author Matthew
 */
public class BackupRestoreTest {
    private final TestNetworkMap map = new TestNetworkMap();
    private final LocalDevice a = new LocalDevice(1, new DefaultTransport(new TestNetwork(map, 1, 0)));
    private final LocalDevice b = new LocalDevice(2, new DefaultTransport(new TestNetwork(map, 2, 0)));

    private File file;

    @Before
    public void before() throws Exception {
        final String filename = "backupTest.txt";
        final String path = getClass().getClassLoader().getResource(filename).getPath();
        file = new File(path);

        b.setPersistence(new FilePersistence(file));

        a.initialize();
        b.initialize();
    }

    @After
    public void after() {
        a.terminate();
        b.terminate();
    }

    /**
     * Perform a "real" backup and restore.
     *
     * @throws Exception
     */
    @Test
    public void backupAndRestore() throws Exception {
        // Backup
        final BackupClient backupClient = new BackupClient(a, 2, null);
        final List<File> files = backupClient.begin(file.getParentFile());

        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean restoreSuccess = new AtomicBoolean(false);
        b.setReinitializeDeviceHandler(new DefaultReinitializeDeviceHandler() {
            @Override
            protected void finishRestore(final LocalDevice localDevice) {
                // NOTE: The configuration files property is used here, but for production purposes the file objects
                // should be used as the definitive source of configuration files.
                final BACnetArray<ObjectIdentifier> configurationFiles = localDevice.getDeviceObject()
                        .get(PropertyIdentifier.configurationFiles);
                assertEquals(1, configurationFiles.size());

                // Verify the file copy here because the file will be deleted in the clean up.
                final FileObject fo = (FileObject) localDevice.getObject(configurationFiles.get(0));
                final File restoredFile = ((StreamAccess) fo.getFileAccess()).getFile();

                try {
                    TestUtils.assertFileContentEquals(restoredFile, files.get(0));
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }

                restoreSuccess.set(true);
                latch.countDown();
            }
        });

        try {
            assertEquals(1, files.size());
            TestUtils.assertFileContentEquals(file, files.get(0));

            // Restore
            final RestoreClient restoreClient = new RestoreClient(a, 2, null);
            restoreClient.begin(files);

            latch.await();
            assertEquals(true, restoreSuccess.get());
        } finally {
            for (final File f : files) {
                f.delete();
            }
        }
    }
}
