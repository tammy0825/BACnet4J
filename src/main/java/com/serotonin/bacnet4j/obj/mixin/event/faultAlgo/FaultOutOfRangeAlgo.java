package com.serotonin.bacnet4j.obj.mixin.event.faultAlgo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.FaultParameter.AbstractFaultParameter;
import com.serotonin.bacnet4j.type.constructed.FaultParameter.FaultOutOfRange;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Reliability;
import com.serotonin.bacnet4j.type.primitive.Real;

//13.4.7
public class FaultOutOfRangeAlgo extends FaultAlgorithm {
    static final Logger LOG = LoggerFactory.getLogger(FaultOutOfRangeAlgo.class);

    private final PropertyIdentifier minimumNormalValueProperty;
    private final PropertyIdentifier maximumNormalValueProperty;
    private final PropertyIdentifier currentReliabilityProperty;

    public FaultOutOfRangeAlgo() {
        this(null, null, null);
    }

    public FaultOutOfRangeAlgo(final PropertyIdentifier minimumNormalValueProperty,
            final PropertyIdentifier maximumNormalValueProperty, final PropertyIdentifier currentReliabilityProperty) {
        this.minimumNormalValueProperty = minimumNormalValueProperty;
        this.maximumNormalValueProperty = maximumNormalValueProperty;
        this.currentReliabilityProperty = currentReliabilityProperty;
    }

    @Override
    public Reliability evaluateIntrinsic(final Encodable oldMonitoredValue, final Encodable newMonitoredValue,
            final BACnetObject bo) {
        return evaluate( //
                bo.get(minimumNormalValueProperty), //
                bo.get(maximumNormalValueProperty), //
                (Real) newMonitoredValue, //
                bo.get(currentReliabilityProperty));
    }

    @Override
    public Reliability evaluateAlgorithmic(final Encodable oldMonitoredValue, final Encodable newMonitoredValue,
            final Reliability currentReliability, final AbstractFaultParameter parameters) {
        final FaultOutOfRange p = (FaultOutOfRange) parameters;
        return evaluate( //
                p.getMinNormalValue().getValue(), //
                p.getMaxNormalValue().getValue(), //
                (Real) newMonitoredValue, //
                currentReliability);
    }

    private static Reliability evaluate(final Real minimumNormalValue, final Real maximumNormalValue,
            final Real monitoredValue, Reliability currentReliability) {
        if (currentReliability == null)
            currentReliability = Reliability.noFaultDetected;

        final float min = minimumNormalValue.floatValue();
        final float max = maximumNormalValue.floatValue();
        final float value = monitoredValue.floatValue();

        Reliability newReliability = null;

        if (Reliability.noFaultDetected.equals(currentReliability) && value < min) // (a)
            newReliability = Reliability.underRange;
        else if (Reliability.noFaultDetected.equals(currentReliability) && value > max) // (b)
            newReliability = Reliability.overRange;
        else if (Reliability.underRange.equals(currentReliability) && value > max) // (c)
            newReliability = Reliability.overRange;
        else if (Reliability.overRange.equals(currentReliability) && value < min) // (d)
            newReliability = Reliability.underRange;
        else if (Reliability.underRange.equals(currentReliability) && value >= min) // (e)
            newReliability = Reliability.noFaultDetected;
        else if (Reliability.overRange.equals(currentReliability) && value <= max) // (f)
            newReliability = Reliability.noFaultDetected;

        if (newReliability != null)
            LOG.debug("FaultState evaluated new reliability: {}", newReliability);

        return newReliability;
    }
}
