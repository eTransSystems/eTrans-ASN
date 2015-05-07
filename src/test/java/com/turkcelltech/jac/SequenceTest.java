package com.turkcelltech.jac;

import com.chaosinmotion.asn1.BerInputStream;
import com.chaosinmotion.asn1.BerNode;
import com.chaosinmotion.asn1.BerOutputStream;
import com.etrans2020.semi.ServiceRequest;
import com.etranssystems.asn1.generated.dsrc.*;
import junit.framework.TestCase;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.util.Arrays;

public class SequenceTest extends TestCase {

    private String getHexEncodedString( Sequence sequence ) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(128);
        BerOutputStream berOutputStream = new BerOutputStream( byteArrayOutputStream );
        sequence.encode(berOutputStream);
        byte[] resultBytes =  byteArrayOutputStream.toByteArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < resultBytes.length; i++) {
            sb.append(String.format("%02X", resultBytes[i]));
        }
        return sb.toString();
    }

    public void testDecode() throws Exception {
        byte[] hexBytes = "30148002009B8101018204BABAEEEEA305810300DAB3".getBytes();
        Hex hex = new Hex();
        byte[] msgBytes = hex.decode(hexBytes);
        ByteArrayInputStream stream = new ByteArrayInputStream( msgBytes );
        BerInputStream in = new BerInputStream( stream);
        ServiceRequest sr = new ServiceRequest();
        sr.decode(in);
        assertEquals(55987, sr.destination.port.getValue());
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
        boolean arraysEqual = Arrays.equals(addressBytes, sr.destination.address.ipv4Address.getValue());
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

    public void testCopySequenceVariables() throws Exception {
        Position3D position3D = new Position3D();
        position3D.lat.setValue( 1 );
        position3D.longElement.setValue( 2 );

        assertEquals(true, position3D.lat.isInitialized);
        assertEquals(true, position3D.longElement.isInitialized);
        assertEquals(false, position3D.elevation.isInitialized);

        Position3D position3DOther = new Position3D();
        position3DOther.copySequenceVariables(position3D);

        assertEquals(true, position3DOther.lat.isInitialized);
        assertEquals(true, position3DOther.longElement.isInitialized);
        assertEquals(false, position3DOther.elevation.isInitialized);
    }

    public void testCopySequenceVariables2() throws Exception {
        GeoRegion geoRegion = new GeoRegion();
        geoRegion.seCorner.lat.setValue( 31 );
        geoRegion.seCorner.longElement.setValue(32);
        geoRegion.nwCorner.lat.setValue(33);
        geoRegion.nwCorner.longElement.setValue(34);

        assertEquals(true, geoRegion.seCorner.lat.isInitialized);
        assertEquals(true, geoRegion.seCorner.longElement.isInitialized);
        assertEquals(false, geoRegion.seCorner.isInitialized);

        GeoRegion geoRegion2 = new GeoRegion();
        geoRegion2.copySequenceVariables(geoRegion);
    }

    public void testCopySequenceVariablesWithOptionalSequence() throws Exception {
        VehicleSituationStatus vehicleSituationStatus = new VehicleSituationStatus();
        vehicleSituationStatus.lights.setValue(0);

        assertEquals(true, vehicleSituationStatus.lights.isInitialized);
        assertEquals(false, vehicleSituationStatus.tirePressure.isInitialized);

        VehicleSituationStatus vehicleSituationStatus2 = new VehicleSituationStatus();
        vehicleSituationStatus2.copySequenceVariables(vehicleSituationStatus);

        assertEquals(true, vehicleSituationStatus2.lights.isInitialized);
        assertEquals(false, vehicleSituationStatus2.tirePressure.isInitialized);
    }

    public void testCopySequenceVariablesWithNestedSequenceOf() throws Exception {
        Approach approach = new Approach();
        approach.id.setValue( 7 );
        VehicleReferenceLane vehicleReferenceLane = new VehicleReferenceLane();
        vehicleReferenceLane.laneNumber.setValue( 8 );
        approach.drivingLanes.addElement(vehicleReferenceLane);
        assertFalse(vehicleReferenceLane.connectsTo.isInitialized );
        assertEquals("300A800107A1053003800108", getHexEncodedString(approach));

        Approach approach2 = new Approach();
        approach2.copySequenceVariables(approach);
        assertFalse( approach2.drivingLanes.getElement(0).connectsTo.isInitialized );
        assertEquals("300A800107A1053003800108", getHexEncodedString(approach2));
    }

    public void testEncodeSequence() throws Exception {
        Position3D position3D = new Position3D();
        position3D.lat.setValue( 1 );
        position3D.longElement.setValue( 2 );

        assertEquals(true, position3D.lat.isInitialized);
        assertEquals(true, position3D.longElement.isInitialized);
        assertEquals(false, position3D.elevation.isInitialized);

        assertEquals("3006800101810102", getHexEncodedString(position3D));
    }

    public void testEncodeEmbeddedSequence() throws Exception {
        GeoRegion geoRegion = new GeoRegion();
        geoRegion.seCorner.lat.setValue( 31 );
        geoRegion.seCorner.longElement.setValue(32);
        geoRegion.nwCorner.lat.setValue(33);
        geoRegion.nwCorner.longElement.setValue(34);

        assertEquals("3010A006800121810122A10680011F810120", getHexEncodedString(geoRegion));

        GeoRegion geoRegion2 = new GeoRegion();
        geoRegion2.copySequenceVariables(geoRegion);

        assertEquals("3010A006800121810122A10680011F810120", getHexEncodedString(geoRegion2));
    }
}