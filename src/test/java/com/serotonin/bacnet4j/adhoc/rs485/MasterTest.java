package com.serotonin.bacnet4j.adhoc.rs485;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.IAmListener;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.Network;
import com.serotonin.bacnet4j.npdu.mstp.Constants;
import com.serotonin.bacnet4j.npdu.mstp.MasterNode;
import com.serotonin.bacnet4j.npdu.mstp.MstpNetwork;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.util.DiscoveryUtils;
import com.serotonin.bacnet4j.util.JsscSerialPortInputStream;
import com.serotonin.bacnet4j.util.JsscSerialPortOutputStream;

import jssc.SerialPort;

/**
 * Run up an MSTP Master and do a WhoIs
 */
public class MasterTest {

    public static void main(final String[] args) throws Exception {
        //Setup Serial Port
        final SerialPort serialPort = new SerialPort("/dev/cu.usbserial-A101OGX5");
        boolean b = serialPort.openPort();
        b = serialPort.setParams(SerialPort.BAUDRATE_38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);

        try (JsscSerialPortInputStream in = new JsscSerialPortInputStream(serialPort);
             JsscSerialPortOutputStream out = new JsscSerialPortOutputStream(serialPort)) {
            //Create Local Device
            MasterNode masterNode = new MasterNode("test", in, out, (byte) 0, 1);
            masterNode.setMaxInfoFrames(Constants.MAX_INFO_FRAMES);
            masterNode.setMaxMaster(Constants.MAX_MASTER);
            masterNode.setUsageTimeout(Constants.USAGE_TIMEOUT);
            Network network = new MstpNetwork(masterNode, 0);
            Transport transport = new DefaultTransport(network);
            transport.setTimeout(Transport.DEFAULT_TIMEOUT);
            transport.setSegTimeout(Transport.DEFAULT_SEG_TIMEOUT);
            transport.setSegWindow(Transport.DEFAULT_SEG_WINDOW);
            transport.setRetries(Transport.DEFAULT_RETRIES);

            LocalDevice localDevice = new LocalDevice(0, transport);
            localDevice.getDeviceObject().writePropertyInternal(PropertyIdentifier.objectName, new CharacterString("Test"));
            //localDevice.getDeviceObject().writePropertyInternal(PropertyIdentifier.vendorName, new CharacterString("InfiniteAutomation"));
            localDevice.getDeviceObject().writePropertyInternal(PropertyIdentifier.modelName, new CharacterString("BACnet4J"));


            //Create listener
            IAmListener listener = (RemoteDevice d) -> {
                try {
                    System.out.println("Found device" + d);
                    DiscoveryUtils.getExtendedDeviceInformation(localDevice, d);
                } catch (BACnetException e) {
                    e.printStackTrace();
                }
            };

            localDevice.getEventHandler().addListener(listener);

            localDevice.initialize();

            //Send WhoIs
            localDevice.sendGlobalBroadcast(new WhoIsRequest());

            //Wait for responses
            int count = 30;
            while (count > 0) {
                Thread.sleep(1000);
                count--;
            }
            localDevice.terminate();

        }finally {
           serialPort.closePort();
        }
    }
}
