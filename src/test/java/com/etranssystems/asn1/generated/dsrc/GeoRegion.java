package com.etranssystems.asn1.generated.dsrc;

/*
 * Created by JAC (Java Asn1 Compiler)
 */

import com.turkcelltech.jac.Sequence;

public class GeoRegion extends Sequence
{
	/**
	 * if you want to set/fill an element below, just call the setValue(..) method over its instance.
	 *
	 * To encode/decode your object, just call encode(..) decode(..) methods.
	 * See 'TestProject.java' in the project to examine encoding/decoding examples
	 */
	public Position3D nwCorner = new Position3D("nwCorner");
	public Position3D seCorner = new Position3D("seCorner");
	/* end of element declarations */
	
	/**
	* asn.1 SEQUENCE constructor
	*/
	public GeoRegion()
	{
		super();
		setUpElements();
	}

	/**
	* asn.1 SEQUENCE constructor with its name
	*/
	public GeoRegion(String name)
	{
		super(name);
		setUpElements();
	}
	

	protected void
	setUpElements()
	{
	    int tagNumber = 0;
		super.addElement(nwCorner);
		nwCorner.setTagNumber( tagNumber ++ );
		super.addElement(seCorner);
		seCorner.setTagNumber( tagNumber ++ );
	/* end of element setup */
	}




}
