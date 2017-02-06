package com.serotonin.bacnet4j.npdu.ip;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IpNetworkUtilsTest {
    @Test
    public void createSubmask() {
        assertEquals(0x00000000L, IpNetworkUtils.createSubmask(0));
        assertEquals(0x80000000L, IpNetworkUtils.createSubmask(1));
        assertEquals(0xC0000000L, IpNetworkUtils.createSubmask(2));
        assertEquals(0xE0000000L, IpNetworkUtils.createSubmask(3));
        assertEquals(0xF0000000L, IpNetworkUtils.createSubmask(4));
        assertEquals(0xF8000000L, IpNetworkUtils.createSubmask(5));
        assertEquals(0xFC000000L, IpNetworkUtils.createSubmask(6));
        assertEquals(0xFE000000L, IpNetworkUtils.createSubmask(7));
        assertEquals(0xFF000000L, IpNetworkUtils.createSubmask(8));
        assertEquals(0xFF800000L, IpNetworkUtils.createSubmask(9));
        assertEquals(0xFFC00000L, IpNetworkUtils.createSubmask(10));
        assertEquals(0xFFE00000L, IpNetworkUtils.createSubmask(11));
        assertEquals(0xFFF00000L, IpNetworkUtils.createSubmask(12));
        assertEquals(0xFFF80000L, IpNetworkUtils.createSubmask(13));
        assertEquals(0xFFFC0000L, IpNetworkUtils.createSubmask(14));
        assertEquals(0xFFFE0000L, IpNetworkUtils.createSubmask(15));
        assertEquals(0xFFFF0000L, IpNetworkUtils.createSubmask(16));
        assertEquals(0xFFFF8000L, IpNetworkUtils.createSubmask(17));
        assertEquals(0xFFFFC000L, IpNetworkUtils.createSubmask(18));
        assertEquals(0xFFFFE000L, IpNetworkUtils.createSubmask(19));
        assertEquals(0xFFFFF000L, IpNetworkUtils.createSubmask(20));
        assertEquals(0xFFFFF800L, IpNetworkUtils.createSubmask(21));
        assertEquals(0xFFFFFC00L, IpNetworkUtils.createSubmask(22));
        assertEquals(0xFFFFFE00L, IpNetworkUtils.createSubmask(23));
        assertEquals(0xFFFFFF00L, IpNetworkUtils.createSubmask(24));
        assertEquals(0xFFFFFF80L, IpNetworkUtils.createSubmask(25));
        assertEquals(0xFFFFFFC0L, IpNetworkUtils.createSubmask(26));
        assertEquals(0xFFFFFFE0L, IpNetworkUtils.createSubmask(27));
        assertEquals(0xFFFFFFF0L, IpNetworkUtils.createSubmask(28));
        assertEquals(0xFFFFFFF8L, IpNetworkUtils.createSubmask(29));
        assertEquals(0xFFFFFFFCL, IpNetworkUtils.createSubmask(30));
        assertEquals(0xFFFFFFFEL, IpNetworkUtils.createSubmask(31));
        assertEquals(0xFFFFFFFFL, IpNetworkUtils.createSubmask(32));
    }
}
