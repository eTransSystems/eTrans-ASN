/*  BerConstruct.java
 *
 *  Created on Jun 1, 2006 by William Edward Woody
 */

/*
 * Copyright 2007 William Woody, All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list 
 * of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, this 
 * list of conditions and the following disclaimer in the documentation and/or other 
 * materials provided with the distribution.
 * 
 * 3. Neither the name of Chaos In Motion nor the names of its contributors may be used 
 * to endorse or promote products derived from this software without specific prior 
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR 
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH 
 * DAMAGE.
 * 
 * Contact William Woody at woody@alumni.caltech.edu or at woody@chaosinmotion.com. 
 * Chaos In Motion is at http://www.chaosinmotion.com
 */

package com.chaosinmotion.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.turkcelltech.jac.AutoParser;
import com.turkcelltech.jac.Choice;
import com.turkcelltech.jac.SequenceOf;
import com.turkcelltech.jac.SetOf;

/**
 * Represents a constructed object. A constructed object is a collection of other
 * BerNode objects.
 */
public abstract class BerConstruct extends JacNode
{
    private ArrayList fList ;

    
    protected BerConstruct(int tag)
    {
        super(tag);

        fList = new ArrayList();
    }
    
    /**
     * Read the construct into memory from the input stream
     * @param tag The tag used to define this element
     * @param state The current read-state we're in
     * @param parser The parser that is being used to parse this ASN.1 stream
     * @param stream The ASN.1 stream being parsed
     * @throws IOException 
     */
    public BerConstruct(int tag, int state, BerParser parser, BerInputStream stream) throws IOException
    {
        this(tag);
        
        int readTag;
        ReadSequence seq = new ReadSequence(stream);
        while (0 != (readTag = seq.readBerTag())) {
            fList.add(parser.read(readTag,stream));	// modified by Fatýh Batuk (state variable is deleted)
        }
    }
    
    /**
     * added by Fatih Batuk
     * @param tag
     * @param parser
     * @param stream
     * @throws IOException
     */
    public BerConstruct(int tag, BerParser parser, BerInputStream stream) throws IOException
    {
        this(tag);
        
        int readTag;
        ReadSequence seq = new ReadSequence(stream);
        while (0 != (readTag = seq.readBerTag())) {
            fList.add(parser.read(readTag,stream));	
        }
    }
    
    /**
     * Added by Fatih Batuk
     * @param tag
     * @param state
     * @param parser
     * @param stream
     * @param asn1Seq
     * @throws IOException
     * @author Fatih Batuk
     */
    public BerConstruct(int tag, AutoParser parser, BerInputStream stream, BerSequence asn1Seq) throws IOException
    {
        this(tag);
        
        int readTag;
        ReadSequence seq = new ReadSequence(stream);
        while (0 != (readTag = seq.readBerTag())) {
            fList.add(parser.readSeq(readTag, stream, asn1Seq));	
        }
    }
   
    
    /**
     * Added by Fatih Batuk
     * @param tag
     * @param state
     * @param parser
     * @param stream
     * @param asn1Seq
     * @throws IOException
     * @author Fatih Batuk
     */
    public BerConstruct(int tag, AutoParser parser, BerInputStream stream, BerSet asn1Set) throws IOException
    {
        this(tag);
        
        int readTag;
        ReadSequence seq = new ReadSequence(stream);
        while (0 != (readTag = seq.readBerTag())) {
            fList.add(parser.readSet(readTag, stream, asn1Set));	
        }
    }
    
