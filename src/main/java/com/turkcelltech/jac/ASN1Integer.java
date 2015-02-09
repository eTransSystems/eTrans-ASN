package	com.turkcelltech.jac;

/*
 * created by Fatih Batuk on September, 2007.
 * 
* The classes of this package have a role between the parser generated classes
* and W.Woody's asn.1 library. Our parser reads the asn.1 file and creates .java classes, 
* and the generated classes extend the related class of this package to use the asn.1 library for doing encoding/decoding.
* The generation of classes is handled by a framework created by javacc.
* The project "Arc" uses this framework to parse an asn.1 file. We have modified and used this framework.
* Also we have made lots of important modifications in W.Woody's Library to integrate and combine library with the generated classes.
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
 * This class represents an ASN.1 INTEGER.
 * <p>
 * @author Fatih Batuk
 */
public class ASN1Integer extends BerInteger
{
	/**
	 * Constructs an empty integer.
	 * @author Fatih Batuk
	 */
	public ASN1Integer() {
		super();
		isInitialized = false;
	}
	/**
	 * constructs a named Integer.. - Many times is not used
	 * @param name_ String
	 * @author Fatih Batuk
	 */
	public		
	ASN1Integer(String name_)
	{
		super();
		setName(name_);
		isInitialized = false;
	}
	
	/**
	 * Constructs an integer with the given value. - most important constructor for integer type
	 * @param value	is the integer value
	 * @author Fatih Batuk
	 */
	public
	ASN1Integer(long value)
	{
		super(value);
		isInitialized = true;
	}
	/**
	 * constructor to build an Asn1Integer object with its value and name.
	 * @param value
	 * @param name
	 */
	public
	ASN1Integer(long value, String name)
	{
		super(value);
		setName(name);
		isInitialized = true;
	}
	
	/**
	 * This constructor is for use in the generated classes. (the classes generated from your asn.1 protocol by javacc)
	 * If you want to set a range to your asn.1 INTEGER object just call setMin(..) and setMax(..) methods.
	 * @param constraintName
	 * @param constraintValue
	 * @param constraint2Name
	 * @param constraint2Value
	 * @author Fatih Batuk
	 */
	public		
	ASN1Integer(String constraintName, long constraintValue, String constraint2Name, long constraint2Value)
	{
		super();
		isInitialized = false;
		
		/*
		 * We handled the min max control in this way, because the order of parameters is unknown
		 * for example following definitions in your asn.1 protocol are all accepted:
		 * 
		 *  Code ::= INTEGER {max (16777215)}
		 *  Minutes ::= INTEGER {first (0), last (59)}
		 *  Hours ::= INTEGER {min (0), max (23)}
		 *  MessageLength ::= INTEGER {min (0)} 
		 *  
		 *  If you have 
		 *  emsNum ::= INTEGER(0..72)
		 *  In this case the min and max values are not set.. (Use min and max keywords)
		 */
		
		if (constraintName.equalsIgnoreCase("min")){
			setMin(constraintValue);
		}
		else if (constraintName.equalsIgnoreCase("max")){
			setMax(constraintValue);
		}
		
		if (constraint2Name.equalsIgnoreCase("min")){
			setMin(constraint2Value);
		}
		else if (constraint2Name.equalsIgnoreCase("max")){
			setMax(constraint2Value);
		}		
	}
	
	/**
	 * This constructor is for use in the generated classes, the classes generated from your asn.1 protocol by javacc.
	 * If you want to set a range to your asn.1 INTEGER object just call setMin(..) and setMax(..) methods.
	 * @param constraintName
	 * @param constraintValue
	 * @author Fatih Batuk
	 */
	public		
	ASN1Integer(String constraintName, long constraintValue)
	{
		super();
		isInitialized = false;
		/*
		 * We handled the min max control in this way, because the order of parameters is unknown
		 * for example following definitions are all accepted in your asn.1 protocol:
		 * 
		 *  Code ::= INTEGER {max (16777215)}
		 *  Minutes ::= INTEGER {first (0), last (59)}
		 *  Hours ::= INTEGER {min (0), max (23)}
		 *  MessageLength ::= INTEGER {min (0)}
		 *  
		 *  If you have 
		 *  emsNum ::= INTEGER(0..72)
		 *  In this case the min and max values are not set.. (Use min and max keywords)
		 */
		
		if (constraintName.equalsIgnoreCase("min")){
			setMin(constraintValue);
		}
		else if (constraintName.equalsIgnoreCase("max")){
			setMax(constraintValue);
		}
	}
	
	/**
	 * to encode this Integer value in BER
	 * @author Fatih Batuk
	 */
	public void encode(BerOutputStream out) throws IOException
	{
		writeElement(out);
	}
	
	/**
	 * To decode a BER encoded Byte value to a BerInteger automatically..
	 * @param parser
	 * @param in
	 * @throws IOException
	 * @author Fatih Batuk
	 */
	public void
	decode(BerInputStream in) throws IOException
	{
		int tag = in.readBerTag();
		if (tag != getTag()) 
			throw new AsnFatalException("\n >> During decoding, the encoded tag value in the byte array does not match with this object's tag number ! ");
		
		readElement(in);
		
	}

}

