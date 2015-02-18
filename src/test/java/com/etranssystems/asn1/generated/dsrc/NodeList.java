package com.etranssystems.asn1.generated.dsrc;

/*
 * Created by JAC (Java Asn1 Compiler)
 */

import com.turkcelltech.jac.*;
//import com.chaosinmotion.asn1.Tag;

public class NodeList extends SequenceOf
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
	NodeList()
	{
		super(new Offsets("Offsets"));
	}
	
	/**
	* Uninitialized SEQUENCE OF constructor with name
	*/
	public
	NodeList(String name)
	{
		super(name, new Offsets("Offsets"));
	}

	public void addElement( Offsets element ) {
	    super.addElement( element );
	}

	public Offsets getElement( int index ) {
	    return (Offsets)get( index );
	}
}