    /**
     * Added by Fatih Batuk to decode SequenceOf objects
     * @param tag
     * @param parser
     * @param stream
     * @param asn1Seq
     * @throws IOException
     * @author Fatih Batuk
     */
    public BerConstruct(int tag, AutoParser parser, BerInputStream stream, SequenceOf asn1Seq) throws IOException
    {
        this(tag);
        
        int readTag;
        ReadSequence seq = new ReadSequence(stream);
        
        if (asn1Seq.getType() == Tag.SequenceType) {
        	
        	AutoParser contentParser;
        	BerSequence content = (BerSequence)asn1Seq.getContent();
        	
        	while ( 0 != (readTag = seq.readBerTag()) ) {
        		if (readTag != content.getTag()) 
        			throw new AsnFatalException("\n >> During decoding, the encoded tag value in the byte array does not match with the content of this SequenceOf object !");

        		contentParser = new AutoParser(content.size());
        		fList.add(new BerSequence(readTag,contentParser,stream,content));   
            }
        }
        else if (asn1Seq.getType() == Tag.SetType) {
        	
        	AutoParser contentParser;
        	BerSet content = (BerSet)asn1Seq.getContent();
        	
        	while ( 0 != (readTag = seq.readBerTag()) ) {
        		if (readTag != content.getTag()) 
        			throw new AsnFatalException("\n >> During decoding, the encoded tag value in the byte array does not match with the content of this SetOf object ! ");

        		contentParser = new AutoParser(content.size());
        		fList.add(new BerSet(readTag,contentParser,stream,content));    
            }
        	
        }
        else {	//Our sequenceOf holds primitives :
        	
        	JacNode content = asn1Seq.getContent();
        	
        	while ( 0 != (readTag = seq.readBerTag()) ) {
        		if (readTag != content.getTag()) 
        			throw new AsnFatalException("\n >> During decoding, the encoded tag value in the byte array does not match with the content of this SetOf object ! ");
        		
        		content.readElement(stream);
        		check_and_add_thePrimitiveNode(content);
        		check_explicitContent(content);
        		
        		//fList.add(parser.read(readTag, stream));
            }	
        }    
    }
    
    /**
     * Added by Fatih Batuk to decode SetOf objects..
     * @param tag
     * @param parser
     * @param stream
     * @param asn1Set
     * @throws IOException
     * @author Fatih Batuk
     */
    public BerConstruct(int tag, AutoParser parser, BerInputStream stream, SetOf asn1Set) throws IOException
    {
        this(tag);
        
        int readTag;
        ReadSequence seq = new ReadSequence(stream);
        
        if (asn1Set.getType() == Tag.SequenceType) {
        	
        	AutoParser contentParser;
        	BerSequence content = (BerSequence)asn1Set.getContent();
        	
        	while ( 0 != (readTag = seq.readBerTag()) ) {
        		if (readTag != content.getTag()) 
        			throw new AsnFatalException("\n >> During decoding, the encoded tag value in the byte array does not match with the content of this SequenceOf object ! ");

        		contentParser = new AutoParser(content.size());
        		fList.add(new BerSequence(readTag,contentParser,stream,content));   
            }
        	
        }
        else if (asn1Set.getType() == Tag.SetType) {
        	
        	AutoParser contentParser;
        	BerSet content = (BerSet)asn1Set.getContent();
        	
        	while ( 0 != (readTag = seq.readBerTag()) ) {
        		if (readTag != content.getTag()) 
        			throw new AsnFatalException("\n >> During decoding, the encoded tag value in the byte array does not match with the content of this SetOf object ! ");

        		contentParser = new AutoParser(content.size());
        		fList.add(new BerSet(readTag,contentParser,stream,content));    
            }
        	
        }
        else {	//Our sequenceOf holds primitives :
        	
        	JacNode content = asn1Set.getContent();
        	
        	while ( 0 != (readTag = seq.readBerTag()) ) {
        		if (readTag != content.getTag()) 
        			throw new AsnFatalException("\n >> During decoding, the encoded tag value in the byte array does not match with the content of this SetOf object ! ");

        		content.readElement(stream);
        		check_and_add_thePrimitiveNode(content);
        		check_explicitContent(content);
        		
        		//fList.add(parser.read(readTag, stream));
            }	
        }    
    }
    
