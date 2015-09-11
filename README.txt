This project contains the JAC parser and runtime.

Changing the Grammar
--------------------
To change the grammar you need to do the following:
- Download javacc
- Make modifications to the AsnParser.jj file
- Run the commands from the runjavacc.bat file included in the project.  This
  will generate some of the java classes that go under the javacc directory in
  the project.  You will need to modify the batch file for your environment.
- Copy the generated java files into the project.
- Rebuild the project.

Running the Parser
------------------
To run the parser on a set of ASN definitions to the following:
- Build the project.
- Use javacc.AsnParser as the main class and run this with the list of files
  that should be parsed.  There can be references to other definition files.
- Command line flags:
  -d The directory where generated files are created.
  -p The package name for generated classes.
You should now have generated classes that can be included in your project
along with the runtime.
