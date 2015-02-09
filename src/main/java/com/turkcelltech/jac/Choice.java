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

import java.io.IOException;

import com.chaosinmotion.asn1.*;

/**
 * Represents asn.1 Choice object.
 * 
 * @author Fatih Batuk
 */
public class Choice extends Sequence
{

	private BerNode currentChoice;

	public	Choice()
	{
		super(Tag.UnknownChoiceTag);
		isInitialized = false;
	}

	/**
	 * Constructs an empty Choice.
	 *<p>
	 * @param name if this object becomes a component in a
	 * constructed type, this name can be used to retrieve
	 * the object.
	 */
	public
	Choice(String name)
	{
		super(Tag.UnknownChoiceTag);
		setName(name);
		isInitialized = false;
	}

	/*
	/*
	 * 
	 * This method "choose(..)" is removed in the JAC version 2.1. 
	 * The method 'choose' is no longer necessary. 
	 * Just set your inner element inside your choice object and then directly call choice.encode(..) method
	 * 
	public void choose(String elementName) throws AsnFatalException 
	{
		int location = getIndex(elementName);
		if (location == -1) {
			throw new AsnFatalException("\n >> No such element exist in this 'Choice' with name : \"" + elementName + "\"");
			//a.printStackTrace();
			//System.exit(-1);
		}
		currentChoice = get(location);
		currentChoice.true_();
		isInitialized = true;
	}
	*/
	
	 /**
	   * You can directly call encode method after setting (initializing) only 1 element in your choice object.
	   * This method encodes the choice object to Ber encoded byte array and writes it to the given BerOutputStream
	   * @param out
	   * @throws IOException
	   *  @author Fatih Batuk
	   */
		public void encode(BerOutputStream out) throws IOException {
			currentChoice = getCurrentChoice();	//this method initializes the current choice object if any element is initialized inside
			if(this.getTag() != Tag.UnknownChoiceTag) {
    			BerSequence seq = new BerSequence(this.getTag());
    			seq.addElement(currentChoice);
    			seq.writeElement(out);
    		}
    		else {
    			currentChoice.writeElement(out);
    		}
		}
		
		/**
		 * You can directly call this method.
		 * Call the method 'getCurrentChoice()' after decoding to learn which element is decoded from byte array inside choice record. 
		 * To AUTO decode a BER encoded Byte value to a Choice object..
		 * @param parser
		 * @param in
		 * @throws IOException
		 * @author Fatih Batuk
		 */
		public void decode(BerInputStream in) throws IOException
		{
			if(this.getTag()==Tag.UnknownChoiceTag) {
				int tag = in.readBerTag();
				BerNode encodedNode = getNodeIfExists(tag);
				if(encodedNode == null) {
					throw new AsnFatalException("\n >> During decoding, the encoded tag value in the byte array does not match with any element in the choice object! (if exists)The name of the choice object is: "+getName()+"\n");
				}
				else if(encodedNode instanceof JacConstruct) {
					((JacConstruct)encodedNode).readElement(tag, in);
				}
				else {
					((JacNode)encodedNode).readElement(in);
				}
			}
			else {
				setAllElementsAsOptional();
				super.decode(in);
			}
			this.isInitialized = true;
		}
		
		/**
		 * is used when decoding a constructed object (Sequence, Set, SequenceOf or SetOf or Choice)
		 * after reading the asn1 tag number from the input stream
		 */
		@Override
		public void readElement(int tag, BerInputStream in) throws IOException {
			setAllElementsAsOptional();
			super.readElement(tag, in);
		}
		
		
		/**
		 * @deprecated MANUAL decoding of a choice object is not allowed. You should use 'decode(BerInputStream in)' instead which is better
		 */
		public void decode(BerParser parser, BerInputStream in) throws IOException {
			
		}
	
		
	/**
	 * @return Returns the initialized element inside the choice object.
	 * @throws AsnFatalException
	 */
	public BerNode getCurrentChoice() throws AsnFatalException
	{
		currentChoice = getChosenElementWhenThisIsChoice();
		
		if (currentChoice == null) 
			throw new AsnFatalException("\n >> You did not set one element of your choice object (Or you have set more than one element). (if exists)name of the problemeatic choice object is: " + getName());
		
		return currentChoice;
	}
	
	public BerNode getNodeIfExists(int tagNumber) {
		BerNode current = null;
		for(int i=0; i<size(); i++) {
			if(this.get(i).getTag() == tagNumber) {
				current = this.get(i);
				break;
			}
		}
		return current;
	}
	
	/**
	 * Since this a a Choice object, every elements inside can be thought as optional elements like in a Sequence
	 */
	private void setAllElementsAsOptional() {
		for(int i=0; i<size(); i++) {
			this.get(i).setOptional(true);
		}
	}
	
	
	public String toString()
    {
        return toLabeledString("BerChoice");
    }
	
}

