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
