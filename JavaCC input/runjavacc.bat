cd jjgen
java -cp C:\Users\Rob\ASN1Compile\jac\javacc-6.0\bin\lib\javacc.jar jjtree -TRACK_TOKENS:true ..\AsnParser.jj
java -cp C:\Users\Rob\ASN1Compile\jac\javacc-6.0\bin\lib\javacc.jar  javacc AsnParser.jj.jj
"C:\Program Files\Java\jdk1.7.0_71\bin\javac" *java
cd ..