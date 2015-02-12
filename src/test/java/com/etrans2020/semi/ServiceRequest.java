package com.etrans2020.semi;

/*
 * Created by JAC (Java Asn1 Compiler)
 */

import com.turkcelltech.jac.*;
import com.chaosinmotion.asn1.Tag;

public class ServiceRequest extends Sequence
{
	/**
	 * if you want to set/fill an element below, just call the setValue(..) method over its instance.
	 *
	 * To encode/decode your object, just call encode(..) decode(..) methods.
	 * See 'TestProject.java' in the project to examine encoding/decoding examples
	 */
	public SemiDialogID dialogID = new SemiDialogID("dialogID");
	public SemiSequenceID seqID = new SemiSequenceID("seqID");
	public TemporaryID requestID = new TemporaryID("requestID");
	public ConnectionPoint destination = new ConnectionPoint("destination");
	/* end of element declarations */
	
	/**
	* asn.1 SEQUENCE constructor
	*/
	public
	ServiceRequest()
	{
		super();
		setUpElements();
	}

	/**
	* asn.1 SEQUENCE constructor with its name
	*/
	public
	ServiceRequest(String name)
	{
		super(name);
		setUpElements();
	}
	

	protected void
	setUpElements()
	{
	    int tagNumber = 0;
		super.addElement(dialogID);
		dialogID.setTagNumber( tagNumber ++ );
		super.addElement(seqID);
		seqID.setTagNumber( tagNumber ++ );
		super.addElement(requestID);
		requestID.setTagNumber( tagNumber ++ );
		super.addElement(destination);
		destination.setOptional(true);
		destination.setTagNumber( tagNumber ++ );
	/* end of element setup */
	}




}
