package com.turkcelltech.jac;

import java.io.IOException;

import com.chaosinmotion.asn1.BerInputStream;

/**
 * this interface is implemented in Sequence, Set, SequenceOf and SetOf java classes.
 * @author Fatih Batuk
 */

public interface JacConstruct {
	
	/**
	 * is used when decoding a constructed object (Sequence, Set, SequenceOf or SetOf or Choice) 
	 * after reading the asn1 tag number from the input stream
	 * @param tag
	 * @param in
	 * @throws IOException
	 */
	public void readElement(int tag, BerInputStream in) throws IOException;
	
}
