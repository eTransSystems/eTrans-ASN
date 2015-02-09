/*  BerInputStream.java
 *
 *  Created on May 31, 2006 by William Edward Woody
 */

/*
 * Copyright 2007 William Woody, All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list 
 * of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, this 
 * list of conditions and the following disclaimer in the documentation and/or other 
 * materials provided with the distribution.
 * 
 * 3. Neither the name of Chaos In Motion nor the names of its contributors may be used 
 * to endorse or promote products derived from this software without specific prior 
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR 
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH 
 * DAMAGE.
 * 
 * Contact William Woody at woody@alumni.caltech.edu or at woody@chaosinmotion.com. 
 * Chaos In Motion is at http://www.chaosinmotion.com
 */

package com.chaosinmotion.asn1;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.BitSet;

/**
 * The input stream reader provides primitives for reading some fundamental objects
 * from a BER input stream
 */
public class BerInputStream extends LengthInputStream
{
    private byte[]      fBuffer;            // internal copy buffer

    /**
     * Construct a new input stream reader
     * @param inStream
     */

    public BerInputStream(InputStream inStream)
    {
        super(inStream);
    }
    
    
    /**
     * Internal read routine reads a byte and throws an exception if
     * the end of file has been reached.
     *
     * @return
     * @throws IOException
     * @see java.io.DataInput#readByte()
     */
    private byte readByte() throws IOException
    {
        int b = read();
        if (b == -1) throw new EOFException();
        return (byte)b;
    }

    /**
     * Read a BER tag.
     * @return The BER tag read at this location
     * @throws IOException
     */
    public int readBerTag() throws IOException
    {
        byte h,x;
        int type;
        
        h = readByte();
        type = h & 0x1F;
        if (type == 0x1F) {
            /* Multi-byte type */
            type = 0;
            do {
                x = readByte();
                type = (type << 7) | (0x7F & x);
            } while (0 != (0x80 & x));
        }
        
      //modified by Fatih Batuk
        int hold = Tag.convertTag(0x03 & (h >> 6), type, (0 != (h & 0x20))); 
        
        /*
       	 * Here we are masking tag value with our UNCONSTRUCTED_MASK constant to gain the original  
       	 * tag value for EXPLICITLY encoded primitives and constructed objects(Sequence/Set).
       	 * The tag values of other primitives are not affected by this masking operation.
       	 * And there is some constraint that if you tagged a data type in your asn.1 protocol like 
       	 * [ <tagNumber> ] (which is CONTEXT class tagging) or
       	 * [PRIVATE <tagNumber>] or [APPLICATION <tagNumber>] 
       	 * the 'tagNumber' value should be in the range 0 to 127 (i.e. -1 < tagNumber < 128)
       	 * This constraint is not related with this masking. This is related 
       	 * with the design of this asn.1 library
       	 * 
       	 * -- Fatih Batuk
       	 */
        return (hold & Tag.UNCONSTRUCTED_MASK);
    }
    
    /**
     * Read a BER length.
     * @return The BER length read at this location. This is -1 if the length is the indefinite form.
     * @throws IOException
     */
    public int readBerLength() throws IOException
    {
        byte h,x;
        int length;
        
        h = readByte();
        if (0 == (0x80 & h)) return h;  // definite short form
        
        h &= 0x7F;                      // definite long form; pull data
        if (h == 0) return -1;          // indefinite long form
        
        length = 0;
        while (h-- > 0) {
            x = readByte();
            length = (length << 8) | (0x00FF & x);
        }
        return length;
    }
    

    /**
     * Reads the primitive BER binary object. This assumes ber length is unread
     * @return
     * @throws AsnEncodingException
     * @see com.chaosinmotion.asn1.io.Asn1InputStream#readBoolean()
     */
    public boolean readBoolean() throws IOException
    {
        byte x;
        
        int length = readBerLength();
        if (length != 1) throw new AsnEncodingException("Unknown boolean value: illegal boolean length");
        x = readByte();
        return (x != 0);
    }

    /**
     * Read a primitive integer object. This assumes the length is unread
     * @return
     * @throws AsnEncodingException
     * @see com.chaosinmotion.asn1.io.Asn1InputStream#readInteger()
     */
    public long readInteger() throws IOException
    {
        int length = readBerLength();
        return readBerInteger(length);
    }
    
