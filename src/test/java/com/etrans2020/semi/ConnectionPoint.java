package com.etrans2020.semi;

/*
 * Created by JAC (Java Asn1 Compiler)
 */

import com.turkcelltech.jac.*;
import com.chaosinmotion.asn1.Tag;

public class ConnectionPoint extends Sequence
{
	/**
	 * if you want to set/fill an element below, just call the setValue(..) method over its instance.
	 *
	 * To encode/decode your object, just call encode(..) decode(..) methods.
	 * See 'TestProject.java' in the project to examine encoding/decoding examples
	 */
	public IpAddress address = new IpAddress("address");
	public PortNumber port = new PortNumber("port");
	/* end of element declarations */
	
	/**
	* asn.1 SEQUENCE constructor
	*/
	public
	ConnectionPoint()
	{
		super();
		setUpElements();
	}

	/**
	* asn.1 SEQUENCE constructor with its name
	*/
	public
	ConnectionPoint(String name)
	{
		super(name);
		setUpElements();
	}
	

	protected void
	setUpElements()
	{
	    int tagNumber = 0;
		super.addElement(address);
		address.setOptional(true);
		address.setTagNumber( tagNumber ++ );
		super.addElement(port);
		port.setTagNumber( tagNumber ++ );
	/* end of element setup */
	}




}
