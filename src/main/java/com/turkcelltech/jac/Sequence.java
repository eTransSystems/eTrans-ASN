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
 * Represents asn.1 SEQUENCE object.
 * 
 * @author Fatih Batuk
 *
 */
public class Sequence extends BerSequence implements JacConstruct
{
	/**
	* empty sequence
	*/
	public
	Sequence()
	{
		super();
		isInitialized = false;
	}

	/**
	 * Constructs an empty unitialized sequence.
	 *<p>
	 * @param name if this object becomes a component in a
	 * constructed type, this name can be used to retrieve
	 * the object.
	 */
	public
	Sequence(String name_)
	{
		super();
		setName(name_);
		isInitialized = false;
	}
	
	protected Sequence(int tag) {
		super(tag);
	}
	
	
  /**
   * encodes the sequence object to Ber encoded byte array
   * and writes it to the given BerOutputStream
   * @param out
   * @throws IOException
   *  @author Fatih Batuk
   */
	public void encode(BerOutputStream out) throws IOException{
		
			writeElement(out);
	}
	
	/**
	 * 
	 * To AUTO decode a BER encoded Byte value to a BerSequence object..
	 * @param parser
	 * @param in
	 * @throws IOException
	 * @author Fatih Batuk
	 */
	public void decode(BerInputStream in) throws IOException
	{
		int tag = in.readBerTag();
	    if (tag != getTag()) 
			throw new AsnFatalException("\n >> During decoding, the encoded tag value in the byte array does not match with this object's tag number ! ");
		
	    AutoParser parser = new AutoParser(size());
		BerSequence generatedSeq = new BerSequence(tag, parser, in, this);

		fillSequenceVariables(generatedSeq);
		isInitialized = true;
	}
	
	/**
	 * is used when decoding a constructed object (Sequence, Set, SequenceOf or SetOf) 
	 * after reading the asn1 tag number from the input stream
	 */
	//@Override
	public void readElement(int tag, BerInputStream in) throws IOException {  
		AutoParser parser = new AutoParser(size());
		BerSequence generatedSeq = new BerSequence(tag, parser, in, this);
		
		fillSequenceVariables(generatedSeq);
		isInitialized = true;
	}
	
	/**
	 * 
	 * To MANUAL decode a BER encoded Byte value to a BerSequence object..
	 * @param parser
	 * @param in
	 * @throws IOException
	 * @author Fatih Batuk
	 */
	public void decode(BerParser parser, BerInputStream in) throws IOException
	{
	   int tag = in.readBerTag();

	   if (tag != getTag()) 
			throw new AsnFatalException("\n >> During decoding, the encoded tag value in the byte array does not match with this object's tag number ! ");
		
	   BerSequence generatedSeq = new BerSequence(tag, parser,in);
	   //System.out.println("GeneratedSequence (manual) : \n" + generatedSeq.toString());	
	   fillSequenceVariables(generatedSeq);
	   isInitialized = true;
	}
	
	/**
	 * This method is used to match and fill the sequence elements
	 * @param generatedSeq
	 * @author Fatih Batuk
	 */
	public void fillSequenceVariables(BerSequence generatedSeq) throws AsnFatalException {
		fillSequenceVariables( generatedSeq, false );
	}

	/**
	 * This method copies values from one sequence to another.
	 * It does not worry about checking initialization or optional elements.
	 * @param generatedSeq
	 * @author Rob Baily
	 */
	public void copySequenceVariables(BerSequence generatedSeq) throws AsnFatalException {
		fillSequenceVariables( generatedSeq, true );
	}


