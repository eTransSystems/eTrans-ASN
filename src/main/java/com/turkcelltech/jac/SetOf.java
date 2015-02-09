package com.turkcelltech.jac;

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

import java.io.IOException;
import java.lang.reflect.Constructor;

import com.chaosinmotion.asn1.*;

/**
 * This class holds an ASN.1 SET OF.

 *@author Fatih Batuk
 */
public class SetOf extends Set
{
	/**
	 * contents of the sequence of.
	 */
	private Class componentType;
	private JacNode content ;
	private int type;

	
	/**
	 * Constructs an empty set.
	 *<p>
	 * @param name if this object becomes a component in a
	 * constructed type, this name can be used to retrieve
	 * the object.
	 * @deprecated This costructor should not have called! 
	 * Because, the "componentType" of a SetOf object must be set.
	 * If you call this constructor then also call setComponentType() method.
	 */
	public
	SetOf()
	{
		super();
	}
	
	/**
	 * Constructs an empty set with given name.
	 *<p>
	 * @param name if this object becomes a component in a
	 * constructed type, this name can be used to retrieve
	 * the object.
	 * @deprecated This costructor should not be called! 
	 * Because, the "componentType" of a SetOf object must be set.
	 * If you call this constructor then also call setComponentType() method.
	 */
	public
	SetOf(String name)
	{
		super(name);
	}

	/**
	 * Constructs a set with the given	components.
	 *<p>
	 * @param name if this object becomes a component in a
	 * constructed type, this name can be used to retrieve
	 * the object.
	 *<p>
	 * @param value to set the "componentType" of a SetOf object
	 */
	public
	SetOf(String name, Object value)
	{
		super(name);
		setContents(value);
	}

	public
	SetOf(Object value)
	{
		super();
		setContents(value);
	}
	
	/**
	 * To decode a BER encoded Byte value to a BerSet automatically..
	 * @param parser
	 * @param in
	 * @throws IOException
	 */
	public void decode(BerInputStream in) throws IOException
	{
		int tag = in.readBerTag();

		if (tag != getTag()) 
			throw new AsnFatalException("\n >> During decoding, the encoded tag value in the byte array does not match with this object's tag number ! ");
		
		AutoParser parser = new AutoParser(size());
		BerSet generatedSet = new BerSet(tag, parser, in, this);	//powerful constructor :)
		
		checkAndSetList(generatedSet);
	}
	
	/**
	 * is used when decoding a constructed object (Sequence, Set, SequenceOf or SetOf) 
	 * after reading the asn1 tag number from the input stream
	 */
	@Override
	public void readElement(int tag, BerInputStream in) throws IOException {
		AutoParser parser = new AutoParser(size());
		BerSet generatedSet = new BerSet(tag, parser, in, this);	//powerful constructor :)
		checkAndSetList(generatedSet);
	}
	
	/**
	 * To decode a BER encoded Byte value to a BerSet manually..
	 * @param parser
	 * @param in
	 * @throws IOException
	 */
	public void decode(BerParser parser, BerInputStream in) throws IOException
	{
	   int tag = in.readBerTag();

	   if (tag != getTag()) 
			throw new AsnFatalException("\n >> During decoding, the encoded tag value in the byte array does not match with this object's tag number ! ");
		
	   BerSet generatedSet = new BerSet(tag, parser,in);
	   
	   checkAndSetList(generatedSet);
	}
	
	public void setComponentType(Object value) {
		
		setContents(value);
	}
	

	/**
	 * Adds an element to the end of the set
	 * @param element BerNode object to append
	 * to the components of	the set.
	 * @author Fatih Batuk
	 */
	public void	addElement(BerNode element)
	{
		if (componentType == null) {
			AsnFatalException a = new AsnFatalException("\n >> You did not set the componentType value for your SequenceOf object with name(if exists) : " + getName());   
			a.printStackTrace();
			System.exit(-1);
		}
		
		Class x = element.getClass();
		/**
		 * If you want to enable also adding "instanceof componentType" objects
		 * You can change the below if control as 
		 * "if (componentType.isInstance(element))" 
		 */
		if(x.equals(componentType))
			super.addElement(element);
		else {
			AsnFatalException e = new AsnFatalException("\n >> You can only add \"" + componentType.toString() + "\" type objects in this SetOf object with name(if exists): " + getName());
			e.printStackTrace();
			System.exit(-1);
		}		
	}	
	
	protected void checkAndSetList(BerSet generatedSet) {
		 if(this.type==Tag.PrimitiveType) {
			   setList(generatedSet.getList());	
			 }
			 else {
				 castSetOfContent(generatedSet);
			}
	}
	
	private void castSetOfContent(BerSet generatedSet) {
		if(generatedSet.size() != 0) {
			try {
				if(type==Tag.SequenceType) {
					for(int i=0; i<generatedSet.size(); i++) {
						Constructor cons = componentType.getConstructor();
						Sequence element = (Sequence)cons.newInstance();
						element.fillSequenceVariables((BerSequence)generatedSet.get(i));
						this.addElement(element);
						this.true_();	//important
					}
				}
				else if(type==Tag.SetType) {
					for(int i=0; i<generatedSet.size(); i++) {
						Constructor cons = componentType.getConstructor();
						Set element = (Set)cons.newInstance();
						element.fillSetVariables((BerSet)generatedSet.get(i));
						this.addElement(element);
						this.true_();	//important
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setContents (Object value) {
		
		if(value instanceof Class)
			componentType = (Class)value;
		else
			componentType = value.getClass();
		
		content = (JacNode)value;
		
		if (content instanceof Sequence) 
			type = Tag.SequenceType;
		else if (content instanceof Set) 
			type = Tag.SetType;
		else 
			type = Tag.PrimitiveType;
	}
	
	public int getType () {
		return type;
	}
	
	public JacNode getContent () {
		return content;
	}
	
}

