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
 * "This class holds an ASN.1 SET.
 *<p>
 * The SET type denotes an unordered collection of one
 * or more types.
 *<p>
 * As the components are unordered, they must have unique
 * tags."
 *<p>
 * @author Fatih Batuk
 */
public class Set extends BerSet implements JacConstruct
{

	/**
	* empty set object
	*/
	public	Set()
	{
		super();
		isInitialized = false;
	}

	/**
	 * Constructs an empty set.
	 *<p>
	 * @param name if this object becomes a component in a
	 * constructed type, this name can be used to retrieve
	 * the object.
	 */
	public
	Set(String name)
	{
		super();
		setName(name);
		isInitialized = false;
	}

	/**
	 * encodes the SET object to Ber encoded byte array
	 * and writes it to the given BerOutputStream
	 * @param out
	 * @throws IOException
	 */
		public void encode(BerOutputStream out) throws IOException {
			
				writeElement(out);
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
			BerSet generatedSet = new BerSet(tag, parser, in, this);

			fillSetVariables(generatedSet);
			isInitialized = true;
		}
		
		/**
		 * is used when decoding a constructed object (Sequence, Set, SequenceOf or SetOf) 
		 * after reading the asn1 tag number from the input stream
		 */
		@Override
		public void readElement(int tag, BerInputStream in) throws IOException {  
			AutoParser parser = new AutoParser(size());
			BerSet generatedSet = new BerSet(tag, parser, in, this);

			fillSetVariables(generatedSet);
			isInitialized = true;
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
		    //System.out.println("GeneratedSequence (manual) : \n" + generatedSeq.toString());	
		    fillSetVariables(generatedSet);
		    isInitialized = true;
		}
		
		/**
		 * This method is used to match and fill the set elements
		 * @param generatedSet
		 * @author Fatih Batuk
		 */
		public void fillSetVariables(BerSet generatedSet) throws AsnFatalException {
			
			int[] decodedStatus = new int[size()];
			boolean status;
			BerNode hold;
			
			for (int i=0; i<generatedSet.size(); i++) {
				for (int j=0; j<this.size(); j++) {
					
					if (decodedStatus[j]==1) {	//to prevent setting again the same element
						continue;
					}
					
					BerNode currentNode = this.get(j);
					BerNode generatedNode = generatedSet.get(i);
					
					//set the status variable
					status = false;
					if(generatedNode.getTag() == currentNode.getTag()) {
						status = true;
					}
					else {
						if (currentNode instanceof Choice) {
							if((hold=((Choice)currentNode).getNodeIfExists(generatedNode.getTag())) != null ) {
								currentNode.true_();  	//this is important
								currentNode = hold;
								status = true;
							}
						}
					}
					//continue if status is OK
					
					if (status) {
						
						if(currentNode instanceof Choice) {
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
							// if it is Null object do nothing. Only set it initialized :
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
						else if (generatedNode instanceof BerBoolean) {
							((ASN1Boolean)currentNode).setValue ( ((BerBoolean)generatedNode).getValue() );
						}
						else if (generatedNode instanceof BerUTCTime) {
							((UTCTime)currentNode).setValue ( ((BerUTCTime)generatedNode).getValue() );
						}	
						else if (generatedNode  instanceof  BerSequence) {
							if (currentNode instanceof SequenceOf) {
								((SequenceOf)currentNode).checkAndSetList((BerSequence)generatedNode);
							} else {
								((Sequence)currentNode).fillSequenceVariables((BerSequence)generatedNode);
								((Sequence)currentNode).true_();
							}
						}
						else if (generatedNode  instanceof  BerSet){
							if (currentNode instanceof SetOf) {
								((SetOf)currentNode).checkAndSetList((BerSet)generatedNode);
							} else {
								((Set)currentNode).fillSetVariables((BerSet)generatedNode);
								((Set)currentNode).true_();
							}
						}
						decodedStatus[j]=1;	// This is important to prevent setting again the same element
						break;
					}	
				}
			}
			check_OptionalAndInitialized_Status();
		}
		
		/**
		 * Checks the elements of the set whether there exists an uninitialized and not non-optional element exists or not
		 * @throws AsnFatalException
		 * @author Fatih Batuk
		 */
		private void check_OptionalAndInitialized_Status() throws AsnFatalException {
			BerNode node;
			for(int i=0; i<size();i++) {
				node = get(i);
				if (node.isInitialized == false && node.isOptional == false) {
					throw new AsnFatalException("\n >> In decoding process one of the elements of your SET (or an element of an inner sequnce/set) is not OPTIONAL and not initialized! (If exists)Element name is : " + node.getName());
				}
			}
		}
		
}

