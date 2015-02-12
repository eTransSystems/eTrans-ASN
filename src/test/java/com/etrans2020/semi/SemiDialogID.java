package com.etrans2020.semi;

/*
 * Created by JAC (Java Asn1 Compiler)
 */

import com.turkcelltech.jac.Enumerated;
//import com.chaosinmotion.asn1.Tag;


public class SemiDialogID extends Enumerated
{
	/**
	* To set your ENUMERATED type, just call one of the "setTo_elementName()" methods.
	* To encode/decode your object, just call encode(..) decode(..) methods
	* See 'TestProject.java' in the project to examine encoding/decoding examples
	*/
	
	public static final int  vehSitData_ = 154;

	public void setTo_vehSitData_154() {
		setValue(vehSitData_);
	}

	public static final int  dataSubscription_ = 155;

	public void setTo_dataSubscription_155() {
		setValue(dataSubscription_);
	}

	public static final int  advSitDataDep_ = 156;

	public void setTo_advSitDataDep_156() {
		setValue(advSitDataDep_);
	}

	public static final int  advSitDatDistRSE_ = 157;

	public void setTo_advSitDatDistRSE_157() {
		setValue(advSitDatDistRSE_);
	}

	public static final int  reserved1_ = 158;

	public void setTo_reserved1_158() {
		setValue(reserved1_);
	}

	public static final int  reserved2_ = 159;

	public void setTo_reserved2_159() {
		setValue(reserved2_);
	}

	public static final int  objReg_ = 160;

	public void setTo_objReg_160() {
		setValue(objReg_);
	}

	public static final int  objDisc_ = 161;

	public void setTo_objDisc_161() {
		setValue(objDisc_);
	}

	public static final int  intersectionSitDataDep_ = 162;

	public void setTo_intersectionSitDataDep_162() {
		setValue(intersectionSitDataDep_);
	}

	public static final int  intersectionSitDataQuery_ = 163;

	public void setTo_intersectionSitDataQuery_163() {
		setValue(intersectionSitDataQuery_);
	}

	/* end of enumerated constants */

	/**
	* constructs empty ENUMERATED object
	*/
	public
	SemiDialogID()
	{
		super();
	}
	
	/**
	* constructs empty ENUMERATED object with its name.
	*/
	public
	SemiDialogID(String name)
	{
		super(name);
	}
	
	/**
	* if you want to set your ENUMERATED to a different undefined value, use this constructor.
	*/
	public
	SemiDialogID(long value)
	{
		super(value);
	}
	
	/**
	* if you want to set your ENUMERATED to a different undefined value, use this constructor.
	*/
	public
	SemiDialogID(String name, long value)
	{
		super(name, value);
	}
}
