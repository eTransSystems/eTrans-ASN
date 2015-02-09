/*  TestUtil.java
 *
 *  Created on Jun 4, 2006 by William Edward Woody
 */

package com.chaosinmotion.asn1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import java.util.Date;

/**
 * Provides some utility methods for debugging BER ASN.1 packets
 */
public class TestUtil
{
    private static byte[] gBlock = new byte[16];
    
    private static char toHex(int c)
    {
        if (c < 10) return (char)(c + '0');
        else return (char)(c + 'A' - 10);
    }
    
    private static void printPos(int pos)
    {
        for (int i = 0; i < 8; ++i) {
            System.out.print(toHex(0x0F & (pos >> (28 - i * 4))));
        }
        System.out.print(": ");
        
    }
    /**
     * Internal routine prints a block of data
     * @param label
     * @param data
     * @param start
     * @param len
     */
    private static void printData(int pos, byte[] data, int start, int len)
    {
        int i;
        int wlen;
        StringBuffer buffer = new StringBuffer();
        
        printPos(pos);
        
        wlen = len;
        if (wlen > 16) wlen = 16;
        for (i = 0; i < wlen; ++i) {
            byte d = data[i + start];
            buffer.append(toHex(0x0F & (d >> 4)));
            buffer.append(toHex(0x0F & d));
            buffer.append(' ');
            
            if (i == 7) buffer.append(' ');
        }
        for (; i < 16; ++i) {
            buffer.append("   ");
            if (i == 7) buffer.append(' ');
        }
        
        buffer.append(": ");
        
        for (i = 0; i < wlen; ++i) {
            byte d = data[i + start];
            if ((d >= 32) && (d < 127)) buffer.append((char)d);
            else buffer.append('.');
            
            if (i == 7) buffer.append(' ');
        }
        
        System.out.println(buffer.toString());
    }
    
    /**
     * Prints the block of data to standard out
     * @param data
     */
    public static void printData(byte[] data)
    {
        int i;
        int len = data.length;
        
        for (i = 0; i < len; i += 16) {
            int l = len - i;
            if (l > 16) l = 16;
            printData(i,data,i,l);
        }
    }
    
    /**
     * Prints BER data in a structured way
     * @param data
     */
    public static void structurePrintData(byte[] data)
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        BerInputStream in = new BerInputStream(bais);
        
        try {
            internalPrintStructure(0,in);
            printPos((int)in.getReadBytes());
            System.out.println();
        }
        catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }
    
    private static void internalPrintData(BerInputStream in, int len) throws IOException
    {
        int rlen;
        int pos = 0;
        int rpos;
        
        while (pos < len) {
            rlen = len - pos;
            if (rlen > 16) rlen = 16;
            rpos = (int)in.getReadBytes();
            rlen = in.read(gBlock,0,rlen);
            if (rlen <= 0) return;
            printData(rpos,gBlock,0,rlen);
            pos += rlen;
        }
    }
    
    private static int internalPrintStructure(int depth, BerInputStream in) throws IOException
    {
        int i;
        
        printPos((int)in.getReadBytes());
        int tag = in.readBerTag();
        int len = in.readBerLength();
        for (i = 0; i < depth; ++i) System.out.print("  ");
        System.out.println(Tag.toString(tag) + " len = " + len);

        if (0 != (tag & Tag.CONSTRUCTED)) {
            /*
             * Constructed type
             */
            
            if (len == -1) {
                for (;;) {
                    int rtag = internalPrintStructure(depth+1,in);
                    if (rtag == Tag.EOFTYPE) break;
                }
            } else {
                long rpos = len + in.getReadBytes();
                while (in.getReadBytes() < rpos) {
                    internalPrintStructure(depth+1,in);
                }
                if (in.getReadBytes() > rpos) {
                    throw new AsnEncodingException("Contents of packet do not align with set");
                }
            }
        } else {
            /*
             * Primitive type.
             */
            
            if (len == -1) {
                throw new AsnEncodingException("Illegal: length = -1 for primitive type");
            }
            internalPrintData(in,len);
        }
        
        return tag;
    }
    
    private static void test1()
    {
        try {
            BerSet set = buildTestVector();
            
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            BerOutputStream out = new BerOutputStream(stream);
            
            set.writeElement(out);
            stream.close();
            System.out.println("Test 1");
            printData(stream.toByteArray());
            System.out.println("Test 2");
            structurePrintData(stream.toByteArray());
        }
        catch (IOException err) {
            err.printStackTrace(System.out);
        }
    }

    /**
     * @return
     * @throws UnsupportedEncodingException
     * @throws AsnEncodingException
     */
    private static BerSet buildTestVector() throws UnsupportedEncodingException, AsnEncodingException
    {
        long[] oid = { 1, 3, 1, 443, 121134 };
        BerSet set = new BerSet();
        set.add(new BerBoolean(true));
        set.add(new BerInteger(12345));
        BitSet s = new BitSet();
        s.set(3);
        set.add(new BerBitString(s));
        set.add(new BerOctetString("Hello World".getBytes("UTF-8")));
        set.add(new BerNull());
        set.add(new BerOID(oid));
        set.add(new BerReal(3.1415926));
        set.add(new BerEnumerated(4));
        set.add(new BerNumericString("12345"));
        set.add(new BerPrintableString("Hi"));
        set.add(new BerTeletexString("text".getBytes()));
        set.add(new BerVideoTextString("vidtext".getBytes()));
        set.add(new BerIA5String("Hi"));
        set.add(new BerUTCTime(new Date()));
        set.add(new BerGeneralTime(new Date()));
        set.add(new BerGeneralString("Test String"));
        set.add(new BerGraphicsString("test".getBytes()));
        set.add(new BerVisibleString("Hi there"));
        set.add(new BerGeneralString("This is a test with UTF-8 Chars."));
        
        BerSequence sequence = new BerSequence();
        sequence.add(new BerInteger(12345));
        sequence.add(new BerOctetString("This is a test".getBytes("UTF-8")));
        set.add(sequence);
        return set;
    }
    
    private static void test2()
    {
        try {
            BerNode node = buildTestVector();
            
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            BerOutputStream out = new BerOutputStream(stream);
            node.writeElement(out);
            stream.close();
            
            System.out.println("Test 1");
            System.out.println(node.toString());
            System.out.println("Test 2");
            
            ByteArrayInputStream inStream = new ByteArrayInputStream(stream.toByteArray());
            BerInputStream in = new BerInputStream(inStream);
            
            TestParser parser = new TestParser();
            
            while (null != (node = parser.readPacket(in))) {
                // do your operation.
            }
            
            node = parser.readPacket(in);
            System.out.println(node.toString());
        }
        catch (IOException err) {
            err.printStackTrace(System.out);
        }
    }
    
    public static void main(String[] args)
    {
        System.out.println("================");
        test1();
        System.out.println("================");
        test2();
    }
}


