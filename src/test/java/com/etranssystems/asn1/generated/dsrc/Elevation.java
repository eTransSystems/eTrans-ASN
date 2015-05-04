package com.etranssystems.asn1.generated.dsrc;

/*
 * Created by JAC (Java Asn1 Compiler)
 */
 
import com.turkcelltech.jac.OctetString;
//import com.chaosinmotion.asn1.Tag;

public class Elevation extends OctetString
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
	Elevation()
	{
		super();
	}

	/**
	* constructor to create "uninitialized" object.
	*/
	public
	Elevation(String name)
	{
		super(name);
	}
	
	/**
	* constructor to create "initialized" object.
	*/
	public
	Elevation(byte[] value)
	{
		super(value);
	}
	
	/**
	* constructor to create "initialized" object with its name.
	*/
	public
	Elevation(String name, byte[] value)
	{
		super(name, value);
	}
}
