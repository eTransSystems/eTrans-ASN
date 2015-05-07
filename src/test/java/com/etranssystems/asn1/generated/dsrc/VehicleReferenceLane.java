package com.etranssystems.asn1.generated.dsrc;

/*
 * Created by JAC (Java Asn1 Compiler)
 */

import com.turkcelltech.jac.ASN1Integer;
import com.turkcelltech.jac.Sequence;

public class VehicleReferenceLane extends Sequence
{
	/**
	 * if you want to set/fill an element below, just call the setValue(..) method over its instance.
	 *
	 * To encode/decode your object, just call encode(..) decode(..) methods.
	 * See 'TestProject.java' in the project to examine encoding/decoding examples
	 */
	public ASN1Integer laneNumber = new ASN1Integer("laneNumber");
	public ConnectsTo connectsTo = new ConnectsTo("connectsTo");
	/* end of element declarations */
	
	/**
	* asn.1 SEQUENCE constructor
	*/
	public VehicleReferenceLane()
	{
		super();
		setUpElements();
	}

	/**
	* asn.1 SEQUENCE constructor with its name
	*/
	public VehicleReferenceLane(String name)
	{
		super(name);
		setUpElements();
	}
	

	protected void
	setUpElements()
	{
	    int tagNumber = 0;
		super.addElement(laneNumber);
		laneNumber.setTagNumber( tagNumber ++ );
		super.addElement(connectsTo);
		connectsTo.setOptional(true);
		connectsTo.setTagNumber( tagNumber ++ );
	/* end of element setup */
	}




}
