package com.etrans2020.semi;

/*
 * Created by JAC (Java Asn1 Compiler)
 */
 
import com.turkcelltech.jac.OctetString;
//import com.chaosinmotion.asn1.Tag;

public class TemporaryID extends OctetString
{
	/**
	* To set your object you can call the  method: "setValue(byte[] array)"  
	* To encode/decode your object, just call encode(..) decode(..) methods.
	* See 'TestProject.java' in the project to examine encoding/decoding examples
	*/
	
	/**
	* constructor to create "uninitialized" object.
	*/
	public
	TemporaryID()
	{
		super();
	}

	/**
	* constructor to create "uninitialized" object.
	*/
	public
	TemporaryID(String name)
	{
		super(name);
	}
	
	/**
	* constructor to create "initialized" object.
	*/
	public
	TemporaryID(byte[] value)
	{
		super(value);
	}
	
	/**
	* constructor to create "initialized" object with its name.
	*/
	public
	TemporaryID(String name, byte[] value)
	{
		super(name, value);
	}
}
