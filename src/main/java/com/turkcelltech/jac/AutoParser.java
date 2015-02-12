package com.turkcelltech.jac;

/*
 * Created by Fatih Batuk on September, 2007.
 * 
 * This class is for decoding asn.1 SEQUENCE/SET objects.
 * 
 * Contact Fatih Batuk at fatih_batuk@yahoo.com
 */

import java.io.IOException;

import com.chaosinmotion.asn1.*;


/**
 *  This class is used for decoding Sequence and Set objects..
 * 
 * @author Fatih Batuk
 */

public class AutoParser {

	/**
	 * array size must be equal or greater than size of the decoded sequence/set element
	 */
	byte[] elementDecodingStatus;
	int arraySize;
	
	/**
	 * Default arraySize is 100.
	 */
	public AutoParser() {
		this(100);
	}
	
    public AutoParser(int size) {
    	arraySize = size;
    	elementDecodingStatus = new byte[arraySize];
    }
    
    public void reset() {
    	elementDecodingStatus = new byte[arraySize];
    }
    
    public void setSize(int size) {
    	arraySize = size;
    	elementDecodingStatus = new byte[arraySize];
    }
   
    /*
     * read(..) method is disabled 
     */
    //BerNode read(int tag, int state, BerInputStream stream) throws IOException {  }
    
    /**
     * Will be called when decoding process to create the Sequence object from input stream
     * @param tag
     * @param stream
     * @param seq
     * @return
     * @throws IOException
     * @author Fatih Batuk
     */
    public BerNode readSeq(int tag, BerInputStream stream, BerSequence seq) throws IOException
    {
    	/*
       	 * Here we are masking tag value with our UNCONSTRUCTED_MASK constant to gain the original  
       	 * tag value for EXPLICITLY encoded primitives and constructed objects(Sequence/Set).
       	 * The tag values of other primitives are not affected by this masking operation.
       	 * However if you tagged a data type in your asn.1 protocol like 
       	 * [ <tagNumber> ] (which is CONTEXT class tagging) or
       	 * [PRIVATE <tagNumber>] or [APPLICATION <tagNumber>] 
       	 * the 'tagNumber' value should be in the range 0 to 127 (i.e. -1 < tagNumber < 128)
       	 * This constraint is not related with this masking. This is related 
       	 * with the design of the asn.1 library
       	 * 
       	 * -- Fatih Batuk
       	 */
  	   tag = tag & Tag.UNCONSTRUCTED_MASK;   
    	 
         if (tag == Tag.EOFTYPE) {
        	 int len = stream.readBerLength();
             if (len != 0) throw new AsnEncodingException("Illegal EOF tag");
             return null;
         } 
       
       ReadSequence readSeq;
       
       BerNode currentNode = null;
       BerNode hold;
       boolean state; 
	      
       for (int i=0; i<seq.size(); i++) {
    	   //set the state var
    	   state = false;
    	   
    	   //JE if(seq.get(i).getTag() == tag) {
       	   int cTag = seq.get(i).getTag()& Tag.UNCONSTRUCTED_MASK;   //JE
       	   if( cTag == tag) {     //JE
    		   state = true;
    		   currentNode = seq.get(i);
    	   }
    	   else {
               /*
               Commented out by RB - for our usage there is always a matching tag and this code
               causes problems with optional choice elements as it gets confused trying to match
               to a choice element that should have been in the sequence.
    		   if(seq.get(i) instanceof Choice) {
    			   if( (hold=((Choice)(seq.get(i))).getNodeIfExists(tag)) != null) {
    				   state = true;
    				   currentNode = hold;
    			   }
    		   }
    		   */
    	   }
    	   //check the state var
            if (state) {
            	
            	if (elementDecodingStatus[i] == 1) {
        			continue;
        		}
            	
            	//CHOICE CONTROL!
            	if (currentNode instanceof Choice) {
            		Choice chc = (Choice)currentNode;
            		elementDecodingStatus[i]=1;
            		return new BerSequence(tag, new AutoParser(((BerConstruct)currentNode).size()), stream, chc);
            	}
            	
            	if (currentNode instanceof ASN1Integer) {
            		elementDecodingStatus[i]=1;
            		if(currentNode.getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            			    return new BerInteger(tag,stream);
            			}
            		}		
            		return new BerInteger(tag,stream);
            	}
            	
            	else if (currentNode instanceof ASN1Boolean) {
            		elementDecodingStatus[i]=1;
            		if(currentNode.getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            				return new BerBoolean(tag,stream);
            			 }
            		}
            		return new BerBoolean(tag,stream);
            	}
            	
            	else if (currentNode instanceof BitString) {
            		elementDecodingStatus[i]=1;
            		if(currentNode.getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            				return new BerBitString(tag,stream);
            			 }
            		}
            		return new BerBitString(tag,stream);
            	}
            	
