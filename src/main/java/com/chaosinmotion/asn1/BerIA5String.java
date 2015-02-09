/*  BerPrintableString.java
 *
 *  Created on Jun 4, 2006 by William Edward Woody
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

import java.io.IOException;

/**
 * Represents a 7-bit ASCII (IA5) string
 */
public class BerIA5String extends BerAbstractString
{
	
	/**
     * added by Fatih Batuk
     * Costructs empty BerIA5String
     * @author Fatih Batuk
     */
    public BerIA5String()	
    { 
    	super(Tag.IA5STRING);
    }
	
    public BerIA5String(int tag, String value) throws AsnEncodingException
    {
        super(tag, value);
        if (!validate(value)) throw new AsnEncodingException("Illegal IA5 string");
    }

    public BerIA5String(int tag, BerInputStream stream) throws IOException
    {
        super(tag, stream);
    }
    
    public BerIA5String(String value) throws AsnEncodingException
    {
        this(Tag.IA5STRING,value);
    }
    
    public static final boolean validate(String str)
    {
        int i,len = str.length();
        for (i = 0; i < len; ++i) {
            char c = str.charAt(i);
            if ((c < 0) || (c > 127)) return false;
        }
        return true;
    }

    public String toString()
    {
        return "BerIA5String(" + Tag.toString(getTag()) + ")=" + getValue();
    }
    
    /**
     * Added by Fatih Batuk to decode the object..
     * Note that : the input of the setValue() method below 
     * 				is exactly copied from the constructor of the asn.1 library
     * @author Fatih Batuk
     */
    public void readElement(BerInputStream in) throws IOException {
		
		if(getTaggingMethod() == Tag.EXPLICIT) {
			ReadSequence readSeq = new ReadSequence(in);
			if (0 != (readSeq.readBerTag())) {
				setValue(new String(in.readOctetString(0 == (getTag() & Tag.CONSTRUCTED)),"UTF-8"));	
			 }
		}
		else {
			setValue(new String(in.readOctetString(0 == (getTag() & Tag.CONSTRUCTED)),"UTF-8"));
		}
		
	}
}
