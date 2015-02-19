all: AsnParser.class

AsnParser.class: AsnParser.java
	javacAll.sh

AsnParser.java: AsnParser.jj.jj
	javacc AsnParser.jj.jj

AsnParser.jj.jj: AsnParser.jj
	jjtree AsnParser.jj

clean:
	rm *.class
	rm *.jj.jj
	rm AsnParser.java AsnParserConstants.java AsnParserTokenManager.java ParseException.java Token.java TokenMgrError.java
