/*
 * Copyright (C) 2021 Radix IoT LLC. All rights reserved.
 * @Author Terry Packer
 *
 */

package com.serotonin.bacnet4j.adhoc;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Terry Packer
 *
 */
public class BACnetIpUtils {

    private BACnetIpUtils() { }

    public static void main(final String[] args) throws Exception {
        List<InterfaceAddress> usable = listUsableBACnetInterfaces();
        for(InterfaceAddress ifAddr : usable) {
            System.out.println("Address: " + ifAddr.getAddress());
            System.out.println("Broadcast: " + ifAddr.getBroadcast());
        }
    }

    /**
     * List all usable Interface addresses on the local machine.
     *
     * Usable: is not loopback, is up, has broadcast address
     *
     * @return
     * @throws SocketException
     */
    public static List<InterfaceAddress> listUsableBACnetInterfaces() throws SocketException {
        List<InterfaceAddress> usable = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }
            for(InterfaceAddress add : networkInterface.getInterfaceAddresses()) {
                if(add.getBroadcast() != null) {
                    usable.add(add);
                }

            }
        }
        return usable;
    }

}
