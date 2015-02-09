/*  BerOutputStream.java
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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.BitSet;

public class BerOutputStream extends FilterOutputStream
{
    /// Tells the output stream to use the BER encoding rules
    public static final int ENCODING_BER = 0;
    
    /// Tells the output stream to use the CER encoding rules
    public static final int ENCODING_CER = 1;
    
    /// Tells the output stream to use the DER encoding rules
    public static final int ENCODING_DER = 2;
    
    private int fEncoding;
    private byte[] fBuffer;

    /**
     * Construt a new output stream writer. This uses the specified encoding
     * style to encode certain objects, such as strings.
     * @param outStream
     * @param encoding
     */
    public BerOutputStream(OutputStream outStream, int encoding)
    {
        super(outStream);
        fEncoding = encoding;
    }
    
    /**
     * Constructs a new output stream, using BER encoding rules
     * @param stream
     */
    public BerOutputStream(OutputStream stream)
    {
        this(stream,ENCODING_BER);
    }
    
    /**
     * Returns the encoding method in force with this BER stream
     * @return
     */
    public int getEncodingMethod()
    {
        return fEncoding;
    }
    
    /**
     * This method calculates the number of bytes that will be
     * written by the integer primitive provided.
     * 
     * @param value
     * @return
     */
    int calcBerIntegerLength(long value)
    {
        int i;
        int n;
        
        /* Scan forward, skipping unused bits */
        for (i = 0; i < 7; ++i) {
            n = 0x1FF & (int)(value >> (55 - i * 8));
            if ((n != 0) && (n != 0x1FF)) break;
        }
        
        /* Return the number of bytes written */
        return 8-i;
    }
    
    /**
     * This method returns the number of bytes that will be
     * written by this unsigned integer value
     * 
     * @param value
     * @return
     */
    private int calcBerUnsignedIntegerLength(long value)
    {
        int i;
        byte b;
        
        /* Scan forward, skipping unused bits */
        for (i = 0; i < 7; ++i) {
            b = (byte)(value >> (56 - i * 8));
            if (b != 0) break;
        }
        
        /* Return the number of bytes written */
        return 8-i;
    }
    
    /**
     * Writes the integer to the underlying stream, and returns
     * the number of bytes that were written.
     * @param value
     * @param length The length as provided by calcBerIntegerLength
     * @throws IOException
     */
    private void writeBerInteger(long value, int length) throws IOException
    {
        byte b;
        int j;
        
        /* Write the bytes that are left */
        for (j = 8-length; j < 8; ++j) {
            b = (byte)(value >> (56 - j * 8));
            write(b);
        }
    }
    
        
    /**
     * Writes the BER tag value to the underlying stream.
     * @param tag The tag value to write
     * @throws IOException
     */
    public void writeBerTag(int tag) throws IOException
    {
        byte b;
        int i,j;
        
        /*
         * Write the encoded tag object
         */
        switch (Tag.CLASSMASK & tag) {
            case Tag.UNIVERSAL:
                b = (byte)0x00;
                break;
            case Tag.APPLICATION:
                b = (byte)0x40;
                break;
            case Tag.CONTEXT:
                b = (byte)0x80;
                break;
            case Tag.PRIVATE:
            default:
                b = (byte)0xC0;
                break;
        }
        if (0 != (tag & Tag.CONSTRUCTED)) b |= 0x20;
        
        tag &= Tag.TYPEMASK;
        
        if (tag < 31) {
            /* Tag short form */
            b = (byte)(b | tag);
            write(b);
        } else {
            /* Tag long form header */
            b = (byte)(b | 0x1F);
            write(b);
            
            /* Tag value */
            for (i = 1; i < 5; ++i) {
                int mask = 0x7F << (7*i);
                if ((mask & tag) == 0) break;
            }
            --i;
            for (j = i; j >= 0; --j) {
                int d = 0x7F & (tag >> (7 * j));
                if (j != 0) d &= 0x80;
                write(d);
            }
        }
    }
    
    /**
     * Write the BER header object to the output stream
     * @param length
     * @throws IOException
     */
    public void writeBerLength(int length) throws IOException
    {
        if (length < 0) {
            // Indefinite form
            write(0x80);
        } else if (length < 0x80) {
            // Definite short form
            write(length);
        } else {
            // Definite long form
            int len = calcBerUnsignedIntegerLength(length);
            write(0x80 | len);
            writeBerInteger(length,len);
        }
    }
    /**
     * Write the bit subset to the output stream
     * @param set
     * @param offset
     * @param length
     * @throws IOException
     */
    private void writeBitString(BitSet set, int offset, int length) throws IOException
    {
        int i,j,b,l;
        
        /*
         * Calculate the number of bytes I'll need to write
         */
        writeBerLength(1 + ((length + 7) >> 3));
        
        /*
         * Calculate the bit remainder
         */
        b = 7 & (8 - length);       // # unused bits
        write(b);
        
        /*
         * Run through and write the bits
         */
        for (i = 0; i < length; i += 8) {
            b = 0;
            l = length - i;
            if (l > 8) l = 8;
            for (j = 0; j < l; ++j) {
                if (set.get(offset + i + j)) {
                    b |= 0x80 >> j;
                }
            }
            write(b);
        }
    }

    /**
     * Write the bit set to the output stream. This writes the length and data, but not
     * the tag type header.
     * @param set
     * @throws IOException
     * @see com.chaosinmotion.asn1.io.Asn1OutputStream#writeBitString(java.util.BitSet)
     */
    public void writeBitString(BitSet set) throws IOException
    {
        int off,wlen;
        int length;
        
        length = set.length();
        if ((length > 7992) && (fEncoding == ENCODING_CER)) {
            writeBerLength(-1);
            
            /*
             * Write the tags
             */
            
            off = 0;
            while (off < length) {
                wlen = length - off;
                if (wlen > 7992) wlen = 7992;
                
                writeBerTag(Tag.BITSTRING);
                writeBitString(set,off,wlen);
                off += wlen;
            }
            
            /*
             * Write end of buffer tag
             */
            writeBerTag(Tag.EOFTYPE);
            writeBerLength(0);
        } else {
            writeBitString(set,0,length);       // write as one packet
        }
    }
    
    /**
     * Returns true if this will write a complex string
     * @param set
     * @return
     */
    public boolean isComplexBitString(BitSet set)
    {
        int length = set.length();
        return ((length > 7992) && (fEncoding == ENCODING_CER));
    }
    
    /**
     * Write a boolean tag out. This writes the full boolean object, but not the tag
     * header
     * @param bool
     * @throws IOException
     * @see com.chaosinmotion.asn1.io.Asn1OutputStream#writeBoolean(boolean)
     */
    public void writeBoolean(boolean bool) throws IOException
    {
        writeBerLength(1);
        write(bool ? 0xFF : 0);
    }

    /**
     * Write a BER integer value out to the output stream, along with the length
     * @param value
     * @throws IOException
     * @see com.chaosinmotion.asn1.io.Asn1OutputStream#writeInteger(long)
     */
    public void writeInteger(long value) throws IOException
    {
        int length;
        
        length = calcBerIntegerLength(value);
        writeBerLength(length);
        writeBerInteger(value,length);
    }
    
    
    /**
     * Internal routine copies the specified number of bytes to the output stream.
     * @param stream
     * @param length
     * @throws IOException
     */
    private void copyFromStream(InputStream stream, int length) throws IOException
    {
        int off,rlen;
        
        if (fBuffer == null) fBuffer = new byte[1024];      // small buffer for copy purposes
        
        off = 0;
        while (off < length) {
            rlen = length - off;
            if (rlen > fBuffer.length) rlen = fBuffer.length;
            
            rlen = stream.read(fBuffer,0,rlen);
            if (rlen == -1) break;
            write(fBuffer,0,rlen);
            
            off += rlen;
        }
        
        if (off != length) throw new AsnEncodingException("Stream EOF hit early");
    }
    
    /**
     * Internal routine attempts to copy 1000 bytes into internal buffer; returns the
     * number of bytes that could be read
     * @param stream
     * @return
     * @throws IOException
     */
    private int copyToBuffer(InputStream stream) throws IOException
    {
        int off,rlen;
        
        if (fBuffer == null) fBuffer = new byte[1024];
        off = 0;
        while (off < 1000) {
            rlen = 1000 - off;
            
            rlen = stream.read(fBuffer,off,rlen);
            if (rlen == -1) break;
            off += rlen;
        }
        
        return off;
    }
    

    /**
     * Writes a BER stream to the output stream. Note that using the unspecified length (len == -1)
     * is illegal if this is writing a DER encoded string.
     * @param stream
     * @param len
     * @throws IOException
     * @see com.chaosinmotion.asn1.io.Asn1OutputStream#writeOctetString(java.io.InputStream, long)
     */
    public void writeOctetString(InputStream stream, int length) throws IOException
    {
        int off,wlen;
        if ((length == -1) && (fEncoding == ENCODING_DER)) {
            throw new AsnEncodingException("DER encoding requires a definite length to write");
        }
        
        if (((fEncoding == ENCODING_CER) && (length > 1000)) || (length == -1)) {
            /*
             * Requires chunked encoding
             */
            
            writeBerLength(-1);
            
            if (length == -1) {
                /*
                 *  Just write until EOF
                 */
                for (;;) {
                    int len = copyToBuffer(stream);
                    
                    if (len == 0) break;
                    writeBerTag(Tag.OCTETSTRING);
                    writeBerLength(len);
                    write(fBuffer,0,len);
                }
            } else {
                /*
                 * Write in chunks until I hit the end
                 */
                off = 0;
                for (;;) {
                    wlen = length - off;
                    if (wlen > 1000) wlen = 1000;
                    writeBerTag(Tag.OCTETSTRING);
                    writeBerLength(wlen);
                    copyFromStream(stream,wlen);
                    off += wlen;
                }
            }
            
            /*
             * Write end of tag
             */
            writeBerTag(Tag.EOFTYPE);
            writeBerLength(0);
        } else {
            /*
             * Write as a single object
             */
            writeBerLength(length);
            copyFromStream(stream,length);
        }
    }
    
    /**
     * Return true if this will write a complex string
     * @param length
     * @return
     */
    public boolean isComplexOctetString(int length)
    {
        return (((fEncoding == ENCODING_CER) && (length > 1000)) || (length == -1));
    }

    /**
     * Write the byte object to the output stream
     * @param value
     * @throws IOException
     * @see com.chaosinmotion.asn1.io.Asn1OutputStream#writeOctetString(byte[])
     */
    public void writeOctetString(byte[] value, int offset, int length) throws IOException
    {
        int off,wlen;

        if ((fEncoding == ENCODING_CER) && (length > 1000)) {
            /*
             * With CER encoding and a length greater than 1000 bytes, this
             * must be broken into chunks.
             */
            
            writeBerLength(-1); // CER constructed type
            
            /*
             * Now write the contents
             */
            off = 0;
            while (off < length) {
                wlen = length - off;
                if (wlen > 1000) wlen = 1000;
                
                writeBerTag(Tag.OCTETSTRING);
                writeBerLength(wlen);   // CER constructed type
                write(value,offset+off,wlen);
                off += wlen;
            }
            
            /*
             * And write the terminating chunk
             */
            writeBerTag(Tag.EOFTYPE);
            writeBerLength(0);
        } else {
            /*
             * Write an octet string object
             */
            writeBerLength(length);
            write(value,offset,length);
        }
    }

    /**
     * Internal routine for getting the OID element length for writing an OID
     * @param component
     * @return
     */
    private int getOIDElementLength(long component)
    {
        long l;
        int i;
        
        if (component < 0) return 10;               // Full 64 bits takes 10*7 bits to encode
        l = 1;
        for (i = 1; i < 9; ++i) {
            l <<= 7;
            if (component < l) break;
        }
        return i;
    }

    /**
     * Write the specified OID, without a tag header
     * @param value
     * @throws IOException
     * @see com.chaosinmotion.asn1.io.Asn1OutputStream#writeOID(com.chaosinmotion.asn1.data.OID)
     */
    public void writeOID(long[] value) throws IOException
    {
        int len;
        int i,j;
        
        /*
         * Write the header
         */

        if (value.length < 2) {
            writeBerLength(0);
            return;
        }
        
        len = 1;
        for (i = 2; i < value.length; ++i) {
            len += getOIDElementLength(value[i]);
        }
        writeBerLength(len);
        
        /*
         * Write the OID
         */
        i = (int)(value[0] * 40 + value[1]);
        out.write(0x00FF & i);
        
        for (i = 2; i < value.length; ++i) {
            long v = value[i];
            len = getOIDElementLength(v);
            
            for (j = len-1; j > 0; --j) {
                long m = 0x0080 | (0x007F & (v >> (j * 7)));
                out.write((int)m);
            }
            out.write((int)(0x007F & v));
        }
    }
    
    /**
     * Calculate the length of a real value as a BER represented string
     * @param value The value to encode
     * @return The number of bytes the encoding takes
     * @throws BerEncodingException Thrown if the value is a NAN, which cannot be sent via
     * ASN.1 encoding.
     */
    private int calcBerRealLength(double value) throws IOException
    {
        int e;
        long m;
        int len;
        
        long bits = Double.doubleToRawLongBits(value);
        e = (int)(0x7FF & (bits >> 52));
        m = 0x000FFFFFFFFFFFFFL & bits;
        
        if (e == 0) {
            if (m == 0) return 0;                       // +0 or -0
            
            // Unnormalized value. Exponent is between -1023 and -1075,
            // which will take 2 bytes to encode. We just now need to know
            // how many bytes the mantessa will take.
            while ((m & 1) == 0) m >>= 1;
            
            // Number of bytes == 1 header + 2 exponent + # bytes for mantessa
            return 3 + calcBerUnsignedIntegerLength(m);
        } else if (e == 0x7FF) {
            // NAN or infinity
            if (m == 0) return 1;                       // infinity
            throw new AsnEncodingException("Value is a NAN; cannot be transmitted");
        } else {
            /*
             * Normalized double-point value. Convert the exponent and
             * mantessa. Note that we adjust the exponent downwards quite
             * a bit because the mantessa sent via ASN.1 is an integer and
             * not a fraction. Thus, adjust and readjust again.
             */
            e -= 1075;          // IEEE 754 e adjust (1023) + 52 == bits in mantessa
            m |= 0x0010000000000000L;
            
            while ((m & 1) == 0) {
                m >>= 1;
                e += 1;
            }
            
            len = ((e < -128) || (e > 127)) ? 3 : 2;        // header + exponent bytes
            return len + calcBerUnsignedIntegerLength(m);
        }
    }

    /**
     * Write a real value to the output stream
     * @param value
     * @throws IOException
     * @see com.chaosinmotion.asn1.io.Asn1OutputStream#writeReal(double)
     */
    public void writeReal(double value) throws IOException
    {
        boolean sign;
        int e;
        long m;
        int i;
        int b;
        
        /*
         * Write the real header information
         */
        
        writeBerLength(calcBerRealLength(value));
        
        /*
         *  Write the real value
         */
        
        long bits = Double.doubleToRawLongBits(value);
        sign = (bits < 0);
        e = (int)(0x7FF & (bits >> 52));
        m = 0x000FFFFFFFFFFFFFL & bits;
        
        if (e == 0) {
            if (m == 0) return;                     // +0 or -0
            
            /*
             * Unnormalied value. Exponent is between -1023 and -1075,
             * which will take 2 bytes to encode. We now adjust the 
             * mantessa
             */
            while ((m & 1) == 0) m >>= 1;
            
            for (i = 51; i > 0; --i) {      // find bit number of first 1
                if ((m & (1L << i)) != 0) break;
            }
            e = i - 1075;
            
            // Write header. (F == 0, base = 2, 2 byte exponent)
            b = 0x81;
            if (sign) b |= 0x40;
            write(b);
            
            // Write exponent.
            writeBerInteger(e,2);
            
            // Write mantessa
            writeBerInteger(m,1+(i>>3));
        } else if (e == 0x7FF) {
            /*
             * NAN or infinity
             */
            if (m == 0) {
                write(sign ? 0x41 : 0x40);  // -INF / INF
            }
            throw new AsnEncodingException("Value is a NAN; cannot be transmitted");
        } else {
            /*
             * Normalized double-point value. Convert the exponent and
             * mantessa. Note that we adjust the exponent downwards quite
             * a bit because the mantessa sent via ASN.1 is an integer and
             * not a fraction. Thus, adjust and readjust again.
             */
            e -= 1075;          // IEEE 754 e adjust (1023) + 52 == bits in mantessa
            m |= 0x0010000000000000L;
            
            i = 53;
            while ((m & 1) == 0) {
                m >>= 1;
                e += 1;
                --i;
            }
            
            // Write header and integer
            b = 0x80;
            if (sign) b |= 0x40;
            if ((e < -128) || (e > 127)) {
                b |= 0x01;
                write(b);
                writeBerInteger(e,2);
            } else {
                write(b);
                write(e);
            }
            
            // write mantessa
            writeBerInteger(m,(i+7)>>3);
        }
    }

}


