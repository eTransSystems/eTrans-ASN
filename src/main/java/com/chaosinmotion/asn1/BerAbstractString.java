/*  BerBoolean.java
 *
 *  Created on Jun 2, 2006 by William Edward Woody
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
import java.io.IOException;

/**
 * Represents an abstract string. This is the base of the limited syntax strings
 * that are part of the BER encoding standard. Because strings are limited in
 * syntax--they basically use 7-bit character encoding--it is safe to use the
 * Java String syntax to represent the strings.
 */
abstract class BerAbstractString extends JacNode
{
    private String fValue;
    
    protected static final int  ASCII = 0x0001;     // A-Z a-z
    protected static final int  NUMBER = 0x0002;    // 0-9
    protected static final int  HEXASCII = 0x0004;  // A-F a-f
    protected static final int  MINUS = 0x0008;     // -
    protected static final int  PUNCT = 0x0010;     // ' ( ) + , - . / : ? sp

    /**
     * This constructor is added by Fatih Batuk
     * Costructs empty asn.1 string
     * @author Fatih Batuk
     */
    public BerAbstractString(int tag)	
    { 
    	super(tag);
    }
    
    /**
     * Construct a new boolean object with the specified tag
     * @param tag
     * @param value
     */
    public BerAbstractString(int tag, String value)
    {
        super(tag);
        fValue = value;
    }
    
    /**
     * Construct a boolean from the input stream
     * @param tag
     * @param stream
     * @throws IOException
     */
    public BerAbstractString(int tag, BerInputStream stream) throws IOException
    {
        super(tag);
        
        fValue = new String(stream.readOctetString(0 == (tag & Tag.CONSTRUCTED)),"UTF-8");
    }

    /**
     * Write this BER element to the output stream
     * Comment
     * @param stream
     * @throws IOException
     * @see com.chaosinmotion.asn1.BerNode#writeElement(com.chaosinmotion.asn1.BerOutputStream)
     */
    public void writeElement(BerOutputStream stream) throws IOException
    {
    	if ( ! isInitialized ) {	//added by Fatih Batuk
    		throw new AsnEncodingException("\n >> This object is uninitialized(empty) and will not be encoded ! (If exists)asn.1 object name is : " + getName());
    	}
    	
    	/*
    	 * Added by Fatih Batuk
    	 * for explicitly encoding :
    	 */
    	if (getTaggingMethod() == Tag.EXPLICIT) {	

    		 stream.writeBerTag(getTag() | Tag.CONSTRUCTED);
    		 
    		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		 
    		 //stream.getEncodingMethod() returns the encoding method : BER, DER or CER
             BerOutputStream tmp = new BerOutputStream(baos,stream.getEncodingMethod());
             
             byte[] bb = fValue.getBytes("UTF-8");
             if (this instanceof BerIA5String)
            	 tmp.writeBerTag(Tag.IA5STRING | (stream.isComplexOctetString(bb.length) ? Tag.CONSTRUCTED : 0)); 
             else if (this instanceof BerPrintableString)
            	 tmp.writeBerTag(Tag.PRINTABLESTRING | (stream.isComplexOctetString(bb.length) ? Tag.CONSTRUCTED : 0)); 
             else 
            	 throw new AsnFatalException("\n >> In explicitly encoding of asn.1 string object the type is not recognizable!");
     		 tmp.writeOctetString(bb,0,bb.length);
             
             tmp.close();
             baos.close();
             
             byte[] data = baos.toByteArray();
             stream.writeBerLength(data.length);
             stream.write(data);       
    	}
    	else {
    		byte[] b = fValue.getBytes("UTF-8");
    		stream.writeBerTag(getTag() | (stream.isComplexOctetString(b.length) ? Tag.CONSTRUCTED : 0));
    		stream.writeOctetString(b,0,b.length);
    	}
    }

    /**
     * Return the value of this boolean object
     * @return
     */
    public String getValue()
    {
        return fValue;
    }
    
    /**
     * This method is added by Fatih Batuk
     * Sets the value of this object
     * @author Fatih Batuk
     */
    public void setValue(String parameter)
    {
        fValue = parameter;
        isInitialized = true;
    }
    
    private static boolean isValidChar(char c, int charSet)
    {
        if (0 != (charSet & ASCII)) {
            if ((c >= 'A') && (c <= 'Z')) return true;
            if ((c >= 'a') && (c <= 'z')) return true;
        }
        if (0 != (charSet & NUMBER)) {
            if ((c >= '0') && (c <= '9')) return true;
        }
        if (0 != (charSet & HEXASCII)) {
            if ((c >= 'A') && (c <= 'F')) return true;
            if ((c >= 'a') && (c <= 'f')) return true;
        }
        if (0 != (charSet & MINUS)) {
            if (c == '-') return true;
        }
        if (0 != (charSet & PUNCT)) {
            if ("'()+,./:? ".indexOf(c) != -1) return true;
        }
        return false;
    }
    
    /**
     * Validate the string against the character sets specified.
     * @param str
     * @param charSet Character set constants defined above
     * @return
     */
    protected static boolean validateString(String str, int charSet)
    {
        int len = str.length();
        int i;
        
        for (i = 0; i < len; ++i) {
            if (!isValidChar(str.charAt(i),charSet)) return false;
        }
        return true;
    }
}


