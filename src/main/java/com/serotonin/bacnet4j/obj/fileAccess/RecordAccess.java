package com.serotonin.bacnet4j.obj.fileAccess;

import com.serotonin.bacnet4j.type.enumerated.FileAccessMethod;

public interface RecordAccess extends FileAccess {
    @Override
    default FileAccessMethod getAccessMethod() {
        return FileAccessMethod.recordAccess;
    }

    @Override
    default boolean hasRecordAccess() {
        return true;
    }
}
