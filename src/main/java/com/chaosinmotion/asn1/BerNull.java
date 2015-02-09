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
 * Represents a null object. This object represents a 'null', which is distinct
 * from no data.
 */
public class BerNull extends JacNode
{
    /**
     * Construct a new boolean object with the specified tag
     * @param tag
     * @param value
     */
    public BerNull(int tag)
    {
        super(tag);
    }
    
    /**
     * Construct a boolean of type BOOLEAN
     * @param value
     */
    public BerNull()
    {
        this(Tag.NULL);
    }
    
    /**
     * Construct a boolean from the input stream
     * @param tag
     * @param stream
     * @throws IOException
     */
    public BerNull(int tag, BerInputStream stream) throws IOException
    {
        super(tag);
        
        int len = stream.readBerLength();
        if (len != 0) throw new AsnEncodingException("Illegal null object");
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
    	/*
    	 * Added by Fatih Batuk
    	 * for explicitly encoding :
    	 */
    	if (getTaggingMethod() == Tag.EXPLICIT) {	

    		 stream.writeBerTag(getTag() | Tag.CONSTRUCTED);
    		 
    		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		 
    		 //stream.getEncodingMethod() returns the encoding method : BER, DER or CER
             BerOutputStream tmp = new BerOutputStream(baos,stream.getEncodingMethod());
             
             tmp.writeBerTag(Tag.NULL);
             tmp.writeBerLength(0);
             
             tmp.close();
             baos.close();
             
             byte[] data = baos.toByteArray();
             stream.writeBerLength(data.length);
             stream.write(data);       
    	}
    	else {
    		stream.writeBerTag(getTag());
    		stream.writeBerLength(0);
    	}
    }

    public Object getValue()
    {
    	System.out.println(">> Warning: in BerNull.java getValue() method should not have been called");
        return null;
    }
    
    public String toString()
    {
        return "BerNull(" + Tag.toString(getTag()) + ")";
    }
    
    /**
     * Added by Fatih Batuk to decode the object..
     * @author Fatih Batuk
     */
    public void readElement(BerInputStream in) throws IOException {  
		
		if(getTaggingMethod() == Tag.EXPLICIT) {
			ReadSequence readSeq = new ReadSequence(in);
			if (0 != (readSeq.readBerTag())) {
				int len = in.readBerLength();
		        if (len != 0) throw new AsnEncodingException("Illegal null object");
			 }
		}
		else {
			int len = in.readBerLength();
	        if (len != 0) throw new AsnEncodingException("Illegal null object");
		}
	}
}