    /**
     * Internal integer read routine
     * @param length
     * @return
     * @throws IOException
     */
    private long readBerInteger(int length) throws IOException
    {
        long value = 0;
        byte x;
        
        if (length == -1) throw new AsnEncodingException("Unknown integer value: illegal integer length");
        if (length == 0) return value;
        
        x = readByte();
        value = x;              // sign extended
        
        while (--length > 0) {
            x = readByte();
            value = (value << 8) | (0x00FF & x);
        }
        
        return value;
    }

    
    /**
     * Read a primitive BER integer object as an unsigned integer
     * @param length
     * @return
     * @throws IOException
     */
    private long readBerUnsignedInteger(int length) throws IOException
    {
        long value = 0;
        byte x;
        
        if (length == -1) throw new AsnEncodingException("Unknown integer value: illegal integer length");
        if (length == 0) return value;
        while (length-- > 0) {
            x = readByte();
            value = (value << 8) | (0x00FF & x);
        }
        
        return value;
    }

    /**
     * Reads the real value from the BER string. This assumes the length is unread
     * @return
     * @throws AsnEncodingException
     * @see com.chaosinmotion.asn1.io.Asn1InputStream#readReal()
     */
    public double readReal() throws IOException
    {
        byte temp;
        byte b;
        int expOctet;
        boolean signNeg;
        int base;
        int f;
        int exponent;
        long mantissa;
        int length;
        
        length = readBerLength();
        if (length == -1) throw new AsnEncodingException("Unknown integer value: illegal integer length");
        if (length == 0) return 0;
        
        b = readByte();
        length--;
        
        /*
         * From the flags of the input buffer, determine the type of real we're
         * dealing with
         */
        
        temp = (byte)(0xC0 & b);
        if (0x40 == temp) {
            /* ITU-T Standard X.690, 8.5.8 */
            if (b == 0x40) return Double.POSITIVE_INFINITY;
            if (b == 0x41) return Double.NEGATIVE_INFINITY;
            throw new AsnEncodingException("Unknown real value: reserved encoding not supported");
        }
        
        if (0x00 == temp) {
            /* ITU-T Standard X.690, 8.5.7 not supported */
            throw new AsnEncodingException("Unknown real value: ISO 6093 encoding not supported");
        }
        
        /*
         * Extract the sign, base and f scaling factors (per 8.5.6)
         */

        signNeg = (0 != (temp & 0x40));
        
        switch (0x03 & (b >> 4)) {
            case 0: base = 2;   break;
            case 1: base = 8;   break;
            case 2: base = 16;  break;
            default: throw new AsnEncodingException("Unknown real value: reserved base not supported");
        }
        
        f = 0x03 & (b >> 2);
        
        /*
         * Read the octet, encoded as a signed binary integer of length given in the header
         * byte.
         */
        
        expOctet = (0x03 & b);
        if (expOctet == 3) {
            expOctet = 0x00FF & readByte();
            length--;
        } else {
            expOctet++;
        }
        exponent = (int)readBerInteger(expOctet);
        length -= expOctet;
        
        /*
         * Read the mantessa and recompute the actual mantessa value
         * based upon the formula in 8.5.6: M = sign * N * 2**F
         */
        mantissa = readBerUnsignedInteger(length);
        if (f != 0) mantissa <<= f;
        if (signNeg) mantissa = -mantissa;
        
        /*
         * Now compute the result and return it.
         */
        return mantissa * Math.pow((double)base,(double)exponent);
    }


    /**
     * Read an object identifier. This assumes the length is unread
     * @return
     * @throws AsnEncodingException
     * @see com.chaosinmotion.asn1.io.Asn1InputStream#readOID()
     */
    public long[] readOID() throws IOException
    {
        long[] value;
        int length;
        int i;
        int v,b;
        
        length = readBerLength();
        
        if (length == -1) throw new AsnEncodingException("Expected primitive type");
        byte[] bstr = new byte[length];
        read(bstr);
        
        /*
         * Calclate the OID length
         */
        length = 2;
        for (i = 1; i < bstr.length; ++i) {
            if (bstr[i] >= 0) ++length;
        }
        value = new long[length];

        /*
         * Parse the OID
         */
        b = 0x00FF & bstr[0];
        value[0] = b/40;
        value[1] = b%40;
        
        v = 0;
        length = 2;
        for (i = 1; i < bstr.length; ++i) {
            v = v * 128 + 0x007F & bstr[i];
            if (bstr[i] >= 0) {
                value[length++] = v;
                v = 0;
            }
        }
        
        /*
         * Return the OID as an object
         */
        return value;
    }
    
