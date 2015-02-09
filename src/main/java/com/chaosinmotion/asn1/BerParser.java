/*  BerParser.java
 *
 *  Created on Jun 1, 2006 by William Edward Woody
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

import java.io.EOFException;
import java.io.IOException;


/**
 * The root BerParser object provides a wrapper for a BerInputStream to help
 * parse the ASN.1 contents. A BerParser object passes an additional bit of
 * information, the 'state' flag, which corresponds to the ASN.1 component that
 * is being constructed.<br><br>
 * It is worth noting that an ASN.1 specification specifies all ASN.1 grammars
 * as LL(1) parsers. This basically means that we only need two bits of 
 * information to know what we're currently parsing: the current state in a
 * finite-state machine (represented here as an integer with the start state
 * given the arbitrary value 0), and the current token being parsed. You can
 * see this when examining an ASN.1 specification; all the tokens are set up
 * so that you only need these two bits of information when making a parser
 * decision.<br><br>
 * To that end, a 'state' value is passed around while reading tags. This state
 * value needs to be set to an integer representing the current ASN.1 production
 * rule being read.<br><br>
 * To construct a BER parser for a particular ASN.1 grammar, you would create
 * an instance of this, and override the 'create' method. The create method
 * will only be called when parsing a non-universal tag. (Universal tags are
 * built independently of state, and are handled separately.) Your create method
 * should then examine the current tag and state, decide which object to
 * construct, then construct that object--passing in a new state if the
 * object to be constructed is a complex type, such as an embedded tag, set
 * or sequence.
 */
public abstract class BerParser
{   
    /**
     * This is the start state in my parsing state machine.
     */
    
    protected static final int START = 0;
    
    /**
     * This reads the contents associated with a tag. If this is a primitive
     * object (a string or integer or real), this simply reads and constructs
     * the object. If this is a complex object or a custom tag, the create
     * method is called.
     * @param tag
     * @param stream
     * @return
     */
    //modified by Fatih Batuk ('state' parameter is deleted)
    BerNode read(int tag, BerInputStream stream) throws IOException
    {
    	/*
    	 * We are masking tag value with our UNCONSTRUCTED_MASK constant to gain the original  
    	 * tag value for EXPLICITLY encoded primitives and constructed objects(Sequence/Set).
    	 * The tag values of other primitives are not affected by this masking operation.
    	 * However if you tagged a data type in your asn.1 protocol like 
    	 * [ <tagNumber> ] (which is CONTEXT class tagging) or
    	 * [PRIVATE <tagNumber>] or [APPLICATION <tagNumber>] 
    	 * the 'tagNumber' value should be in the range 0 to 127 (i.e. -1 < tagNumber < 128)
    	 * This constraint is not related with this masking above. This is related 
    	 * with the design of the asn.1 library
    	 * 
    	 * -- Fatih Batuk
    	 */
    	tag = tag & Tag.UNCONSTRUCTED_MASK;		//added by Fatih Batuk
    	
        switch (tag) {
            case Tag.EOFTYPE:
                int len = stream.readBerLength();
                if (len != 0) throw new AsnEncodingException("Illegal EOF tag");
                return null;
            case Tag.BOOLEAN:
                return new BerBoolean(tag,stream);
            case Tag.INTEGER:
                return new BerInteger(tag,stream);
            case Tag.BITSTRING:
            case Tag.BITSTRING | Tag.CONSTRUCTED:
                return new BerBitString(tag,stream);
            case Tag.OCTETSTRING:
            case Tag.OCTETSTRING | Tag.CONSTRUCTED:
                return new BerOctetString(tag,stream);
            case Tag.NULL:
                return new BerNull(tag,stream);
            case Tag.OBJECTID:
                return new BerOID(tag,stream);
            case Tag.REAL:
                return new BerReal(tag,stream);
            case Tag.ENUMERATED:
                return new BerEnumerated(tag,stream);
            case Tag.NUMERICSTRING:
            case Tag.NUMERICSTRING | Tag.CONSTRUCTED:
                return new BerNumericString(tag,stream);
            case Tag.PRINTABLESTRING:
            case Tag.PRINTABLESTRING | Tag.CONSTRUCTED:
                return new BerPrintableString(tag,stream);
            case Tag.TELETEXSTRING:
            case Tag.TELETEXSTRING | Tag.CONSTRUCTED:
                return new BerTeletexString(tag,stream);
            case Tag.VIDEOTEXTSTRING:
            case Tag.VIDEOTEXTSTRING | Tag.CONSTRUCTED:
                return new BerVideoTextString(tag,stream);
            case Tag.GRAPHICSTRING:
            case Tag.GRAPHICSTRING | Tag.CONSTRUCTED:
                return new BerGraphicsString(tag,stream);
            case Tag.IA5STRING:
            case Tag.IA5STRING | Tag.CONSTRUCTED:
                return new BerIA5String(tag,stream);
            case Tag.VISIBLESTRING:
            case Tag.VISIBLESTRING | Tag.CONSTRUCTED:
                return new BerVisibleString(tag,stream);
            case Tag.GENERALSTRING:
            case Tag.GENERALSTRING | Tag.CONSTRUCTED:
                return new BerGeneralString(tag,stream);
            case Tag.UTCTIME:
            case Tag.UTCTIME | Tag.CONSTRUCTED:
                return new BerUTCTime(tag,stream);
            case Tag.GENERALTIME:
            case Tag.GENERALTIME | Tag.CONSTRUCTED:
                return new BerGeneralTime(tag,stream);
            default:
            	return create(tag, stream);		//modified by  Fatih Batuk (state parameter is deleted)
        }
    }    
    

    /**
     * This method must be overridden by a custom parser which takes the provided
     * tag identifier, determines based on the current parser state the appropriate
     * object to construct, and constructs that object. The method createUniversal
     * exists in order to simplify the parser; if the tag type is a universal type,
     * the universal tag may be called.
     * @param tag The tag used to define this element
     * @param state The current read-state we're in
     * @param stream The ASN.1 stream being parsed
     * @throws IOException If a problem occurs during parsing. Note that your
     * method may decide to throw an AsnEncodingException if it believes the
     * tag is illegal given the current state.
     * @return
     */
    public abstract BerNode create(int tag, BerInputStream stream) throws IOException;
    
    
    
    /**
     * The readPacket method is called by any code looking to parse an incoming
     * BER packet, such as a request packet. This starts the internal state of
     * this custom parser, and builds the appropriate node object, returning it.
     * On the end of file, this returns null.
     * @param stream
     * @return
     * @throws IOException
     */
    public BerNode readPacket(BerInputStream stream) throws IOException
    {
        int tag;
        
        /*
         * Attempt to read a tag. If we get an EOF while reading the tag, then
         * return null; assume the packet stream was closed.
         */
        try {
            tag = stream.readBerTag();
        }
        catch (EOFException err) {
            return null;
        }
        
        /*
         * Now start my state, read the node and finish up.
         */
        
        return read(tag,stream);
    }
}


