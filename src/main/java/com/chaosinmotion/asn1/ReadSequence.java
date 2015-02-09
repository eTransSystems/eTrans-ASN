//Created on Apr 26, 2005 11:44:27 PM by woody
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
 *  This is a wrapper class that supports reading in a sequence. This is called
 *  prior to reading the length object, and uses the length object to determine
 *  when it's time to stop reading objects.
 */
public class ReadSequence		// changed to public view by Fatih Batuk
{
    private BerInputStream		fInputStream;
    private long					fStart;
    private int					fLength;
    private boolean				fEOF;
    
    /**
     * Creates a new sequence tracking wrapper.
     * @param stream
     */
    public ReadSequence(BerInputStream stream) throws IOException
    {
        fInputStream = stream;
        fLength = fInputStream.readBerLength();
        fStart = fInputStream.getReadBytes();
        
        fEOF = (fLength == 0);
    }
    
    /**
     * Read the next tag in this encapsulated sequence. Returns null if we
     * have reached the end of the sequence of tags.
     * @return
     * @throws IOException
     */
    public int readBerTag() throws IOException
    {
        if (fEOF) return Tag.EOFTYPE;
        
        if (fLength != -1) {
            if (fStart + fLength <= fInputStream.getReadBytes()) {
                fEOF = true;
                return 0;
            }
        }
        
        int tag = fInputStream.readBerTag();
        if (tag == Tag.EOFTYPE) {
            if (0 != fInputStream.readBerLength()) {
                throw new AsnEncodingException("EOF tag must be zero length");
            }
            fEOF = true;
            
            if (fLength != -1) {
                /* EOF marker early? Skip rest of container */
                long remain = fInputStream.getReadBytes() - (fStart + fLength);
                if (remain > 0) fInputStream.skip(remain);
            }
            
            return Tag.EOFTYPE;
        }
        
        return tag;
    }
}


