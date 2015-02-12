package com.etrans2020.semi;

/*
 * Created by JAC (Java Asn1 Compiler)
 */

import com.turkcelltech.jac.*;
import com.chaosinmotion.asn1.Tag;

public class IpAddress extends Choice
{
	/**
	* For encoding, just call directly encode(..) method after after setting (initializing) only 1 element in your choice object.
	* 
	* For decoding, just directly call decode(..) method. 
	* You can call the method 'getCurrentChoice()' after decoding to learn which element is decoded from byte array inside choice record.
	*
	* See 'choice example' section in 'TestProject.java' in the project for encoding/decoding examples with Choice objects.
	*/
	
	public IPv4Address ipv4Address = new IPv4Address("ipv4Address");
	public IPv6Address ipv6Address = new IPv6Address("ipv6Address");
	/* end of element declarations */
	
	/**
	* constructor without a name
	*/
	public
	IpAddress()
	{
		super();
		setUpElements();
	}
	
	/**
	* constructor with a name
	*/
	public
	IpAddress(String name)
	{
		super(name);
		setUpElements();
	}

	protected void
	setUpElements()
	{
	    int tagNumber = 0;
		super.addElement(ipv4Address);
		ipv4Address.setTagNumber( tagNumber ++ );
		super.addElement(ipv6Address);
		ipv6Address.setTagNumber( tagNumber ++ );
	/* end of element setup */
	}
}