    /**
     * added by Fatih Batuk to check and add the content of the SequenceOf/SetOf object
     * @param content
     * @throws IOException
     * @author Fatih Batuk
     */
    private void check_and_add_thePrimitiveNode(JacNode content) throws IOException {
    	
    	if (content instanceof BerBoolean) 
    		fList.add( new BerBoolean (content.getTag(), ((BerBoolean)content).getValue()) );
    	
    	else if (content instanceof BerInteger) 
    		fList.add( new BerInteger (content.getTag(), ((BerInteger)content).getValue()) );
    	
    	else if (content instanceof BerBitString)
    		fList.add( new BerBitString (content.getTag(), ((BerBitString)content).getValue()) );
    	
    	else if (content instanceof BerOctetString)
    		fList.add( new BerOctetString (content.getTag(), ((BerOctetString)content).getValue()) );
    	
    	else if (content instanceof BerOID)
    		fList.add( new BerOID (content.getTag(), ((BerOID)content).getValue()) );
    	
    	else if (content instanceof BerNull)
    		fList.add( new BerNull (content.getTag()));
    	
    	else if (content instanceof BerEnumerated)
    		fList.add( new BerEnumerated (content.getTag(), ((BerEnumerated)content).getValue()) );
    	
    	else if (content instanceof BerPrintableString)
    		fList.add( new BerPrintableString (content.getTag(), ((BerPrintableString)content).getValue()) );
    	
    	else if (content instanceof BerIA5String)
    		fList.add( new BerIA5String (content.getTag(), ((BerIA5String)content).getValue()) );
    	
    	else if (content instanceof BerUTCTime)
    		fList.add( new BerUTCTime (content.getTag(), ((BerUTCTime)content).getValue()) );
    	
    }
    
    /**
     * adde by Fatih Batuk to check the tagging class of the content of a SequenceOf/SetOf object
     * @param content
     * @author Fatih Batuk
     */
    private void check_explicitContent(JacNode content) {
    	if (content.getTaggingMethod() == Tag.EXPLICIT) {
    		Iterator it = fList.iterator();
    		while (it.hasNext()) {
    			BerNode b = (BerNode)it.next();
    			b.setTaggingMethod(Tag.EXPLICIT);
    		}
    	}
    }
    

    /**
     * Write the element out. This will use either a definite length (BER/DER)
     * or an indefinite length (CER) encoding mechanism depending on the
     * output format specified in the BerOutputStream object.
     * @param stream
     * @throws IOException 
     * @see com.chaosinmotion.asn1.BerNode#writeElement(com.chaosinmotion.asn1.BerOutputStream)
     */
    public void writeElement(BerOutputStream stream) throws IOException
    {
    	if (isInitialized == false) {
    		this.checkInitializedOrNot();
    		if (isInitialized == false)
    			throw new AsnEncodingException("\n >> This SEQUENCE/SET is uninitialized(empty) and will not be encoded ! (If exists)Object name is : " + getName());
		}
    	
        Iterator it;
        stream.writeBerTag(getTag() | Tag.CONSTRUCTED);
        
        if (stream.getEncodingMethod() == BerOutputStream.ENCODING_CER) {
            /*
             * Write this as an indefinite length
             */
        	
        	stream.writeBerLength(-1);
            it = fList.iterator();
            while (it.hasNext()) {
                BerNode node = (BerNode)it.next();
                
                if(node instanceof Choice) {
                	BerConstruct chc = (BerConstruct)node;
                	BerNode currentChoice = chc.getChosenElementWhenThisIsChoice();
                	if(currentChoice == null) {
                		if(chc.isOptional==false)
                			throw new AsnEncodingException("\n >> You did not set one element of your NON-OPTIONAL inner choice object (Or you have set more than one element). (if exists)name of the problemeatic choice object is: " + node.getName());
                	}
                	else {
                		if(chc.getTag() != Tag.UnknownChoiceTag) {	//If the Choice object is a tagged type, then encode it as a tagged BerSequence
                			BerSequence seq = new BerSequence(chc.getTag());
                			seq.addElement(currentChoice);
                			seq.writeElement(stream);
                		}
                		else {
                			currentChoice.writeElement(stream);
                		}
                	}
                	
                }
                else {
                	
                	 if (node instanceof BerConstruct && node.isInitialized == false) {	//Nested constructed objects in a Sequence/Set should be checked..
                     	((BerConstruct)node).checkInitializedOrNot();
                     }
                     
                     if (node.isInitialized == false ) {		//Added by Fatih Batuk
                     	if (node.isOptional == true) {
                     		continue;
                     	}
                     	else {
                     		throw new AsnEncodingException("\n >> The non-optional elements of the SEQUENCE/SET are not totally initialized ! (if exists)Uninialized element name is : " + node.getName());
                     	}	
                 	}
                     node.writeElement(stream);
                	
                }
                
            }
            stream.writeBerTag(Tag.EOFTYPE);
            stream.writeBerLength(0);
            
        } else {
            /*
             * Write as definite length
             */
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BerOutputStream tmp = new BerOutputStream(baos,stream.getEncodingMethod());
            
            it = fList.iterator();

            while (it.hasNext()) {
                BerNode node = (BerNode)it.next();
                
                if(node instanceof Choice) {
                	BerConstruct chc = (BerConstruct)node;
                	BerNode currentChoice = chc.getChosenElementWhenThisIsChoice();
                	if(currentChoice == null) {
                		if(chc.isOptional==false)
                			throw new AsnEncodingException("\n >> You did not set one element of your NON-OPTIONAL inner choice object (Or you have set more than one element). (if exists)name of the problemeatic choice object is: " + node.getName());
                	}
                	else {
                		if(chc.getTag() != Tag.UnknownChoiceTag) {
                			BerSequence seq = new BerSequence(chc.getTag());
                			seq.addElement(currentChoice);
                			seq.writeElement(tmp);
                		}
                		else {
                			currentChoice.writeElement(tmp);
                		}
                	}
                	
                }
                else {
                	if (node instanceof BerConstruct && node.isInitialized == false) {	//Nested constructed objects in a Sequence/Set should be checked..
                    	((BerConstruct)node).checkInitializedOrNot();
                    }
                    
                    if (node.isInitialized == false ) {		//Added by Fatih Batuk
                    	if (node.isOptional == true) {
                    		continue;
                    	}
                    	else {
                    		throw new AsnEncodingException("\n >> The non-optional elements of the SEQUENCE/SET are not totally initialized ! (if exists)Uninialized element name is : " + node.getName());
                    	}	
                	}
                	node.writeElement(tmp);
                }
            }
            tmp.close();
            baos.close();
            
            byte[] data = baos.toByteArray();
            stream.writeBerLength(data.length);
            stream.write(data);
        }
    }
    
