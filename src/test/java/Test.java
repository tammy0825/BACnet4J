import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkUtils;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyMultipleAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyMultipleRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.ReadAccessSpecification;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class Test {
    public static void main(final String[] args) throws Exception {
        final IpNetwork network = new IpNetworkBuilder() //
                .withNetworkPrefixLength(24) //
                .withPort(47809) //
                .build();

        final LocalDevice l = new LocalDevice(321, new DefaultTransport(network)).initialize();

        try {
            write(l, 14F, 10);
            //            write(l, null, 10);
            read(l);
        } finally {
            l.terminate();
        }
    }

    static void write(final LocalDevice l, final Float value, final int priority) throws BACnetException {
        final Address address = IpNetworkUtils.toAddress("192.168.0.103", 47808);
        final WritePropertyRequest req = new WritePropertyRequest(new ObjectIdentifier(ObjectType.analogValue, 0),
                PropertyIdentifier.presentValue, null, value == null ? Null.instance : new Real(value),
                new UnsignedInteger(priority));

        l.send(address, req).get();
    }

    static void read(final LocalDevice l) throws BACnetException {
        final Address address = IpNetworkUtils.toAddress("192.168.0.102", 47808);
        final ObjectIdentifier oid = new ObjectIdentifier(ObjectType.analogValue, 1);
        final ReadPropertyMultipleRequest req = new ReadPropertyMultipleRequest(new SequenceOf<>( //
                new ReadAccessSpecification(oid, PropertyIdentifier.presentValue),
                new ReadAccessSpecification(oid, PropertyIdentifier.priorityArray),
                new ReadAccessSpecification(oid, PropertyIdentifier.currentCommandPriority)));

        final ReadPropertyMultipleAck ack = l.send(address, req).get();

        System.out.println("Present value: " + ack.getListOfReadAccessResults().get(0));
        System.out.println("Priority array: " + ack.getListOfReadAccessResults().get(1));
        System.out.println("Priority: " + ack.getListOfReadAccessResults().get(2));
    }
}