	/**
	 * This method is used to match and fill the sequence elements
	 * @param generatedSeq
	 * @author Fatih Batuk
	 */
	private void fillSequenceVariables(BerSequence generatedSeq, boolean copyMode) throws AsnFatalException {

		int[] decodedStatus = new int[size()];
		boolean status;
		BerNode hold;
		
		for (int i=0; i<generatedSeq.size(); i++) {
			
			for (int j=0; j<this.size(); j++) {
				
				if (decodedStatus[j]==1) {		//to prevent setting again the same element
					continue;
				}
				BerNode currentNode = this.get(j);
				BerNode generatedNode = generatedSeq.get(i);
				
				//set the status variable
				status = false;
				//JE if(generatedNode.getTag() == currentNode.getTag()) {
				int cTag = currentNode.getTag() & Tag.UNCONSTRUCTED_MASK;  //JE
				if(generatedNode.getTag() == cTag) {	                   //JE
					// figure out if this is a composite node
					boolean isComposite = generatedNode instanceof BerSequence || generatedNode instanceof BerSet;
					// figure out whether the initialization status is OK
					boolean initializationStateOk =  generatedNode.isInitialized || (isComposite && ((BerConstruct)generatedNode).checkInitializedOrNot());
					if (!copyMode || initializationStateOk) {
						status = true;
					}
				}
				else {
                    /*
                    Commented out by RB - for our usage there is always a matching tag and this code
                    causes problems with optional choice elements as it gets confused trying to match
                    to a choice element that should have been in the sequence.
					//if there is a choice element which is not tagged and if there is an element inside that choice element which is encoded:
					if (currentNode instanceof Choice) {
						if((hold=((Choice)currentNode).getNodeIfExists(generatedNode.getTag())) != null ) {
							currentNode.true_(); //this is important
							currentNode = hold;
							status = true;
						}
					}
					*/
				}
				//continue if status is OK
				if (status) {
					
					while(currentNode instanceof Choice) {
						Choice chc = (Choice)currentNode;
						chc.true_();  	//this is important
						generatedNode = ((BerSequence)generatedNode).get(0);
						hold = chc.getNodeIfExists(generatedNode.getTag());
						if(hold == null) {
							throw new AsnFatalException("\n >> During decoding of inner choice object, the read tag value is not matched with any element inside the choice! (if exists) name of the choice object is: "+chc.getName());
						}
						currentNode = hold;
					}
					
					if (generatedNode instanceof BerEnumerated) {
						((Enumerated)currentNode).setValue ( ((BerEnumerated)generatedNode).getValue() );
					}	
					else if (generatedNode instanceof BerInteger) {
						((ASN1Integer)currentNode).setValue ( ((BerInteger)generatedNode).getValue() );
					}
					else if (generatedNode instanceof BerBoolean) {
						((ASN1Boolean)currentNode).setValue ( ((BerBoolean)generatedNode).getValue() );
					}
					else if (generatedNode instanceof BerBitString) {
						((BitString)currentNode).setValue ( ((BerBitString)generatedNode).getValue() );
					}
					else if (generatedNode instanceof BerIA5String) {
						((IA5String)currentNode).setValue ( ((BerIA5String)generatedNode).getValue() );
					}
					else if (generatedNode instanceof BerNull) {
						// if it is Null object do nothing, only set it as initialized :
						currentNode.true_();
					}
					else if (generatedNode instanceof BerOID) {
						((ObjectID)currentNode).setValue ( ((BerOID)generatedNode).getValue() );
					}
					else if (generatedNode instanceof BerOctetString) {
						((OctetString)currentNode).setValue ( ((BerOctetString)generatedNode).getValue() );
					}
					else if (generatedNode instanceof BerPrintableString) {
						((PrintableString)currentNode).setValue ( ((BerPrintableString)generatedNode).getValue() );
					}
					else if (generatedNode instanceof BerUtf8String) {
                        ((UTF8String)currentNode).setValue ( ((BerUtf8String)generatedNode).getValue() );
                    }
					else if (generatedNode instanceof BerUTCTime) {
						((UTCTime)currentNode).setValue ( ((BerUTCTime)generatedNode).getValue() );
					}
					else if (generatedNode  instanceof  BerSequence) {
						if (currentNode instanceof SequenceOf) {
							((SequenceOf)currentNode).checkAndSetList((BerSequence)generatedNode,copyMode);
						} else {
							((Sequence)currentNode).fillSequenceVariables((BerSequence)generatedNode,copyMode);
							currentNode.true_();	//This is important !
						}
					}
					else if (generatedNode  instanceof  BerSet) {
						if (currentNode instanceof SetOf) {
							((SetOf)currentNode).checkAndSetList((BerSet)generatedNode,copyMode);
							currentNode.true_(); //This is important !
						} else {
							((Set)currentNode).fillSetVariables((BerSet)generatedNode,copyMode);
							currentNode.true_();		//This is important !
						}
					}
					decodedStatus[j]=1;	// This is important to prevent setting again the same element
					break;
				}
			}
		}
		if (!copyMode) check_OptionalAndInitialized_Status();
	}
	
	/**
	 *  Checks the elements of the set whether there exists an uninitialized and not non-optional element exists or not
	 * @throws AsnFatalException
	 * @author Fatih Batuk
	 */
	private void check_OptionalAndInitialized_Status() throws AsnFatalException {
		BerNode node;
		for(int i=0; i<size();i++) {
			node = get(i);
			if (node.isInitialized == false && node.isOptional == false) {
				throw new AsnFatalException("\n >> In decoding process, one of the elements of your SEQUENCE (or an element of an inner sequnce/set) is not OPTIONAL and not initialized! (If exists)name of this element is : " + node.getName());
			}
		}
	}

}

