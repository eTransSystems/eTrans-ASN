package com.etrans2020.semi;

/*
 * Created by JAC (Java Asn1 Compiler)
 */
 
import com.turkcelltech.jac.OctetString;
//import com.chaosinmotion.asn1.Tag;

public class IPv4Address extends OctetString
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
	IPv4Address()
	{
		super();
	}

	/**
	* constructor to create "uninitialized" object.
	*/
	public
	IPv4Address(String name)
	{
		super(name);
	}
	
	/**
	* constructor to create "initialized" object.
	*/
	public
	IPv4Address(byte[] value)
	{
		super(value);
	}
	
	/**
	* constructor to create "initialized" object with its name.
	*/
	public
	IPv4Address(String name, byte[] value)
	{
		super(name, value);
	}
}
