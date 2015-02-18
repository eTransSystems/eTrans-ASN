package com.turkcelltech.jac;

import com.chaosinmotion.asn1.BerInputStream;
import com.chaosinmotion.asn1.BerOutputStream;
import com.etranssystems.asn1.generated.dsrc.NodeList;
import com.etranssystems.asn1.generated.dsrc.Offsets;
import junit.framework.TestCase;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SequenceOfTest extends TestCase {

    public void testDecode() throws Exception {
        byte[] hexBytes = "300C0404000A00140404001E0028".getBytes();
        Hex hex = new Hex();
        byte[] msgBytes = hex.decode(hexBytes);
        ByteArrayInputStream stream = new ByteArrayInputStream( msgBytes );
        BerInputStream in = new BerInputStream( stream);
        NodeList nodeList = new NodeList();
        nodeList.decode(in);
        assertEquals( 2, nodeList.getList().size() );
        Offsets offsets = nodeList.getElement(0);
    }

    public void testEncode() throws Exception {
        NodeList nodeList = new NodeList();
        byte[] offsetData1 = new byte[] { 0x00, 0x0A, 0x00, 0x14 };
        Offsets offsets1 = new Offsets( offsetData1 );
        byte[] offsetData2 = new byte[] { 0x00, 0x1E, 0x00, 0x28 };
        Offsets offsets2 = new Offsets( offsetData2 );
        nodeList.addElement( offsets1 );
        nodeList.addElement( offsets2 );

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(128);
        BerOutputStream berOutputStream = new BerOutputStream( byteArrayOutputStream );
        nodeList.encode(berOutputStream);
        byte[] resultBytes =  byteArrayOutputStream.toByteArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < resultBytes.length; i++) {
            sb.append(String.format("%02X", resultBytes[i]));
        }
        assertEquals("300C0404000A00140404001E0028", sb.toString());

    }
}