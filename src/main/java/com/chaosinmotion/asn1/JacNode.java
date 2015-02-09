package com.chaosinmotion.asn1;

import java.io.IOException;

/**
 * A transition class between our project JAC's classes and the asn.1 library.
 * 
 * We need to handle the decoding process polymorphic in our project. 
 * Because of that we defined abstract readElement(..) method in this class.
 * 
 * We did not define this abstract method in BerNode, because we do not want to 
 * affect the all W.Woody's asn.1 libary classes since we do not use all of them.
 * 
 * -- The role of this class and the polymoprphic decoding mechanism is not totally implemented yet but now enough to generate true results.
 * -- Now only implemented for simple asn1 types (not the constructed ones: Sequence, Set, etc)
 * 
 * @author Fatih Batuk
 */
public abstract class JacNode extends BerNode {
	
	public JacNode(int tag) {
		super(tag);
	}
	
	public abstract void readElement(BerInputStream in ) throws IOException;
	
}
