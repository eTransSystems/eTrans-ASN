// Created on Apr 8, 2005 5:42:46 PM by woody
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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *	The LengthInputStream is an internal object used to track the
 *	number of bytes that have been read so far in the input stream.
 *	This can be used by BER object readers to track when an object
 *	data block has started, to know when it is time to terminate
 *	reading for a compound object
 */

class LengthInputStream extends FilterInputStream
{
	private long		fRead;
	private long		fMark;
	
	/**
	 * Construct a length input stream object
	 */
	protected LengthInputStream(InputStream arg0)
	{
		super(arg0);
		fRead = 0;
	}

	public int read() throws IOException
	{
		int r = super.read();
		if (r != -1) ++fRead;
		return r;
	}
	
	public int read(byte[] b, int off, int len) throws IOException
	{
		int r = super.read(b,off,len);
		if (r > 0) fRead += r;
		return r;
	}
	
	public void mark(int readlimit)
	{
		super.mark(readlimit);
		fMark = fRead;
	}
	
	public void reset() throws IOException
	{
		super.reset();
		fRead = fMark;
	}
	
	/**
	 * Returns the number of bytes that have been read so far from this stream.
	 * This returns the number of octets read since this object was created.
	 * @return The number of bytes read
	 */
	public long getReadBytes()
	{
		return fRead;
	}
    
    /**
     * Resets the read count to zero.
     */
    public void resetReadCount()
    {
        fRead = 0;
    }
}
