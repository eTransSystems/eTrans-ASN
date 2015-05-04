package com.etranssystems.asn1.generated.dsrc;

/*
 * Created by JAC (Java Asn1 Compiler)
 */

import com.turkcelltech.jac.Sequence;

public class Position3D extends Sequence
{
	/**
	 * if you want to set/fill an element below, just call the setValue(..) method over its instance.
	 *
	 * To encode/decode your object, just call encode(..) decode(..) methods.
	 * See 'TestProject.java' in the project to examine encoding/decoding examples
	 */
	public Latitude lat = new Latitude("lat");
	public Longitude longElement = new Longitude("longElement");
	public Elevation elevation = new Elevation("elevation");
	/* end of element declarations */
	
	/**
	* asn.1 SEQUENCE constructor
	*/
	public Position3D()
	{
		super();
		setUpElements();
	}

	/**
	* asn.1 SEQUENCE constructor with its name
	*/
	public Position3D(String name)
	{
		super(name);
		setUpElements();
	}
	

	protected void
	setUpElements()
	{
	    int tagNumber = 0;
		super.addElement(lat);
		lat.setTagNumber( tagNumber ++ );
		super.addElement(longElement);
		longElement.setTagNumber( tagNumber ++ );
		super.addElement(elevation);
		elevation.setOptional(true);
		elevation.setTagNumber( tagNumber ++ );
	/* end of element setup */
	}




}
