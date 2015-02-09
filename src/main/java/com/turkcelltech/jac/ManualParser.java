package com.turkcelltech.jac;

import example_cdr.CallDetailRecord;

/*
 * Created by Fatih Batuk. Manual parser example.
 * 
 * You should also see AutoParser.java class. In JAC AutoParser.java is used during decoding.
 * And you may also want to see W.Woody's TestParser.java class to see 
 * the unmodified pure version just for use of his library.
 */

import java.io.IOException;
import com.chaosinmotion.asn1.*;

/**
 * This class was created to show manual decoding example of 
 * a "SEQUENCE OF/SET OF" object which holds CallDetailRecord.java objects
 * i.e. 
 * Pool ::= SEQUENCE OF CallDetailRecord
 * 
 * For "CallDetailRecord" look at the CallDetailRecord.java class under the "example_cdr" package
 * 
 * Such an object can also be decoded with AutoParser.
 * This is just an example to show.. Look "TestProject.java" to see usage of this class
 * 
 * -----------------
 * 
 * Another example is that :
 * if you will decode a SET OF or a SEQUENCE OF object which holds a tagged type
 * For example :
      MyInteger ::= [APPLICATION 76] INTEGER	--tagged type
      MySeqOf  ::= SEQUENCE OF MYINTEGER	-- SequenceOf that holds tagged types..
 * 
 * then create method should be overridden as:
 * 
   public BerNode create(int tag, BerInputStream stream) throws IOException
    {
		return new BerInteger(tag,stream);
	} 
	
	This also can be decoded automatically.
	This is only an example to show..
 * 
 * @author Fatih Batuk
 */
public class ManualParser extends BerParser {

	/**
	 * set the unknown object in your SequenceOf / SetOf below..
	 * <p>FIXME
	 * @author Fatih Batuk
	 */
	public BerNode create(int tag, BerInputStream stream) throws IOException
    {
		
		CallDetailRecord ss = new CallDetailRecord();
		AutoParser auto = new AutoParser(ss.size());		//We used autoParser, because a CallDetailRecord object is 
															//"well-formed" and can be decoded with auto-parser.
		return new BerSequence(tag, auto,stream, ss );
		
    }
	
}
	
	
	