            	else if (currentNode instanceof Null) {
            		elementDecodingStatus[i]=1;
            		if(currentNode.getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            				return new BerNull(tag,stream);
            			 }
            		}
            		return new BerNull(tag,stream);
            	}
            	else if (currentNode instanceof IA5String) {
            		elementDecodingStatus[i]=1;
            		if(currentNode.getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            			    return new BerIA5String(tag,stream);
            			 }
            		}
            		return new BerIA5String(tag,stream);
            	}
            	else if (currentNode instanceof ObjectID) {
            		elementDecodingStatus[i]=1;
            		if(currentNode.getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            			    return new BerOID(tag,stream);
            			 }
            		}
            		return new BerOID(tag,stream);
            	}
            	else if (currentNode instanceof OctetString) {
            		elementDecodingStatus[i]=1;
            		if(currentNode.getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            			    return new BerOctetString(tag,stream);
            			 }
            		}
            		return new BerOctetString(tag,stream);
            	}
            	else if (currentNode instanceof PrintableString) {
            		elementDecodingStatus[i]=1;
            		if(currentNode.getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            			    return new BerPrintableString(tag,stream);
            			 }
            		}
            		return new BerPrintableString(tag,stream);
            	}
            	else if (currentNode instanceof UTCTime) {
            		elementDecodingStatus[i]=1;
            		if(currentNode.getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            			    return new BerUTCTime(tag,stream);
            			 }
            		}
            		return new BerUTCTime(tag,stream);
            	}
            	else if (currentNode instanceof Enumerated) {
            		elementDecodingStatus[i]=1;
            		if(currentNode.getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            			    return new BerEnumerated(tag,stream);
            			 }
            		}
            		return new BerEnumerated(tag,stream);
            	}
            	
            	else if (currentNode instanceof SequenceOf) {
        			elementDecodingStatus[i]=1;
        			return new BerSequence(tag, new AutoParser(((SequenceOf)currentNode).size()), stream, (SequenceOf)currentNode);
            	}
        	
            	else if (currentNode instanceof SetOf) {
        			elementDecodingStatus[i]=1;
        			return new BerSet(tag, new AutoParser(((SetOf)currentNode).size()),stream, (SetOf)currentNode );	
            	}
            	
            	else if (currentNode instanceof Sequence) {
            			elementDecodingStatus[i]=1;
            			return new BerSequence(tag, new AutoParser(((Sequence)currentNode).size()), stream, (Sequence)currentNode);
            	}
            	
            	else if (currentNode instanceof Set) {
            			elementDecodingStatus[i]=1;
            			return new BerSet(tag, new AutoParser(((Set)currentNode).size()),stream, (Set)currentNode );	
            	}
            }
       } //end of for
       throw new AsnFatalException("\n >> Tag number could not have found in this SEQUENCE! (If exists) Sequence name is : " +  seq.getName()); 

    }
    
    /**
     * Will be called during decoding process to create the Set object from the input stream
     * @param tag
     * @param stream
     * @param set
     * @return
     * @throws IOException
     * @author Fatih Batuk
     */
    public BerNode readSet(int tag, BerInputStream stream, BerSet set) throws IOException
    {
    	/*
      	 * Here we are masking tag value with our UNCONSTRUCTED_MASK constant to gain the original  
      	 * tag value for EXPLICITLY encoded primitives and constructed objects(Sequence/Set).
      	 * The tag values of other primitives are not affected by this masking operation.
      	 * However if you tagged a data type in your asn.1 protocol like 
      	 * [ <tagNumber> ] (which is CONTEXT class tagging) or
      	 * [PRIVATE <tagNumber>] or [APPLICATION <tagNumber>] 
      	 * the 'tagNumber' value should be in the range 0 to 127 (i.e. -1 < tagNumber < 128)
      	 * This constraint is not related with this masking. This is related 
      	 * with the design of the asn.1 library
      	 * 
      	 * -- Fatih Batuk
      	 */
       tag = tag & Tag.UNCONSTRUCTED_MASK;
    	 
       if (tag == Tag.EOFTYPE) {
      	 int len = stream.readBerLength();
           if (len != 0) throw new AsnEncodingException("Illegal EOF tag");
           return null;
       }
       
       ReadSequence readSeq;
       
       BerNode currentNode = null;
       BerNode hold;
       boolean state; 
       
       for (int i=0; i<set.size(); i++) {
    	   
    	 //set the state var
    	   state = false;
    	   if(set.get(i).getTag() == tag) {
    		   state = true;
    		   currentNode = set.get(i);
    	   }
    	   else {
    		   if(set.get(i) instanceof Choice) {
    			   if( (hold=((Choice)(set.get(i))).getNodeIfExists(tag)) != null) {
    				   state = true;
    				   currentNode = hold;
    			   }
    		   }
    	   }
    	   //check the state var
            if (state) {
            	
            	if (elementDecodingStatus[i] == 1) {
        			continue;
        		}
            	
            	//CHOICE CONTROL!
            	if (currentNode instanceof Choice) {
            		Choice chc = (Choice)currentNode;
            		elementDecodingStatus[i]=1;
            		return new BerSequence(tag, new AutoParser(((BerConstruct)currentNode).size()), stream, chc);
            	}
            	
            	if (currentNode instanceof ASN1Integer) {
            		elementDecodingStatus[i]=1;
            		if((currentNode).getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            			    return new BerInteger(tag,stream);
            			 }
            		}		
            		return new BerInteger(tag,stream);
            	}
            	
            	else if (currentNode instanceof ASN1Boolean) {
            		elementDecodingStatus[i]=1;
            		if((currentNode).getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            				return new BerBoolean(tag,stream);
            			 }
            		}
            		return new BerBoolean(tag,stream);
            	}
            	
            	else if (currentNode instanceof BitString) {
            		elementDecodingStatus[i]=1;
            		if((currentNode).getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            				return new BerBitString(tag,stream);
            			 }
            		}
            		return new BerBitString(tag,stream);
            	}
            	
            	else if (currentNode instanceof Null) {
            		elementDecodingStatus[i]=1;
            		if((currentNode).getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            				return new BerNull(tag,stream);
            			 }
            		}
            		return new BerNull(tag,stream);
            	}
            	else if (currentNode instanceof IA5String) {
            		elementDecodingStatus[i]=1;
            		if((currentNode).getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            			    return new BerIA5String(tag,stream);
            			 }
            		}
            		return new BerIA5String(tag,stream);
            	}
            	else if (currentNode instanceof ObjectID) {
            		elementDecodingStatus[i]=1;
            		if((currentNode).getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            			    return new BerOID(tag,stream);
            			 }
            		}
            		return new BerOID(tag,stream);
            	}
            	else if (currentNode instanceof OctetString) {
            		elementDecodingStatus[i]=1;
            		if((currentNode).getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            			    return new BerOctetString(tag,stream);
            			 }
            		}
            		return new BerOctetString(tag,stream);
            	}
            	else if (currentNode instanceof PrintableString) {
            		elementDecodingStatus[i]=1;
            		if((currentNode).getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            			    return new BerPrintableString(tag,stream);
            			 }
            		}
            		return new BerPrintableString(tag,stream);
            	}
            	else if (currentNode instanceof UTCTime) {
            		elementDecodingStatus[i]=1;
            		if((currentNode).getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            			    return new BerUTCTime(tag,stream);
            			 }
            		}
            		return new BerUTCTime(tag,stream);
            	}
            	else if (currentNode instanceof Enumerated) {
            		elementDecodingStatus[i]=1;
            		if((currentNode).getTaggingMethod() == Tag.EXPLICIT) {
            			readSeq = new ReadSequence(stream);
            			if (0 != (readSeq.readBerTag())) {
            			    return new BerEnumerated(tag,stream);
            			 }
            		}
            		return new BerEnumerated(tag,stream);
            	}
            	
            	else if (currentNode instanceof SequenceOf) {
        			elementDecodingStatus[i]=1;
        			return new BerSequence(tag, new AutoParser(((SequenceOf)currentNode).size()), stream, (SequenceOf)currentNode);	
            	}
        	
            	else if (currentNode instanceof SetOf) {	
        			elementDecodingStatus[i]=1;
        			return new BerSet(tag, new AutoParser(((SetOf)currentNode).size()),stream, (SetOf)currentNode );		
            	}
            	
            	else if (currentNode instanceof Sequence) {
            			elementDecodingStatus[i]=1;
            			return new BerSequence(tag, new AutoParser(((Sequence)currentNode).size()), stream, (Sequence)currentNode);	
            	}
            	
            	else if (currentNode instanceof Set) {	
            			elementDecodingStatus[i]=1;
            			return new BerSet(tag, new AutoParser(((Set)currentNode).size()),stream, (Set)currentNode );		
            	}
            }
       }  //end of for
       throw new AsnFatalException("\n >> Tag number could not have found in this SET! (If exists) Set name is : " +  set.getName()); 
     
    }
    
    
    /**
     * For reading(decoding) SequenceOf/SetOf objects when they hold primitives... 
     * @param tag
     * @param stream
     * @return
     */
    public BerNode read(int tag, BerInputStream stream) throws IOException
    {
    	/*
       	 * Here we are masking tag value with our UNCONSTRUCTED_MASK constant to gain the original  
       	 * tag value for EXPLICITLY encoded primitives and constructed objects(Sequence/Set).
       	 * The tag values of other primitives are not affected by this masking operation.
       	 * However if you tagged a data type in your asn.1 protocol like 
       	 * [ <tagNumber> ] (which is CONTEXT class tagging) or
       	 * [PRIVATE <tagNumber>] or [APPLICATION <tagNumber>] 
       	 * the 'tagNumber' value should be in the range 0 to 127 (i.e. -1 < tagNumber < 128)
       	 * This constraint is not related with this masking. This is related 
       	 * with the design of the asn.1 library
       	 * 
       	 * -- Fatih Batuk
       	 */
         tag = tag & Tag.UNCONSTRUCTED_MASK;
         
        switch (tag) {
            case Tag.EOFTYPE:
                int len = stream.readBerLength();
                if (len != 0) throw new AsnEncodingException("Illegal EOF tag");
                return null;
            case Tag.BOOLEAN:
                return new BerBoolean(tag,stream);
            case Tag.INTEGER:
                return new BerInteger(tag,stream);
            case Tag.BITSTRING:
                return new BerBitString(tag,stream);
            case Tag.OCTETSTRING:
                return new BerOctetString(tag,stream);
            case Tag.NULL:
                return new BerNull(tag,stream);
            case Tag.OBJECTID:
                return new BerOID(tag,stream);
            case Tag.ENUMERATED:
                return new BerEnumerated(tag,stream);
            case Tag.PRINTABLESTRING:
                return new BerPrintableString(tag,stream);
            case Tag.IA5STRING:
                return new BerIA5String(tag,stream);
            case Tag.UTCTIME:
                return new BerUTCTime(tag,stream);
            default:
            	
            	throw new AsnFatalException("\n >> The given 'Tag' does not belong to any primitive UNIVERSAL tag! Invalid tag number : " + tag);
        }
    }
    
        
    /*
      public BerNode readPacket(BerInputStream stream) throws IOException
    {
        int tag;
        
  
        try {
            tag = stream.readBerTag();
        }
        catch (EOFException err) {
            return null;
        }
        
        return read(tag,stream);
    }
    */

}
