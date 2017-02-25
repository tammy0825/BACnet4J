/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2015 Infinite Automation Software. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * When signing a commercial license with Infinite Automation Software,
 * the following extension to GPL is made. A special exception to the GPL is
 * included to allow you to distribute a combined work that includes BAcnet4J
 * without being obliged to provide the source code for any proprietary components.
 *
 * See www.infiniteautomation.com for commercial license options.
 *
 * @author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.npdu.ip;

public class IpNetworkBuilder {
    private String subnetMask = IpNetwork.DEFAULT_SUBNET_MASK;
    private Integer networkPrefixLength;
    private int port = IpNetwork.DEFAULT_PORT;
    private String localBindAddress = IpNetwork.DEFAULT_BIND_IP;
    private int localNetworkNumber = 0;
    private boolean reuseAddress = false;

    public IpNetworkBuilder withSubnetMask(final String subnetMask) {
        this.subnetMask = subnetMask;
        return this;
    }

    public IpNetworkBuilder withNetworkPrefixLength(final int networkPrefixLength) {
        this.networkPrefixLength = networkPrefixLength;
        return this;
    }

    public IpNetworkBuilder withPort(final int port) {
        this.port = port;
        return this;
    }

    public IpNetworkBuilder withLocalBindAddress(final String localBindAddress) {
        this.localBindAddress = localBindAddress;
        return this;
    }

    public IpNetworkBuilder withLocalNetworkNumber(final int localNetworkNumber) {
        this.localNetworkNumber = localNetworkNumber;
        return this;
    }

    public IpNetworkBuilder withReuseAddress(final boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
        return this;
    }

    public IpNetwork build() {
        return new IpNetwork(port, localBindAddress, subnetMask, networkPrefixLength, localNetworkNumber, reuseAddress);
    }
}
