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


/**
 * We store a Tag as an integer. This provides a series of classes and
 * declarations that allow us to declare a Ber tag object
 */
public class Tag
{
	
///////////////////////////////////////////////////////////////////////////////////////////
/******************************************************************************************
 *Added  by Fatih Batuk
 */

	public static final int IMPLICIT =0;	

	public static final int EXPLICIT =1;
	
	public static final int SequenceType = 	10;
	public static final int SetType = 		20;
	public static final int PrimitiveType = 30;
	
	public static final int UnknownChoiceTag = 999999999;		// To use in Choice.java (to construct an initial Choice object)
	
	 /**
     * Added by fatih Batuk
     * <p>This tag will be used to convert a constructed object's tag to the orijinal value 
     */
    public static final int UNCONSTRUCTED_MASK =   0xDFFFFFFF;
    
/*******************************************************************************************/
/////////////////////////////////////////////////////////////////////////////////////////////
	
	
    /*
     * Standard asn.1 class types
     */
    public static final int UNIVERSAL =     0x00000000;
    public static final int APPLICATION =   0x40000000;
    public static final int CONTEXT =       0x80000000;
    public static final int PRIVATE =       0xC0000000;
    
    /**
     * Mask which masks out the ASN.1 class
     */
    public static final int CLASSMASK =     0xC0000000;
    
    /**
     * Bit indicates this is a constructed object
     */
    public static final int CONSTRUCTED =   0x20000000;
    
    /*
     * Standard asn.1 primtive types
     */
    public static final int EOFTYPE = 0x00;
    public static final int BOOLEAN = 0x01;			
    public static final int INTEGER = 0x02;
    public static final int BITSTRING = 0x03;
    public static final int OCTETSTRING = 0x04;
    public static final int NULL = 0x05;
    public static final int OBJECTID = 0x06;
    public static final int REAL = 0x09;
    public static final int ENUMERATED = 0x0A;
    public static final int SEQUENCE = 0x10;
    public static final int SET = 0x11;
    public static final int NUMERICSTRING = 0x12;
    public static final int PRINTABLESTRING = 0x13;
    public static final int TELETEXSTRING = 0x14;
    public static final int VIDEOTEXTSTRING = 0x15;
    public static final int IA5STRING = 0x16;
    public static final int UTCTIME = 0x17;
    public static final int GENERALTIME = 0x18;
    public static final int GRAPHICSTRING = 0x19;
    public static final int VISIBLESTRING = 0x1A;
    public static final int GENERALSTRING = 0x1B;
    
    public static final int TYPEMASK = 0x1FFFFFFF;
    
    /**
     * Internal routine used by my parser. This takes as a class ID the actual
     * integer value from 0 to three, the tag ID and the constructed ID, and
     * stuffs the integer to match the constants in this class
     * @param classType Class type from 0 (universal) to 3 (private)
     * @param tagID Class ID
     * @param constructed Constructed type?
     * @return
     */
    public static int convertTag(int classType, int tagID, boolean constructed)
    {
        return tagID | (classType << 30) | (constructed ? CONSTRUCTED : 0);
    }
    
    /**
     * Given a tag formatted using convertTag, this prints a string representation
     * of the tag
     * @param tag
     * @return
     */
    public static String toString(int tag)
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append('[');
        switch (tag & CLASSMASK) {
            case UNIVERSAL:
                buffer.append("Universal ");
                break;
            case APPLICATION:
                buffer.append("Application ");
                break;
            case CONTEXT:
                buffer.append("Context ");
                break;
            case PRIVATE:
                buffer.append("Private ");
                break;
        }
        buffer.append(TYPEMASK & tag);
        if (0 != (tag & CONSTRUCTED)) buffer.append(",c");
        buffer.append(']');
        
        return buffer.toString();
    }
}
