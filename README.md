# TopBloc
Code designed for TopBloc Code Test, reading in data from spreadsheets and posting it to a server.

Used libraries from apache to interact with XSSF workbooks as well as interacting with HTTP clients to post the extracted data.
Libraries from JSON were used to convert our data into suitable JSON objects, and java.IO libraries in order to interact with our file system.

NOTE: When compiling, insert -Xlint:unchecked
-> javac -Xlint:unchecked DataReaderAndPoster.java
-> java DataReaderAndPoster
