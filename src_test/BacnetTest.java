import java.util.List;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.RequestUtils;

public class BacnetTest {

    static LocalDevice localDevice;

    public static void main(String[] args) {
        IpNetwork network = new IpNetworkBuilder().
            withBroadcast("192.168.1.255", 24).
        build();
        Transport transport = new DefaultTransport(network);
        localDevice = new LocalDevice(1234, transport);
        try {
            localDevice.initialize();
            localDevice.getEventHandler().addListener(new Listener());
            localDevice.sendGlobalBroadcast(new WhoIsRequest());

            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            localDevice.terminate();
        }
    }

    static class Listener extends DeviceEventAdapter {
        @Override
        public void iAmReceived(final RemoteDevice d) {
            System.out.println("IAm received from " + d);
            System.out.println("Segmentation: " + d.getSegmentationSupported());
            //d.setSegmentationSupported(Segmentation.noSegmentation);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        getExtendedDeviceInformation(d);
                        System.out.println("Done getting extended information");

                        List oids = ((SequenceOf) RequestUtils.sendReadPropertyAllowNull(localDevice, d,
                                d.getObjectIdentifier(), PropertyIdentifier.objectList)).getValues();
                        System.out.println(oids);
                    } catch (BACnetException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    static void getExtendedDeviceInformation(RemoteDevice d) throws BACnetException {
        ObjectIdentifier oid = d.getObjectIdentifier();

        // Get the device's supported services
        System.out.println("protocolServicesSupported");
        ReadPropertyAck ack = localDevice.send(d, new ReadPropertyRequest(oid, PropertyIdentifier.protocolServicesSupported)).get();
        //d.setServicesSupported((ServicesSupported) ack.getValue());

        System.out.println("objectName");
        ack = localDevice.send(d, new ReadPropertyRequest(oid, PropertyIdentifier.objectName)).get();
        // d.setName(ack.getValue().toString());

        System.out.println("protocolVersion");
        ack = localDevice.send(d, new ReadPropertyRequest(oid, PropertyIdentifier.protocolVersion)).get();
        //d.setProtocolVersion((UnsignedInteger) ack.getValue());

        //        System.out.println("protocolRevision");
        //        ack = localDevice.send(d, new ReadPropertyRequest(oid, PropertyIdentifier.protocolRevision)).get();
        //        d.setProtocolRevision((UnsignedInteger) ack.getValue());
    }

}
