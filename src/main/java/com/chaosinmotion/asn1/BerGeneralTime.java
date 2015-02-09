/*  BerGeneralTime.java
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a UTC time object.
 */
public class BerGeneralTime extends BerNode
{
    private static  SimpleDateFormat    gFormat;
    private Date    fDate;

    public BerGeneralTime(int tag, Date date)
    {
        super(tag);
        
        fDate = date;
    }
    
    public BerGeneralTime(Date date)
    {
        this(Tag.GENERALTIME,date);
    }

    /**
     * Construct a boolean from the input stream
     * @param tag
     * @param stream
     * @throws IOException
     */
    public BerGeneralTime(int tag, BerInputStream stream) throws IOException
    {
        super(tag);
        
        fDate = parseDate(new String(stream.readOctetString(0 == (tag & Tag.CONSTRUCTED)),"UTF-8"));
    }

    /**
     * Write the BER element to the stream
     * @param stream
     * @throws IOException
     * @see com.chaosinmotion.asn1.BerNode#writeElement(com.chaosinmotion.asn1.BerOutputStream)
     */
    public void writeElement(BerOutputStream stream) throws IOException
    {
        String date = formatDate(fDate);
        
        byte[] b = date.getBytes("UTF-8");
        stream.writeBerTag(getTag() | (stream.isComplexOctetString(b.length) ? Tag.CONSTRUCTED : 0));
        stream.writeOctetString(b,0,b.length);
    }

    public Date getDate()
    {
        return fDate;
    }
    
    private static void initFormat()
    {
        if (gFormat == null) {
            gFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
        }
    }
    
    private static String formatDate(Date date)
    {
        initFormat();
        
        synchronized(gFormat) {
            String str = gFormat.format(date);
            
            /*
             * Trim the end of str until we either hit a '.' or a non zero value
             */
            for (;;) {
                if (str.endsWith("0")) {
                    str = str.substring(0,str.length()-1);
                } else if (str.endsWith(".")) {
                    str = str.substring(0,str.length()-1);
                    break;
                } else {
                    break;
                }
            }
            
            return str + "Z";
        }
    }
    
    private static Date parseDate(String date) throws AsnEncodingException
    {
        initFormat();
        
        synchronized(gFormat) {
            if (date.endsWith("Z")) {
                date = date.substring(0,date.length()-1);
            }
            try {
                return gFormat.parse(date);
            }
            catch (ParseException e) {
                throw new AsnEncodingException("Illegal formatted date read from input stream");
            }
        }
    }

    public String toString()
    {
        return "BerGeneralTime(" + Tag.toString(getTag()) + ")=" + fDate;
    }
}


