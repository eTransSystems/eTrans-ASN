
/*  
 *  TestParser.java Created on Jun 5, 2006 by William Edward Woody
 */

/* 
 * This TestParser.java class is an example of parsing the asn.1 protocol below : 
   (for more detail look at : http://www.chaosinmotion.com/wiki/index.php?title=ASN.1_Library)
 
ExampleProtocol DEFINITIONS  ::=
BEGIN
 ExampleProtocolMessage ::= CHOICE {
                            rlcGeneralBroadcastInformation  RlcGeneralBroadcastInformation,
                            rlcFrequencyList                RlcFrequencyList  }
 RlcGeneralBroadcastInformation
                       ::= [APPLICATION 0] SEQUENCE {
                            duplexMode                      DuplexMode,
                            frameOffset                     FrameOffset,
                            uplinkPowerMaxRangingStart      UplinkPowerMax,
                            infoText                        InfoText   }
 DuplexMode            ::= ENUMERATED {fdd(0), tdd(1)}
 FrameOffset           ::= INTEGER   (0 | 8..20)
 UplinkPowerMax        ::= INTEGER   (10..20)
 InfoText              ::= IA5String (SIZE (0..128))
 RlcFrequencyList      ::= SEQUENCE  (SIZE(32)) OF PairOfCarrierFrequencies
 PairOfCarrierFrequencies
                       ::= [APPLICATION 1] SEQUENCE {
                            uplinkCarrierFrequency          CarrierFrequency,
                            downlinkCarrierFrequency        CarrierFrequency }
 CarrierFrequency      ::= INTEGER (0..130000)
END
 *
 *(comment by Fatih Batuk)
 */

package com.chaosinmotion.asn1;

import java.io.IOException;

public class TestParser extends BerParser {
	
    private static final int ExampleProtocolMessage = BerParser.START;
    private static final int RlcGeneralBroadcastInformation = 1;
    private static final int RlcFrequencyList = 2;
    private static final int PairOfCarrierFrequencies = 3;
    
    //modified by Fatih Batuk (state variable is deleted)
    public BerNode create(int tag, BerInputStream stream) throws IOException
    {
    	
    	return new BerSequence(tag, this,stream); 
    	
    	// to understand how to write the create(..) method of the asn.1 protocol example given above.
    	
    	//W.Woody's TestParser example :
    		 
    	
    	/*
        switch (state) {
            case ExampleProtocolMessage:
                // Application 0 == RlcGeneralBroadcastInformation
                // Application 1 == RlcFrequencyList
                if (tag == (Tag.APPLICATION | 0)) {
                    return new BerSequence(tag,RlcGeneralBroadcastInformation,this,stream);
                } else if (tag == (Tag.APPLICATION | 1)) {
                    return new BerSequence(tag,RlcFrequencyList,this,stream);
                } 
                else {
                    throw new AsnEncodingException("Unknown tag: " + tag);
                }
                
            case RlcGeneralBroadcastInformation:
            case RlcFrequencyList:
            case PairOfCarrierFrequencies:
                // All contents are primitive. If they weren't, we'd use the state
                // information to sort out what the tag actually is.
                throw new AsnEncodingException("Unknown tag: " + tag);
                
            default:
                throw new AsnEncodingException("Unknown state: " + state);
           
        }
      */       
    }

}


