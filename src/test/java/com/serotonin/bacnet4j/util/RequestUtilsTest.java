package com.serotonin.bacnet4j.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.ServiceFuture;
import com.serotonin.bacnet4j.apdu.Abort;
import com.serotonin.bacnet4j.cache.CachePolicies;
import com.serotonin.bacnet4j.exception.AbortAPDUException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.ServiceTooBigException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyMultipleAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyMultipleRequest;
import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult.Result;
import com.serotonin.bacnet4j.type.constructed.ReadAccessSpecification;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import com.serotonin.bacnet4j.type.enumerated.AbortReason;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

public class RequestUtilsTest {
    /**
     * Test that occasionally throws ServiceTooBig exception so that RequestUtils is forced to repartition the request
     * on the fly.
     *
     * @throws BACnetException
     */
    @Test
    public void repartition() throws BACnetException {
        final ServicesSupported servicesSupported = new ServicesSupported();
        servicesSupported.setAll(true);

        final CachePolicies policies = new CachePolicies();

        final LocalDevice d = Mockito.mock(LocalDevice.class);
        when(d.getCachePolicies()).thenReturn(policies);

        final RemoteDevice rd = new RemoteDevice(d, 123);
        rd.setDeviceProperty(PropertyIdentifier.protocolServicesSupported, servicesSupported);
        rd.setDeviceProperty(PropertyIdentifier.segmentationSupported, Segmentation.segmentedBoth);
        rd.setMaxReadMultipleReferences(200);

        final AtomicInteger exceptionCount = new AtomicInteger();
        when(d.send(any(RemoteDevice.class), any(ReadPropertyMultipleRequest.class)))
                .thenAnswer(new Answer<ServiceFuture>() {
                    private int threshold = 50;

                    @Override
                    public ServiceFuture answer(final InvocationOnMock invocation) throws Throwable {
                        final ReadPropertyMultipleRequest req = (ReadPropertyMultipleRequest) invocation.getArgument(1);
                        if (req.getNumberOfProperties() < threshold) {
                            final SequenceOf<ReadAccessResult> accessResults = new SequenceOf<>();
                            for (final ReadAccessSpecification spec : req.getListOfReadAccessSpecs()) {
                                final SequenceOf<Result> results = new SequenceOf<>();
                                for (final PropertyReference ref : spec.getListOfPropertyReferences()) {
                                    results.add(new Result(ref.getPropertyIdentifier(), ref.getPropertyArrayIndex(),
                                            BinaryPV.active));
                                }
                                accessResults.add(new ReadAccessResult(spec.getObjectIdentifier(), results));
                            }

                            if (threshold == 100)
                                threshold = 50;
                            else
                                threshold--;

                            final ReadPropertyMultipleAck ack = new ReadPropertyMultipleAck(accessResults);
                            return new ServiceFuture() {
                                @SuppressWarnings("unchecked")
                                @Override
                                public <T extends AcknowledgementService> T get() throws BACnetException {
                                    return (T) ack;
                                }
                            };
                        }

                        exceptionCount.incrementAndGet();

                        final int mod = exceptionCount.get() % 3;
                        if (mod == 1)
                            throw new ServiceTooBigException("much too big");
                        else if (mod == 2)
                            throw new AbortAPDUException(
                                    new Abort(false, (byte) 0, AbortReason.bufferOverflow.intValue()));
                        throw new AbortAPDUException(
                                new Abort(false, (byte) 0, AbortReason.segmentationNotSupported.intValue()));
                    }
                });

        // Add quite a lot of references to the request.
        final PropertyReferences refs = new PropertyReferences();
        final ObjectIdentifier oid = new ObjectIdentifier(ObjectType.analogInput, 0);
        for (int i = 0; i < 1000; i++) {
            refs.add(oid, PropertyIdentifier.forId(i));
        }

        final PropertyValues pvs = RequestUtils.readProperties(d, rd, refs, null);
        assertEquals(1000, pvs.size());
        assertEquals(9, exceptionCount.get());

        for (int i = 0; i < 1000; i++) {
            assertEquals(BinaryPV.active, pvs.getNoErrorCheck(oid, PropertyIdentifier.forId(i)));
        }
    }
}
