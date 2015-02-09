package	com.turkcelltech.jac;

/*
 * created by Fatih Batuk on September, 2007.
 * 
* The classes of this package have a role between the parser generated classes
* and W.Woody's asn.1 library. Our parser reads the asn.1 file and creates .java classes, 
* and the generated classes extends the related class of this package to use the asn.1 library for doing encoding/decoding.
* The generation of classes is handled by a framework created by javacc.
* The project "Arc" uses this framework to parse an asn.1 file. We have modified and used this framework.
* Also we have made important modifications in W.Woody's Library to integrate and combine library with the generated classes.
* But we tried not to change the general hierarchy and usage of the library.
* And all of these modifications are notified in the code.
* 
* If you want to have a look at the pure versions of these seperate projects:
* for ARC :
* 	http://www.forge.com.au/Research/products/arc/arc.htm
* for W.Woody's Library : 
* 	http://www.chaosinmotion.com/wiki/index.php?title=ASN.1_Library
*  
* Take a look at the README.html file of the project.
* If you want to have a look of the creation of .java classes take a look at the
* ASTBuitinType.java class. And if you need supply of more asn.1 types, you may
* try to modify this class, or whatever. However you should clearly understand 
* the generated Javacc an JJTree class hierarchy.
* 
* The supported asn.1 data types in this project are :
* CHOICE, SEQUENCE, SEQUENCE OF, SET, SET OF,  BOOLEAN, INTEGER, ANY, BIT STRING, 
* IA5String, Null, OBJECT IDENTIFIER, OCTET STRING, PRINTABLE STRING, 
* UTCTime and ENUMERATED.
*
* Contact Fatih Batuk at fatih_batuk@yahoo.com
*/

import com.chaosinmotion.asn1.*;

import java.io.IOException;

/**
 * This class represents an ASN.1 ANY object.
 * <p>
 * " This class is not a true representation of an ANY,
 *   as it does not understand the DEFINED BY
 *   syntax "
 * This class is not tested. You should care about this ýf you are using ANY data type.
 *
 * @deprecated ANY is a deprecated ASN.1 Structured Type. It has been replaced with X.680 Open Type.
 *
 * @author Fatih Batuk
 */

public class Any extends BerPrimitive
{

	public
	Any(int tag, byte[] data)
	{
		super(tag, data);
		isInitialized = true;
	}

	/**
	 * Constructs an empty ANY.
	 *<p>
	 * @param name if this object becomes a	component in a
	 * constructed type, this name can be used to retrieve
	 * the object.
	 */
	public
	Any(int tag, byte[] data, String name)
	{
		super(tag, data);
		setName(name);
		isInitialized = true;
	}

	/**
	 * to encode this ANY value in BER
	 */
	public void encode(BerOutputStream out) throws IOException
	{
		writeElement(out);
	}
	
	/**
	 * To decode a BER encoded Byte value to a BerPrimitive..
	 * @param parser
	 * @param in
	 * @throws IOException
	 */
	public void
	decode(int tag, BerInputStream in) throws IOException
	{
		setTag(tag);
		readElement(in);
	}
	
	 
	
}

