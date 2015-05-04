package com.etranssystems.asn1.generated.dsrc;

/*
 * Created by JAC (Java Asn1 Compiler)
 */

import com.turkcelltech.jac.Sequence;

public class VehicleSituationStatus extends Sequence
{
	/**
	 * if you want to set/fill an element below, just call the setValue(..) method over its instance.
	 *
	 * To encode/decode your object, just call encode(..) decode(..) methods.
	 * See 'TestProject.java' in the project to examine encoding/decoding examples
	 */
	public ExteriorLights lights = new ExteriorLights("lights");
	public ThrottlePosition throttlePos = new ThrottlePosition("throttlePos");
	public TirePressureGroup tirePressure = new TirePressureGroup("tirePressure");
	/* end of element declarations */
	
	/**
	* asn.1 SEQUENCE constructor
	*/
	public VehicleSituationStatus()
	{
		super();
		setUpElements();
	}

	/**
	* asn.1 SEQUENCE constructor with its name
	*/
	public VehicleSituationStatus(String name)
	{
		super(name);
		setUpElements();
	}
	

	protected void
	setUpElements()
	{
	    int tagNumber = 0;
		super.addElement(lights);
		lights.setTagNumber( tagNumber ++ );
		super.addElement(throttlePos);
		throttlePos.setOptional(true);
		throttlePos.setTagNumber( tagNumber ++ );
		super.addElement(tirePressure);
		tirePressure.setOptional(true);
		tirePressure.setTagNumber( tagNumber ++ );
	/* end of element setup */
	}



public static class TirePressureGroup extends Sequence
{
	/**
	 * if you want to set/fill an element below, just call the setValue(..) method over its instance.
	 *
	 * To encode/decode your object, just call encode(..) decode(..) methods.
	 * See 'TestProject.java' in the project to examine encoding/decoding examples
	 */
	public TirePressure leftFront = new TirePressure("leftFront");
	public TirePressure leftRear = new TirePressure("leftRear");
	public TirePressure rightFront = new TirePressure("rightFront");
	public TirePressure rightRear = new TirePressure("rightRear");
	/* end of element declarations */
	
	/**
	* asn.1 SEQUENCE constructor
	*/
	public
	TirePressureGroup()
	{
		super();
		setUpElements();
	}

	/**
	* asn.1 SEQUENCE constructor with its name
	*/
	public
	TirePressureGroup(String name)
	{
		super(name);
		setUpElements();
	}
	

	protected void
	setUpElements()
	{
	    int tagNumber = 0;
		super.addElement(leftFront);
		leftFront.setTagNumber( tagNumber ++ );
		super.addElement(leftRear);
		leftRear.setTagNumber( tagNumber ++ );
		super.addElement(rightFront);
		rightFront.setTagNumber( tagNumber ++ );
		super.addElement(rightRear);
		rightRear.setTagNumber( tagNumber ++ );
	/* end of element setup */
	}


}

	// END element classes
}
