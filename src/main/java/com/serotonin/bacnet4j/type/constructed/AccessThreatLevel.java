package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class AccessThreatLevel extends UnsignedInteger {
    private static final long serialVersionUID = 1140739710414372762L;

    public AccessThreatLevel(final int value) {
        super(value);
        if (value < 0 || value > 100)
            throw new IllegalArgumentException("value must be between 0 and 100 inclusive. Given " + value);
    }
}
