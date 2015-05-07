package com.etranssystems.asn1.generated.dsrc;

/*
 * Created by JAC (Java Asn1 Compiler)
 */

import com.turkcelltech.jac.ASN1Integer;
import com.turkcelltech.jac.Sequence;
import com.turkcelltech.jac.SequenceOf;

public class Approach extends Sequence
{
	/**
	 * if you want to set/fill an element below, just call the setValue(..) method over its instance.
	 *
	 * To encode/decode your object, just call encode(..) decode(..) methods.
	 * See 'TestProject.java' in the project to examine encoding/decoding examples
	 */
	public DrivingLanes drivingLanes = new DrivingLanes("drivingLanes");
		public ASN1Integer id = new ASN1Integer("id");
	/* end of element declarations */
	
	/**
	* asn.1 SEQUENCE constructor
	*/
	public Approach()
	{
		super();
		setUpElements();
	}

	/**
	* asn.1 SEQUENCE constructor with its name
	*/
	public Approach(String name)
	{
		super(name);
		setUpElements();
	}
	

	protected void
	setUpElements()
	{
	    int tagNumber = 0;
		super.addElement(id);
		id.setTagNumber( tagNumber ++ );
		super.addElement(drivingLanes);
		drivingLanes.setOptional(true);
		drivingLanes.setTagNumber( tagNumber ++ );
	/* end of element setup */
	}



public static class DrivingLanes extends SequenceOf
{
	/**
	* To add elements to your SequenceOf object, just call the addElement(..) method.
	* You can only add an element to your SequenceOf object if it is 
	* an instance of the elementType defined in below constructors.
	*
	* To encode/decode your object, just call encode(..) decode(..) methods
	* See 'TestProject.java' in the project to examine encoding/decoding examples
	*/
	
	/**
	* Uninitialized SEQUENCE OF constructor 
	*/
	public
	DrivingLanes()
	{
		super(new VehicleReferenceLane("DrivingLanes"));
	}
	
	/**
	* Uninitialized SEQUENCE OF constructor with name
	*/
	public
	DrivingLanes(String name)
	{
		super(name, new VehicleReferenceLane("DrivingLanes"));
	}

	public void addElement( VehicleReferenceLane element ) {
	    super.addElement( element );
	}

	public VehicleReferenceLane getElement( int index ) {
	    return (VehicleReferenceLane)get( index );
	}


}

	// END element classes
}
