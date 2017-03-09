package com.serotonin.bacnet4j.type.eventParameter;

import com.serotonin.bacnet4j.obj.mixin.event.eventAlgo.EventAlgorithm;
import com.serotonin.bacnet4j.type.constructed.BaseType;

abstract public class AbstractEventParameter extends BaseType {
    abstract public EventAlgorithm createEventAlgorithm();
}