    private void checkInitializedOrNot() {
    	Iterator it = this.iterator();
    	while (it.hasNext()) {
            BerNode content = (BerNode)it.next();
            if(content instanceof BerConstruct && content.isInitialized==false) {
            	((BerConstruct)content).checkInitializedOrNot();
            	if(content.isInitialized==true) {
            		this.isInitialized = true;
            		break;
            	}
            }
            if (content.isInitialized == true) {
            	this.isInitialized = true;
            	break;
            }
    	}
    }
    
    //protected visibility: also used in Choice.java
    protected BerNode getChosenElementWhenThisIsChoice() {
    	Iterator it = this.iterator();
    	BerNode currentChoice = null;
    	int hit = 0;	//should be if and only if 1! user should not set more than 1 element in the choice object
    	while (it.hasNext()) {
            BerNode content = (BerNode)it.next();
            if(content instanceof BerConstruct) {
            	((BerConstruct)content).checkInitializedOrNot();
            }
            if (content.isInitialized == true) {
              	this.isInitialized = true;
              	currentChoice = content;
              	hit++;
            }
    	}
    	if(hit != 1) {
    		if(hit>1)
    			System.out.println("\n>> WARNING!: You did initialize (choose) more than 1 element in CHOICE object. This is not allowed!\n");
    		return null;
    	}
    	else {
    		return currentChoice;
    	}
    }

    /**
     * Modified by Fatih Batuk
     * <p>Add a BerNode object to this object
     * @param o
     * @return
     * @deprecated Use "addElement()" method instead. It is deprecated for directly use for our project JAC.
     * <p> Use this method directly only if you are only busy with the W.Woody's asn.1 library
     */
    public boolean add(BerNode o)
    {
        return fList.add(o);
    }
   

    /**
     * Clear this constructed object
     */
    public void clear()
    {
        fList.clear();
    }

    /**
     * Get the node entry by index
     * @param index
     * @return
     */
    public BerNode get(int index)
    {
        return (BerNode)fList.get(index);
    }
    

