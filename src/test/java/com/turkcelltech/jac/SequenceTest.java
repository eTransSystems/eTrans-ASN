package com.turkcelltech.jac;

import com.chaosinmotion.asn1.BerInputStream;
import com.chaosinmotion.asn1.BerNode;
import com.etrans2020.semi.ServiceRequest;
import junit.framework.TestCase;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayInputStream;
import java.net.InetAddress;
import java.util.Arrays;

public class SequenceTest extends TestCase {

    public void testDecode() throws Exception {
        byte[] hexBytes = "30148002009B8101018204BABAEEEEA305810300DAB3".getBytes();
        Hex hex = new Hex();
        byte[] msgBytes = hex.decode(hexBytes);
        ByteArrayInputStream stream = new ByteArrayInputStream( msgBytes );
        BerInputStream in = new BerInputStream( stream);
        ServiceRequest sr = new ServiceRequest();
        sr.decode(in);
        assertEquals( 55987, sr.destination.port.getValue());
        BerNode node = sr.destination.address.getCurrentChoice();
        assertEquals( null, node );
    }

    public void testDecodeIpv4() throws Exception {
        byte[] hexBytes = "301C8002009B8101018204BABAEEEEA30DA00680040A000001810300DAB3".getBytes();
        Hex hex = new Hex();
        byte[] msgBytes = hex.decode(hexBytes);
        ByteArrayInputStream stream = new ByteArrayInputStream( msgBytes );
        BerInputStream in = new BerInputStream( stream);
        ServiceRequest sr = new ServiceRequest();
        sr.decode(in);
        assertEquals( 55987, sr.destination.port.getValue());
        byte[] addressBytes = InetAddress.getByName("10.0.0.1").getAddress();
        boolean arraysEqual = Arrays.equals( addressBytes, sr.destination.address.ipv4Address.getValue() );
        assertEquals( true, arraysEqual );
    }

    public void testDecodeIpv6() throws Exception {
        byte[] hexBytes = "301C8002009B8101018204BABAEEEEA30DA00681040A000001810300DAB3".getBytes();
        Hex hex = new Hex();
        byte[] msgBytes = hex.decode(hexBytes);
        ByteArrayInputStream stream = new ByteArrayInputStream( msgBytes );
        BerInputStream in = new BerInputStream( stream);
        ServiceRequest sr = new ServiceRequest();
        sr.decode(in);
        assertEquals( 55987, sr.destination.port.getValue());
        byte[] addressBytes = InetAddress.getByName("10.0.0.1").getAddress();
        boolean arraysEqual = Arrays.equals( addressBytes, sr.destination.address.ipv6Address.getValue() );
        assertEquals( true, arraysEqual );
    }
}