    /**
     * Internal routine for reading bit strings. This assumes he length is
     * unread
     * 
     * @param set The set of bits that is being created
     * @param start The first bit where to flip stuff
     * @return One more than the highest bit read
     */
    private int readBerBitString(BitSet set, int start, boolean primitive) throws IOException
    {
        int length;
        byte b;
        byte m;
        int i,j;
        
        if (primitive) {
            /*
             * The current tag is primitive, which means what follows is the
             * array of bits
             */
            
            length = readBerLength();
            if (length < 0) throw new AsnEncodingException("Ill formed bit stream");
            b = readByte();
            --length;
            for (i = 1; i < length; ++i) {
                m = readByte();
                for (j = 0; j < 8; ++j) {
                    if (0 != (m & (0x80 >> j))) set.set(start);
                    ++start;
                }
            }
            m = readByte();
            for (j = 0; j < 8-b; ++j) {
                if (0 != (m & (0x80 >> j))) set.set(start);
                ++start;
            }
        } else {
            /*
             * The current tag is compound. Read all of the components
             */
            
            ReadSequence rseq = new ReadSequence(this);
            
            int tag;
            while (0 != (tag = rseq.readBerTag())) {
                start = readBerBitString(set,start,0 == (tag & Tag.CONSTRUCTED));
            }
        }
        
        return start;
    }

    /**
     * Routine to read a bit string. The flag is the primitive setting from the
     * loaded tag
     * @param primitive
     * @return
     * @throws IOException
     */
    public BitSet readBitString(boolean primitive) throws IOException
    {
        BitSet set = new BitSet();
        readBerBitString(set,0,primitive);
        return set;
    }
    
    /**
     * Internal routine copies the specified number of bytes to the output stream.
     * @param stream
     * @param length
     * @throws IOException
     */
    private void copyToStream(OutputStream stream, int length) throws IOException
    {
        int off,rlen;
        
        if (fBuffer == null) fBuffer = new byte[1024];      // small buffer for copy purposes
        
        off = 0;
        while (off < length) {
            rlen = length - off;
            if (rlen > fBuffer.length) rlen = fBuffer.length;
            
            rlen = read(fBuffer,0,rlen);
            if (rlen == -1) throw new AsnEncodingException("EOF reached unexpectedly");
            stream.write(fBuffer,0,rlen);
            
            off += rlen;
        }
    }
    
    /**
     * Read a primitive octet string. The flag is the primitive setting from the
     * loaded tag
     * @param primitive
     * @param stream
     * @throws AsnEncodingException
     * @see com.chaosinmotion.asn1.io.Asn1InputStream#readOctetString(java.io.OutputStream)
     */
    public void readOctetString(OutputStream stream, boolean primitive) throws IOException
    {
        int length;
        
        /*
         * From the current tag determine if this is a primitive or
         * a compound string.
         */
        if (primitive) {
            /*
             * This is a primitive string. Simply copy the octets
             */
            length = readBerLength();
            copyToStream(stream,length);
        } else {
            /*
             * This is a compound string. Read each of the components
             * as strings inside of me, recursing for each
             */
            ReadSequence rseq = new ReadSequence(this);
            int tag;
            
            while (0 != (tag = rseq.readBerTag())) {
                readOctetString(stream, 0 == (tag & Tag.CONSTRUCTED));
            }
        }
    }
    
    /**
     * Read a primitive octet string. The flag is the primitive setting from the
     * loaded tag
     * @param primitive
     * @return
     */
    public byte[] readOctetString(boolean primitive) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        readOctetString(baos,primitive);
        baos.close();
        return baos.toByteArray();
    }
}
