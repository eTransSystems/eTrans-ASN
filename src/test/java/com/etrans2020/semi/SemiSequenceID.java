package com.etrans2020.semi;

/*
 * Created by JAC (Java Asn1 Compiler)
 */

import com.turkcelltech.jac.Enumerated;
//import com.chaosinmotion.asn1.Tag;


public class SemiSequenceID extends Enumerated
{
	/**
	* To set your ENUMERATED type, just call one of the "setTo_elementName()" methods.
	* To encode/decode your object, just call encode(..) decode(..) methods
	* See 'TestProject.java' in the project to examine encoding/decoding examples
	*/
	
	public static final int  svcReq_ = 1;

	public void setTo_svcReq_1() {
		setValue(svcReq_);
	}

	public static final int  svcResp_ = 2;

	public void setTo_svcResp_2() {
		setValue(svcResp_);
	}

	public static final int  dataReq_ = 3;

	public void setTo_dataReq_3() {
		setValue(dataReq_);
	}

	public static final int  dataConf_ = 4;

	public void setTo_dataConf_4() {
		setValue(dataConf_);
	}

	public static final int  data_ = 5;

	public void setTo_data_5() {
		setValue(data_);
	}

	public static final int  accept_ = 6;

	public void setTo_accept_6() {
		setValue(accept_);
	}

	public static final int  receipt_ = 7;

	public void setTo_receipt_7() {
		setValue(receipt_);
	}

	public static final int  subscriptionReq_ = 8;

	public void setTo_subscriptionReq_8() {
		setValue(subscriptionReq_);
	}

	public static final int  subscriptinoResp_ = 9;

	public void setTo_subscriptinoResp_9() {
		setValue(subscriptinoResp_);
	}

	public static final int  subscriptionCancel_ = 10;

	public void setTo_subscriptionCancel_10() {
		setValue(subscriptionCancel_);
	}

	/* end of enumerated constants */

	/**
	* constructs empty ENUMERATED object
	*/
	public
	SemiSequenceID()
	{
		super();
	}
	
	/**
	* constructs empty ENUMERATED object with its name.
	*/
	public
	SemiSequenceID(String name)
	{
		super(name);
	}
	
	/**
	* if you want to set your ENUMERATED to a different undefined value, use this constructor.
	*/
	public
	SemiSequenceID(long value)
	{
		super(value);
	}
	
	/**
	* if you want to set your ENUMERATED to a different undefined value, use this constructor.
	*/
	public
	SemiSequenceID(String name, long value)
	{
		super(name, value);
	}
}
