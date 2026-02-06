package org.dynmap.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class IpAddressMatcherTest {

    // IPv4 exact match tests
    @Test
    public void testIPv4ExactMatch() {
        IpAddressMatcher matcher = new IpAddressMatcher("192.168.1.100");
        assertTrue(matcher.matches("192.168.1.100"));
    }

    @Test
    public void testIPv4ExactMatchNoMatch() {
        IpAddressMatcher matcher = new IpAddressMatcher("192.168.1.100");
        assertFalse(matcher.matches("192.168.1.101"));
    }

    @Test
    public void testIPv4Localhost() {
        IpAddressMatcher matcher = new IpAddressMatcher("127.0.0.1");
        assertTrue(matcher.matches("127.0.0.1"));
        assertFalse(matcher.matches("127.0.0.2"));
    }

    // IPv4 CIDR tests
    @Test
    public void testIPv4Cidr24() {
        IpAddressMatcher matcher = new IpAddressMatcher("192.168.1.0/24");
        assertTrue(matcher.matches("192.168.1.0"));
        assertTrue(matcher.matches("192.168.1.1"));
        assertTrue(matcher.matches("192.168.1.255"));
        assertFalse(matcher.matches("192.168.2.1"));
    }

    @Test
    public void testIPv4Cidr16() {
        IpAddressMatcher matcher = new IpAddressMatcher("192.168.0.0/16");
        assertTrue(matcher.matches("192.168.0.1"));
        assertTrue(matcher.matches("192.168.255.255"));
        assertFalse(matcher.matches("192.169.0.1"));
    }

    @Test
    public void testIPv4Cidr8() {
        IpAddressMatcher matcher = new IpAddressMatcher("10.0.0.0/8");
        assertTrue(matcher.matches("10.0.0.1"));
        assertTrue(matcher.matches("10.255.255.255"));
        assertFalse(matcher.matches("11.0.0.1"));
    }

    @Test
    public void testIPv4Cidr32() {
        IpAddressMatcher matcher = new IpAddressMatcher("192.168.1.100/32");
        assertTrue(matcher.matches("192.168.1.100"));
        assertFalse(matcher.matches("192.168.1.101"));
    }

    @Test
    public void testIPv4CidrZero() {
        IpAddressMatcher matcher = new IpAddressMatcher("0.0.0.0/0");
        assertTrue(matcher.matches("192.168.1.1"));
        assertTrue(matcher.matches("10.0.0.1"));
        assertTrue(matcher.matches("255.255.255.255"));
    }

    // IPv6 tests
    @Test
    public void testIPv6ExactMatch() {
        IpAddressMatcher matcher = new IpAddressMatcher("::1");
        assertTrue(matcher.matches("::1"));
        assertTrue(matcher.matches("0:0:0:0:0:0:0:1"));
    }

    @Test
    public void testIPv6Cidr() {
        IpAddressMatcher matcher = new IpAddressMatcher("2001:db8::/32");
        assertTrue(matcher.matches("2001:db8::1"));
        assertTrue(matcher.matches("2001:db8:ffff:ffff:ffff:ffff:ffff:ffff"));
        assertFalse(matcher.matches("2001:db9::1"));
    }

    // Cross-family rejection tests
    @Test
    public void testIPv4DoesNotMatchIPv6() {
        IpAddressMatcher matcher = new IpAddressMatcher("192.168.1.0/24");
        assertFalse(matcher.matches("::1"));
    }

    @Test
    public void testIPv6DoesNotMatchIPv4() {
        IpAddressMatcher matcher = new IpAddressMatcher("::1");
        assertFalse(matcher.matches("127.0.0.1"));
    }

    // Edge cases
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidIPAddress() {
        new IpAddressMatcher("invalid.ip.address");
    }

    @Test
    public void testPrivateNetworkRanges() {
        // Class A private
        IpAddressMatcher classA = new IpAddressMatcher("10.0.0.0/8");
        assertTrue(classA.matches("10.0.0.1"));
        assertTrue(classA.matches("10.255.255.254"));

        // Class B private
        IpAddressMatcher classB = new IpAddressMatcher("172.16.0.0/12");
        assertTrue(classB.matches("172.16.0.1"));
        assertTrue(classB.matches("172.31.255.254"));
        assertFalse(classB.matches("172.32.0.1"));

        // Class C private
        IpAddressMatcher classC = new IpAddressMatcher("192.168.0.0/16");
        assertTrue(classC.matches("192.168.0.1"));
        assertTrue(classC.matches("192.168.255.254"));
    }
}
