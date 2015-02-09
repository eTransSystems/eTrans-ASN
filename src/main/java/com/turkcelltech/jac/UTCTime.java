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

import java.util.Date;
import java.io.IOException;

import com.chaosinmotion.asn1.*;

/*
 * "
 * The UTCTime type denotes a "coordinated universal time"
 * or Greenwich Mean Time (GMT) value. A UTCTime value
 * includes the local time precise to either minutes or
 * seconds, and an offset from GMT in hours and minutes.
 *<p>
 * It takes any of the following forms:
 *<p>
 *<pre>
 *YYMMDDhhmmZ
 *YYMMDDhhmm+hh'mm'
 *YYMMDDhhmm-hh'mm'
 *YYMMDDhhmmssZ
 *YYMMDDhhmmss+hh'mm'
 *YYMMDDhhmmss-hh'mm'
 *</pre>
 *<p>
 * where:
 *<p>
 *<pre>
 * YY is the least significant two digits of the year
 * MM is the month (01 to 12)
 * DD is the day (01 to	31)
 * hh is the hour (00 to 23)
 * mm are the minutes (00 to 59)
 * ss are the seconds (00 to 59)
 *</pre>
 *<p>
 * Z indicates that local time is GMT, + indicates that	local
 * time is later than GMT, and - indicates that local time is
 * earlier than GMT.
 *<p>
 * hh' is the absolute value of the offset from GMT in hours and
 * mm' is the absolute value of the offset from GMT in minutes
 * "
 * comment by David Taylor
 */

/**
 * Represents asn.1 UTC Time object
 * @author Fatih Batuk
 */
public class UTCTime extends BerUTCTime
{

	public
	UTCTime()
	{
		super();
		isInitialized = false;
	}
	
	public
	UTCTime(String name)
	{
		super();
		setName(name);
		isInitialized = false;
	}

	public
	UTCTime(Date value)
	{
		super(value);
		isInitialized = true;
	}
	
	public
	UTCTime(String name, Date value)
	{
		super(value);
		setName(name);
		isInitialized = true;
	}

	/**
	 * to encode this UTCTime value in BER
	 */
	public void encode(BerOutputStream out) throws IOException
	{
		writeElement(out);
	}
	
	/**
	 * To decode a BER encoded Byte value to a BerUTCTime..
	 * @param parser
	 * @param in
	 * @throws IOException
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