    /**
     * Returns true if this is empty
     * @return
     */
    public boolean isEmpty()
    {
        return fList.isEmpty();
    }

    /**
     * Return an iterator that iterates through the contents of this object
     * @return
     */
    public Iterator iterator()
    {
        return fList.iterator();
    }

    /**
     * Remove the specified node
     * @param o The node to remove
     * @return
     */
    public boolean remove(BerNode o)
    {
        boolean b = fList.remove(o);
        if (fList.size() == 0)
        	isInitialized = false;
        return b;
    }
    
    /**
     * <p>Remove the specified node
     * @param index The node index to remove
     * @author Fatih Batuk
     */
    public void remove(int index)
    {
    	try {
        fList.remove(index);
    	}
    	catch(IndexOutOfBoundsException e){
    		AsnFatalException a = new AsnFatalException(">> Out Of Bounds in the ArrayList of sequence object when trying to remove the element at index : " + index);
    		a.printStackTrace();
    		System.exit(-1);
    	}
        if (fList.size() == 0)
        	isInitialized = false;
    }
    
    /**
     * Retruns the object's arraylist ..
     * @return fList
     * @author Fatih Batuk
     */
    public ArrayList getList() {
    	return fList;
    }
    
    /**
     * This method is added by Fatih Batuk
     * Sets the arrayList
     * @param someList - ArrayList type
     * @author Fatih Batuk
     */
    public void setList(ArrayList someList) {
    	if (someList.size() !=0 )
    		isInitialized = true;
    	else
    		isInitialized = false;
    	fList = someList;
    }
    
    /**
	 * (This method is not necessary now..  Optional to use..)
	 * <p>Fýnds and updates the given element in the arrayList
	 *<p>
	 * @author Fatih Batuk
	 */
    public void setElement(String elementName, BerNode newObj) {
		int location = getIndex(elementName);
		if (location == -1) {
			System.out.println(">>Asn1Exception : No such element exist in the Sequence/Set with name : \"" + elementName + "\"");
			System.exit(-1);
		}
		updateElement(location, newObj);
	}
    
    public boolean removeElement(String elementName) {
		int location = getIndex(elementName);
		if (location == -1) {
			return false;
		}
		else {
			remove(location);
			return true;
		}
			
	}
	
	/**
	 * This method is added by Fatih Batuk
	 * <p>Retrieves the index of named element of the sequence
	 *<p>
	 * @return the index of given element if exists.. otherwise -1.
	 * @author Fatih Batuk
	 */
	public int getIndex(String name)
	{
		for(int	i=0; i < size(); i++)
		{
			BerNode element = get(i);
			String elementName = element.getName();
			if(elementName != null && elementName.equalsIgnoreCase(name))
				return i;
		}
		return -1;
	}
    
    /**
     *<p> Updates the BerNode object which is located in the given index of ArrayList
     * @param index int
     * @param obj BerNode
     * @author Fatih Batuk
     */
    protected void updateElement(int index, BerNode obj) {
    	fList.set(index,obj);
    }

    /**
	 * Adds an element to the end of the sequence
	 *<p>
	 * @param a berNode object to append
	 * to the arraylist of	the sequence.
	 * @author Fatih Batuk
	 */
	public void
	addElement(BerNode element)
	{
		if (element.isInitialized == true) 
			isInitialized = true;
		
		if (!add(element)) {
			AsnFatalException a = new AsnFatalException(" >> The element is not valid !");
			a.printStackTrace();
			System.exit(-1);
		}
	}
	
    /**
     * Return the number of elements in this object
     * @return
     */
    public int size()
    {
        return fList.size();
    }

    /**
     * Return the contents of this object as an array
     * @return
     */
    public BerNode[] toArray()
    {
        return (BerNode[])fList.toArray(new BerNode[fList.size()]);
    }
    
    public String toLabeledString(String name)
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append(name).append("(").append(Tag.toString(getTag())).append(")=[");
        Iterator it = fList.iterator();
        while (it.hasNext()) {
            buffer.append('\n');
            buffer.append(it.next().toString());
            if (it.hasNext()) {
                buffer.append(';');
            }
        }
        buffer.append(']');
        
        return buffer.toString();
    }
}


