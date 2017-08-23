package com.serotonin.bacnet4j.adhoc.rs485;

import java.util.Arrays;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.npdu.mstp.MasterNode;
import com.serotonin.bacnet4j.npdu.mstp.MstpNetwork;
import com.serotonin.bacnet4j.npdu.mstp.SlaveNode;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.util.RemoteDeviceDiscoverer;
import com.serotonin.bacnet4j.util.sero.JsscSerialPortInputStream;
import com.serotonin.bacnet4j.util.sero.JsscSerialPortOutputStream;
import com.serotonin.bacnet4j.util.sero.ThreadUtils;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class PortTest {
    public static void main(final String[] args) throws Exception {
        System.out.println(Arrays.toString(SerialPortList.getPortNames()));

        final SerialPort serialPort = new SerialPort("COM4");
        boolean b = serialPort.openPort();
        System.out.println(b);
        b = serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        System.out.println(b);

        //        listen(serialPort);
        //        slave(serialPort);
        master(serialPort);

        b = serialPort.closePort();
        System.out.println(b);
    }

    static void master(final SerialPort serialPort) throws Exception {
        try (JsscSerialPortInputStream in = new JsscSerialPortInputStream(serialPort);
                JsscSerialPortOutputStream out = new JsscSerialPortOutputStream(serialPort)) {
            final MasterNode node = new MasterNode("test", in, out, (byte) 3, 2);
            final MstpNetwork network = new MstpNetwork(node, 0);
            final Transport transport = new DefaultTransport(network);
            final LocalDevice ld = new LocalDevice(1970, transport);
            ld.initialize();

            final RemoteDeviceDiscoverer rdd = ld.startRemoteDeviceDiscovery((r) -> {
                System.out.println(prefix() + "Device: " + r + ", " + r.getName());
            });

            ThreadUtils.sleep(60000);

            System.out.println(rdd.getRemoteDevices());
            rdd.stop();

            //            System.out.println(node.getBytesIn());
            //            System.out.println(node.getBytesOut());

            ld.terminate();
        }
    }

    static void slave(final SerialPort serialPort) throws Exception {
        try (JsscSerialPortInputStream in = new JsscSerialPortInputStream(serialPort);
                JsscSerialPortOutputStream out = new JsscSerialPortOutputStream(serialPort)) {
            final SlaveNode node = new SlaveNode("test", in, out, (byte) 3);
            final MstpNetwork network = new MstpNetwork(node, 0);
            final Transport transport = new DefaultTransport(network);
            final LocalDevice ld = new LocalDevice(1968, transport);
            ld.initialize();

            ld.startRemoteDeviceDiscovery((r) -> {
                System.out.println(r.getInstanceNumber());
            });

            ThreadUtils.sleep(10000);
            System.out.println(node.getBytesIn());
            System.out.println(node.getBytesOut());

            ld.terminate();
        }
    }

    static void listen(final SerialPort serialPort) throws SerialPortException {
        while (true) {
            //            final byte[] buf = serialPort.readBytes();
            //            if (buf != null) {
            //                System.out.println(Arrays.toString(buf));
            //            }

            final String s = serialPort.readHexString();
            if (s != null) {
                System.out.println(prefix() + s.toLowerCase());
                //            } else {
                //                System.out.println("null was returned");
            }
        }
    }

    static String prefix() {
        return System.currentTimeMillis() % 10000000 + ": ";
    }
}
